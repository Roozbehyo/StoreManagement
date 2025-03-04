module com.storemgmt.storemanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires commons.dbcp2;
    requires static lombok;

    opens com.storemgmt to javafx.fxml;
    exports com.storemgmt;
}