package grading;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;


import application.DatabaseManager;
import application.UserSession;

public class GradingController {

    @FXML
    private Label btnSearch;

    @FXML
    private TextField inputSearch;

    @FXML
    private Label labelCourse;

    @FXML
    private Label labelFullname;

    @FXML
    private Label labelYrSec;

    @FXML
    private TableColumn<?, String> status;

    @FXML
    private TableColumn<?, String> subjCode;

    @FXML
    private TableColumn<?, String> subjName;

    @FXML
    private TableColumn<?, Integer> units;

    @FXML
    private TableColumn<?, Integer> grades;
    
    

    @FXML
    void searchStudentFunc(MouseEvent event) {
    	fetchStudent();
    }
    
    public void fetchStudent() {
		Connection con = DatabaseManager.getConnection();
		
		String verifySearch = "SELECT * FROM student WHERE scode = ?";

		try {
			PreparedStatement stmt = con.prepareStatement(verifySearch);
			stmt.setString(1, inputSearch.getText());

			ResultSet result = stmt.executeQuery();

			if (result.next()) {
				String FName = result.getString("First_name");
				String LName = result.getString("last_name");
				String Year = result.getString("year");
				String Section = result.getString("section");
				String Fullname = FName + " " + LName;
				String YearandSec = Year + "-" + Section;
				labelFullname.setText(Fullname);
				labelCourse.setText(result.getString("course"));
				labelYrSec.setText(YearandSec);
				
				fetchSubjGrades(inputSearch.getText());
				
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Search results:");
				alert.setHeaderText(null);
				alert.setContentText("Student Code do not exist!");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Search results:");
			alert.setHeaderText(null);
			alert.setContentText("Database Error!");
			alert.showAndWait();
		}

    }
    public void fetchSubjGrades(String studentID) {
    	Connection con = DatabaseManager.getConnection();
		
		String verifySearch = "SELECT * FROM studentgrades WHERE scode = ?";

		try {
			PreparedStatement stmt = con.prepareStatement(verifySearch);
			stmt.setString(1, studentID);
			stmt.setFetchSize(Integer.MIN_VALUE);

			ResultSet result = stmt.executeQuery();
			
			while (result.next()) {
				System.out.println(result.getString("subjectCode"));
				System.out.println(result.getString("subjectTitle"));
				System.out.println(result.getString("units"));
				System.out.println(result.getString("subjectGrades"));
				result.getString("SubjectCode");
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Search results:");
			alert.setHeaderText(null);
			alert.setContentText("Database Error!"); 
			alert.showAndWait();
		}
    }
    

    
}
