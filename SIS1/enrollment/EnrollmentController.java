package enrollment;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import application.DatabaseManager;
import application.UserSession;

public class EnrollmentController implements Initializable {

    @FXML
    private ComboBox<String> courseCMB, genderCMB, locCMB, secCMB, yrCMB, statCMB, semCMB;

    @FXML
    private TextField dateTF, fNameTF, lNameTF, mNameTF, sidTF;

    @FXML
    private Button enrollBTN;

    @FXML
    private ImageView insertIMG;

    @FXML
    private TableView<Subject> subjectsTableView;

    @FXML
    private TableColumn<Subject, Integer> idColumn;

    @FXML
    private TableColumn<Subject, String> subCodeColumn;

    @FXML
    private TableColumn<Subject, Integer> unitsColumn;

    @FXML
    private TableColumn<Subject, String> subjectColumn;

    private ObservableList<Subject> subjectsList = FXCollections.observableArrayList();

    private void fetchSubjectsFromDatabase() {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM subjects")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String subCode = resultSet.getString("sub_code");
                int units = resultSet.getInt("units");
                String subject = resultSet.getString("subject");

                Subject subjectObj = new Subject(id, subCode, units, subject);
                subjectsList.add(subjectObj);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize ComboBoxes with sample data
        ObservableList<String> courses = FXCollections.observableArrayList("BSCS", "BSIT", "BSIS", "BSEMC");
        courseCMB.setItems(courses);

        // Initialize other ComboBoxes similarly...

        // Set up TableView columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        subCodeColumn.setCellValueFactory(new PropertyValueFactory<>("subCode"));
        unitsColumn.setCellValueFactory(new PropertyValueFactory<>("units"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));

        // Fetch subjects from the database
        fetchSubjectsFromDatabase();

        // Set the items of the TableView to the subjectsList
        subjectsTableView.setItems(subjectsList);

        // ... (remaining initialization code)

    }

    @FXML
    private void enrollButtonClicked() {
        // ... (remaining code for enrolling a student)

    }

    // ... (remaining methods)

    public class Subject {
        private int id;
        private String subCode;
        private int units;
        private String subject;

        public Subject(int id, String subCode, int units, String subject) {
            this.id = id;
            this.subCode = subCode;
            this.units = units;
            this.subject = subject;
        }

        public int getId() {
            return id;
        }

        public String getSubCode() {
            return subCode;
        }

        public int getUnits() {
            return units;
        }

        public String getSubject() {
            return subject;
        }
    }

    // ... (remaining code)
}
