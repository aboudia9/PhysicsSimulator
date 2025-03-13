package input;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class UserInput {
    public static void setup(Scene scene) {
        // Handle keyboard input
        scene.setOnKeyPressed((KeyEvent event) -> {
            switch (event.getCode()) {
                case SPACE:
                    System.out.println("Spacebar pressed!");
                    break;
                case R:
                    System.out.println("Reset simulation!");
                    break;
                default:
                    break;
            }
        });

        // Handle mouse clicks
        scene.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("Mouse clicked at: " + event.getX() + ", " + event.getY());
        });

        // Handle mouse dragging
        scene.setOnMouseDragged((MouseEvent event) -> {
            System.out.println("Dragging at: " + event.getX() + ", " + event.getY());
        });
    }
}
