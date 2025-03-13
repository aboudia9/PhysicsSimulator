package main;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import physics.Sandbox;
import input.UserInput;
import ui.Window;

public class SimManager {
    private Sandbox sandbox;
    private Window window;
    private AnimationTimer timer;

    public SimManager(Window window) {
        this.window = window;
        this.sandbox = new Sandbox();

        Pane simulationPane = new Pane();
        window.getRoot().setCenter(simulationPane);

        // Set up user input handling
        UserInput.setup(window.getRoot().getScene());

        // Start physics update loop
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sandbox.step();
                System.out.println("Object Position: " + sandbox.getObjectPosition());
            }
        };
        timer.start();
    }
}
