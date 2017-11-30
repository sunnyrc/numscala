package numscala.ui.GUI;

import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import nonlinear.NeuralNetwork;
import numscala.ui.constant.Constant;
import numscala.ui.model.DataWrapper;
import numscala.ui.service.ScalaIntepreterService;
import scala.Tuple2;
import scala.collection.Seq;
import test.NeuralNetworkDemo;
import test.PerceptronDemo;

import java.util.List;

/**
 * Created by Trung on 11/30/2017.
 */
public class MenuUI {

    private ScalaIntepreterService scalaIntepreterService = new ScalaIntepreterService();

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

        Button neuralNetwork = new Button();
        neuralNetwork.setText("Neural Network");
        neuralNetwork.setPrefSize(Constant.MENU_PREF_WIDTH,Constant.MENU_PREF_WIDTH);
        neuralNetwork.setOnAction(e -> handleMenuSelection(primaryStage, neuralNetwork.getText()));
        grid.add(neuralNetwork, 0, 0);
        grid.setHalignment(neuralNetwork, HPos.CENTER);

        Button perceptron = new Button();
        perceptron.setText("Perceptron");
        perceptron.setPrefSize(Constant.MENU_PREF_WIDTH,Constant.MENU_PREF_WIDTH);
        perceptron.setOnAction(e -> handleMenuSelection(primaryStage, perceptron.getText()));
        grid.add(perceptron, 0, 1);
        grid.setHalignment(perceptron, HPos.CENTER);

        root.getChildren().addAll(grid);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(Constant.MENU_PREF_WIDTH);
        primaryStage.setHeight(Constant.MENU_PREF_HEIGHT);
        primaryStage.show();
    }

    public void handleMenuSelection(Stage primaryStage, String buttonName) {
        LineChartUI lineChartUI = new LineChartUI();
        Tuple2<Seq<Object>, Object> axis;

        if (buttonName.equals("Neural Network")) {
            axis = neuralNetworkDemo.getAxis();
        } else {
            axis = perceptronDemo.getAxis();
        }

        Double accuracy = scalaIntepreterService.roundAccuracy(axis._2());
        List<Double> ys = scalaIntepreterService.scalaSequenceToList(axis._1());
        ys = ys.subList(1, ys.size());

        DataWrapper dataWrapper = new DataWrapper();
        dataWrapper.setAxis(ys);
        dataWrapper.setAccuracy(accuracy);

        HBox hBox = lineChartUI.drawChartViewMode(primaryStage, dataWrapper);
        Scene scene = new Scene(hBox);

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
