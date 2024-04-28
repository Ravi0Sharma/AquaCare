module ui.aquacare {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;

    opens ui.aquacare to javafx.fxml;
    exports ui.aquacare;
}