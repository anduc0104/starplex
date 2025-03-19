package com.cinema.starplex.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertUtils {
    /**
     * Hiển thị thông báo lỗi
     * @param title Tiêu đề của hộp thoại
     * @param message Nội dung thông báo
     */
    private static boolean lastResult;
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hiển thị thông báo thông tin
     * @param title Tiêu đề của hộp thoại
     * @param message Nội dung thông báo
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hiển thị thông báo cảnh báo
     * @param title Tiêu đề của hộp thoại
     * @param message Nội dung thông báo
     */
    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hiển thị hộp thoại xác nhận với hai lựa chọn Yes/No
     * @param title Tiêu đề của hộp thoại
     * @param message Nội dung thông báo
     * @return true nếu người dùng chọn Yes, false nếu chọn No
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        lastResult = result.isPresent() && result.get() == ButtonType.OK;
        return lastResult;
    }

    public static boolean getResult() {
        return lastResult;
    }
}