package evaluation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;

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
    private TextField Type;

    @FXML
    private TextField searchbar;

    @FXML
    private Button searchbtn;
    
    @FXML
    private Button addbtn;
    
    @FXML
    private VBox vBoxContainer;
    
    @FXML 
    private TextField totalgwa;
    
    @FXML
    private Button ComputeBtn;
    

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
                Type.setText(resultSet.getString("type"));
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
        Type.clear();
    }
    
    @FXML
    void addrow(ActionEvent event) {
        // Create a new row (HBox) similar to the structure in your FXML
        HBox newRow = createNewRow();

        // Add the new row (HBox) to the VBox
        vBoxContainer.getChildren().add(newRow);
    }

    private HBox createNewRow() {
        // Create TextFields and Label for the new row
    	
    	Label label = new Label("Subject");
    	label.setAlignment(Pos.CENTER);
    	label.setPrefWidth(30.0);
        label.setFont(new Font(15.0));
        label.setMaxWidth(Double.MAX_VALUE);
        label.setPrefHeight(30.0);
        HBox.setHgrow(label, Priority.ALWAYS);

        TextField unit = new TextField();
        unit.setPrefWidth(15.0);
        unit.setPrefHeight(30.0);
        unit.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(unit, Priority.ALWAYS);

        Label colon = new Label(":");
        colon.setFont(new Font(20.0));
        colon.setPrefWidth(5.0);
        colon.setPrefHeight(20.0);

        TextField grade = new TextField();
        grade.setPrefWidth(15.0);
        grade.setPrefHeight(30.0);
        grade.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(grade, Priority.ALWAYS);

        Button deleteButton = new Button("Delete");
        deleteButton.setPrefWidth(20.0);
        deleteButton.setPrefHeight(30.0);
        deleteButton.setMaxWidth(100);
        HBox.setHgrow(deleteButton, Priority.ALWAYS);
        deleteButton.setOnAction(e -> {
            vBoxContainer.getChildren().remove(((Button) e.getSource()).getParent());
        });

        HBox newRow = new HBox(10); // Spacing between elements, adjust as needed
        newRow.setAlignment(Pos.TOP_CENTER);
        newRow.setPrefHeight(30.0);
        newRow.setPrefWidth(330.0);
        newRow.setSpacing(10.0);
        newRow.getChildren().addAll(label, unit, colon, grade, deleteButton);

        // Set VBox.margin for the new row
        VBox.setMargin(newRow, new Insets(0, 0, 0, 0)); // Set margins if needed
        
        unit.textProperty().addListener((observable, oldValue, newValue) -> {
        });

        grade.textProperty().addListener((observable, oldValue, newValue) -> {
        });

        return newRow;
    }
    
    private void calculateGWA() {
        double totalUnits = 0;
        double totalGradePoints = 0;

        for (Node node : vBoxContainer.getChildren()) {
            if (node instanceof HBox) {
                HBox row = (HBox) node;

                TextField unitTextField = (TextField) row.getChildren().get(1);
                TextField gradeTextField = (TextField) row.getChildren().get(3);

                try {
                    double units = Double.parseDouble(unitTextField.getText());
                    double grade = Double.parseDouble(gradeTextField.getText());

                    totalUnits += units;
                    totalGradePoints += units * grade;
                } catch (NumberFormatException ignored) {
                    // Handle invalid input if necessary
                }
            }
        }

        double gwa = totalGradePoints / totalUnits;

        // Display the computed GWA in the Total TextField
        totalgwa.setText(String.valueOf(gwa));
    }

    @FXML
    void computegwa(ActionEvent event) {
    	boolean hasEmptyGrades = checkForEmptyGrades();
        
        if (hasEmptyGrades) {
            showAlert("Error", "Empty Grades", "Please fill in all grade fields before computing.");
        } else {
            calculateGWA();
        }
    }
    
    private boolean checkForEmptyGrades() {
        for (Node node : vBoxContainer.getChildren()) {
            if (node instanceof HBox) {
                HBox row = (HBox) node;

                TextField gradeTextField = (TextField) row.getChildren().get(3);
                String grade = gradeTextField.getText().trim();

                if (grade.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}