package ui.aquacare;

import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import ui.utilities.ApplicationInterface;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This controller is for visualizing historical sensor readings
 */

public class ReadingController implements Initializable {

    // TODO: NOTE: IN THE FXML FILES, CHART CONTROLLERS SHOULD BE ADDED (as fx:controller = ""). But im not sure if it can be done

    ApplicationInterface applicationInterface = new ApplicationInterface();

    @FXML
    private LineChart<String, Number> linechartPh;
    @FXML
    private LineChart<String, Number> linechartTemp;
    @FXML
    private LineChart<String, Number> linechartLight;
    @FXML
    private LineChart<String, Number> linechartDisp;

    private String activeFishMonitor;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applicationInterface = new ApplicationInterface();

        //"test" is a placeholder for the actual sensor name. As an example, check updateAllCharts() function
        /**
         * try catch?
         */

        if(linechartTemp != null) {
            updateChart(linechartTemp, "test");
        } else {
            System.out.println("linechartTemp is null");
        }

        if(linechartDisp!= null) {
            updateChart(linechartDisp, "test");
        } else {
            System.out.println("linechartDisp is null");
        }

        if(linechartLight != null) {
            updateChart(linechartLight, "test");
        } else {
            System.out.println("linechartLight is null");
        }

        if(linechartPh!= null) {
            updateChart(linechartPh, "test");
        } else {
            System.out.println("linechartPh is null");
        }

    }

    private void updateAllCharts() {
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
                System.out.println("value: " + fluxRecord.getValue() + "stamp:" + fluxRecord.getTime().toString());

                //Save date and value to the data series
                series.getData().add(new XYChart.Data(fluxRecord.getTime().toString(), fluxRecord.getValue()));

            }
        }

        //Update the line chart with milked values
        chart.getData().add(series);

    }

}
