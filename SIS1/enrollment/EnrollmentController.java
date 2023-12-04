package enrollment;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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
import students.Students;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

	@FXML
	private AnchorPane newContentAnchorPane;

	@FXML
	private ImageView imageView;

	private ObservableList<Subject> subjectsList = FXCollections.observableArrayList();

	private Image image;
	
	private String studCode;

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

	@FXML
    private void enrollButtonClickedAction(ActionEvent event) throws SQLException, IOException {
        String studCode = enrollButtonClicked(convertImageToInputStream(image), createStudentsObject());
        System.out.println("Enrolled student with studCode: " + studCode);
    }
	
	private void replaceTableViewContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/enrollment/Transaction.fxml"));
            AnchorPane newTableAnchorPane = loader.load();

            AnchorPane tableViewParent = (AnchorPane) subjectsTableView.getParent();
            Double topAnchor = AnchorPane.getTopAnchor(subjectsTableView);
            Double bottomAnchor = AnchorPane.getBottomAnchor(subjectsTableView);
            Double leftAnchor = AnchorPane.getLeftAnchor(subjectsTableView);
            Double rightAnchor = AnchorPane.getRightAnchor(subjectsTableView);

            tableViewParent.getChildren().remove(subjectsTableView);

            AnchorPane.setTopAnchor(newTableAnchorPane, topAnchor);
            AnchorPane.setBottomAnchor(newTableAnchorPane, bottomAnchor);
            AnchorPane.setLeftAnchor(newTableAnchorPane, leftAnchor);
            AnchorPane.setRightAnchor(newTableAnchorPane, rightAnchor);

            tableViewParent.getChildren().add(newTableAnchorPane);

            TransactionController transactionController = loader.getController();
            transactionController.setStudCode(this.studCode);

            System.out.println("Stud code from enrollment: " + studCode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	
	private void setSubjectsBasedOnSelection() {
		String selectedCourse = courseCMB.getValue();
		String selectedYear = yrCMB.getValue();
		String selectedSection = secCMB.getValue();
		String selectedSemester = semCMB.getValue();
		String selectedType = statCMB.getValue();

		int startId;
		int endId;

		if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) && "Regular".equals(selectedType)) {
			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) && "Regular".equals(selectedType)) {
			startId = 10;
			endId = 17;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) && "Regular".equals(selectedType)) {
			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) && "Regular".equals(selectedType)) {
			startId = 10;
			endId = 17;
		} else {
			// Clear the subjects table if none of the conditions are met
			clearSubjectsTable();
			return;
		}

		// Set subjects for the specified conditions
		setSubjectsForSemester(createStudentsObject());
	}

	private Students createStudentsObject() {
		String selectedCourse = courseCMB.getValue();
		String selectedYear = yrCMB.getValue();
		String selectedSection = secCMB.getValue();
		String selectedSemester = semCMB.getValue();
		String selectedType = statCMB.getValue();

		int startId = 1; // Default values
		int endId = 9; // Default values

		if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) && "Regular".equals(selectedType)) {
			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) && "Regular".equals(selectedType)) {
			startId = 10;
			endId = 17;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) && "Regular".equals(selectedType)) {
			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) && "Regular".equals(selectedType)) {
			startId = 10;
			endId = 17;
		}

		return new Students(selectedType, selectedYear, selectedSection, selectedType, selectedType, selectedType,
				selectedType, endId, selectedType, endId, selectedType, null, startId, endId);
	}

	private void clearSubjectsTable() {
		subjectsTableView.getItems().clear();
	}

	private void setSubjectsForSemester(Students student) {
		int startId = student.getStart();
		int endId = student.getEnd();

		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM subjects WHERE id between ? and ?")) {

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
		if (image != null && image.getPixelReader() != null) {
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} else {
			InputStream defaultImageStream = getClass().getResourceAsStream("/img_icons/NoPeep.png");
			return defaultImageStream;
		}
	}
	
	private String enrollButtonClicked(InputStream imageStream, Students student) throws SQLException, IOException {
		String selectedCourse = courseCMB.getValue();
		String enrollmentDate = dateTF.getText();
		String firstName = fNameTF.getText();
		String gender = genderCMB.getValue();
		String location = locCMB.getValue();
		String lastName = lNameTF.getText();
		String middleName = mNameTF.getText();
		String section = secCMB.getValue();
		String year1 = yrCMB.getValue();
		String sy = "2023";
		String startID = String.valueOf(student.getStart());
		String endID = String.valueOf(student.getEnd());

		String studCode = null;

		UserSession session = UserSession.getInstance();
		String username = session.getUsername();

		if (isFormFilled()) {
			// Form is filled, proceed with replacing content and database operations
			replaceTableViewContent();

			try (Connection con = DatabaseManager.getConnection()) {
				String sql;

				if (image != null) {
					sql = "INSERT INTO student (course, date, First_name, gender, location, last_name, Middle_name, section, sy, year, eSubjectsStart, eSubjectsEnd, image, encoder) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				} else {
					sql = "INSERT INTO student (course, date, First_name, gender, location, last_name, Middle_name, section, sy, year, eSubjectsStart, eSubjectsEnd, encoder) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
					preparedStatement.setString(9, sy);
					preparedStatement.setString(10, year1);

					if (image != null) {
						preparedStatement.setString(11, startID);
						preparedStatement.setString(12, endID);
						preparedStatement.setBlob(13, imageStream);
						preparedStatement.setString(14, username);
					} else {
						preparedStatement.setString(11, startID);
						preparedStatement.setString(12, endID);
						preparedStatement.setString(13, username);
					}

					int rowsAffected = preparedStatement.executeUpdate();

					if (rowsAffected > 0) {
						try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
							if (generatedKeys.next()) {
								int generatedId = generatedKeys.getInt(1);
								System.out.println("Student with ID " + generatedId + " inserted successfully.");

								String formattedId = String.format("%04d", generatedId);
								studCode = sy + formattedId; // Set studCode here

								System.out.println(studCode + " vs. alfredy");
								
								TransactionController transacCon = TransactionController.getInstance();
								transacCon.setStudCode(studCode);
								
								String sql1 = "UPDATE student SET scode = ? WHERE sid = ?";
								try (PreparedStatement scodeStatement = con.prepareStatement(sql1)) {
									scodeStatement.setString(1, studCode);
									scodeStatement.setInt(2, generatedId);
									int scodeRowsAffected = scodeStatement.executeUpdate();		
			
									if (scodeRowsAffected > 0) {
										System.out.println("Scode inserted successfully.");
									} else {
										System.out.println("Failed to insert scode.");
									}
								}
							}
						}
					} else {
						System.out.println("No rows affected. Insertion failed.");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			// Form is not filled, display an error message or take appropriate action
			System.out.println("Form is not filled. Please fill in all required fields.");

		}
		return studCode;
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

	@FXML
	private void insertIMG() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			Image newImage = new Image(selectedFile.toURI().toString());
			imageView.setImage(newImage);
			image = newImage;
		}
	}

	public void setStudCode(String studCode) {
        this.studCode = studCode;
    }
	
	public String getStudCode() {
	    return this.studCode;
	}
	
	private boolean isFormFilled() {
        if (courseCMB.getValue() == null || dateTF.getText().isEmpty() || fNameTF.getText().isEmpty()
                || genderCMB.getValue() == null || locCMB.getValue() == null || lNameTF.getText().isEmpty()
                || mNameTF.getText().isEmpty() || secCMB.getValue() == null || semCMB.getValue() == null
                || statCMB.getValue() == null || yrCMB.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Form");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the required fields.");
            alert.showAndWait();

            return false;
        }

        return true;
    }
}
