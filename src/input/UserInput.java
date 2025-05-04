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
     * @param Scene      The JavaFX Scene to listen on.
     * @param simManager Callback target for simulation control.
     * @param ctrlPanel  Reference to control panel (if needed).
     */
    public static void setup(Scene Scene, SimManager simManager, CtrlPanel ctrlPanel) {
        // Handle keyboard input
        Scene.setOnKeyPressed((KeyEvent event) -> {
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
        Scene.setOnMousePressed((MouseEvent event) -> {
            simManager.beginDrag(new Vec2((float)event.getSceneX(), (float)event.getSceneY()));
            System.out.println("Mouse clicked at: " + event.getX() + ", " + event.getY());
        });

        // Handle mouse dragging
        Scene.setOnMouseDragged((MouseEvent event) -> {
            //System.out.println("Dragging at: " + event.getX() + ", " + event.getY());
            simManager.dragObject(new Vec2((float) event.getSceneX(), (float) event.getSceneY()));
        });

        Scene.setOnMouseReleased((MouseEvent event) -> {
            simManager.releaseDrag();
            /*
            String selectedShape = ctrlPanel.getSelectedShape();
            simManager.addToColorList(ctrlPanel.getSelectedColor());
            simManager.addObject(selectedShape);
            */
        });
    }
}
