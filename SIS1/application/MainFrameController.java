package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class MainFrameController implements Initializable{

    @FXML
    private Button StudentProf;
    
    @FXML 
    private StackPane stackarea;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
	
	@FXML
	public void StudentProfbtn(ActionEvent event) throws IOException {
		 StudentProf.setStyle("-fx-background-color: #eff0f3;-fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 StudentProf.setTextFill(Color.BLACK);
		 
			 //Parent fxml = FXMLLoader.load(getClass().getResource("/studentprof/StudentProfile.fxml"));
				//stackarea.getChildren().removeAll();
				//stackarea.getChildren().setAll(fxml);
		 
    }
    

}
    