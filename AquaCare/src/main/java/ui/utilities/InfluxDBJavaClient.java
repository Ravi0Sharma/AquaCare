package ui.utilities;

import java.time.Instant;
import java.util.List;

//import com.influxdb.annotations.Column;
//import com.influxdb.annotations.Measurement;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
//import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;;

public class InfluxDBJavaClient {

    //! ********************************************
    // Common values will be
    // Bucket: Storage
    // Org: DIT113
    // Measure: -Sensor Type-

    // Tag key: "deviceID"
    // Tag value: -Device ID-

    // Field key: "value"
    // Field value: -Value-
    //! ********************************************

    //Creating a single instance of the class
    private static InfluxDBJavaClient singleInstance = null;

    //Declaring the connection values
    private final String token;
    private final String bucket;
    private final String org;
    private InfluxDBClient client;
    private WriteApiBlocking writeApi;

    private InfluxDBJavaClient() {

// TODO: create a secret document in gitlab to store those values
        //Defining the connection values
        //!WIP This is not the best way to hold values such as api keys
        //token = "U3_AyxHK1iflHLBaCW4ph-hrQBzc8ECvKuOP02kUwzGAL1CjKEokPG7wMnRtAWIziZT4SmHX7w9qVs0LJQ3AcA==";
        token = "ZK-vVqNlD2iFXpKPWfQsvkFFe_wl4mNlAm3FHCzBCbjzPnXKBZDswr5RKRFb8KDinw_b4mSWQIx3xMNnIFDvkA==";
        bucket = "Storage";
        org = "DIT113";

       
        client = InfluxDBClientFactory.create("https://eu-central-1-1.aws.cloud2.influxdata.com", token.toCharArray());
        //client = InfluxDBClientFactory.create("http://localhost:8086/", token.toCharArray());


        writeApi = client.getWriteApiBlocking();
    }

    // Singleton initiator
    public static InfluxDBJavaClient getInstance() {
        if (singleInstance == null) {
            singleInstance = new InfluxDBJavaClient();
        }
        return singleInstance;
    }

    /**********************************************************/

    // Writing on the database

    /**********************************************************/

    public void WriteData(String measurement, String tagKey, String tagValue, String fieldKey, double fieldValue,
                          Long timestamp) {

        // Creating a write point and filling it with values
        Point point = Point
                .measurement(measurement)
                .addTag(tagKey, tagValue)
                .addField(fieldKey, fieldValue)
                .time(timestamp, WritePrecision.NS);

        // Push the data point to the database
        writeApi.writePoint(bucket, org, point);

        //!For testing purposes
    }

    /**********************************************************/

    // Write Overloads

    /**********************************************************/

    // Overlaods are for testing purposes, this class is not meant to have anything
    // to do with what is going to be saved.
    // Its job is to save and retrieve data
    // These should be implemented in ApplicationInterface class
    //! Or sjould they :-)

    // Able to not excpilictly state timestamp
    public void WriteData(String measurement, String tagKey, String tagValue, String fieldKey, double fieldValue) {
        WriteData(measurement, tagKey, tagValue, fieldKey, fieldValue, Instant.now().toEpochMilli() * 1000000);
        // Time is saved in apoch nano rather than mili so a conversion is needed
    }

    // Able to not excpilictly state fieldKey - default is "value" and tagkey - default is "deviceID"
    public void WriteData(String measurement, String tagValue, double fieldValue, Long timestamp) {
        WriteData(measurement, "deviceID", tagValue, "value", fieldValue, timestamp);
    }

    // Able to not excpilictly state fieldKey - default is "value" and tagkey - default is "deviceID" and timestamp default is current time
    public void WriteData(String measurement, String tagValue, double fieldValue) {
        WriteData(measurement, "deviceID", tagValue, "value", fieldValue);
    }

    /**********************************************************/

    // Querying the database

    /**********************************************************/

    public List<FluxTable> QueryDatabase(String duration, String measurement, String field,
                                         String deviceID, boolean mean) {
        // Field could be "value" at default

        //Declaring the query
        String query;

        //Defining the query
        //If mean is true, the query will return the mean value of the data
        if (mean) {
            query = String.format(
                    "from(bucket: \"%s\") |> range(start: -%s) |> filter(fn: (r) => r[\"_measurement\"] == \"%s\") |> filter(fn: (r) => r[\"_field\"] == \"%s\") |> filter(fn: (r) => r[\"deviceID\"] == \"%s\") |> mean() |> yield()",
                    bucket, duration, measurement, field, deviceID);
        } else {
            query = String.format(
                    "from(bucket: \"%s\") |> range(start: -%s) |> filter(fn: (r) => r[\"_measurement\"] == \"%s\") |> filter(fn: (r) => r[\"_field\"] == \"%s\") |> filter(fn: (r) => r[\"deviceID\"] == \"%s\") |> yield()",
                    bucket, duration, measurement, field, deviceID);
        }

        //Querying the database
        List<FluxTable> tables = client.getQueryApi().query(query, org);

        //Printing the data
        //!For testing purposes
        for (FluxTable fluxTable : tables) {
            System.out.println(fluxTable);
            List<FluxRecord> records = fluxTable.getRecords();
            for (FluxRecord fluxRecord : records) {
                System.out.println(fluxRecord.getRow());

            }
        }
        return tables;
    }

    /**********************************************************/
    // Query Overloads

    /**********************************************************/
    //! May be implemented in ApplicationInterface class instead

    // Able to not excpilictly state mean
    public List<FluxTable> QueryDatabase(String duration, String measurement, String field,
                                         String deviceID) {
        return QueryDatabase(duration, measurement, field, deviceID, false);
    }

    // Able to not excpilictly state field - default is "value"
    public List<FluxTable> QueryDatabase(String duration, String measurement,
                                         String deviceID) {
        return QueryDatabase(duration, measurement, "value", deviceID, false);
    }

    public List<FluxTable> QueryDatabaseSensorCustom(String duration, String measurement, String deviceID, String customQuery) {

        //Declaring the query
        String query;

        //Defining the query
        //If mean is true, the query will return the mean value of the data
        query = String.format(
                "from(bucket: \"%s\") |> range(start: -%s) |> filter(fn: (r) => r[\"_measurement\"] == \"%s\") |> filter(fn: (r) => r[\"_field\"] == \"%s\") |> filter(fn: (r) => r[\"deviceID\"] == \"%s\") %s |> yield()",
                bucket, duration, measurement, "value", deviceID, customQuery);

        //Querying the database
        List<FluxTable> tables = client.getQueryApi().query(query, org);

        return tables;
    }
}

