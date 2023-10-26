package com.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    @FXML
    TextField regFname;
    @FXML
    TextField regLname;
    @FXML
    TextField regUser;
    @FXML
    TextField regPass;
    
    JOptionPane tbox = new JOptionPane();
    
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
            stage.setScene(scene);        
            stage.show();
            stage.isResizable();
            stage.initStyle(StageStyle.DECORATED);
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
    
    public void getInfo(ActionEvent e) {
    	DatabaseManager connectNow = new DatabaseManager();
        Connection con = connectNow.getConnection();
        
    	String fname = regFname.getText();
    	String lname = regLname.getText();
    	String username = regUser.getText();
    	String password = regPass.getText();
    	
    	String infos = "INSERT INTO encoders(FName, LName, username, password) VALUE(?,?,?,?)";
    	
    	try {
    		PreparedStatement stmt = con.prepareStatement(infos);
    		
    		stmt.setString(1, fname);
			stmt.setString(2, lname);
			stmt.setString(3, username);
			stmt.setString(4, password);
			if (fname.isEmpty() || lname.isEmpty() || username.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(tbox, "add something");
				return;
			}
			int rs = stmt.executeUpdate();
			if (rs == 1) {
				JOptionPane.showMessageDialog(tbox, "You Have Successfully Registered");
				regFname.setText("");
				regLname.setText("");
				regUser.setText("");
				regPass.setText("");
				
			} else {
				JOptionPane.showMessageDialog(tbox, "You Failed Now Flee");
			
			}
    	} catch (SQLException ex){
    		ex.printStackTrace();
    	}
    	
    	
    }
    
    
    
}