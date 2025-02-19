module com.cinema.starplex {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;

    requires org.kordamp.bootstrapfx.core;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;

    opens com.cinema.starplex to javafx.fxml;

    opens com.cinema.starplex.ui to javafx.graphics;
    exports com.cinema.starplex.ui.controllers;
}