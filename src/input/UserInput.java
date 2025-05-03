package input;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.SimManager;
import org.jbox2d.common.Vec2;
import ui.CtrlPanel;


public class UserInput {
    public static void setup(Scene Scene, SimManager simManager, CtrlPanel ctrlPanel) {
        // Handle keyboard input
        Scene.setOnKeyPressed((KeyEvent event) -> {
            switch (event.getCode()) {
                case SPACE:
                    System.out.println("Spacebar pressed!");
                    break;
                case R:
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
        Scene.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("Mouse clicked at: " + event.getX() + ", " + event.getY());
        });

        // Handle mouse dragging
        Scene.setOnMouseDragged((MouseEvent event) -> {
            //System.out.println("Dragging at: " + event.getX() + ", " + event.getY());
            Vec2 mousePosition = new Vec2((float) event.getSceneX(), (float) event.getSceneY());
            simManager.setObjectPositionSim(mousePosition);
        });

        Scene.setOnMouseClicked((MouseEvent event) -> {
            /*
            String selectedShape = ctrlPanel.getSelectedShape();
            simManager.addToColorList(ctrlPanel.getSelectedColor());
            simManager.addObject(selectedShape);
            */
        });
    }
}
