package Physics2D;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.collision.shapes.CircleShape;

public class Sandbox {
    private World world;
    private Body dynamicBody;

    public Sandbox() {
        // Create a new physics world with gravity
        world = new World(new Vec2(0, -9.8f));

        // Define a simple circular object (e.g., ball)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(5.0f, 10.0f);  // Start position

        dynamicBody = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(1.0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        // Ball fixture
        dynamicBody.createFixture(fixtureDef);

        // Define a ground object that is static
        BodyDef groundDef = new BodyDef();
        groundDef.position.set(5.0f, 1.0f);
        Body groundBody = world.createBody(groundDef);

        PolygonShape groundShape = new PolygonShape();
        // Box with width of 10m & height of 1m
        groundShape.setAsBox(5.0f, 0.5f);

        FixtureDef groundFixture = new FixtureDef();
        groundFixture.shape = groundShape;
        groundFixture.density = 0.0f;
        groundBody.createFixture(groundFixture);

    }

    public void step() {
        // Advance physics simulation by one step (time step = 1/60 sec)
        world.step(1.0f / 60.0f, 8, 3);
    }

    public Vec2 getObjectPosition() {
        return dynamicBody.getPosition();
    }
}