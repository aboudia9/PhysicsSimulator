package input;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.SimManager;
import org.jbox2d.common.Vec2;
import ui.CtrlPanel;

public class UserInput {
    /**
     * Install all input handlers onto the given Scene.
     *
     * @param scene      The JavaFX Scene to listen on.
     * @param simManager Callback target for simulation control.
     * @param ctrlPanel  Reference to control panel (if needed).
     */
    public static void setup(Scene scene, SimManager simManager, CtrlPanel ctrlPanel) {
        // Handle keyboard input
        scene.setOnKeyPressed((KeyEvent event) -> {
            switch (event.getCode()) {
                case SPACE:
                    simManager.toggleSimulation();
                    System.out.println("Spacebar pressed!");
                    break;
                case R:
                    // TODO: Add Reset Key
                    System.out.println("Reset simulation!");
                    break;
                case P:
                    simManager.toggleSimulation();
                    break;
                case G:
                    if (event.isShiftDown()) {
                        simManager.decreaseGravity();
                    } else {
                        simManager.increaseGravity();
                    }
                    break;
                default:
                    break;
            }
        });

        // Handle mouse clicks
        scene.setOnMousePressed((MouseEvent event) -> {
            simManager.beginDrag(new Vec2((float)event.getSceneX(), (float)event.getSceneY()));
            System.out.println("Mouse pressed at: " + event.getX() + ", " + event.getY());
        });

        // Handle mouse dragging
        scene.setOnMouseDragged((MouseEvent event) -> {
            simManager.dragObject(new Vec2((float) event.getSceneX(), (float) event.getSceneY()));
        });

        // Handle mouse release
        scene.setOnMouseReleased((MouseEvent event) -> {
            simManager.releaseDrag();
        });
    }
}
