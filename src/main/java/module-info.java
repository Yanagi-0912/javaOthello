module main.javaothello {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens main.javaothello to javafx.fxml;
    exports main.javaothello;

    opens main.javaothello.controller to javafx.fxml;
    exports main.javaothello.controller to javafx.fxml;
}