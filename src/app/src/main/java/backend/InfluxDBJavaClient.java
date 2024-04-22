package backend;

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

    //There are mistakes I need to fix in this class
    //Our common values will be
    //Bucket: Storage
    //Org: DIT113
    //Measure: -Sensor Type-

    //Tag key: "deviceID"
    //Tag value: -Device ID-

    //Field key: "value"
    //Field value: -Value-




    //Data I get in db: _value = -value-, _field = "value"

    private static InfluxDBJavaClient singleInstance = null;

    private final String token;
    private final String bucket;
    private final String org;
    private InfluxDBClient client;
    private WriteApiBlocking writeApi;


    
    private InfluxDBJavaClient() {
        token = "U3_AyxHK1iflHLBaCW4ph-hrQBzc8ECvKuOP02kUwzGAL1CjKEokPG7wMnRtAWIziZT4SmHX7w9qVs0LJQ3AcA==";
        bucket = "Storage";
        org = "DIT113";

        System.out.println("Connecting to InfluxDB");
        client = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray());
        System.out.println("Connected to InfluxDB");

        writeApi = client.getWriteApiBlocking();
    }


    //Singleton initiator
    public static InfluxDBJavaClient getInstance()
    {
        if (singleInstance == null){
            singleInstance = new InfluxDBJavaClient();
        }
        return singleInstance;
    }


    /**********************************************************/
    
    //Writing on the database

    /**********************************************************/


    public void WriteData(String measurement, String tagKey, String tagValue, String fieldKey, double fieldValue,
    Long timestamp) {
        //Creating a write point and filling it with values
        Point point = Point
        .measurement(measurement)
        .addTag(tagKey, tagValue)
        .addField(fieldKey, fieldValue)
        .time(timestamp, WritePrecision.NS);
        
        writeApi.writePoint(bucket, org, point);
        System.out.println("Wrote data to InfluxDB");
    }
    

    //Overloading
    //Overlaods are for testing purposes, this class is not meant to have anything to do with what is going to be saved.
    //Its job is to save and retrieve data
    //These should be implemented in ApplicationInterface class

    public void WriteData(String measurement, String tagKey, String tagValue, String fieldKey, double fieldValue) {
        WriteData(measurement, tagKey, tagValue, fieldKey, fieldValue, Instant.now().toEpochMilli()*1000000);
        //Time is saved in apoch nano rather than mili so a conversion is needed
    }

    public void WriteData(String measurement, String tagValue, double fieldValue) {
        WriteData(measurement, "deviceID", tagValue, "value", fieldValue);
    }

    public void WriteData(String measurement, String tagValue, double fieldValue, Long timestamp) {
        WriteData(measurement, "deviceID", tagValue, "value", fieldValue , timestamp);
    }


    /**********************************************************/

    //Querying the database

    /**********************************************************/


    public List<FluxTable> QueryDatabase(String bucket, String measurement, String duration, String field, String deviceID,
            boolean mean) {
                //Field could be "value" at default
        String query;
        
        System.out.println(String.format(
            "from(bucket: \"%s\") |> range(start: -%s) |> filter(fn: (r) => r._field == \"%s\" and r._measurement == \"%s\" and r.deviceID == \"%s\") |> mean() |> yield()",
            bucket, duration, field, measurement, deviceID));
            
            if (mean) {
                query = String.format(
                        "from(bucket: \"%s\") |> range(start: -%s) |> filter(fn: (r) => r._field == \"%s\" and r._measurement == \"%s\" and r.deviceID == \"%s\") |> mean() |> yield()",
                        bucket, duration, field, measurement, deviceID);
            } else {
                query = "from(bucket: \"Storage\") |> range(start: -1h)";
            }
        
        /* if (mean) {
            query = String.format(
                    "from(bucket: \"%s\") |> range(start: -%s) |> filter(fn: (r) => r._field == \"%s\" and r._measurement == \"%s\" and r.deviceID == \"%s\") |> mean() |> yield()",
                    bucket, duration, field, measurement, deviceID);
        } else {
            query = String.format(
                    "from(bucket: \"%s\") |> range(start: -%s) |> filter(fn: (r) => r._field == \"%s\" and r._measurement == \"%s\" and r._field == \"%s\") |> yield()",
                    bucket, duration, field, measurement, deviceID);
        } */
        List<FluxTable> tables = client.getQueryApi().query(query, org);
        System.out.println("Queried data from InfluxDB");


        for (FluxTable fluxTable : tables) {
            System.out.println(fluxTable);
            List<FluxRecord> records = fluxTable.getRecords();
            for (FluxRecord fluxRecord : records) {
                System.out.println(fluxRecord);

            }
        }

        return tables;
    }

    // Overloading
    
    //Able to not excpilictly state bucket
    public List<FluxTable> QueryDatabase(String measurement, String duration, String field, String deviceID, boolean mean) {
        return QueryDatabase(bucket, measurement, duration, field, deviceID, mean);
    }
    //Able to not excpilictly state mean
    public List<FluxTable> QueryDatabase(String measurement, String duration, String field, String deviceID) {
        return QueryDatabase(bucket, measurement, duration, field, deviceID, false);
    }    

}