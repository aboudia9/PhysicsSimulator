package physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.collision.shapes.*;

import java.util.ArrayList;
import java.util.List;

public class Sandbox {
    private World world;
    private List<Body> objects;

    public Sandbox() {
        // Create the physics world with gravity
        world = new World(new Vec2(0, 9.8f));
        objects = new ArrayList<>();
    }

    // 🔹 Adds a new shape to the physics world
    public void addObject(String shapeType, float x, float y, float size) {
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

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f; // Bounciness

        body.createFixture(fixtureDef);
        objects.add(body);
    }

    // 🔹 Creates a circle shape
    private CircleShape createCircle(float radius) {
        CircleShape circle = new CircleShape();
        circle.setRadius(radius);
        return circle;
    }

    // 🔹 Creates a square shape
    private PolygonShape createSquare(float size) {
        PolygonShape square = new PolygonShape();
        square.setAsBox(size / 2, size / 2);
        return square;
    }

    // 🔹 Creates a triangle shape
    private PolygonShape createTriangle(float size) {
        PolygonShape triangle = new PolygonShape();
        Vec2[] vertices = new Vec2[3];
        vertices[0] = new Vec2(-size / 2, -size / 2);
        vertices[1] = new Vec2(size / 2, -size / 2);
        vertices[2] = new Vec2(0, size / 2);
        triangle.set(vertices, 3);
        return triangle;
    }

    // 🔹 Advances the physics simulation
    public void step() {
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
}
