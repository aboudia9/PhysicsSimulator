package main;

import javafx.application.Application;
import javafx.stage.Stage;
import ui.Window;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Window window = new Window(primaryStage);
        new SimManager(window);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
