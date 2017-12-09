package numscala.ui.GUI;

import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import numscala.ui.constant.Constant;
import numscala.ui.model.DataWrapper;
import numscala.ui.service.ScalaIntepreterService;
import scala.Tuple2;
import scala.collection.Seq;
import test.LinearRegressionDemo;
import test.LogisticRegressionDemo;
import test.NeuralNetworkDemo;
import test.PerceptronDemo;

import java.util.List;
import java.util.Objects;

/**
 * Created by Trung on 11/30/2017.
 */
public class MenuUI {

    private ScalaIntepreterService scalaIntepreterService = new ScalaIntepreterService();

    private LinearRegressionDemo linearRegressionDemo = new LinearRegressionDemo();

    private LogisticRegressionDemo logisticRegressionDemo = new LogisticRegressionDemo();

    private PerceptronDemo perceptronDemo = new PerceptronDemo();

    private NeuralNetworkDemo neuralNetworkDemo = new NeuralNetworkDemo();

    /**
     * Draw navigation pane
     * @return
     */
    public void drawSidePane(Stage primaryStage) {
        HBox root = new HBox();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setMaxSize(Constant.PARENT_PREF_WIDTH, Constant.PARENT_PREF_HEIGHT);

        Button linearRegression = new Button();
        linearRegression.setText("Linear Regression");
        linearRegression.setPrefSize(Constant.MENU_PREF_WIDTH,Constant.MENU_PREF_WIDTH);
        linearRegression.setOnAction(e -> handleMenuSelection(primaryStage, linearRegression.getText()));
        grid.add(linearRegression, 0, 0);
        grid.setHalignment(linearRegression, HPos.CENTER);

        Button logisticRegression = new Button();
        logisticRegression.setText("Logistic Regression");
        logisticRegression.setPrefSize(Constant.MENU_PREF_WIDTH,Constant.MENU_PREF_WIDTH);
        logisticRegression.setOnAction(e -> handleMenuSelection(primaryStage, logisticRegression.getText()));
        grid.add(logisticRegression, 0, 1);
        grid.setHalignment(logisticRegression, HPos.CENTER);

        Button neuralNetwork = new Button();
        neuralNetwork.setText("Neural Network");
        neuralNetwork.setPrefSize(Constant.MENU_PREF_WIDTH,Constant.MENU_PREF_WIDTH);
        neuralNetwork.setOnAction(e -> handleMenuSelection(primaryStage, neuralNetwork.getText()));
        grid.add(neuralNetwork, 0, 2);
        grid.setHalignment(neuralNetwork, HPos.CENTER);

        Button perceptron = new Button();
        perceptron.setText("Perceptron");
        perceptron.setPrefSize(Constant.MENU_PREF_WIDTH,Constant.MENU_PREF_WIDTH);
        perceptron.setOnAction(e -> handleMenuSelection(primaryStage, perceptron.getText()));
        grid.add(perceptron, 0, 3);
        grid.setHalignment(perceptron, HPos.CENTER);

        root.getChildren().addAll(grid);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(Constant.MENU_PREF_WIDTH);
        primaryStage.setHeight(Constant.MENU_PREF_HEIGHT);
        primaryStage.show();
    }

    /**
     * Handle button click
     * @param primaryStage
     * @param buttonName
     */
    public void handleMenuSelection(Stage primaryStage, String buttonName) {
        NonLinearModelUI nonLinearModelUI = new NonLinearModelUI();
        LinearModelUI linearModelUI = new LinearModelUI();

        Scene scene = null;
        Tuple2<Seq<Object>, Object> axis;
        Object error;

        if (buttonName.equals("Linear Regression")) {
            DataWrapper dataWrapper = new DataWrapper();

            error = linearRegressionDemo.getError();
            Double doubleError = scalaIntepreterService.roundAccuracy(error);

            dataWrapper.setParam("Error");
            dataWrapper.setDoubleInfo(doubleError);
            HBox hBox = linearModelUI.drawInfo(primaryStage, dataWrapper);

            if (hBox.getScene() == null) scene = new Scene(hBox);
            else scene = hBox.getScene();
        } else if (buttonName.equals("Perceptron") || buttonName.equals("Neural Network")) {
            DataWrapper dataWrapper = new DataWrapper();

            if (buttonName.equals("Neural Network")) {
                axis = neuralNetworkDemo.getAxis();
                dataWrapper.setParam("Cost");
            } else {
                axis = perceptronDemo.getAxis();
                dataWrapper.setParam("Error");
            }

            Double accuracy = scalaIntepreterService.roundAccuracy(axis._2());
            List<Double> ys = scalaIntepreterService.scalaSequenceToList(axis._1());
            ys = ys.subList(1, ys.size());

            dataWrapper.setAxis(ys);
            dataWrapper.setDoubleInfo(accuracy);

            HBox hBox = nonLinearModelUI.drawChartViewMode(primaryStage, dataWrapper);
            scene = new Scene(hBox);
        } else {
            Seq<Object> logisticAxis = logisticRegressionDemo.getAxis();
            DataWrapper dataWrapper = new DataWrapper();

            List<Double> ys = scalaIntepreterService.scalaSequenceToList(logisticAxis);
            ys = ys.subList(1, ys.size());

            dataWrapper.setAxis(ys);
            dataWrapper.setParam("Loss");
            dataWrapper.setDoubleInfo(0D);

            HBox hBox = nonLinearModelUI.drawChartViewMode(primaryStage, dataWrapper);
            scene = new Scene(hBox);
        }
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
