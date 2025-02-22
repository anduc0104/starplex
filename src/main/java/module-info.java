module com.cinema.starplex {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens com.cinema.starplex to javafx.fxml;
    exports com.cinema.starplex.ui;

    requires java.naming;

    opens com.cinema.starplex.Controller to javafx.fxml;
}