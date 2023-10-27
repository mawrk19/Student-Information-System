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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;

public class MainFrameController implements Initializable{

	@FXML
	private BorderPane Borderpane;
	
    @FXML 
    private StackPane stackarea;
    
    @FXML
    private Button Dashboard,  StudentProf, Timetable, Enrollment;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
	
	@FXML
	public void Dashboardbtn(ActionEvent event) throws IOException {
		 Dashboard.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Dashboard.setTextFill(Color.BLACK);
		 StudentProf.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 StudentProf.setTextFill(Color.WHITE);
		 Timetable.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Timetable.setTextFill(Color.WHITE);
		 Enrollment.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Enrollment.setTextFill(Color.WHITE);
		 
			 Pane dashbordstage = FXMLLoader.load(getClass().getResource("/dashboard/Dashboard.fxml"));
				stackarea.getChildren().removeAll();
				stackarea.getChildren().setAll(dashbordstage);
		 
    }
	
	@FXML
	public void StudentProfbtn(ActionEvent event) throws IOException {
		 Dashboard.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Dashboard.setTextFill(Color.WHITE);
		 StudentProf.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 StudentProf.setTextFill(Color.BLACK);
		 Timetable.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Timetable.setTextFill(Color.WHITE);
		 Enrollment.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Enrollment.setTextFill(Color.WHITE);
		 
			 Pane studprofstage = FXMLLoader.load(getClass().getResource("/studentprof/StudentProfile.fxml"));
				stackarea.getChildren().removeAll();
				stackarea.getChildren().setAll(studprofstage);
		 
    }
    
	public void Timetablebtn(ActionEvent event) throws IOException {
		 Dashboard.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Dashboard.setTextFill(Color.WHITE);
		 StudentProf.setStyle("-fx-background-color:  #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 StudentProf.setTextFill(Color.WHITE);
		 Timetable.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Timetable.setTextFill(Color.BLACK);
		 Enrollment.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Enrollment.setTextFill(Color.WHITE);
		 
			 Pane studprofstage = FXMLLoader.load(getClass().getResource("/timetable/Timetable.fxml"));
				stackarea.getChildren().removeAll();
				stackarea.getChildren().setAll(studprofstage);
		 
   }
	
	public void Enrollmentbtn(ActionEvent event) throws IOException {
		 Dashboard.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Dashboard.setTextFill(Color.WHITE);
		 StudentProf.setStyle("-fx-background-color:  #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 StudentProf.setTextFill(Color.WHITE);
		 Timetable.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Timetable.setTextFill(Color.WHITE);
		 Enrollment.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Enrollment.setTextFill(Color.BLACK);
		 
			 Pane studprofstage = FXMLLoader.load(getClass().getResource("/enrollment/MainEnrollment.fxml"));
				stackarea.getChildren().removeAll();
				stackarea.getChildren().setAll(studprofstage);
		 
  }
   

}
    