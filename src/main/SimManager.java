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

public class SimManager {
    private Sandbox sandbox;
    private Window window;
    private AnimationTimer timer;
    private Canvas canvas;
    private GraphicsContext gc;
    public SimManager(Window window) {
        this.window = window;
        this.sandbox = new Sandbox();

        Pane simulationPane = new Pane();
        window.getRoot().setCenter(simulationPane);

        // create a canvas to draw objects (same dimensions as window)
        canvas = new Canvas(800,600);
        gc = canvas.getGraphicsContext2D();
        simulationPane.getChildren().add(canvas);

        // DEBUG
        System.out.println("Canvas added to the screen!");

        // Set up user input handling
        UserInput.setup(window.getRoot().getScene());

        // Start physics update loop
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sandbox.step();
                render();
            }
        };
        timer.start();
    }

    private void render() {
        // clear the frame
        gc.clearRect(0, 0, 800, 600);

        // fill the ground brown (ground doesn't move so it's simple)
        double groundYcoordinate = 600 - (2.0 * 50);
        gc.setFill(Color.PAPAYAWHIP);
        gc.fillRect(0, groundYcoordinate, 800, 100);


        // get ball object position and draw the object
        Vec2 position = sandbox.getObjectPosition();
        gc.setFill(Color.GREEN);
        // Convert physics coordinates to screen draw
        gc.fillOval(position.x*50, 600 - position.y * 50, 25, 25);
    }
}