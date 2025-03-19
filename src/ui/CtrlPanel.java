package ui;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import main.SimManager;

public class CtrlPanel extends VBox {
    private ComboBox<String> shapeSelector;
    public CtrlPanel(SimManager simManager) {
        Button pauseButton = new Button("Pause/Resume");
        pauseButton.setOnAction(e -> simManager.toggleSimulation());

        Button increaseGravityButton = new Button("Increase Gravity");
        increaseGravityButton.setOnAction(e -> simManager.increaseGravity());

        Button decreaseGravityButton = new Button("Decrease Gravity");
        decreaseGravityButton.setOnAction(e -> simManager.decreaseGravity());

        this.getChildren().addAll(pauseButton, increaseGravityButton, decreaseGravityButton);

        shapeSelector = new ComboBox<>();
        shapeSelector.getItems().addAll("Circle", "Square", "Triangle");
        shapeSelector.setValue("Circle"); // Default shape

        Button addObjectButton = new Button("Add Object");
        addObjectButton.setOnAction(e -> simManager.addObject(shapeSelector.getValue()));

        this.getChildren().addAll(shapeSelector, addObjectButton);

    }
    public String getSelectedShape() {
        return shapeSelector.getValue().toLowerCase();
    }
}