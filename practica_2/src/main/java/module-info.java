module com.example.practica_2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.practica_2 to javafx.fxml;
    exports com.example.practica_2;
    exports com.example.practica_2.controllers;
}