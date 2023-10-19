package com.login;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import application.DatabaseManager;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainController implements Initializable {   
     
    @FXML
    private VBox vbox;    
    private Parent fxml;
    @FXML
	TextField emailField;
	@FXML
	PasswordField passField;
	@FXML
	Button signInBTN;
	@FXML
	Label messLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(vbox.getLayoutX() * 20);
        t.play();
        t.setOnFinished((e) ->{
            try{
                fxml = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            }catch(IOException ex){
                
            }
        });
    }
    @FXML
    private void open_signin(ActionEvent event){
          TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(vbox.getLayoutX() * 20);
        t.play();
        t.setOnFinished((e) ->{
            try{
                fxml = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            }catch(IOException ex){
                
            }
        });
    }   
    @FXML
    private void open_signup(ActionEvent event){
          TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(0);
        t.play();
        t.setOnFinished((e) ->{
            try{
                fxml = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            }catch(IOException ex){
                
            }
        });
    } 
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
		
		String verifyLogin = "'select * from where username ='" + emailField.getText() + "'and password'" + passField.getText()+"'";
		
		try {
			Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery(verifyLogin);
			
			while(result.next()) {
				if(result.getInt(1) == 1) {
					messLabel.setText("Tanginanyu");
				} else {
					messLabel.setText("Tanginanyu ule");
				}
			} 
			
			
			} catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
    }
    

