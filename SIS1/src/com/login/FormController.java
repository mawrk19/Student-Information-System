package com.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import application.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FormController {

    @FXML
    TextField emailField;
    @FXML
    PasswordField passField;
    @FXML
    Button signInBTN;
    @FXML
    Label messLabel;

    public void LogIn(ActionEvent e) {
        if (!emailField.getText().isBlank() && !passField.getText().isBlank()) {
            messLabel.setText("Baliw ka ba?");
        } else {
            messLabel.setText("tanga maglagay ka (nag side eye)");
        }
        ValidateCon((Stage) signInBTN.getScene().getWindow()); // Pass the stage
    }

    public void openMF(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/application/MainFrame.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that may occur during FXML loading
        }
    }

    public void ValidateCon(Stage stage) {
        DatabaseManager connectNow = new DatabaseManager();
        Connection con = connectNow.getConnection();

        String verifyLogin = "SELECT * FROM users WHERE username = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(verifyLogin);
            preparedStatement.setString(1, emailField.getText());
            preparedStatement.setString(2, passField.getText());

            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                messLabel.setText("Yun oh nakapasok si Idok");
                openMF(stage); // Pass the stage
            } else {
                messLabel.setText("Boba mali credentials mo -,- ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messLabel.setText("Database error");
        }
    }
}