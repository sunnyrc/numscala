package numscala.ui.GUI;

import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import numscala.ui.constant.Constant;
import numscala.ui.model.DataWrapper;

/**
 * Created by Trung on 11/30/2017.
 */
public class LinearModelUI {

    /**
     * Show info
     * @param dataWrapper
     * @return
     */
    public HBox drawInfo(Stage primaryStage, DataWrapper dataWrapper) {
        Double error = dataWrapper.getDoubleInfo();

        HBox root = new HBox();

        // For Chart
        RowConstraints chartRowConstraints = new RowConstraints();
        chartRowConstraints.setPercentHeight(10);
        // For Button
        RowConstraints button1RowConstraints = new RowConstraints();
        chartRowConstraints.setPercentHeight(10);
        // For Button
        RowConstraints button2RowConstraints = new RowConstraints();
        chartRowConstraints.setPercentHeight(80);

        GridPane grid = new GridPane();
        grid.getRowConstraints().addAll(chartRowConstraints, button1RowConstraints, button2RowConstraints);
        grid.setMaxSize(Constant.PARENT_PREF_WIDTH, Constant.PARENT_PREF_HEIGHT);

        // Accuracy
        Label label = new Label(dataWrapper.getParam() + ": " + error);
        grid.add(label, 0, 0);

        Button back = new Button();
        back.setText("Back");
        back.setPrefSize(Constant.MENU_PREF_WIDTH,Constant.MENU_PREF_WIDTH);
        back.setOnAction(e -> handleBack(primaryStage));
        grid.add(back, 0, 3);
        grid.setHalignment(back, HPos.CENTER);
        grid.setHalignment(label, HPos.CENTER);

        root.getChildren().addAll(grid);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(Constant.MENU_PREF_WIDTH);
        primaryStage.setHeight(Constant.MENU_PREF_HEIGHT);
        primaryStage.show();
        return root;
    }

    /**
     * Handle button back to selection menu
     * @param primaryStage
     */
    private void handleBack(Stage primaryStage) {
        MenuUI menuUI = new MenuUI();

        menuUI.drawSidePane(primaryStage);
    }
}
