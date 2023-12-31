package com.login;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import application.DatabaseManager;
import application.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FormController implements Initializable{

	@FXML
	TextField emailField, regFname, regLname, regUser, regPass;
	@FXML
	PasswordField passField;
	@FXML
	Button signInBTN;
    @FXML
    private Label Exit;
	
	@FXML
	Label messLabel,tbox;
	
	

	public void LogIn(ActionEvent e) {
		if (!emailField.getText().isBlank() && !passField.getText().isBlank()) {
			messLabel.setText("Incorrect username or password");
		} else {
			messLabel.setText("No inputs taken");
		}	
		ValidateCon((Stage) signInBTN.getScene().getWindow()); // Pass the stage
	}

	public void openMF(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/MainFrame.fxml"));
			Scene scene = new Scene(root, 1200, 850);
			scene.getStylesheets().add(getClass().getResource("/application/MainFrame.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
			stage.isResizable();
		} catch (Exception e) {
			e.printStackTrace();
			// Handle any exceptions that may occur during FXML loading
		}
	}
	
	public void openEI(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/encoderui/Encoder.fxml"));
			Scene scene = new Scene(root, 1200, 850);
			scene.getStylesheets().add(getClass().getResource("/encoderui/Encoder.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
			stage.isResizable();
		} catch (Exception e) {
			e.printStackTrace();
			// Handle any exceptions that may occur during FXML loading
		}
	}

	public void ValidateCon(Stage stage) {
		Connection con = DatabaseManager.getConnection();

		String verifyLogin = "SELECT * FROM users WHERE username = ? AND password = ?";

		try {
			PreparedStatement stmt = con.prepareStatement(verifyLogin);
			stmt.setString(1, emailField.getText());
			stmt.setString(2, passField.getText());

			ResultSet result = stmt.executeQuery();

			if (result.next()) {
				// gets username
				int sessionId = result.getInt("ID");
				String sessionUsername = result.getString("username");
				String sessionType = result.getString("type");
				String sessionLastname = result.getString("lname");
				String sessionFirstname = result.getString("fname");

				UserSession session = UserSession.getInstance();
				session.setId(sessionId);
				session.setUsername(sessionUsername);
				session.setLastname(sessionLastname);
				session.setFirstname(sessionFirstname);
				session.setType(sessionType);

				messLabel.setText("Logging in...");
				
				if ("admin".equals(sessionType)) {
				    openMF(stage); // Pass the stage
				} else {
				    openEI(stage);
				}
				
			} else {
				showAlert("Error", "Wrong Credentials", "Please valid Username and Password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			messLabel.setText("Database error");
			showAlert("Error", "Wrong Credentials", "Please valid Username and Password");
		}
	}

	public void getInfo(ActionEvent e) {
		DatabaseManager connectNow = new DatabaseManager();
		Connection con = connectNow.getConnection();

		String fname = regFname.getText();
		String lname = regLname.getText();
		String username = regUser.getText();
		String password = regPass.getText();

		String infos = "INSERT INTO users	(FName, LName, username, password) VALUE(?,?,?,?)";

		try {
			PreparedStatement stmt = con.prepareStatement(infos);

			stmt.setString(1, fname);
			stmt.setString(2, lname);
			stmt.setString(3, username);
			stmt.setString(4, password);
			if (fname.isEmpty() || lname.isEmpty() || username.isEmpty() || password.isEmpty()) {
				tbox.setText("Kulang kulang ka ba?");
				return;
			}
			int rs = stmt.executeUpdate();
			if (rs == 1) {
				tbox.setText("Succesfully Registered");
				regFname.setText("");
				regLname.setText("");
				regUser.setText("");
				regPass.setText("");

			} else {
				tbox.setText("Failed Registration");

			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        Exit.setOnMouseClicked(event -> {
			System.exit(0);
		});
		
	}
	
	private void showAlert(String title, String header, String content) {
    	Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
        alert.showAndWait();

    }

	

}