module org.example.consumonutrireceitas {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;

    opens org.example.consumonutrireceitas to javafx.graphics, javafx.fxml;
    opens org.example.consumonutrireceitas.controller to javafx.fxml;
    opens org.example.consumonutrireceitas.model to com.fasterxml.jackson.databind, javafx.base;


    exports org.example.consumonutrireceitas.controller to javafx.fxml;
    exports org.example.consumonutrireceitas to javafx.fxml;
}