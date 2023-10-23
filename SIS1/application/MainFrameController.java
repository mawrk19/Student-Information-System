package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class MainFrameController implements Initializable{

    @FXML
    private Label StudentProf;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		StudentProf.setOnMouseClicked(this::StudentProfScene);
	}
	
	@FXML
    void StudentProfScene(MouseEvent event) {
		 StudentProf.setStyle("-fx-background-color: #eff0f3;-fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 StudentProf.setTextFill(Color.BLACK);
    }
    
    
    

}
    