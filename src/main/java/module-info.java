module com.cinema.starplex {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;

    requires org.kordamp.bootstrapfx.core;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.sql;

    opens com.cinema.starplex to javafx.fxml;

    opens com.cinema.starplex.ui to javafx.graphics;
    exports com.cinema.starplex.ui.controllers;
    exports com.cinema.starplex.models;
    opens com.cinema.starplex.models;
}