package com.login;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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

		String verifyLogin = "'select * from where username ='" + emailField.getText() + "'and password'"
				+ passField.getText() + "'";

		try {
			Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery(verifyLogin);

			while (result.next()) {
				if (result.getInt(1) == 1) {
					messLabel.setText("Tanginanyu");
				} else {
					messLabel.setText("Tanginanyu ule");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
