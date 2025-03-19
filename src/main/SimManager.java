package main;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import Physics2D.Sandbox;
import input.UserInput;
import org.jbox2d.common.Vec2;
import ui.Window;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;
import ui.CtrlPanel;

public class SimManager {
    private Sandbox sandbox;
    private Window window;
    private AnimationTimer timer;
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean isRunning = true;
    // Arbitrary scale of pixels (javaFx) to meters (jbox2d)
    private static final float SCALE = 50.0f;


    public SimManager(Window window) {
        this.window = window;
        this.sandbox = new Sandbox();

        Pane simulationPane = new Pane();
        window.getRoot().setCenter(simulationPane);

        // Initialize the control Panel
        CtrlPanel ctrlPanel = new CtrlPanel(this);
        window.getRoot().setRight(ctrlPanel);


        // create a canvas to draw objects (same dimensions as window)
        canvas = new Canvas(800,600);
        gc = canvas.getGraphicsContext2D();
        simulationPane.getChildren().add(canvas);
        window.getRoot().setCenter(simulationPane);

        // DEBUG
        System.out.println("Canvas added to the screen!");

        // Set up user input handling
        UserInput.setup(window.getRoot().getScene(), this, ctrlPanel);


        // Start physics update loop
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isRunning) {
                    sandbox.step();
                    render();
                }
            }
        };
        timer.start();
    }

    private void render() {
        // clear the frame
        gc.clearRect(0, 0, 800, 600);
        sandbox.createGround();

        // fill the ground brown (ground doesn't move so it's simple)
        double groundYcoordinate = 600 - (1.0 * 100);
        gc.setFill(Color.PAPAYAWHIP);
        gc.fillRect(0, groundYcoordinate, 800, 100);


        // Get all ball object positions and draw each one
        List<Vec2> positions = sandbox.getObjectPositions();
        List<String> shapeTypes = sandbox.getObjectTypes();

//        System.out.println("Rendering " + positions.size() + " objects");
        gc.setFill(Color.GREEN);

        for (int i = 0; i < positions.size(); i++) {
            Vec2 pos = positions.get(i);
            String type = shapeTypes.get(i);

            double screenPosX = (pos.x * SCALE);
            double screenPosY = 600 - (pos.y * SCALE);
            // Convert physics coordinates to screen draw
            switch (type) {
                case "circle":
                    gc.fillOval(screenPosX, screenPosY, SCALE/2, SCALE/2);
                    break;
                case "square":
                    gc.fillRect(screenPosX, screenPosY, SCALE/2, SCALE/2);
                    break;
                case "triangle":
                    double[] xPoints = {screenPosX, screenPosX + (SCALE/4), screenPosX - (SCALE/4)};
                    double[] yPoints = {screenPosY, screenPosY + (SCALE/2), screenPosY + (SCALE/2)};
                    gc.fillPolygon(xPoints, yPoints, 3);
                    break;
            }
        }
    }
    public void addObject(String shape) {
        float worldX = (float) 5;
        float worldY = (float) ((600 - 100)/SCALE);
        sandbox.addObject(shape, worldX, worldY, 1.0f);
        System.out.println(shape + " added!");
    }

    public void toggleSimulation() {
        isRunning = !isRunning;
        System.out.println(isRunning ? "Simulation Resumed" : "Simulation Paused");
    }

    public void increaseGravity() {
        sandbox.adjustGravity(1.0f);
        System.out.println("Increased gravity");
    }

    public void decreaseGravity() {
        sandbox.adjustGravity(-1.0f);
        System.out.println("Decreased gravity");
    }
}