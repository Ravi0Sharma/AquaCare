module ui.aquacare {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;

    //Dammit split packages!!!
    //requires mqtt.client;
    //requires influxdb.client.java;
    //requires influxdb.client.core;

    opens ui.aquacare to javafx.fxml;
    exports ui.aquacare;
}