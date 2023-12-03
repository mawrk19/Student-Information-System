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

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Blob;

import application.DatabaseManager;

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
    private void initialize() {
        try {
            Students student = getStudent(1); // Assuming you want the student with sid = 1
            if (student != null) {
                setLabel(student);
            } else {
                // Handle the case where the student is not found
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
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

                    // Retrieve the image as a java.sql.Blob
                    Blob studentImageBlob = resultSet.getBlob("image");

                    Students student = new Students(firstName, middleName, lastName, course1, year1, section1,
                            location1, scode1, date1, sid1, gender1, studentImageBlob);

                    // Set the image in the Students object
                    student.setStudentImage(studentImageBlob);

                    return student;
                } else {
                    return null; // No student found with the given sid
                }
            }
        }
    }

    // Corrected the method name to match the one specified in the FXML file
    @FXML
    private void searchActionPerformed(ActionEvent e) {
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM student WHERE scode = ?")) {

            String scode = search.getText();
            if (scode.length() > 0) {
                stmt.setString(1, scode);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        String course = resultSet.getString("course");
                        String year = resultSet.getString("year");
                        String section = resultSet.getString("section");
                        String address = resultSet.getString("location");

                        // Update your labels with the retrieved data
                        courseLBL.setText(course);
                        yrAnsSecLBL.setText(year);
                        
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Handle the exception according to your needs
        }
    }
    

    @FXML
    private void setLabel(Students student) throws SQLException {
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


//    private void shawerla() {
//    	Blob imageBlob = Students.getStudentImage();
//        byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
//        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
//        Image image = new Image(bis, 129, 173, false, true);
//        studIMG.setImage(image);
//    }
}
