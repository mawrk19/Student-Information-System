package evaluation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import application.DatabaseManager;

public class EvaluationController implements Initializable{
	
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
    private Button addbtn;
    
    @FXML
    private VBox vBoxContainer;
    
    @FXML 
    private TextField totalgwa;
    
    @FXML
    private Button ComputeBtn;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                searchbar.setText(newValue.replaceAll("[^\\d]", ""));
            }
            
            if (newValue.length() > 8) {
                String limitedValue = newValue.substring(0, 8);
                searchbar.setText(limitedValue);
            }
        });
    }

    @FXML
    void searchscodebtn(ActionEvent event) {
        String searchedCode = searchbar.getText();

        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM student WHERE scode = ?";
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

                int subjectsStart = resultSet.getInt("eSubjectsStart");
                int subjectsEnd = resultSet.getInt("eSubjectsEnd");

                // Clear existing rows in the VBox
                vBoxContainer.getChildren().clear();

                // Fetch subjects for the student using the start and end IDs
                String subjectsQuery = "SELECT sub_code, units FROM subjects WHERE id BETWEEN ? AND ?";
                PreparedStatement subjectsStatement = connection.prepareStatement(subjectsQuery);
                subjectsStatement.setInt(1, subjectsStart);
                subjectsStatement.setInt(2, subjectsEnd);

                ResultSet subjectsResultSet = subjectsStatement.executeQuery();

                while (subjectsResultSet.next()) {
                    String subjectName = subjectsResultSet.getString("sub_code");
                    int subjectUnits = subjectsResultSet.getInt("units");
                    // Create a new row (HBox) with subject label and required fields
                    HBox newRow;
                    if (subjectUnits >= 0) {
                        newRow = createNewRowdatabase(subjectName, subjectUnits);
                    } else {
                        newRow = createNewRowdatabase(subjectName);
                    }
                    vBoxContainer.getChildren().add(newRow);
                }

                subjectsResultSet.close();
                subjectsStatement.close();
            } else {
                clearFields();
                showAlert("Error", "Invalid scode", "The entered Studend Code does not exist.");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
    
    private HBox createNewRowdatabase(String sub_code) {
        return createNewRowdatabase(sub_code); // Call the main createNewRow method with a default value for units
    }

    private HBox createNewRowdatabase(String sub_code, int units) {
        // Create Labels and TextFields for the new row
    	
        Label subject = new Label(sub_code);
        subject.setAlignment(Pos.CENTER);
        subject.setPrefWidth(30.0);
        subject.setFont(new Font(15.0));
        subject.setMaxWidth(Double.MAX_VALUE);
        subject.setPrefHeight(30.0);
        HBox.setHgrow(subject, Priority.ALWAYS);

        TextField unit = new TextField(String.valueOf(units));
        unit.setAlignment(Pos.CENTER);
        unit.setPrefWidth(15.0);
        unit.setPrefHeight(30.0);
        unit.setMaxWidth(Double.MAX_VALUE);
        unit.setEditable(false); // Set unit TextField as uneditable
        if (units >= 0) {
            unit.setText(String.valueOf(units)); // Set the units value if it's provided
            unit.setEditable(false); // Make the unit TextField uneditable
        }
        HBox.setHgrow(unit, Priority.ALWAYS);

        Label colon = new Label(":");
        colon.setFont(new Font(20.0));
        colon.setPrefWidth(5.0);
        colon.setPrefHeight(20.0);

        TextField grade = new TextField();
        grade.setAlignment(Pos.CENTER);
        grade.setPrefWidth(15.0);
        grade.setPrefHeight(30.0);
        grade.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(grade, Priority.ALWAYS);
        
        int maxLength = 4; // Maximum length for the grade, including the decimal point and two decimal places

        grade.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                grade.setText(oldValue);
            }
            
            if (grade.getText().length() > maxLength) {
                String limitedText = grade.getText().substring(0, maxLength);
                grade.setText(limitedText);
            }
        });
        

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
        newRow.getChildren().addAll(subject, unit, colon, grade, deleteButton);

        // Set VBox.margin for the new row
        VBox.setMargin(newRow, new Insets(0, 0, 0, 0)); // Set margins if needed

        unit.textProperty().addListener((observable, oldValue, newValue) -> {
        });

        grade.textProperty().addListener((observable, oldValue, newValue) -> {
        });

        return newRow;
    }
    
    private HBox createNewRow() {
        // Create Labels and TextFields for the new row
        Label subject = new Label("Sub Code");
        subject.setAlignment(Pos.CENTER);
        subject.setPrefWidth(30.0);
        subject.setFont(new Font(15.0));
        subject.setMaxWidth(Double.MAX_VALUE);
        subject.setPrefHeight(30.0);
        HBox.setHgrow(subject, Priority.ALWAYS);

        TextField unit = new TextField();
        unit.setAlignment(Pos.CENTER);
        unit.setPrefWidth(15.0);
        unit.setPrefHeight(30.0);
        unit.setMaxWidth(Double.MAX_VALUE);
        unit.setEditable(true);
        HBox.setHgrow(unit, Priority.ALWAYS);
        
        unit.textProperty().addListener((observable, oldValue, newValue) -> {
            // Check if the new text is a valid whole number
            if (newValue.isEmpty() || newValue.matches("^\\d+$")) {
                // Allow the change or if the field is empty
            } else {
                // Deny the change
                // Reset the text to the previous value
                unit.setText(oldValue);
                // Show an error message if the field is not empty and the input is invalid
                if (!newValue.isEmpty()) {
                    showAlert("Error", "Invalid Units Format", "Please enter a valid X (whole number) for units.");
                }
            }
        });

        Label colon = new Label(":");
        colon.setFont(new Font(20.0));
        colon.setPrefWidth(5.0);
        colon.setPrefHeight(20.0);

        TextField grade = new TextField();
        grade.setAlignment(Pos.CENTER);
        grade.setPrefWidth(15.0);
        grade.setPrefHeight(30.0);
        grade.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(grade, Priority.ALWAYS);
        
        int grademaxLength = 4; // Maximum length for the grade, including the decimal point and two decimal places
        grade.textProperty().addListener((observable, oldValue, newValue) -> {
            // Check if the new text is a valid decimal number or a whole number
            if (newValue.isEmpty() || (newValue.matches("^\\d+(\\.\\d{1,2})?$") && newValue.length() <= grademaxLength)) {
                // Allow the change or if the field is empty
            } else {
                // Deny the change
                // Reset the text to the previous value
                grade.setText(oldValue);
                // Show an error message if the field is not empty and the input is invalid
                if (!newValue.isEmpty()) {
                    showAlert("Error", "Invalid Grade Format", "Please enter a valid grade format: X.X or X.XX or X (whole number)");
                }
            }
        });
        

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
        newRow.getChildren().addAll(subject, unit, colon, grade, deleteButton);

        // Set VBox.margin for the new row
        VBox.setMargin(newRow, new Insets(0, 0, 0, 0)); // Set margins if needed

        unit.textProperty().addListener((observable, oldValue, newValue) -> {
        });

        grade.textProperty().addListener((observable, oldValue, newValue) -> {
        });

        return newRow;
    }
    
    @FXML
    void addrow(ActionEvent event) {
    	HBox newRow = createNewRow();
        vBoxContainer.getChildren().add(newRow);
    }
    
    
    private void calculateGWA() {
        int rowCount = vBoxContainer.getChildren().size();

        if (rowCount == 0) {
            // Display an error pop-up when no rows are added
            showAlert("Error", "No rows added", "Please add rows before computing.");
            return;
        }

        double totalUnits = 0;
        double totalGradePoints = 0;
        boolean hasError = false;

        for (Node node : vBoxContainer.getChildren()) {
            if (node instanceof HBox) {
                HBox row = (HBox) node;

                TextField unitTextField = (TextField) row.getChildren().get(1);
                TextField gradeTextField = (TextField) row.getChildren().get(3);

                try {
                    double units = Double.parseDouble(unitTextField.getText());
                    String gradeStr = gradeTextField.getText();

                    if (!isValidGrade(gradeStr)) {
                        throw new NumberFormatException();
                    }

                    double grade = Double.parseDouble(gradeStr);

                    totalUnits += units;
                    totalGradePoints += units * grade;
                } catch (NumberFormatException ignored) {
                    // Handle invalid input (non-numeric values)
                    hasError = true;
                }
            }
        }

        if (!hasError) {
            if (totalUnits != 0) {
                double gwa = totalGradePoints / totalUnits;

                // Round off to 2 decimal places
                double roundedGWA = Math.round(gwa * 100.0) / 100.0;

                // Display the computed GWA in the Total TextField
                totalgwa.setText(String.format("%.2f", roundedGWA));
            } else {
                // Handle the case where totalUnits is zero to avoid division by zero
                totalgwa.setText("0.00");
            }
        } else {
            // Display an error pop-up for non-numeric input or invalid grades
            showAlert("Error", "Invalid Input", "Please enter valid numeric unit and grades. Please enter one of the following values: 1, 1.25, 1.5, 1.75, 2, 2.25, 2.5, 2.75, 3, 3.0, 4, 5");
        }
    }

    @FXML
    void computegwa(ActionEvent event) {
    	
    	if (essentialFieldsEmpty()) {
            showAlert("Error", "Empty Fields", "Please fill in all required fields before computing.");
        } else {
            boolean hasEmptyGrades = checkForEmptyGrades();

            if (hasEmptyGrades) {
                showAlert("Error", "Empty Units or Grades", "Please fill in all grade and unit fields before computing.");
            } else {
                calculateGWA();
                sendGWAtoDatabase();
            }
        }
    }
    
    private boolean essentialFieldsEmpty() {
        // Check if any of the essential fields are empty
        return Name.getText().isEmpty() || YearLevel.getText().isEmpty() ||
               Section.getText().isEmpty() || Course.getText().isEmpty() ||
               Semester.getText().isEmpty() || Scode.getText().isEmpty() ||
               Campus.getText().isEmpty();
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
    
    private void sendGWAtoDatabase() {
        String studentCode = Scode.getText();
        String gwaText = totalgwa.getText().trim(); // Trim leading/trailing whitespaces

        if (!gwaText.isEmpty()) {
            try {
                double gwa = Double.parseDouble(gwaText);

                try (Connection connection = DatabaseManager.getConnection()) {
                    String query = "UPDATE student SET gwa = ? WHERE scode = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setDouble(1, gwa);
                    preparedStatement.setString(2, studentCode);

                    int rowsUpdated = preparedStatement.executeUpdate();

                    if (rowsUpdated > 0) {
                        if (gwa > 2.25) {
                            showAlert("Failed", "GWA Too Low", "GWA is Lower than 2.25. GWA added to Database.");
                        } else {
                            Label label = new Label("GWA successfully updated");
                            label.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/src/imgs_icons/checkicon.png"))));

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success");
                            alert.setHeaderText(null);
                            alert.getDialogPane().setContent(label);
                            alert.showAndWait();
                        }
                        clearAllFields(); // Clear all fields only on successful update
                    } else {
                        showAlert("Error", "Update Failed", "Failed to update GWA in the database.");
                    }

                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Error", "Database Error", "An error occurred while accessing the database.");
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid GWA", "Please enter a valid numeric GWA.");
            }
        } else {
            showAlert("Error", "Empty GWA", "Please compute GWA before updating.");
        }
    }
    
    private boolean isValidGrade(String gradeStr) {
        String[] validGrades = {"1", "1.25", "1.5", "1.75", "2", "2.25", "2.5", "2.75", "3", "3.0", "4", "5"};

        return Arrays.asList(validGrades).contains(gradeStr);
    }
    
    private void clearAllFields() {
    	Name.clear();
        YearLevel.clear();
        Section.clear();
        Course.clear();
        Semester.clear();
        Scode.clear();
        Campus.clear();
        totalgwa.clear();
        searchbar.clear();

        vBoxContainer.getChildren().clear();
    }

}