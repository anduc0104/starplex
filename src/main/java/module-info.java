module com.cinema.starplex {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires jakarta.persistence;

    opens com.cinema.starplex to javafx.fxml;
    exports com.cinema.starplex;
}