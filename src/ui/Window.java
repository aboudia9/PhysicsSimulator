package ui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Window {
    private Stage stage;
    private Scene scene;
    private BorderPane root;

    public Window(Stage stage) {
        this.stage = stage;
        this.root = new BorderPane();
        this.scene = new Scene(root, 800, 600);

        stage.setTitle("Physics Simulator");
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }

    public BorderPane getRoot() {
        return root;
    }
}
