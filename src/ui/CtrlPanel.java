package ui;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import main.SimManager;

public class CtrlPanel extends VBox {
    private ComboBox<String> shapeSelector;
    private ComboBox<String> colorSelector;
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

        colorSelector = new ComboBox<>();
        colorSelector.getItems().addAll("Red", "Green", "Blue", "Orange");
        colorSelector.setValue("Red");

        Button setColorButton = new Button("Set Color");
        setColorButton.setOnAction(e -> simManager.setColorFill(colorSelector.getValue()));

        this.getChildren().addAll(colorSelector, setColorButton);

    }
    public String getSelectedShape() {
        return shapeSelector.getValue().toLowerCase();
    }

    public String getSelectedColor() {
        return colorSelector.getValue().toLowerCase();
    }
}