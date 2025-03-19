package Physics2D;

import org.jbox2d.callbacks.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.collision.*;

import java.util.ArrayList;
import java.util.List;

public class Sandbox {
    private World world;
    private List<Body> objects;
    private List<String> objectTypes;
    private final float SCALE = 50.0f;
    private final float SCALING_FACTOR = 12.0f;

    public Sandbox() {
        // Create a new physics world with gravity
        world = new World(new Vec2(0, -9.8f));
        world.setContactListener(new CollisionHandler());
        objects = new ArrayList<>();
        objectTypes = new ArrayList<>();
    }
    private class CollisionHandler implements ContactListener {
        @Override
        public void beginContact(Contact contact) {
//            System.out.println("Collision started between: " + contact.getFixtureA().getBody() + " and " + contact.getFixtureB().getBody());
        }

        @Override
        public void endContact(Contact contact) {
//            System.out.println("Collision ended between: " + contact.getFixtureA().getBody() + " and " + contact.getFixtureB().getBody());
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {}

        @Override
        public void postSolve(Contact contact, ContactImpulse contactImpulse) {}
    }

    public void addObject(String shapeType, float x, float y, float size) {
        System.out.println("Attempting to add object: " + shapeType + " at (" + x + ", " + y + ")");

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);

        Shape shape;
        switch (shapeType.toLowerCase()) {
            case "circle":
                shape = createCircle(size);
                break;
            case "square":
                shape = createSquare(size);
                break;
            case "triangle":
                shape = createTriangle(size);
                break;
            default:
                System.out.println("Unknown shape: " + shapeType);
                return;
        }
        if (shape == null) {
            System.out.println("Error: Shape is null!");
            return;
        }

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f; // Bounciness

        body.createFixture(fixtureDef);
        objects.add(body);
        objectTypes.add(shapeType);

        System.out.println("Object added successfully! Total objects: " + objects.size());
    }
    // 🔹 Creates a circle shape
    private CircleShape createCircle(float radius) {
        CircleShape circle = new CircleShape();
        circle.setRadius(radius / (SCALE/SCALING_FACTOR));
        return circle;
    }

    // 🔹 Creates a square shape
    private PolygonShape createSquare(float size) {
        PolygonShape square = new PolygonShape();
        square.setAsBox(size / (SCALE/SCALING_FACTOR), size / (SCALE/SCALING_FACTOR));
        return square;
    }

    // 🔹 Creates a triangle shape
    private PolygonShape createTriangle(float size) {
        PolygonShape triangle = new PolygonShape();
        Vec2[] vertices = new Vec2[3];
        vertices[0] = new Vec2(-size / SCALING_FACTOR, -size / SCALING_FACTOR);
        vertices[1] = new Vec2(size / SCALING_FACTOR, -size / SCALING_FACTOR);
        vertices[2] = new Vec2(0, size / SCALING_FACTOR);
        triangle.set(vertices, 3);
        return triangle;
    }

    // Define a ground object that is static
    public void createGround() {
        BodyDef groundDef = new BodyDef();
        groundDef.position.set(5.0f, 1.0f);
        Body groundBody = world.createBody(groundDef);

        PolygonShape groundShape = new PolygonShape();
        // Box with width of 10m & height of 1m
        groundShape.setAsBox(5.0f, 0.4f);

        FixtureDef groundFixture = new FixtureDef();
        groundFixture.shape = groundShape;
        groundFixture.density = 0.0f;
        groundBody.createFixture(groundFixture);

    }

    public List<String> getObjectTypes() {
        return objectTypes;
    }

    public void step() {
        // Advance physics simulation by one step (time step = 1/60 sec)
        world.step(1.0f / 60.0f, 8, 3);
    }
    // 🔹 Returns a list of object positions (for UI rendering)
    public List<Vec2> getObjectPositions() {
        List<Vec2> positions = new ArrayList<>();
        for (Body body : objects) {
            positions.add(body.getPosition());
        }
        return positions;
    }

    // 🔹 Removes all objects from the world (for resets)
    public void clearObjects() {
        for (Body body : objects) {
            world.destroyBody(body);
        }
        objects.clear();
    }


    public void adjustGravity(float v) {
        world.setGravity(new Vec2(0, v));
    }
}
