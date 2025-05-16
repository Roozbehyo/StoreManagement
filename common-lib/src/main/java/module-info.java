module com.storemgmt.common{
    exports com.storemgmt.common.Entity;
    exports com.storemgmt.common.Entity.Enum;
    exports com.storemgmt.common.Service;
    exports com.storemgmt.common.Validation;

    requires static lombok;
    requires org.apache.commons.dbcp2;
    requires java.sql;
    requires log4j;

}