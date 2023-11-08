package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainFrameController implements Initializable{

	@FXML
	private BorderPane Borderpane;
	
	@FXML
	private Label Exit, Maximize, Minimize;
	
    @FXML 
    private AnchorPane contentarea;
    
    @FXML
    private Button Dashboard,  StudentProf, Timetable, Enrollment, Schedule;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		Exit.setOnMouseClicked(event -> {
			System.exit(0);
		});
		
		try {
			Parent fxml = FXMLLoader.load(getClass().getResource("/application/WelcomePage.fxml"));
			contentarea.getChildren().removeAll();
			contentarea.getChildren().setAll(fxml);
			AnchorPane.setLeftAnchor(fxml, 10.0);
			AnchorPane.setRightAnchor(fxml, 10.0);
			AnchorPane.setTopAnchor(fxml, 10.0);
			AnchorPane.setBottomAnchor(fxml, 20.0);
		} catch (IOException ex) {
			Logger.getLogger(MainFrameController.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
	
	@FXML
	public void Maximizebtn(MouseEvent event) {
		Stage stage = (Stage) Maximize.getScene().getWindow();
		if(stage.isMaximized()) {
			stage.setMaximized(false);
		}else {
			stage.setMaximized(true);
		}
	}
	
	public void Minimizebtn(MouseEvent event) {
		Stage stage = (Stage) Minimize.getScene().getWindow();
		stage.setIconified(true);
	}
	
	@FXML
	public void Dashboardbtn(ActionEvent event) throws IOException {
		 Dashboard.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Dashboard.setTextFill(Color.BLACK);
		 StudentProf.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 StudentProf.setTextFill(Color.WHITE);
		 Timetable.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Timetable.setTextFill(Color.WHITE);
		 Schedule.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Schedule.setTextFill(Color.WHITE);
		 Enrollment.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Enrollment.setTextFill(Color.WHITE);
		 
		 Pane dashboardstage = FXMLLoader.load(getClass().getResource("/dashboard/Dashboard.fxml"));
		 contentarea.getChildren().removeAll();
		 contentarea.getChildren().setAll(dashboardstage);
		 AnchorPane.setLeftAnchor(dashboardstage, 10.0);
		 AnchorPane.setRightAnchor(dashboardstage, 10.0);
		 AnchorPane.setTopAnchor(dashboardstage, 10.0);
		 AnchorPane.setBottomAnchor(dashboardstage, 20.0);
    }
	
	@FXML
	public void StudentProfbtn(ActionEvent event) throws IOException {
		 Dashboard.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Dashboard.setTextFill(Color.WHITE);
		 StudentProf.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 StudentProf.setTextFill(Color.BLACK);
		 Timetable.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Timetable.setTextFill(Color.WHITE);
		 Schedule.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Schedule.setTextFill(Color.WHITE);
		 Enrollment.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Enrollment.setTextFill(Color.WHITE);
		 
		 Pane studprofstage = FXMLLoader.load(getClass().getResource("/studentprof/StudentProfile.fxml"));
		 contentarea.getChildren().removeAll();
		 contentarea.getChildren().setAll(studprofstage);
		 AnchorPane.setLeftAnchor(studprofstage, 10.0);
		 AnchorPane.setRightAnchor(studprofstage, 10.0);
		 AnchorPane.setTopAnchor(studprofstage, 10.0);
		 AnchorPane.setBottomAnchor(studprofstage, 20.0);
		 
    }
    
	public void Timetablebtn(ActionEvent event) throws IOException {
		 Dashboard.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Dashboard.setTextFill(Color.WHITE);
		 StudentProf.setStyle("-fx-background-color:  #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 StudentProf.setTextFill(Color.WHITE);
		 Timetable.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Timetable.setTextFill(Color.BLACK);
		 Schedule.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Schedule.setTextFill(Color.WHITE);
		 Enrollment.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Enrollment.setTextFill(Color.WHITE);
		 
		 Pane timetablestage = FXMLLoader.load(getClass().getResource("/timetable/Timetable.fxml"));
		 contentarea.getChildren().removeAll();
		 contentarea.getChildren().setAll(timetablestage);
		 AnchorPane.setLeftAnchor(timetablestage, 10.0);
		 AnchorPane.setRightAnchor(timetablestage, 10.0);
		 AnchorPane.setTopAnchor(timetablestage, 10.0);
		 AnchorPane.setBottomAnchor(timetablestage, 20.0);
		 
		 
   }
	
	public void Schedulebtn(ActionEvent event) throws IOException {
		 Dashboard.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Dashboard.setTextFill(Color.WHITE);
		 StudentProf.setStyle("-fx-background-color:  #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 StudentProf.setTextFill(Color.WHITE);
		 Timetable.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Timetable.setTextFill(Color.WHITE);
		 Schedule.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Schedule.setTextFill(Color.BLACK);
		 Enrollment.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Enrollment.setTextFill(Color.WHITE);
		 
		 Pane schedulestage = FXMLLoader.load(getClass().getResource("/schedule/Schedule.fxml"));
		 contentarea.getChildren().removeAll();
		 contentarea.getChildren().setAll(schedulestage);
		 AnchorPane.setLeftAnchor(schedulestage, 10.0);
		 AnchorPane.setRightAnchor(schedulestage, 10.0);
		 AnchorPane.setTopAnchor(schedulestage, 10.0);
		 AnchorPane.setBottomAnchor(schedulestage, 20.0);
		 
 }

	
	public void Enrollmentbtn(ActionEvent event) throws IOException {
		 Dashboard.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Dashboard.setTextFill(Color.WHITE);
		 StudentProf.setStyle("-fx-background-color:  #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 StudentProf.setTextFill(Color.WHITE);
		 Timetable.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Timetable.setTextFill(Color.WHITE);
		 Schedule.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Schedule.setTextFill(Color.WHITE);
		 Enrollment.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		 Enrollment.setTextFill(Color.BLACK);
		 
		 Pane enrollmentstage = FXMLLoader.load(getClass().getResource("/enrollment/Enrollment.fxml"));
		 contentarea.getChildren().removeAll();
		 contentarea.getChildren().setAll(enrollmentstage);
		 AnchorPane.setLeftAnchor(enrollmentstage, 10.0);
		 AnchorPane.setRightAnchor(enrollmentstage, 10.0);
		 AnchorPane.setTopAnchor(enrollmentstage, 10.0);
		 AnchorPane.setBottomAnchor(enrollmentstage, 20.0);
		 
  }
   
}
    