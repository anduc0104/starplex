module com.cinema.starplex {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.cinema.starplex to javafx.fxml;

    requires java.naming;

    opens com.cinema.starplex.ui.controllers to javafx.fxml;
    opens com.cinema.starplex.ui.controllers.admin to javafx.fxml;

    requires org.hibernate.orm.core;
    requires jakarta.persistence;

    opens com.cinema.starplex.ui to javafx.fxml;
    exports com.cinema.starplex.ui to javafx.graphics;
}