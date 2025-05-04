// Physics2D/Sandbox.java
package Physics2D;

import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Sandbox wraps a JBox2D World, managing dynamic bodies,
 * static boundaries (ground, walls, ceiling), and mouse dragging.
 */
public class Sandbox {
    /** Physics world with gravity. */
    private final World world;
    /** Static ground body (used as MouseJoint anchor). */
    private Body groundBody;
    /** Active mouse joint for dragging; null when not dragging. */
    private MouseJoint mouseJoint;
    /** All dynamic bodies added to the world. */
    private final List<Body> objects;
    /** Parallel list of shape type strings for rendering. */
    private final List<String> objectTypes;
    /** Pixel-to-meter scaling for initial shape sizing. */
    private static final float SCALE = 50.0f;
    /** Additional scale factor to fine-tune Fixture dimensions. */
    private static final float SCALING_FACTOR = 12.0f;

    /**
     * Construct a new Sandbox: initialize world, contact listener, and storage.
     */
    public Sandbox() {
        // Gravity points downward in world coordinates.
        world = new World(new Vec2(0, -9.8f));
        world.setContactListener(new CollisionHandler());
        objects = new ArrayList<>();
        objectTypes = new ArrayList<>();
    }

    /**
     * Create the static ground at y = 0, spanning widely in x.
     */
    public void createGround() {
        BodyDef bd = new BodyDef();
        bd.position.set(0.0f, 0.0f);
        // Ground body is static by default (density = 0).
        groundBody = world.createBody(bd);

        PolygonShape groundShape = new PolygonShape();
        // 25 meters wide, 0.25 meters tall
        groundShape.setAsBox(25.0f, 0.25f);

        FixtureDef fd = new FixtureDef();
        fd.shape = groundShape;
        fd.density = 0.0f;
        groundBody.createFixture(fd);
    }

    /**
     * Add static vertical walls on the left/right and a ceiling at the top.
     *
     * @param worldWidth  Width of simulation (meters).
     * @param worldHeight Height of simulation (meters).
     */
    public void createBounds(float worldWidth, float worldHeight) {
        float halfThickness = 0.1f;  // 0.2m thick boundaries

        // —— Left wall
        BodyDef leftDef = new BodyDef();
        leftDef.position.set(0, worldHeight / 2);
        Body leftWall = world.createBody(leftDef);
        PolygonShape vertShape = new PolygonShape();
        vertShape.setAsBox(halfThickness, worldHeight / 2);
        leftWall.createFixture(vertShape, 0.0f);
        leftWall.setUserData("wall");

        // —— Right wall
        BodyDef rightDef = new BodyDef();
        rightDef.position.set(worldWidth, worldHeight / 2);
        Body rightWall = world.createBody(rightDef);
        rightWall.createFixture(vertShape, 0.0f);
        rightWall.setUserData("wall");

        // —— Ceiling
        BodyDef ceilDef = new BodyDef();
        ceilDef.position.set(worldWidth / 2, worldHeight);
        Body ceiling = world.createBody(ceilDef);
        PolygonShape horizShape = new PolygonShape();
        horizShape.setAsBox(worldWidth / 2, halfThickness);
        ceiling.createFixture(horizShape, 0.0f);
        ceiling.setUserData("ceiling");
    }

    /**
     * Advance the physics simulation by one fixed timestep.
     */
    public void step() {
        world.step(1.0f / 60.0f, 8, 3);
    }

    /**
     * Add a new dynamic object of the given type at (x,y) with visual size.
     *
     * @param shapeType "circle", "square", or "triangle"
     * @param x         Horizontal position in meters
     * @param y         Vertical position in meters
     * @param size      Logical size for rendering/scaling
     */
    public void addObject(String shapeType, float x, float y, float size) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(x, y);
        Body body = world.createBody(bd);

        // Choose shape based on user selection
        Shape shape;
        switch (shapeType.toLowerCase()) {
            case "circle":   shape = createCircle(size);   break;
            case "square":   shape = createSquare(size);   break;
            case "triangle": shape = createTriangle(size); break;
            default:
                // Unknown shape: skip creation
                System.err.println("Unknown shape: " + shapeType);
                return;
        }

        // Physical properties: mass via density, friction, restitution for bounciness
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1.0f;
        fd.friction = 0.3f;
        fd.restitution = 0.5f;
        body.createFixture(fd);

