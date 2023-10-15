package application;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LogInController {

	public void LogIn() {
		
	}
	
	
	@FXML
	private TextField EncUserName;
	@FXML
	private PasswordField EncPassWord;
	@FXML
	private Label wrongLogin;
	
	private void EncoderLogIn(ActionEvent event) throws IOException { 
		checkLogin();
		
	}
	
	private void checkLogin() throws IOException {
		Main m = new Main();
		if (EncUserName.getText().toString().equals("Mawrk") && EncPassWord.getText().toString().equals("123") ) {
			wrongLogin.setText("Success!");
			
			//m.changeScene();
		}
		
		
	}
	
	
}
