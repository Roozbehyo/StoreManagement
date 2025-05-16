module com.storemgmt.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    requires com.storemgmt.common;
    requires static lombok;
    requires log4j;

    exports com.storemgmt.desktop to javafx.graphics;
    exports com.storemgmt.desktop.Controller to javafx.fxml;

    opens com.storemgmt.desktop.Controller to javafx.fxml;
}