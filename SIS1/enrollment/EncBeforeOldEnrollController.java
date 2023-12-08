package enrollment;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DatabaseManager;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import encoderui.EncoderController;
import application.UserSession;

public class EncBeforeOldEnrollController {
	@FXML
	private Button searchbtn;

	@FXML
	private TextField searchbar;

	@FXML
	private Label gwalbl;

	@FXML
	private Button submitbtn;

	private double gwaValue = 0.0;
	
	public String searchedCode;

	@FXML
	void searchscodebtn(ActionEvent event) {
		searchedCode = searchbar.getText();
		SearchBarSingleton.getInstance().setSearchbarText(searchedCode);
		 
		 try (Connection connection = DatabaseManager.getConnection()) {
		        String query = "SELECT gwa FROM student WHERE scode = ?";
		        PreparedStatement preparedStatement = connection.prepareStatement(query);
		        preparedStatement.setString(1, searchedCode);

		        ResultSet resultSet = preparedStatement.executeQuery();

		        if (resultSet.next()) {
		            String gwaString = resultSet.getString("gwa");
		            gwaValue = Double.parseDouble(gwaString);

		            gwalbl.setText(gwaString);
		        } else if (searchedCode.isEmpty()) {
			        showAlert("Error", "Empty Input", "Please input a valid Student Code");
			        return; // Stop further execution if the search bar is empty
			    } else if (searchedCode.isEmpty() || !isValidStudentCode(searchedCode)) {
			        showAlert("Error", "Invalid Student Code", "Please enter a valid Student Code");
			        return; // Stop further execution if the student code is invalid or empty
			    } else {
		            showAlert("Error", "Invalid Student Code", "Please enter a valid Student Code");
		            clearFields();
		            // Display an alert or message to inform the user about an invalid student code
		        }

		        resultSet.close();
		        preparedStatement.close();
		    } catch (SQLException e) {
		        e.printStackTrace();
		        // Handle any database connection or query errors
		    }
	}

	
	
	@FXML
	void gotoOldEnrollment(ActionEvent event) throws IOException {
		searchedCode = SearchBarSingleton.getInstance().getSearchbarText();
	    System.out.println("Searched Code: " + searchedCode);
		 
	    if (searchedCode.isEmpty()) {
	        showAlert("Error", "Empty Input", "Please input a valid Student Code");
	        return;
	    } else if (!isValidStudentCode(searchedCode)) {
	        showAlert("Error", "Invalid Student Code", "Please enter a valid Student Code");
	        return;
	    }
	    
	    if (gwalbl.getText().isEmpty()) {
	        showAlert("Error", "Invalid GWA", "Please fetch the GWA before proceeding");
	        return;
	    }

	    try (Connection connection = DatabaseManager.getConnection()) {
	        String query = "SELECT gwa FROM student WHERE scode = ?";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, searchedCode);
	        searchedCode = SearchBarSingleton.getInstance().getSearchbarText();
	       
	        
	        ResultSet resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            String gwaString = resultSet.getString("gwa");
	            gwaValue = Double.parseDouble(gwaString);

	            gwalbl.setText(gwaString);

	            if (gwaValue <= 2.25) {

				BackgroundFill ube = new BackgroundFill(Color.web("#3c5199"), null, null);
	
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/enrollment/EncOldEnrollment.fxml"));
				Parent encoldenrollment = loader.load();
	
				AnchorPane.setLeftAnchor(encoldenrollment, 10.0);
				AnchorPane.setRightAnchor(encoldenrollment, 10.0);
				AnchorPane.setTopAnchor(encoldenrollment, 10.0);
				AnchorPane.setBottomAnchor(encoldenrollment, 20.0);
	
				FXMLLoader encoderLoader = new FXMLLoader(getClass().getResource("/encoderui/Encoder.fxml"));
				Parent encoderui = encoderLoader.load();
				EncoderController encoderController = encoderLoader.getController();
	
				encoderController.Profileicn.setStyle("-fx-background-color: #5d76dc; -fx-border-radius: 50; -fx-background-radius: 25;");
	
				encoderController.Dashboard.setStyle("-fx-border-radius: 25 0 0 25;");
				encoderController.Dashboard.setBackground(new Background(ube));
				encoderController.Dashboard.setTextFill(Color.WHITE);
				encoderController.StudentProf.setStyle("-fx-border-radius: 25 0 0 25;");
				encoderController.StudentProf.setBackground(new Background(ube));
				encoderController.StudentProf.setTextFill(Color.WHITE);
	//	        Timetable.setStyle("-fx-border-radius: 25 0 0 25;");
	//	        Timetable.setBackground(new Background(ube));
	//	        Timetable.setTextFill(Color.WHITE);
//				mainFrameController.Schedule.setStyle("-fx-border-radius: 25 0 0 25;");
//				mainFrameController.Schedule.setBackground(new Background(ube));
//				mainFrameController.Schedule.setTextFill(Color.WHITE);
//				mainFrameController.Evaluation.setStyle("-fx-border-radius: 25 0 0 25;");
//				mainFrameController.Evaluation.setBackground(new Background(ube));
//				mainFrameController.Evaluation.setTextFill(Color.WHITE);
	//	        Grading.setStyle("-fx-border-radius: 25 0 0 25;");
	//	        Grading.setBackground(new Background(ube));
	//	        Grading.setTextFill(Color.WHITE);
				encoderController.Enrollment.setStyle("-fx-border-radius: 25 0 0 25;");
				encoderController.Enrollment.setBackground(new Background(ube));
				encoderController.Enrollment.setTextFill(Color.WHITE);
				encoderController.oldEnrollment.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
				encoderController.oldEnrollment.setTextFill(Color.BLACK);
				encoderController.Students.setStyle("-fx-border-radius: 25 0 0 25;");
				encoderController.Students.setBackground(new Background(ube));
				encoderController.Students.setTextFill(Color.WHITE);
	
				encoderController.setContent(encoldenrollment);
	
				Scene scene = new Scene(encoderui);
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.show();
	
				double windowWidth = stage.getWidth();
				double windowHeight = stage.getHeight();
	
				stage.setWidth(windowWidth);
				stage.setHeight(windowHeight);
	            } else {
	                showAlert("Failed!", "GWA Too Low", "GWA should be lower or equal to 2.25");
	            }
	        } else {
	            showAlert("Error", "Invalid Student Code", "Please enter a valid Student Code");
	            clearFields();
	        }
	        
	        resultSet.close();
	        preparedStatement.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Handle any database connection or query errors
	    }
	}
	
	private void showAlert(String title, String header, String content) {
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle(title);
	    alert.setHeaderText(header);
	    alert.setContentText(content);
	    alert.showAndWait();
	}
	
	private boolean isValidStudentCode(String studentCode) {
		return studentCode.matches("[a-zA-Z0-9]+");

	}

	private void clearFields() {
		gwalbl.setText("");
		gwaValue = 0.0; // Reset the GWA value
	}
	
	
}

