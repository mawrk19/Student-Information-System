package evaluation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DatabaseManager;

public class EvaluationController {
	
    @FXML
    private TextField Campus;

    @FXML
    private TextField Course;

    @FXML
    private TextField Name;

    @FXML
    private TextField Scode;

    @FXML
    private TextField Section;

    @FXML
    private TextField Semester;

    @FXML
    private TextField YearLevel;

    @FXML
    private TextField searchbar;

    @FXML
    private Button searchbtn;

    @FXML
    void searchscodebtn(ActionEvent event) {
        String searchedCode = searchbar.getText();

        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM student WHERE Scode = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, searchedCode);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
            	String firstName = resultSet.getString("First_name");
                String middleName = resultSet.getString("Middle_name");
                String lastName = resultSet.getString("Last_name");
                
                String fullName = firstName + " " + middleName + " " + lastName;
                Name.setText(fullName);
                YearLevel.setText(resultSet.getString("year"));
                Section.setText(resultSet.getString("section"));
                Course.setText(resultSet.getString("course"));
                Semester.setText(resultSet.getString("sem"));
                Scode.setText(resultSet.getString("scode"));
                Campus.setText(resultSet.getString("location"));
            } else {
                // Handle case when no student with entered code is found
                clearFields();
                // You can also display an alert or message to inform the user about no matching student code
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database connection or query errors
        }
    }

    private void clearFields() {
        Name.clear();
        YearLevel.clear();
        Section.clear();
        Course.clear();
        Semester.clear();
        Scode.clear();
        Campus.clear();
    }
}