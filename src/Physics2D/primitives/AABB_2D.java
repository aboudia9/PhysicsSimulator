package Physics2D.primitives;

import org.joml.*;


// Axis Aligned Bounding Box (AABB)
public class AABB_2D {
    private Vector2f center = new Vector2f();
    private Vector2f size = new Vector2f();

    public AABB_2D(){

    }

    public AABB_2D(Vector2f min, Vector2f max) {
        this.size = new Vector2f(max).sub(min);
        this.center = new Vector2f(min).add(new Vector2f(size).div(2.0f));
    }

//    public Vector2f getMin() {
//
//    }
//
//    public Vector2f getMax() {
//
//    }
}
