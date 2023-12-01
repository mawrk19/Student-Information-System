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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Background;

public class MainFrameController implements Initializable{

	@FXML
	private BorderPane Borderpane;
	
	@FXML
	private Label Exit, Maximize, Minimize;
	
    @FXML 
    private AnchorPane contentarea;
    
    @FXML
    public Button Dashboard, StudentProf, Timetable, Enrollment, Schedule, oldEnrollment, Grading, Profileicn, Students;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		Exit.setOnMouseClicked(event -> {
			System.exit(0);
		});
		
		try {
			BackgroundFill ube = new BackgroundFill(Color.web("#3c5199"), null, null);
			
			Profileicn.setStyle("-fx-background-color: #5d76dc; -fx-border-radius: 50; -fx-background-radius: 25;");
			
			Dashboard.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
			Dashboard.setTextFill(Color.BLACK);
			StudentProf.setStyle("-fx-border-radius: 25 0 0 25;");
	        StudentProf.setBackground(new Background(ube));
	        StudentProf.setTextFill(Color.WHITE);
	        Timetable.setStyle("-fx-border-radius: 25 0 0 25;");
	        Timetable.setBackground(new Background(ube));
	        Timetable.setTextFill(Color.WHITE);
	        Schedule.setStyle("-fx-border-radius: 25 0 0 25;");
	        Schedule.setBackground(new Background(ube));
	        Schedule.setTextFill(Color.WHITE);
	        Grading.setStyle("-fx-border-radius: 25 0 0 25;");
	        Grading.setBackground(new Background(ube));
	        Grading.setTextFill(Color.WHITE);
	        Enrollment.setStyle("-fx-border-radius: 25 0 0 25;");
	        Enrollment.setBackground(new Background(ube));
	        Enrollment.setTextFill(Color.WHITE);
	        oldEnrollment.setStyle("-fx-border-radius: 25 0 0 25;");
	        oldEnrollment.setBackground(new Background(ube));
	        oldEnrollment.setTextFill(Color.WHITE);
	        Students.setStyle("-fx-border-radius: 25 0 0 25;");
	        Students.setBackground(new Background(ube));
	        Students.setTextFill(Color.WHITE);
			
			 
			 Parent dashboardstage = FXMLLoader.load(getClass().getResource("/dashboard/Dashboard.fxml"));
			 contentarea.getChildren().removeAll();
			 contentarea.getChildren().setAll(dashboardstage);
			 AnchorPane.setLeftAnchor(dashboardstage, 10.0);
			 AnchorPane.setRightAnchor(dashboardstage, 10.0);
			 AnchorPane.setTopAnchor(dashboardstage, 10.0);
			 AnchorPane.setBottomAnchor(dashboardstage, 20.0);
			 UserSession session = UserSession.getInstance();
		     String username = session.getUsername();
			 System.out.println(username);
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
	
	public void profileicnbtn(ActionEvent event) throws IOException {
		
		Profileicn.setStyle("-fx-background-color: #5d76dc; -fx-border-radius: 50; -fx-background-radius: 25;");
		
		BackgroundFill ube = new BackgroundFill(Color.web("#3c5199"), null, null);
		
		Dashboard.setStyle("-fx-background-radius: 25 0 0 25;");
		Dashboard.setTextFill(Color.WHITE);
		StudentProf.setStyle("-fx-border-radius: 25 0 0 25;");
        StudentProf.setBackground(new Background(ube));
        StudentProf.setTextFill(Color.WHITE);
        Timetable.setStyle("-fx-border-radius: 25 0 0 25;");
        Timetable.setBackground(new Background(ube));
        Timetable.setTextFill(Color.WHITE);
        Schedule.setStyle("-fx-border-radius: 25 0 0 25;");
        Schedule.setBackground(new Background(ube));
        Schedule.setTextFill(Color.WHITE);
        Grading.setStyle("-fx-border-radius: 25 0 0 25;");
        Grading.setBackground(new Background(ube));
        Grading.setTextFill(Color.WHITE);
        Enrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        Enrollment.setBackground(new Background(ube));
        Enrollment.setTextFill(Color.WHITE);
        oldEnrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        oldEnrollment.setBackground(new Background(ube));
        oldEnrollment.setTextFill(Color.WHITE);
        Students.setStyle("-fx-border-radius: 25 0 0 25;");
        Students.setBackground(new Background(ube));
        Students.setTextFill(Color.WHITE);
		 
		 Pane profilestage = FXMLLoader.load(getClass().getResource("/profile/Profile.fxml"));
		 contentarea.getChildren().removeAll();
		 contentarea.getChildren().setAll(profilestage);
		 AnchorPane.setLeftAnchor(profilestage, 10.0);
		 AnchorPane.setRightAnchor(profilestage, 10.0);
		 AnchorPane.setTopAnchor(profilestage, 10.0);
		 AnchorPane.setBottomAnchor(profilestage, 20.0);
		 
	}
	
	@FXML
	public void Dashboardbtn(ActionEvent event) throws IOException {
		
		BackgroundFill ube = new BackgroundFill(Color.web("#3c5199"), null, null);
		
		Profileicn.setStyle("-fx-background-color: #5d76dc; -fx-border-radius: 50; -fx-background-radius: 25;");
		
		Dashboard.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		Dashboard.setTextFill(Color.BLACK);
		StudentProf.setStyle("-fx-border-radius: 25 0 0 25;");
        StudentProf.setBackground(new Background(ube));
        StudentProf.setTextFill(Color.WHITE);
        Timetable.setStyle("-fx-border-radius: 25 0 0 25;");
        Timetable.setBackground(new Background(ube));
        Timetable.setTextFill(Color.WHITE);
        Schedule.setStyle("-fx-border-radius: 25 0 0 25;");
        Schedule.setBackground(new Background(ube));
        Schedule.setTextFill(Color.WHITE);
        Grading.setStyle("-fx-border-radius: 25 0 0 25;");
        Grading.setBackground(new Background(ube));
        Grading.setTextFill(Color.WHITE);
        Enrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        Enrollment.setBackground(new Background(ube));
        Enrollment.setTextFill(Color.WHITE);
        oldEnrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        oldEnrollment.setBackground(new Background(ube));
        oldEnrollment.setTextFill(Color.WHITE);
        Students.setStyle("-fx-border-radius: 25 0 0 25;");
        Students.setBackground(new Background(ube));
        Students.setTextFill(Color.WHITE);
		
		 
		 Pane dashboardstage = FXMLLoader.load(getClass().getResource("/dashboard/Dashboard.fxml"));
		 contentarea.getChildren().removeAll();
		 contentarea.getChildren().setAll(dashboardstage);
		 AnchorPane.setLeftAnchor(dashboardstage, 10.0);
		 AnchorPane.setRightAnchor(dashboardstage, 10.0);
		 AnchorPane.setTopAnchor(dashboardstage, 10.0);
		 AnchorPane.setBottomAnchor(dashboardstage, 20.0);
		 UserSession session = UserSession.getInstance();
	     String username = session.getUsername();
		 System.out.println(username);
    }
	
	@FXML
	public void StudentProfbtn(ActionEvent event) throws IOException {
		
		BackgroundFill ube = new BackgroundFill(Color.web("#3c5199"), null, null);
		
		Profileicn.setStyle("-fx-background-color: #5d76dc; -fx-border-radius: 50; -fx-background-radius: 25;");
		
		Dashboard.setStyle("-fx-border-radius: 25 0 0 25;");
		Dashboard.setBackground(new Background(ube));
		Dashboard.setTextFill(Color.WHITE);
		StudentProf.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
        StudentProf.setTextFill(Color.BLACK);
        Timetable.setStyle("-fx-border-radius: 25 0 0 25;");
        Timetable.setBackground(new Background(ube));
        Timetable.setTextFill(Color.WHITE);
        Schedule.setStyle("-fx-border-radius: 25 0 0 25;");
        Schedule.setBackground(new Background(ube));
        Schedule.setTextFill(Color.WHITE);
        Grading.setStyle("-fx-border-radius: 25 0 0 25;");
        Grading.setBackground(new Background(ube));
        Grading.setTextFill(Color.WHITE);
        Enrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        Enrollment.setBackground(new Background(ube));
        Enrollment.setTextFill(Color.WHITE);
        oldEnrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        oldEnrollment.setBackground(new Background(ube));
        oldEnrollment.setTextFill(Color.WHITE);
        Students.setStyle("-fx-border-radius: 25 0 0 25;");
        Students.setBackground(new Background(ube));
        Students.setTextFill(Color.WHITE);
		
		 
		 Pane studprofstage = FXMLLoader.load(getClass().getResource("/studentprof/StudentProfile.fxml"));
		 contentarea.getChildren().removeAll();
		 contentarea.getChildren().setAll(studprofstage);
		 AnchorPane.setLeftAnchor(studprofstage, 10.0);
		 AnchorPane.setRightAnchor(studprofstage, 10.0);
		 AnchorPane.setTopAnchor(studprofstage, 10.0);
		 AnchorPane.setBottomAnchor(studprofstage, 20.0);
		 
    }
    
	public void Timetablebtn(ActionEvent event) throws IOException {
		
		BackgroundFill ube = new BackgroundFill(Color.web("#3c5199"), null, null);
		
		Profileicn.setStyle("-fx-background-color: #5d76dc; -fx-border-radius: 50; -fx-background-radius: 25;");
		
		Dashboard.setStyle("-fx-border-radius: 25 0 0 25;");
		Dashboard.setBackground(new Background(ube));
		Dashboard.setTextFill(Color.WHITE);
		StudentProf.setStyle("-fx-border-radius: 25 0 0 25;");
        StudentProf.setBackground(new Background(ube));
        StudentProf.setTextFill(Color.WHITE);
        Timetable.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
        Timetable.setTextFill(Color.BLACK);
        Schedule.setStyle("-fx-border-radius: 25 0 0 25;");
        Schedule.setBackground(new Background(ube));
        Schedule.setTextFill(Color.WHITE);
        Grading.setStyle("-fx-border-radius: 25 0 0 25;");
        Grading.setBackground(new Background(ube));
        Grading.setTextFill(Color.WHITE);
        Enrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        Enrollment.setBackground(new Background(ube));
        Enrollment.setTextFill(Color.WHITE);
        oldEnrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        oldEnrollment.setBackground(new Background(ube));
        oldEnrollment.setTextFill(Color.WHITE);
        Students.setStyle("-fx-border-radius: 25 0 0 25;");
        Students.setBackground(new Background(ube));
        Students.setTextFill(Color.WHITE);
		
		 
		 Pane timetablestage = FXMLLoader.load(getClass().getResource("/timetable/Timetable.fxml"));
		 contentarea.getChildren().removeAll();
		 contentarea.getChildren().setAll(timetablestage);
		 AnchorPane.setLeftAnchor(timetablestage, 10.0);
		 AnchorPane.setRightAnchor(timetablestage, 10.0);
		 AnchorPane.setTopAnchor(timetablestage, 10.0);
		 AnchorPane.setBottomAnchor(timetablestage, 20.0);
		 
		 
   }
	
	public void Schedulebtn(ActionEvent event) throws IOException {
		
		BackgroundFill ube = new BackgroundFill(Color.web("#3c5199"), null, null);
		
		Profileicn.setStyle("-fx-background-color: #5d76dc; -fx-border-radius: 50; -fx-background-radius: 25;");
		
		Dashboard.setStyle("-fx-border-radius: 25 0 0 25;");
		Dashboard.setBackground(new Background(ube));
		Dashboard.setTextFill(Color.WHITE);
		StudentProf.setStyle("-fx-border-radius: 25 0 0 25;");
        StudentProf.setBackground(new Background(ube));
        StudentProf.setTextFill(Color.WHITE);
        Timetable.setStyle("-fx-border-radius: 25 0 0 25;");
        Timetable.setBackground(new Background(ube));
        Timetable.setTextFill(Color.WHITE);
        Schedule.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
        Schedule.setTextFill(Color.BLACK);
        Grading.setStyle("-fx-border-radius: 25 0 0 25;");
        Grading.setBackground(new Background(ube));
        Grading.setTextFill(Color.WHITE);
        Enrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        Enrollment.setBackground(new Background(ube));
        Enrollment.setTextFill(Color.WHITE);
        oldEnrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        oldEnrollment.setBackground(new Background(ube));
        oldEnrollment.setTextFill(Color.WHITE);
        Students.setStyle("-fx-border-radius: 25 0 0 25;");
        Students.setBackground(new Background(ube));
        Students.setTextFill(Color.WHITE);
		
		 
		 Pane schedulestage = FXMLLoader.load(getClass().getResource("/schedule/Schedule.fxml"));
		 contentarea.getChildren().removeAll();
		 contentarea.getChildren().setAll(schedulestage);
		 AnchorPane.setLeftAnchor(schedulestage, 10.0);
		 AnchorPane.setRightAnchor(schedulestage, 10.0);
		 AnchorPane.setTopAnchor(schedulestage, 10.0);
		 AnchorPane.setBottomAnchor(schedulestage, 20.0);
		 
	}
	
	public void Gradingbtn(ActionEvent event) throws IOException {
		
		BackgroundFill ube = new BackgroundFill(Color.web("#3c5199"), null, null);
		
		Profileicn.setStyle("-fx-background-color: #5d76dc; -fx-border-radius: 50; -fx-background-radius: 25;");
		
		Dashboard.setStyle("-fx-border-radius: 25 0 0 25;");
		Dashboard.setBackground(new Background(ube));
		Dashboard.setTextFill(Color.WHITE);
		StudentProf.setStyle("-fx-border-radius: 25 0 0 25;");
        StudentProf.setBackground(new Background(ube));
        StudentProf.setTextFill(Color.WHITE);
        Timetable.setStyle("-fx-border-radius: 25 0 0 25;");
        Timetable.setBackground(new Background(ube));
        Timetable.setTextFill(Color.WHITE);
        Schedule.setStyle("-fx-border-radius: 25 0 0 25;");
        Schedule.setBackground(new Background(ube));
        Schedule.setTextFill(Color.WHITE);
        Grading.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
        Grading.setTextFill(Color.BLACK);
        Enrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        Enrollment.setBackground(new Background(ube));
        Enrollment.setTextFill(Color.WHITE);
        oldEnrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        oldEnrollment.setBackground(new Background(ube));
        oldEnrollment.setTextFill(Color.WHITE);
        Students.setStyle("-fx-border-radius: 25 0 0 25;");
        Students.setBackground(new Background(ube));
        Students.setTextFill(Color.WHITE);
		
		 
		 Pane gradingstage = FXMLLoader.load(getClass().getResource("/grading/Grading.fxml"));
		 contentarea.getChildren().removeAll();
		 contentarea.getChildren().setAll(gradingstage);
		 AnchorPane.setLeftAnchor(gradingstage, 10.0);
		 AnchorPane.setRightAnchor(gradingstage, 10.0);
		 AnchorPane.setTopAnchor(gradingstage, 10.0);
		 AnchorPane.setBottomAnchor(gradingstage, 20.0);
		 
	}

	
	public void Enrollmentbtn(ActionEvent event) throws IOException {
		
		BackgroundFill ube = new BackgroundFill(Color.web("#3c5199"), null, null);
		
		Profileicn.setStyle("-fx-background-color: #5d76dc; -fx-border-radius: 50; -fx-background-radius: 25;");
		
		Dashboard.setStyle("-fx-border-radius: 25 0 0 25;");
		Dashboard.setBackground(new Background(ube));
		Dashboard.setTextFill(Color.WHITE);
		StudentProf.setStyle("-fx-border-radius: 25 0 0 25;");
        StudentProf.setBackground(new Background(ube));
        StudentProf.setTextFill(Color.WHITE);
        Timetable.setStyle("-fx-border-radius: 25 0 0 25;");
        Timetable.setBackground(new Background(ube));
        Timetable.setTextFill(Color.WHITE);
        Schedule.setStyle("-fx-border-radius: 25 0 0 25;");
        Schedule.setBackground(new Background(ube));
        Schedule.setTextFill(Color.WHITE);
        Grading.setStyle("-fx-border-radius: 25 0 0 25;");
        Grading.setBackground(new Background(ube));
        Grading.setTextFill(Color.WHITE);
        Enrollment.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
        Enrollment.setTextFill(Color.BLACK);
        oldEnrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        oldEnrollment.setBackground(new Background(ube));
        oldEnrollment.setTextFill(Color.WHITE);
        Students.setStyle("-fx-border-radius: 25 0 0 25;");
        Students.setBackground(new Background(ube));
        Students.setTextFill(Color.WHITE);
		

		 Pane enrollmentstage = FXMLLoader.load(getClass().getResource("/enrollment/Enrollment.fxml"));
		 contentarea.getChildren().removeAll();
		 contentarea.getChildren().setAll(enrollmentstage);
		 AnchorPane.setLeftAnchor(enrollmentstage, 10.0);
		 AnchorPane.setRightAnchor(enrollmentstage, 10.0);
		 AnchorPane.setTopAnchor(enrollmentstage, 10.0);
		 AnchorPane.setBottomAnchor(enrollmentstage, 20.0);
		 
		 
	}
	
	public void oldEnrollmentbtn(ActionEvent event) throws IOException {
		
		BackgroundFill ube = new BackgroundFill(Color.web("#3c5199"), null, null);
		
		Profileicn.setStyle("-fx-background-color: #5d76dc; -fx-border-radius: 50; -fx-background-radius: 25;");
		
		Dashboard.setStyle("-fx-border-radius: 25 0 0 25;");
		Dashboard.setBackground(new Background(ube));
		Dashboard.setTextFill(Color.WHITE);
		StudentProf.setStyle("-fx-border-radius: 25 0 0 25;");
        StudentProf.setBackground(new Background(ube));
        StudentProf.setTextFill(Color.WHITE);
        Timetable.setStyle("-fx-border-radius: 25 0 0 25;");
        Timetable.setBackground(new Background(ube));
        Timetable.setTextFill(Color.WHITE);
        Schedule.setStyle("-fx-border-radius: 25 0 0 25;");
        Schedule.setBackground(new Background(ube));
        Schedule.setTextFill(Color.WHITE);
        Grading.setStyle("-fx-border-radius: 25 0 0 25;");
        Grading.setBackground(new Background(ube));
        Grading.setTextFill(Color.WHITE);
        Enrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        Enrollment.setBackground(new Background(ube));
        Enrollment.setTextFill(Color.WHITE);
        oldEnrollment.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
        oldEnrollment.setTextFill(Color.BLACK);
        Students.setStyle("-fx-border-radius: 25 0 0 25;");
        Students.setBackground(new Background(ube));
        Students.setTextFill(Color.WHITE);
        
		 
		 Pane oldenrollmentstage = FXMLLoader.load(getClass().getResource("/enrollment/OldEnrollment.fxml"));
		 contentarea.getChildren().removeAll();
		 contentarea.getChildren().setAll(oldenrollmentstage);
		 AnchorPane.setLeftAnchor(oldenrollmentstage, 10.0);
		 AnchorPane.setRightAnchor(oldenrollmentstage, 10.0);
		 AnchorPane.setTopAnchor(oldenrollmentstage, 10.0);
		 AnchorPane.setBottomAnchor(oldenrollmentstage, 20.0);
		 
	}
	
	public void studentsbtn(ActionEvent event) throws IOException {
		
		BackgroundFill ube = new BackgroundFill(Color.web("#3c5199"), null, null);
		
		Profileicn.setStyle("-fx-background-color: #5d76dc; -fx-border-radius: 50; -fx-background-radius: 25;");
		
		Dashboard.setStyle("-fx-border-radius: 25 0 0 25;");
		Dashboard.setBackground(new Background(ube));
		Dashboard.setTextFill(Color.WHITE);
		StudentProf.setStyle("-fx-border-radius: 25 0 0 25;");
        StudentProf.setBackground(new Background(ube));
        StudentProf.setTextFill(Color.WHITE);
        Timetable.setStyle("-fx-border-radius: 25 0 0 25;");
        Timetable.setBackground(new Background(ube));
        Timetable.setTextFill(Color.WHITE);
        Schedule.setStyle("-fx-border-radius: 25 0 0 25;");
        Schedule.setBackground(new Background(ube));
        Schedule.setTextFill(Color.WHITE);
        Grading.setStyle("-fx-border-radius: 25 0 0 25;");
        Grading.setBackground(new Background(ube));
        Grading.setTextFill(Color.WHITE);
        Enrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        Enrollment.setBackground(new Background(ube));
        Enrollment.setTextFill(Color.WHITE);
        oldEnrollment.setStyle("-fx-border-radius: 25 0 0 25;");
        oldEnrollment.setBackground(new Background(ube));
        oldEnrollment.setTextFill(Color.WHITE);
        Students.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
        Students.setTextFill(Color.BLACK);
		
		 
		 Pane studentsstage = FXMLLoader.load(getClass().getResource("/students/Students.fxml"));
		 contentarea.getChildren().removeAll();
		 contentarea.getChildren().setAll(studentsstage);
		 AnchorPane.setLeftAnchor(studentsstage, 10.0);
		 AnchorPane.setRightAnchor(studentsstage, 10.0);
		 AnchorPane.setTopAnchor(studentsstage, 10.0);
		 AnchorPane.setBottomAnchor(studentsstage, 20.0);
		 
	}
	
	public void setContent(Node node) {
        contentarea.getChildren().setAll(node);
	}
   
}
    