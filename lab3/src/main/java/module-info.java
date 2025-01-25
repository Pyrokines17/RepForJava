module ru.nsu.gunko.lab3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires java.desktop;

    opens ru.nsu.gunko.lab3 to javafx.fxml;
    exports ru.nsu.gunko.lab3;
}