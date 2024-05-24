module ap.jfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens ap.jfx to javafx.fxml;
    exports ap.jfx;
    exports ap.auth;
    opens ap.auth to javafx.fxml;
}