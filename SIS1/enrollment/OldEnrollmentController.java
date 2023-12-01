package enrollment;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.embed.swing.SwingFXUtils;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import application.DatabaseManager;
import application.UserSession;

public class OldEnrollmentController implements Initializable {

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
	
	@FXML
	private ImageView imageView; 

	private ObservableList<Subject> subjectsList = FXCollections.observableArrayList();
	
	private Image image;


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ObservableList<String> courses = FXCollections.observableArrayList("BSCS", "BSIT", "BSIS", "BSEMC");
		courseCMB.setItems(courses);

		ObservableList<String> genders = FXCollections.observableArrayList("Male", "Female");
		genderCMB.setItems(genders);

		ObservableList<String> locations = FXCollections.observableArrayList("Camarin", "Congress", "South");
		locCMB.setItems(locations);

		ObservableList<String> sections = FXCollections.observableArrayList("A", "B", "C");
		secCMB.setItems(sections);

		ObservableList<String> years = FXCollections.observableArrayList("1st", "2nd", "3rd", "4th");
		yrCMB.setItems(years);

		ObservableList<String> sem = FXCollections.observableArrayList("1st", "2nd");
		semCMB.setItems(sem);

		ObservableList<String> type = FXCollections.observableArrayList("Regular", "Irregular");
		statCMB.setItems(type);

		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			dateTF.setText(LocalDateTime.now().format(formatter));
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();

		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		subCodeColumn.setCellValueFactory(new PropertyValueFactory<>("subCode"));
		unitsColumn.setCellValueFactory(new PropertyValueFactory<>("units"));
		subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));

		courseCMB.setOnAction(event -> setSubjectsBasedOnSelection());
	    yrCMB.setOnAction(event -> setSubjectsBasedOnSelection());
	    secCMB.setOnAction(event -> setSubjectsBasedOnSelection());
	    semCMB.setOnAction(event -> setSubjectsBasedOnSelection());
	    statCMB.setOnAction(event -> setSubjectsBasedOnSelection());
	    
	    imageView.setOnMouseClicked(event -> {
	        if (event.getButton() == MouseButton.PRIMARY) {
	            // Handle primary (left) mouse click
	            insertIMG();
	        }
	    });
	}

	private void setSubjectsBasedOnSelection() {
	    String selectedCourse = courseCMB.getValue();
	    String selectedYear = yrCMB.getValue();
	    String selectedSection = secCMB.getValue();
	    String selectedSemester = semCMB.getValue();
	    String selectedType = statCMB.getValue();

	    if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
	            && "1st".equals(selectedSemester) && "Regular".equals(selectedType)) {
	        setSubjectsForSemester("BSCS1A1st", 1, 9);
	    } else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
	            && "2nd".equals(selectedSemester) && "Regular".equals(selectedType)) {
	        setSubjectsForSemester("BSCS1A2nd", 10, 17);
	    } else {
	        clearSubjectsTable();
	    }
	}
	

	private void clearSubjectsTable() {
	    subjectsTableView.getItems().clear();
	}
	
	private void setSubjectsForSemester(String semester, int startId, int endId) {
	    try (Connection connection = DatabaseManager.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM subjects WHERE id between ? and ?")) {

	        preparedStatement.setInt(1, startId);
	        preparedStatement.setInt(2, endId);

	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            clearSubjectsTable(); // Clear existing items before adding new ones
	            while (resultSet.next()) {
	                int id = resultSet.getInt("id");
	                String subCode = resultSet.getString("sub_code");
	                int units = resultSet.getInt("units");
	                String subject = resultSet.getString("subject");

	                Subject subjectObj = new Subject(id, subCode, units, subject);
	                subjectsList.add(subjectObj);
	            }
	            subjectsTableView.setItems(subjectsList);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Handle the exception as needed
	    }
	}
	
	private InputStream convertImageToInputStream(Image image) throws IOException {
        // Convert Image to InputStream
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
	
	@FXML
	private void enrollButtonClicked(InputStream image) throws SQLException, IOException {
	    String selectedCourse = courseCMB.getValue();
	    String enrollmentDate = dateTF.getText();
	    String firstName = fNameTF.getText();
	    String gender = genderCMB.getValue();
	    String location = locCMB.getValue();
	    String lastName = lNameTF.getText();
	    String middleName = mNameTF.getText();
	    String section = secCMB.getValue();
	    String year = "2023";

	    UserSession session = UserSession.getInstance();
	    String username = session.getUsername(); // Assuming you have a method to get the username

	    try (Connection con = DatabaseManager.getConnection()) {
	        String sql;

	        if (image != null) {
	            sql = "INSERT INTO student (course, date, First_name, gender, location, last_name, Middle_name, section, year, image, encoder) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        } else {
	            sql = "INSERT INTO student (course, date, First_name, gender, location, last_name, Middle_name, section, year, encoder) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        }

	        try (PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	            preparedStatement.setString(1, selectedCourse);
	            preparedStatement.setString(2, enrollmentDate);
	            preparedStatement.setString(3, firstName);
	            preparedStatement.setString(4, gender);
	            preparedStatement.setString(5, location);
	            preparedStatement.setString(6, lastName);
	            preparedStatement.setString(7, middleName);
	            preparedStatement.setString(8, section);
	            preparedStatement.setString(9, year);

	            if (image != null) {
	                // Assuming image is a byte array
	            	preparedStatement.setBlob(10, image); // Use setBlob for InputStream
	                preparedStatement.setString(11, username); // Set encoder for the image case
	            } else {
	                preparedStatement.setString(10, username); // Set encoder for the non-image case
	            }

	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
	                    if (generatedKeys.next()) {
	                        int generatedId = generatedKeys.getInt(1);
	                        System.out.println("Student with ID " + generatedId + " inserted successfully.");
	                    }
	                }
	            } else {
	                System.out.println("No rows affected. Insertion failed.");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Handle the exception as needed
	    }
	}


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

	private void clearFields() {
		courseCMB.setValue(null);
		dateTF.clear();
		fNameTF.clear();
		genderCMB.setValue(null);
		locCMB.setValue(null);
		lNameTF.clear();
		mNameTF.clear();
		secCMB.setValue(null);
	}
	
	private void insertIMG() {
	    // Open a FileChooser to allow the user to select an image file
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Select Image File");
	    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));

	    File selectedFile = fileChooser.showOpenDialog(null);

	    if (selectedFile != null) {
	        // Load the selected image into the ImageView
	        Image newImage = new Image(selectedFile.toURI().toString());
	        imageView.setImage(newImage);

	        // Set the global 'image' variable for later use in enrollment
	        image = newImage;
	    }
	}
	
	@FXML
	private void enrollButtonClickedAction(ActionEvent event) throws SQLException, IOException {
	    enrollButtonClicked(convertImageToInputStream(image));
	}
}
