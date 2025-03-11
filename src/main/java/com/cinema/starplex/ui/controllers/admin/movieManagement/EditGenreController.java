package com.cinema.starplex.ui.controllers.admin.movieManagement;

import com.cinema.starplex.models.Genre;
import com.cinema.starplex.util.DatabaseConnection;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditGenreController {
    @FXML
    private TextField nameField;
    @FXML
    private Button saveButton, clearButton;

    private Genre genre;

    public void setGenre(Genre genre){
        this.genre = genre;
        nameField.setText(genre.getName());
    }

    @FXML
    public void handleEditGenre(ActionEvent actionEvent) throws SQLException {
        String sql = "UPDATE movie_genres SET name = ? WHERE id = ?";
        try(Connection conn = DatabaseConnection.getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nameField.getText());
            pstmt.setInt(2, genre.getId());

            int affectedRows = pstmt.executeUpdate();
            if(affectedRows > 0) {
                genre.setName(nameField.getText());

                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close(); // Đóng cửa sổ sau khi lưu
            }
        }
    }
    @FXML
    public void handleEditClear(ActionEvent actionEvent) {
        nameField.clear();
    }
    @FXML
    public void handleBack(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            SceneSwitcher.switchTo(stage, "admin/moviemanagement/genre-view.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