        objects.add(body);
        objectTypes.add(shapeType);
    }

    /** @return List of current object positions (in world meters). */
    public List<Vec2> getObjectPositions() {
        List<Vec2> positions = new ArrayList<>();
        for (Body b : objects) {
            positions.add(b.getPosition());
        }
        return positions;
    }

    /** @return Parallel list of shape-type strings for rendering. */
    public List<String> getObjectTypes() {
        return objectTypes;
    }

    /** Clear all dynamic bodies from the world (e.g., for a reset). */
    public void clearObjects() {
        for (Body b : objects) {
            world.destroyBody(b);
        }
        objects.clear();
        objectTypes.clear();
    }

    /**
     * Change the gravity vector (vertical component) at runtime.
     *
     * @param newGravityY New vertical gravity (negative = downward)
     */
    public void adjustGravity(float newGravityY) {
        Vec2 g = world.getGravity();
        world.setGravity(new Vec2(g.x, newGravityY));
    }

    //─────────────────────────────────────────────────────────────────────────
    // MouseJoint-based dragging
    //─────────────────────────────────────────────────────────────────────────

    /**
     * Begin dragging the topmost dynamic body under worldPt.
     * Called on mouse-press.
     *
     * @param worldPt Cursor location in world meters.
     */
    public void startDrag(Vec2 worldPt) {
        // Create small axis aligned bounding box around mouse point
        float r = 0.01f;
        AABB aabb = new AABB(
                new Vec2(worldPt.x - r, worldPt.y - r),
                new Vec2(worldPt.x + r, worldPt.y + r)
        );
        // Query the world around it with the above AABB
        final Body[] hit = {null};
        world.queryAABB(fixture -> {
            if (fixture.getBody().getType() == BodyType.DYNAMIC
                    && fixture.testPoint(worldPt)) {
                hit[0] = fixture.getBody();
                return false;   // stop querying once we’ve found a body
            }
            return true;        // keep looking
        }, aabb);

        if (hit[0] != null) {
            // Create a MouseJoint pulling the body toward the cursor
            MouseJointDef mjd = new MouseJointDef();
            mjd.bodyA = groundBody;
            mjd.bodyB = hit[0];
            mjd.target.set(worldPt);
            mjd.maxForce = 1000 * hit[0].getMass();
            mjd.frequencyHz = 5.0f;
            mjd.dampingRatio = 0.9f;
            mouseJoint = (MouseJoint) world.createJoint(mjd);
        }
    }

    /**
     * Update the MouseJoint’s target as the user drags.
     *
     * @param worldPt Current cursor location in world meters.
     */
    public void dragTo(Vec2 worldPt) {
        if (mouseJoint != null) {
            mouseJoint.setTarget(worldPt);
        }
    }

    /** Destroy the MouseJoint, releasing the body with its current velocity. */
    public void endDrag() {
        if (mouseJoint != null) {
            world.destroyJoint(mouseJoint);
            mouseJoint = null;
        }
    }

    /**
     * ContactListener stub: expand if you need to react when things collide
     * (e.g. triggers, scoring, special effects).
     */
    private static class CollisionHandler implements ContactListener {
        @Override public void beginContact(Contact contact) { }
        @Override public void endContact(Contact contact) { }
        @Override public void preSolve(Contact contact, Manifold oldManifold) { }
        @Override public void postSolve(Contact contact, ContactImpulse impulse) { }
    }

    //─────────────────────────────────────────────────────────────────────────
    // Shape-creation helpers (private)
    //─────────────────────────────────────────────────────────────────────────

    /** Build a circle shape scaled to the physics world. */
    private CircleShape createCircle(float radius) {
        CircleShape c = new CircleShape();
        c.setRadius(radius / (SCALE / SCALING_FACTOR));
        return c;
    }

    /** Build a square (as box) scaled to the physics world. */
    private PolygonShape createSquare(float size) {
        PolygonShape p = new PolygonShape();
        float half = size / (SCALE / SCALING_FACTOR);
        p.setAsBox(half, half);
        return p;
    }

    /** Build an equilateral triangle scaled to the physics world. */
    private PolygonShape createTriangle(float size) {
        PolygonShape t = new PolygonShape();
        float half = size / (SCALE / SCALING_FACTOR);
        Vec2[] verts = {
                new Vec2(-half, -half),
                new Vec2(half, -half),
                new Vec2(0, half)
        };
        t.set(verts, verts.length);
        return t;
    }
}
