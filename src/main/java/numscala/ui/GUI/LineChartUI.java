package numscala.ui.GUI;

import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import numscala.ui.constant.Constant;
import numscala.ui.model.DataWrapper;

import java.util.List;

/**
 * Created by Trung on 11/29/2017.
 */
public class LineChartUI {

    /**
     * Draw chart, return HBox Object
     * @param dataWrapper
     * @return
     */
    public HBox drawChartHoverMode(Stage primaryStage, DataWrapper dataWrapper) {
        List<Double> ys = dataWrapper.getAxis();
        Double accuracy = dataWrapper.getAccuracy();

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

        int prefWidth = Constant.PARENT_PREF_WIDTH;

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Epoch");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Error");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);
        lineChart.setPrefWidth(prefWidth);
        lineChart.setMaxSize(Constant.CHILDREN_MAX_WIDTH, Constant.CHILDREN_MAX_HEIGHT);

        // Create chart from xs and ys
        int epoch = 0;
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        while (epoch < ys.size()) {
            Number y = ys.get(epoch);

            if (epoch%2 == 0)
                series.getData().add(new XYChart.Data<>(epoch, y));

            epoch++;
        }

        // Configure line chart
        lineChart.getData().addAll(series);
        grid.add(lineChart, 0, 0);

        // Mode switch button
        Button modeSwitch = new Button();
        modeSwitch.setText("View Mode");
        modeSwitch.setPrefSize(180,40);
        modeSwitch.setOnAction(e -> handleHoverModeSwitch(primaryStage, dataWrapper));
        grid.add(modeSwitch, 0, 1);
        grid.setHalignment(modeSwitch, HPos.CENTER);

        // Back button
        Button back = new Button();
        back.setText("Back");
        back.setPrefSize(240,40);
        back.setOnAction(e -> handleBack(primaryStage));
        grid.add(back, 0, 2);
        grid.setHalignment(back, HPos.CENTER);

        // Accuracy
        grid.add(new Label("Accuracy: " + accuracy), 1, 0);

        // Epoch vs Error Side information
        for (XYChart.Data<Number, Number> data: series.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,
                    (Event event) -> {
                        ((Node)(event.getSource())).setCursor(Cursor.HAND);
                        // Some text is already there
                        grid.getChildren().remove(grid.getChildren().size()-1);

                        Label label = new Label("Epoch: " + data.getXValue() +
                                                    "\nError: " + data.getYValue() +
                                                    "\nAccuracy: " + accuracy);
                        grid.add(label, 1, 0);
                    });
        }

        root.getChildren().add(grid);
        return root;
    }

    /**
     * Draw chart, return HBox Object
     * @param dataWrapper
     * @return
     */
    public HBox drawChartViewMode(Stage primaryStage, DataWrapper dataWrapper) {
        List<Double> ys = dataWrapper.getAxis();
        Double accuracy = dataWrapper.getAccuracy();

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

        int prefWidth = Constant.PARENT_PREF_WIDTH;

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Epoch");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Error");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        lineChart.setPrefWidth(prefWidth);
        lineChart.setMaxSize(Constant.CHILDREN_MAX_WIDTH, Constant.CHILDREN_MAX_HEIGHT);

        // Create chart from xs and ys
        int epoch = 0;
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        while (epoch < ys.size()) {
            Number y = ys.get(epoch);

            series.getData().add(new XYChart.Data<>(epoch, y));

            epoch++;
        }

        // Configure line chart
        lineChart.getData().addAll(series);
        grid.add(lineChart, 0, 0);

        // Accuracy
        grid.add(new Label("Accuracy: " + accuracy), 1, 0);

        // Mode switch button
        Button modeSwitch = new Button();
        modeSwitch.setText("Hover Mode");
        modeSwitch.setPrefSize(180,40);
        modeSwitch.setOnAction(e -> handleViewModeSwitch(primaryStage, dataWrapper));
        grid.add(modeSwitch, 0, 1);
        grid.setHalignment(modeSwitch, HPos.CENTER);

        // Back button
        Button back = new Button();
        back.setText("Back");
        back.setPrefSize(240,40);
        back.setOnAction(e -> handleBack(primaryStage));
        grid.add(back, 0, 2);
        grid.setHalignment(back, HPos.CENTER);

        root.getChildren().add(grid);
        return root;
    }

    /**
     * Handle button switch from hover to view
     * @param primaryStage
     * @param dataWrapper
     */
    public void handleHoverModeSwitch(Stage primaryStage, DataWrapper dataWrapper) {
        HBox hBox = this.drawChartViewMode(primaryStage, dataWrapper);

        Scene scene = new Scene(hBox);
        primaryStage.sizeToScene();
        primaryStage.setScene(scene);
    }

    /**
     * Handle button switch from view to hover
     * @param primaryStage
     * @param dataWrapper
     */
    public void handleViewModeSwitch(Stage primaryStage, DataWrapper dataWrapper) {
        HBox hBox = this.drawChartHoverMode(primaryStage, dataWrapper);

        Scene scene = new Scene(hBox);
        primaryStage.sizeToScene();
        primaryStage.setScene(scene);
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
