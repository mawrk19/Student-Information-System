package enrollment;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import application.DatabaseManager;

public class EnrollmentController implements Initializable {

    @FXML
    private ComboBox<String> courseCMB;

    @FXML
    private TextField dateTF;

    @FXML
    private Button enrollBTN;

    @FXML
    private TextField fNameTF;

    @FXML
    private ComboBox<String> genderCMB;

    @FXML
    private ImageView insertIMG;

    @FXML
    private TextField lNameTF;

    @FXML
    private ComboBox<String> locCMB;

    @FXML
    private TextField mNameTF;

    @FXML
    private ComboBox<String> secCMB;

    @FXML
    private TextField sidTF;

    @FXML
    private ComboBox<String> yrCMB;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	

        // Initialize ComboBoxes with sample data
        ObservableList<String> courses = FXCollections.observableArrayList("BSCS", "BSIT", "BSIS", "BSEMC");
        courseCMB.setItems(courses);

        ObservableList<String> genders = FXCollections.observableArrayList("Male", "Female");
        genderCMB.setItems(genders);

        ObservableList<String> locations = FXCollections.observableArrayList("Camarin", "Congress", "South");
        locCMB.setItems(locations);

        ObservableList<String> sections = FXCollections.observableArrayList("A", "B", "C");
        secCMB.setItems(sections);

        ObservableList<String> years = FXCollections.observableArrayList("1st", "2nd", "3rd","4th");
        yrCMB.setItems(years);
        
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            dateTF.setText(formatter.format(now));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void enrollButtonClicked() {
        String selectedCourse = courseCMB.getValue();
        String enrollmentDate = dateTF.getText();
        String firstName = fNameTF.getText();
        String gender = genderCMB.getValue();
        String location = locCMB.getValue();
        String lastName = lNameTF.getText();
        String middleName = mNameTF.getText();
        String section = secCMB.getValue();
        
        String year = "2023"; // Define the year value here or get it from a ComboBox

        try {
            Connection con = DatabaseManager.getConnection();
            String sql = "INSERT INTO students (course, date, First_name, gender, location, last_name, Middle_name, section, year) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, selectedCourse);
            preparedStatement.setString(2, enrollmentDate);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, gender);
            preparedStatement.setString(5, location);
            preparedStatement.setString(6, lastName);
            preparedStatement.setString(7, middleName);
            preparedStatement.setString(8, section);
            preparedStatement.setString(9, year);

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                // Retrieve the auto-generated ID
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    String formattedId = String.format("%s%04d", year, generatedId);
                    sidTF.setText(formattedId);
                }

                System.out.println("Enrollment successful!");
                
                courseCMB.setValue(null);
                dateTF.clear();
                fNameTF.clear();
                genderCMB.setValue(null);
                locCMB.setValue(null);
                lNameTF.clear();
                mNameTF.clear();
                secCMB.setValue(null);
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors here
        }
    }

}
