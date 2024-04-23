module com.example.aq5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens com.example.aq5 to javafx.fxml;
    exports com.example.aq5;
}