module com.example.dohoa {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jsapi;

    opens com.example.dohoa to javafx.fxml;
    exports com.example.dohoa;
}