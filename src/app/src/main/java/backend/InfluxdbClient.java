package backend;

import java.time.Instant;
import java.util.List;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;;

public class InfluxdbClient {

    private final String token;
    private final String bucket;
    private final String org;
    private InfluxDBClient client;
    private WriteApiBlocking writeApi;


    
    protected InfluxdbClient() {
        token = "U3_AyxHK1iflHLBaCW4ph-hrQBzc8ECvKuOP02kUwzGAL1CjKEokPG7wMnRtAWIziZT4SmHX7w9qVs0LJQ3AcA==";
        bucket = "Storage";
        org = "DIT113";

        System.out.println("Connecting to InfluxDB");
        client = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray());
        System.out.println("Connected to InfluxDB");

        writeApi = client.getWriteApiBlocking();
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
    
    public void WriteData(String measurement, String tagKey, String tagValue, String fieldKey, double fieldValue) {
        WriteData(measurement, tagKey, tagValue, fieldKey, fieldValue, Instant.now().toEpochMilli()*1000000);
        //Time is saved in apoch nano rather than mili so a conversion is needed
    }


    /**********************************************************/

    //Querying the database

    /**********************************************************/


    public List<FluxTable> QueryDatabase(String bucket, String measurement, String duration, String field, String deviceID,
            boolean mean) {
                //Field could be "value" at default
        String query;
        
        System.out.println(String.format(
            "from(bucket: \"%s\") |> range(start: -%s) |> filter(fn: (r) => r._field == \"%s\" and r._measurement == \"%s\" and r.deviceID != \"%s\") |> mean() |> yield()",
            bucket, duration, field, measurement, deviceID));
        
        
        if (mean) {
            query = String.format(
                    "from(bucket: \"%s\") |> range(start: -%s) |> filter(fn: (r) => r._field == \"%s\" and r._measurement == \"%s\" and r.deviceID != \"%s\") |> mean() |> yield()",
                    bucket, duration, field, measurement, deviceID);
        } else {
            query = String.format(
                    "from(bucket: \"%s\") |> range(start: -%s) |> filter(fn: (r) => r._field == \"%s\" and r._measurement == \"%s\" and r._field != \"%s\") |> yield()",
                    bucket, duration, field, measurement, deviceID);
        }
        List<FluxTable> tables = client.getQueryApi().query(query, org);
        System.out.println("Queried data from InfluxDB");


        /* for (FluxTable fluxTable : tables) {
            System.out.println(fluxTable);
            List<FluxRecord> records = fluxTable.getRecords();
            for (FluxRecord fluxRecord : records) {
                System.out.println(fluxRecord);

            }
        } */

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