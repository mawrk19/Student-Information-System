package evaluation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;
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
        TextField textField1 = new TextField();
        TextField textField2 = new TextField();
        Label label = new Label(":");

        // Set properties for TextFields and Label
        textField1.setMaxHeight(40);
        textField1.setMaxWidth(Double.MAX_VALUE);
        textField2.setMaxHeight(40);
        textField2.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(40);
        label.setFont(new Font(20));

        // Create a Delete button with specific properties
        Button deleteButton = new Button("Delete");
        deleteButton.setPrefSize(100, 30); // Set specific width and height for the Delete button
        deleteButton.setOnAction(e -> {
            // Get the parent of the Delete button (HBox) and remove it from the VBox
            vBoxContainer.getChildren().remove(((Button) e.getSource()).getParent());
        });

        // Create an HBox to hold the components in a specific structure
        HBox newRow = new HBox(10); // Spacing between elements, adjust as needed
        newRow.setAlignment(Pos.CENTER);
        newRow.setPrefHeight(12);
        newRow.setPrefWidth(330);
        newRow.setSpacing(10);
        newRow.getChildren().addAll(textField1, label, textField2, deleteButton);

        // Set HBox and TextField properties similar to your FXML
        HBox.setHgrow(textField1, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(textField2, javafx.scene.layout.Priority.ALWAYS);

        // Set VBox.margin for the new row
        VBox.setMargin(newRow, new Insets(0, 0, 0, 0)); // Set margins if needed

        return newRow;
    }
}