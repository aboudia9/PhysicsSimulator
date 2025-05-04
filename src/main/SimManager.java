// main/SimManager.java
package main;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import org.jbox2d.common.Vec2;
import Physics2D.Sandbox;
import input.UserInput;
import ui.CtrlPanel;
import ui.Window;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * SimManager bridges the physics Sandbox with JavaFX:
 *  - sets up ground, walls, ceiling
 *  - handles the render loop
 *  - converts between world and screen coordinates
 *  - provides methods for input callbacks
 */
public class SimManager {
    /** Physics backend managing bodies and joints. */
    private final Sandbox sandbox;
    /** JavaFX canvas where shapes are drawn. */
    private final Canvas canvas;
    /** GraphicsContext from the canvas for drawing calls. */
    private final GraphicsContext gc;
    /** AnimationTimer driving the update/render loop. */
    private final AnimationTimer timer;
    /** Random generator for spawning new objects. */
    private final Random random = new Random();
    /** Tracks whether physics simulation is running. */
    private boolean isRunning = true;
    /** Colors selected for each object, in parallel with bodies. */
    private final List<String> colorTypes = new ArrayList<>();
    /** Pixel-to-meter conversion factor. */
    private static final float SCALE = 50.0f;
    /** World dimensions in pixels. */
    private static final int WIDTH = 800, HEIGHT = 600;

    /**
     * Set up physics, UI, and input, then start the main loop.
     */
    public SimManager(Window window) {
        // ── Initialize physics sandbox ──
        sandbox = new Sandbox();
        sandbox.createGround();
        // Convert pixel dimensions to world meters and add walls/ceiling
        float worldW = WIDTH / SCALE;
        float worldH = HEIGHT / SCALE;
        sandbox.createBounds(worldW, worldH);

        // ── Set up JavaFX scene graph ──
        Pane simulationPane = new Pane();
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        simulationPane.getChildren().add(canvas);
        window.getRoot().setCenter(simulationPane);

        // ── Control panel on the right ──
        CtrlPanel ctrlPanel = new CtrlPanel(this);
        window.getRoot().setRight(ctrlPanel);

        // ── Hook up user input callbacks ──
        UserInput.setup(window.getRoot().getScene(), this, ctrlPanel);

        // ── Main update/render loop ──
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isRunning) {
                    sandbox.step();  // advance physics
                    render();       // draw current state
                }
            }
        };
        timer.start();
    }

    /**
     * Draw ground and all objects each frame.
     * Converts physics coords (meters) → screen pixels.
     */
    private void render() {
        // Clear entire canvas
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw ground as a static brown rectangle
        gc.setFill(Color.SANDYBROWN);
        // Ground height is 100px (2m * 50px/m)
        gc.fillRect(0, HEIGHT - 100, WIDTH, 100);

        // Retrieve dynamic object data
        List<Vec2> positions = sandbox.getObjectPositions();
        List<String> shapes   = sandbox.getObjectTypes();

        // Render each object with its assigned color and shape
        for (int i = 0; i < positions.size(); i++) {
            Vec2 pos = positions.get(i);
            String type = shapes.get(i);
            String color = colorTypes.get(i);
            setColorFill(color);

            // Convert: worldX * SCALE, worldY * SCALE flipped vertically
            double x = pos.x * SCALE;
            double y = HEIGHT - (pos.y * SCALE);

            switch (type) {
                case "circle":
                    gc.fillOval(x, y, SCALE / 2, SCALE / 2);
                    break;
                case "square":
                    gc.fillRect(x, y, SCALE / 2, SCALE / 2);
                    break;
                case "triangle":
                    double[] xs = {x, x + SCALE/4, x - SCALE/4};
                    double[] ys = {y, y + SCALE/2, y + SCALE/2};
                    gc.fillPolygon(xs, ys, 3);
                    break;
                default:
                    // Unknown type – skip
                    break;
            }
        }
    }

    /**
     * Spawn a new object at a random horizontal position above the ground.
     */
    public void addObject(String shape) {
        float worldX = random.nextFloat() * 15;           // up to 15 meters
        float worldY = (HEIGHT - 100) / SCALE;           // ground level in meters
        sandbox.addObject(shape, worldX, worldY, 1.0f);
        colorTypes.add(CtrlPanel.getSelectedColor());
    }

    /**
     * Set the current fill color for drawing subsequent shapes.
     */
    public void setColorFill(String color) {
        switch (color.toLowerCase()) {
            case "red":    gc.setFill(Color.RED);    break;
            case "green":  gc.setFill(Color.GREEN);  break;
            case "blue":   gc.setFill(Color.BLUE);   break;
            case "orange": gc.setFill(Color.ORANGE); break;
            default:       gc.setFill(Color.BLACK);  break;
        }
    }

    /** Pause or resume the physics simulation. */
    public void toggleSimulation() {
        isRunning = !isRunning;
    }

    /** Increase downward gravity to make objects fall faster. */
    public void increaseGravity() {
        sandbox.adjustGravity(-9.8f * 1.1f);
    }

    /** Decrease downward gravity to make objects fall slower. */
    public void decreaseGravity() {
        sandbox.adjustGravity(-9.8f * 0.9f);
    }

    //─────────────────────────────────────────────────────────────────────────
    // Input callbacks for mouse-based dragging
    //─────────────────────────────────────────────────────────────────────────

    /**
     * Called on mouse-press: initiate the MouseJoint-based drag.
     *
     * @param screenPt Cursor in screen pixels.
     */
    public void beginDrag(Vec2 screenPt) {
        Vec2 worldPt = new Vec2(screenPt.x / SCALE, (HEIGHT - screenPt.y) / SCALE);
        sandbox.startDrag(worldPt);
    }

    /**
     * Called on mouse-drag: update MouseJoint target.
     *
     * @param screenPt Cursor in screen pixels.
     */
    public void dragObject(Vec2 screenPt) {
        Vec2 worldPt = new Vec2(screenPt.x / SCALE, (HEIGHT - screenPt.y) / SCALE);
        sandbox.dragTo(worldPt);
    }

    /** Called on mouse-release: destroy the MouseJoint to release the body. */
    public void releaseDrag() {
        sandbox.endDrag();
    }
}
