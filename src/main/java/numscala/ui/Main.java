package numscala.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import numscala.ui.GUI.MenuUI;

public class Main extends Application {

    private MenuUI menuUI = new MenuUI();

    @Override
    public void start(Stage primaryStage) throws Exception{
        menuUI.drawSidePane(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
