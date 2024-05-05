package ui.aquacare;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import ui.utilities.ApplicationInterface;

import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

public class SceneController implements Initializable {

    ApplicationInterface applicationInterface = new ApplicationInterface();

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private LineChart<String, Number> linechartPh;
    @FXML
    private LineChart<String, Number> linechartTemp;
    @FXML
    private LineChart<String, Number> linechartLight;
    @FXML
    private LineChart<String, Number> linechartDisp;

    @FXML
    private Button homeButton;
    @FXML
    private Button searchFishButton;
    @FXML
    private Button buttonLeft3;
    @FXML
    private Button buttonLeft4;
    @FXML
    private Button exitButton;

    @FXML
    private Button phButton;
    @FXML
    private Button feedButton;
    @FXML
    private Button lightButton;
    @FXML
    private Button tempButton;

    @FXML
    private Button buttonRight;
    @FXML
    private Button buttonRight1;
    @FXML
    private Button buttonRight2;

    @FXML
    private TextField textFieldLight;
    @FXML
    private Text lcTextPh;
    @FXML
    private Text lcTextLight;
    @FXML
    private Text lcTextTemp;
    @FXML
    private Text lcTextFood;

    private String activeFishMonitor;

    private void updateAllCharts() {
        //! Should be triggered after a fish change
        //The current structure of the UI makes this function obsolete since the charts are updated in the initialize function

        updateChart(linechartTemp, "Temperature");
        updateChart(linechartLight, "Light");
        updateChart(linechartPh, "PhLevel");
        updateChart(linechartDisp, "Dispenser");
    }

    private void updateChart(LineChart chart, String sensorName) {

        //Create data series for the line chart
        XYChart.Series series = new XYChart.Series();

        //Affects legend which, at the moment, does not exist
        series.setName(sensorName);

        //Query data based on active fish monitor and sensor name
        List<FluxTable> tables = applicationInterface.QueryOfDuration(activeFishMonitor, "1w", sensorName);

        //Divide tables into individual tables
        for (FluxTable table : tables) {

            //Get the records from the table, which correspond to rows in a table
            List<FluxRecord> records = table.getRecords();

            for (FluxRecord fluxRecord : records) {

                //This actually works as intended
                System.out.println("value: " + fluxRecord.getValue() + "    stamp:" + fluxRecord.getTime().toString());

                //Save date and value to the data series
                series.getData().add(new XYChart.Data(fluxRecord.getTime().toString(), fluxRecord.getValue()));

            }
        }

        //*************************************************************************************
        //These can be set up within fxml files themselves.

        //Turn off the legend for the line chart, since it takes precious space
        chart.setLegendVisible(false);

        //Turn off the symbols or dots on the line chart, since they over-crowd the chart
        chart.setCreateSymbols(false);

        //Turn off the animation of the line chart, since it is not needed
        chart.setAnimated(false);

        //Instead of turning them off, try shortening time stamps
        //Giving user about when data is collected is important
        //Another choice would be to somehow downsample the labels(not data as a whole), to make it less crowded

        //Turn off the horizontal grid labels of the line chart, since they make the chart much smaller
        chart.getXAxis().setTickLabelsVisible(false);
        chart.getXAxis().setOpacity(0);

        //Turns off effects on the line chart, since they are not needed
        chart.setEffect(null);

        //Turn auto-scaling off for the line chart, realistically value will be between two values
        chart.getYAxis().setAutoRanging(false);

        //These values should be based on sensor type and tresholds set in json file
        ((NumberAxis) chart.getYAxis()).setLowerBound(0);
        ((NumberAxis) chart.getYAxis()).setUpperBound(100);

        //*************************************************************************************

        //Update the line chart with milked values
        chart.getData().add(series);
    }



    //  will be added when some solution is found for switching pages (not scenes)
//    @FXML
//    private void clickPh() {
//        linechartPh.setVisible(true);
//        linechartDisp.setVisible(false);
//    }
//    @FXML
//    private void bringFront(ActionEvent event) {
//        anchorPane.toFront(true);
//    }

    @FXML
    private void quit(){
        exitButton.setOnAction(event -> {
            Platform.exit();
        });
    }



    private void goToPage(String fxmlFileName, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goHomePage(ActionEvent event) throws IOException {
        goToPage("homepage.fxml", event);
    }

    @FXML
    private void goSearchPage(ActionEvent event) throws IOException {
        goToPage("fish-search.fxml", event);
    }

    @FXML
    public void goSearchPage2(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("fish-search.fxml"));
        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goPhPage(ActionEvent event) throws IOException {
        goToPage("ph-scene.fxml", event);
    }

    //this is now the default homepage
//    @FXML
//    private void goLightPage(ActionEvent event) throws IOException {
//        goToPage("lightScene.fxml", event);
//    }

    @FXML
    private void goFoodPage(ActionEvent event) throws IOException {
        goToPage("food-scene.fxml", event);
    }

    @FXML
    private void goTempPage(ActionEvent event) throws IOException {
        goToPage("temperature-scene.fxml", event);
    }

    @FXML
    private void goConfigPage(ActionEvent event) throws IOException {
        goToPage("configRanges.fxml", event);
    }

    @FXML
    private void goSettings(ActionEvent event) throws IOException {
        goToPage("settings.fxml", event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Set active fish monitor to 1 since owning multiple monitors is not supported yet
        activeFishMonitor = "1";

        Map<LineChart, String> linecharts = new HashMap<>();
        linecharts.put(linechartTemp, "Temperature");
        linecharts.put(linechartDisp, "Feed");
        linecharts.put(linechartLight, "Light");
        linecharts.put(linechartPh, "Ph");

        //***********************************************
        //Check which linechart is not null and update it
        //***********************************************

        for (Map.Entry<LineChart, String> chartEntry : linecharts.entrySet()) {
            if (chartEntry.getKey() != null) {
                updateChart(chartEntry.getKey(), chartEntry.getValue());
            } else {
                System.out.println(chartEntry.getValue() + " is null");
            }
        }

    }
}
