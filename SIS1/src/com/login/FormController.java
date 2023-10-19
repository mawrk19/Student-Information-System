package com.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import application.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

		if (emailField.getText().isBlank() == false && passField.getText().isBlank() == false) {
			messLabel.setText("Baliw ka ba?");
		} else {
			messLabel.setText("tanga maglagay ka (nag side eye)");
		}

		ValidateCon();
	}

//	public void Cancel() {
//		
//	}

	public void ValidateCon() {
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
	        } else {
	            messLabel.setText("Bobo mali credentials mo -,- ");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        messLabel.setText("Database error");
	    }
	}

	
}
