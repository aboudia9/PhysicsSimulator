package PhysicsSimulator.Physics2D.rigidbody;

import org.joml.Vector2f;

public class RigidBody2D {
    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    private Vector2f position = new Vector2f();
    private float rotation = 0.0f;

}
