package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.SimManager;

public class CtrlPanel extends VBox {
    private ComboBox<String> shapeSelector;
    private ComboBox<String> colorSelector;
    private Circle colorPreview;

    public CtrlPanel(SimManager simManager) {
        this.setPadding(new Insets(15));
        this.setSpacing(10);
        this.setStyle("-fx-background-color: rgba(240, 240, 240, 0.85); -fx-border-color: #aaa; -fx-border-radius: 10; -fx-background-radius: 10;");
        this.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Controls");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        this.getChildren().add(title);

        // Pause/Gravity Buttons
        Button pauseButton = new Button("⏯ Pause/Resume");
        pauseButton.setOnAction(e -> simManager.toggleSimulation());

        Button increaseGravityButton = new Button("⬇ Increase Gravity");
        increaseGravityButton.setOnAction(e -> simManager.increaseGravity());

        Button decreaseGravityButton = new Button("⬆ Decrease Gravity");
        decreaseGravityButton.setOnAction(e -> simManager.decreaseGravity());

        this.getChildren().addAll(pauseButton, increaseGravityButton, decreaseGravityButton);

        // Shape Selector
        shapeSelector = new ComboBox<>();
        shapeSelector.getItems().addAll("Circle", "Square", "Triangle");
        shapeSelector.setValue("Circle");

        this.getChildren().add(new Label("Shape:"));
        this.getChildren().add(shapeSelector);

        // Color Selector + Preview
        colorSelector = new ComboBox<>();
        colorSelector.getItems().addAll("Red", "Green", "Blue", "Orange");
        colorSelector.setValue("Red");

        HBox colorRow = new HBox(8);
        colorRow.setAlignment(Pos.CENTER_LEFT);
        colorPreview = new Circle(10, Color.RED);  // live preview
        colorSelector.setOnAction(e -> {
            simManager.setColorFill(getSelectedColor());
            updateColorPreview();
        });

        colorRow.getChildren().addAll(new Label("Color:"), colorSelector, colorPreview);
        this.getChildren().add(colorRow);

        // Add Object Button
        Button addObjectButton = new Button("➕ Add Object");
        addObjectButton.setOnAction(e -> {
            simManager.addToColorList(getSelectedColor());
            simManager.addObject(getSelectedShape());
        });

        this.getChildren().add(addObjectButton);
    }

    private void updateColorPreview() {
        switch (getSelectedColor()) {
            case "red": colorPreview.setFill(Color.RED); break;
            case "green": colorPreview.setFill(Color.GREEN); break;
            case "blue": colorPreview.setFill(Color.BLUE); break;
            case "orange": colorPreview.setFill(Color.ORANGE); break;
        }
    }

    public String getSelectedShape() {
        return shapeSelector.getValue().toLowerCase();
    }

    public String getSelectedColor() {
        return colorSelector.getValue().toLowerCase();
    }
}
