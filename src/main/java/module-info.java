module com.cinema.starplex {
    requires javafx.controls;

    opens com.cinema.starplex.ui.controllers.admin.showtimesManagement;


    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.cinema.starplex to javafx.fxml;

    requires spring.security.crypto;
    requires org.controlsfx.controls;

    opens com.cinema.starplex.ui.controllers to javafx.fxml;
    opens com.cinema.starplex.ui.controllers.admin to javafx.fxml;

    opens com.cinema.starplex.ui.controllers.admin.movieManagement to javafx.fxml;
    opens com.cinema.starplex.ui.controllers.admin.usermanagement to javafx.fxml;
    requires flyway.core;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.javafx;
    requires com.zaxxer.hikari;
    requires java.sql;

    opens com.cinema.starplex.ui to javafx.fxml;
    exports com.cinema.starplex.ui to javafx.graphics;

    opens com.cinema.starplex.models to javafx.base;
    exports com.cinema.starplex.ui.controllers.admin;

    exports com.cinema.starplex.ui.controllers.admin.movieManagement;
    exports com.cinema.starplex.ui.controllers.admin.usermanagement;

    exports com.cinema.starplex.models;

    opens com.cinema.starplex.staff;
    opens com.cinema.starplex.ui.controllers.admin.roommanagement;
    exports com.cinema.starplex.ui.controllers.admin.roommanagement;
    opens com.cinema.starplex.ui.controllers.admin.seatManagement;
    exports com.cinema.starplex.ui.controllers.admin.seatManagement;
    opens com.cinema.starplex.ui.controllers.admin.seatTypeManagement;
    exports com.cinema.starplex.ui.controllers.admin.seatTypeManagement;
    exports com.cinema.starplex.ui.controllers.admin.showtimesManagement;
    exports com.cinema.starplex.ui.controllers.admin.movieGenreManagement;
    opens com.cinema.starplex.ui.controllers.admin.movieGenreManagement to javafx.fxml;

    opens com.cinema.starplex.ui.controllers.staff to javafx.fxml;

    exports com.cinema.starplex.ui.controllers.admin.bookingManagement;

    // Mở (opens) package cho JavaFX FXML để có thể truy cập được
    opens com.cinema.starplex.ui.controllers.admin.bookingManagement to javafx.fxml;

    exports com.cinema.starplex.ui.controllers.admin.paymentManagement;

    // Mở (opens) package cho JavaFX FXML để có thể truy cập được
    opens com.cinema.starplex.ui.controllers.admin.paymentManagement to javafx.fxml;
}