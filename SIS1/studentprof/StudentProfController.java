package studentprof;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import students.Students;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import java.sql.Blob;

import application.DatabaseManager;
import application.UserSession;

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
    
    private Image image;
    
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

        try (Connection con = DatabaseManager.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT userImg FROM users WHERE id = ?")) {

               stmt.setInt(1, UserSession.getInstance().getId());
               ResultSet resultSet = stmt.executeQuery();

               if (resultSet.next()) {
                   // If there is a saved image, load it
                   InputStream imageStream = resultSet.getBinaryStream("userImg");
                   if (imageStream != null) {
                       Image savedImage = new Image(imageStream);
                       studIMG.setImage(savedImage);
                       studIMG.setClip(createClip(studIMG));
                       centerImage(studIMG);
                   }
               }

           } catch (SQLException e) {
               e.printStackTrace();
               // Handle the exception as needed
           }
        // Set up the click event to change the image
        studIMG.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                insertIMG(null);
            }
        });
    }

    
    @FXML
    private void insertIMG(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                Image selectedImage = new Image(selectedFile.toURI().toString());
                studIMG.setImage(selectedImage);
                studIMG.setClip(createClip(studIMG));
                centerImage(studIMG);

                // Save the image to the database
                saveImageToDatabase(selectedFile);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
        }
    }

    private void saveImageToDatabase(File imageFile) {
        Connection con = null;
        try {
            con = DatabaseManager.getConnection();
            con.setAutoCommit(false);

            // Convert Image to BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(studIMG.getImage(), null);

            // Convert BufferedImage to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] imageData = baos.toByteArray();

            try (PreparedStatement stmt = con.prepareStatement("UPDATE student SET image = ? WHERE sid = ?")) {
                // Set parameters for the PreparedStatement
                stmt.setBytes(1, imageData);
                stmt.setInt(2, UserSession.getInstance().getId());  // Use the user ID from UserSession

                // Execute the update
                stmt.executeUpdate();

                // Commit the transaction
                con.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception as needed
                con.rollback();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        } finally {
            try {
                if (con != null) {
                    if (!con.getAutoCommit()) {
                        con.setAutoCommit(true); // Reset autocommit to true
                    }
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
                // Handle the rollback exception as needed
            }
        }
    }

    
    private Circle createClip(ImageView imageView) {
        Circle clip = new Circle();
        double radius = Math.min(imageView.getFitWidth() / 2, imageView.getFitHeight() / 2);
        clip.setRadius(radius);
        clip.setCenterX(radius);
        clip.setCenterY(radius);
        return clip;
    }
    
    private void centerImage(ImageView imageView) {
        double imageViewWidth = imageView.getFitWidth();
        double imageViewHeight = imageView.getFitHeight();

        Image image = imageView.getImage();

        if (image != null) {
            double imageWidth = image.getWidth();
            double imageHeight = image.getHeight();

            double offsetX = 0;
            double offsetY = 0;

            if (imageWidth > imageViewWidth) {
                offsetX = (imageWidth - imageViewWidth) / 2;
            } else {
                offsetX = (imageViewWidth - imageWidth) / 2;
            }

            if (imageHeight > imageViewHeight) {
                offsetY = (imageHeight - imageViewHeight) / 2;
            } else {
                offsetY = (imageViewHeight - imageHeight) / 2;
            }

            imageView.setViewport(new Rectangle2D(offsetX, offsetY, imageViewWidth, imageViewHeight));
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

        } else {
            // Handle the case where the student is null
        }
    }

}
