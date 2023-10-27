package com.login;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.ImageView;

import application.DatabaseManager;
import application.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FormController {
	
	@FXML
	TextField emailField, regFname, regLname, regUser, regPass;
	@FXML
	PasswordField passField;
	@FXML
	Button signInBTN;
	@FXML
	Label messLabel, tbox;
	@FXML
	ImageView mageView;
		
	void LogIn(ActionEvent e) {
		if (!emailField.getText().isBlank() && !passField.getText().isBlank()) {
			messLabel.setText("Incorrect username or password");
		} else {
			messLabel.setText("No inputs taken");
		}
		ValidateCon((Stage) signInBTN.getScene().getWindow()); // Pass the stage
	}

	void openMF(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/MainFrame.fxml"));
			Scene scene = new Scene(root);
			stage.setMaximized(true);
			stage.setScene(scene);
			stage.show();
			stage.isResizable();
		} catch (Exception e) {
			e.printStackTrace();
			// Handle any exceptions that may occur during FXML loading
		}
	}

	void ValidateCon(Stage stage) {
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
				String sessionUsername = result.getString("fname");

				UserSession session = UserSession.getInstance();
				session.setId(sessionId);
				session.setUsername(sessionUsername);

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

	void getInfo(ActionEvent e) {
		DatabaseManager connectNow = new DatabaseManager();
		Connection con = connectNow.getConnection();

		String fname = regFname.getText();
		String lname = regLname.getText();
		String username = regUser.getText();
		String password = regPass.getText();

		String infos = "INSERT INTO users (FName, LName, username, password) VALUE(?,?,?,?)";

		try {
			PreparedStatement stmt = con.prepareStatement(infos);

			stmt.setString(1, fname);
			stmt.setString(2, lname);
			stmt.setString(3, username);
			stmt.setString(4, password);
			if (fname.isEmpty() || lname.isEmpty() || username.isEmpty() || password.isEmpty()) {
				//EB easter egg
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
	
//	void getImage() {
//	    DatabaseManager connectNow = new DatabaseManager();
//	    Connection con = connectNow.getConnection();
//	    
//	    try {
//	        File imageFile = new File("path_to_image.jpg");
//	        FileInputStream fis = new FileInputStream(imageFile);
//
//	        String insertSql = "INSERT INTO Images (ImageName, ImageData) VALUES (?, ?)";
//	        PreparedStatement pstmt = con.prepareStatement(insertSql);
//	        pstmt.setString(1, "MyImage");
//	        pstmt.setBinaryStream(2, fis, (int) imageFile.length());
//	        pstmt.executeUpdate();
//
//	        pstmt.close();
//	    } catch (FileNotFoundException e) {
//	        e.printStackTrace();
//	        // Handle the error, e.g., show an error message to the user
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	        // Handle SQL-related errors
//	    } finally {
//	        try {
//	            con.close();
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	            // Handle any database connection closing errors
//	        }
//	    }
//	}

	
//	void showImage() {
//		DatabaseManager connectNow = new DatabaseManager();
//		Connection con = connectNow.getConnection();
//		int imageId = 1; // Change this to the desired ImageID
//
//		String selectSql = "SELECT ImageData FROM student WHERE ImageID = ?";
//		PreparedStatement pstmt = con.prepareStatement(selectSql);
//		pstmt.setInt(1, imageId);
//		ResultSet rs = pstmt.executeQuery();
//
//		if (rs.next()) {
//		    Blob imageBlob = rs.getBlob("ImageData");
//		    InputStream imageStream = imageBlob.getBinaryStream();
//
//		    Image image = new Image(imageStream);
//		    ImageView imageView = new ImageView(image);
//		    // Add the imageView to your JavaFX scene for display
//		}
//
//		rs.close();
//		pstmt.close();
//		con.close();
//		
//	}
}