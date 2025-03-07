module com.cinema.starplex {
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.cinema.starplex to javafx.fxml;

    requires spring.security.crypto;
    requires java.sql;
    requires org.controlsfx.controls;

    opens com.cinema.starplex.ui.controllers to javafx.fxml;
    opens com.cinema.starplex.ui.controllers.admin to javafx.fxml;

    opens com.cinema.starplex.ui to javafx.fxml;
    exports com.cinema.starplex.ui to javafx.graphics;

    opens com.cinema.starplex.ui.controllers.admin.usermanagement to javafx.fxml;
    exports com.cinema.starplex.dao;
    exports com.cinema.starplex.models;

    opens com.cinema.starplex.ui.controllers.admin.movieManagement to javafx.fxml;

}