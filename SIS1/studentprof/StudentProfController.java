	package studentprof;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import students.Students;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Blob;

import application.DatabaseManager;
import studentprof.Subject;

public class StudentProfController {
    @FXML
    private Label addressLBL;

    @FXML
    private Label ageLBL;

    @FXML
    private Label courseLBL;

    @FXML
    private Label emailLBL;

    @FXML
    private Label fullnameLBL;

    @FXML
    private Button search;

    @FXML
    private Label syAndScodeLBL;

    @FXML
    private Label yrAnsSecLBL;
    
    @FXML
    private ImageView studIMG;

    @FXML
    private TextField searchTF;
    
    @FXML
    private TableView<Subject> Table;
    
    @FXML
    private TableColumn<Subject, String> subjectColumn;
    
    @FXML
    private TableColumn<Subject, Integer> credColumn;
    
//    @FXML
//    private TableColumn<Subject, String> secColumn;
    
    @FXML
    private TableColumn<Subject, String> dayColumn;
    
//    @FXML
//    private TableColumn<Subject, String> timeColumn;
//    
//    @FXML
//    private TableColumn<Subject, String> professorColumn;
    
    
    @FXML
    private void initialize() {
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subjectCode"));
        credColumn.setCellValueFactory(new PropertyValueFactory<>("creditUnits"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
//        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
//        professorColumn.setCellValueFactory(new PropertyValueFactory<>("professor"));

        // Load data for the student with scode 20230001
        loadStudentProfile("20230001");
    }

    private void loadStudentProfile(String scode) {
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM student WHERE scode = ?")) {

            stmt.setString(1, scode);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    Students student = createStudentFromResultSet(resultSet);
                    System.out.println("Student found: " + student.getFirstName() + " " + student.getLastName());
                    setLabels(student);
                    retrieveSubjectsForStudent(student);
                } else {
                    System.out.println("No student found with scode: " + scode);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Handle the exception according to your needs
        }
    }

    private Students getStudent(int sid) throws SQLException {
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM student WHERE sid = ?")) {
            stmt.setInt(1, sid);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("First_name");
                    String middleName = resultSet.getString("Middle_name");
                    String lastName = resultSet.getString("last_name");
                    String course1 = resultSet.getString("course");
                    String year1 = resultSet.getString("year");
                    String section1 = resultSet.getString("section");
                    String location1 = resultSet.getString("location");
                    int scode1 = resultSet.getInt("scode");
                    String date1 = resultSet.getString("date");
                    int sid1 = resultSet.getInt("sid");
                    String gender1 = resultSet.getString("gender");
                    int start = resultSet.getInt("eSubjectsStart");
                    int end = resultSet.getInt("eSubjectsEnd");

                    // Retrieve the image as a java.sql.Blob
                    Blob studentImageBlob = resultSet.getBlob("image");

                    Students student = new Students(firstName, middleName, lastName, course1, year1, section1,
                            location1, scode1, date1, sid1, gender1, studentImageBlob, start, end, gender1);

                    // Set the image in the Students object
                    student.setStudentImage(studentImageBlob);

                    return student;
                } else {
                    return null; // No student found with the given sid
                }
            }
        }
    }

    @FXML
    private void searchActionPerformed(ActionEvent event) {
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM student WHERE scode = ?")) {

            String scode = searchTF.getText();
            if (scode.length() > 0) {
                stmt.setString(1, scode);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        Students student = createStudentFromResultSet(resultSet);
                        System.out.println("Student found: " + student.getFirstName() + " " + student.getLastName());
                        setLabels(student);
                        retrieveSubjectsForStudent(student);
                    } else {
                        System.out.println("No student found with scode: " + scode);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Handle the exception according to your needs
        }
    }

        
    private Students createStudentFromResultSet(ResultSet resultSet) throws SQLException {
        String firstName = resultSet.getString("First_name");
        String middleName = resultSet.getString("Middle_name");
        String lastName = resultSet.getString("last_name");
        String course1 = resultSet.getString("course");
        String year1 = resultSet.getString("year");
        String section1 = resultSet.getString("section");
        String location1 = resultSet.getString("location");
        int scode1 = resultSet.getInt("scode");
        String date1 = resultSet.getString("date");
        int sid1 = resultSet.getInt("sid");
        String gender1 = resultSet.getString("gender");
        int start = resultSet.getInt("eSubjectsStart");
        int end = resultSet.getInt("eSubjectsEnd");

        // Retrieve the image as a java.sql.Blob
        Blob studentImageBlob = resultSet.getBlob("image");

        Students student = new Students(firstName, middleName, lastName, course1, year1, section1,
                location1, scode1, date1, sid1, gender1, studentImageBlob, start, end, gender1);

        // Set the image in the Students object
        student.setStudentImage(studentImageBlob);

        return student;
    }

    private void retrieveSubjectsForStudent(Students student) {
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM subjects WHERE id BETWEEN ? AND ?")) {

            int startId = student.getStart();
            int endId = student.getEnd();

            stmt.setInt(1, startId);
            stmt.setInt(2, endId);

            try (ResultSet resultSet = stmt.executeQuery()) {
                ObservableList<Subject> subjectList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Subject subject = new Subject(
                            // Extract data from ResultSet and create a Subject object
                            resultSet.getString("sub_code"),
                            resultSet.getInt("units"),
                            resultSet.getString("subject"),
                            resultSet.getString("day"),
                            resultSet.getString("schedule"),
                            resultSet.getString("professor")
                    );

                    // Add the subject to the ObservableList
                    subjectList.add(subject);
                }

                // Set the items in the TableView
                Table.setItems(subjectList);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Handle the exception according to your needs
        }
    }

    
    private void setLabels(Students student) throws SQLException {
        if (student != null) {
            fullnameLBL.setText(student.getFirstName() + " " + student.getMiddleName() + " " + student.getLastName());
            courseLBL.setText(student.getCourse());
            yrAnsSecLBL.setText(student.getYear() + " " + student.getSection());
            addressLBL.setText(student.getLocation());

            // Check if the student has an image
            if (student.getStudentImage() != null) {
                showImage(student.getStudentImage());
            } else {
                // Handle the case where no image is available
                studIMG.setImage(null);
            }
        } else {
            // Handle the case where the student is null
        }
    }

    private void showImage(Blob imageBlob) throws SQLException {
        if (imageBlob != null) {
            byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            Image image = new Image(bis, 150, 150, false, true);
            studIMG.setImage(image);
        } else {
            // Handle the case where the imageBlob is null
            studIMG.setImage(null);
        }
    }

}
