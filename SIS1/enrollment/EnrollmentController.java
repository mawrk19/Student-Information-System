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

		ObservableList<String> genders = FXCollections.observableArrayList("Male", "Female");
		genderCMB.setItems(genders);

		ObservableList<String> locations = FXCollections.observableArrayList("Camarin", "Congress", "South");
		locCMB.setItems(locations);

		ObservableList<String> sections = FXCollections.observableArrayList("A", "B", "C");
		secCMB.setItems(sections);

		ObservableList<String> years = FXCollections.observableArrayList("1st", "2nd", "3rd", "4th");
		yrCMB.setItems(years);

		ObservableList<String> type = FXCollections.observableArrayList("Regular", "Irregular");
		statCMB.setItems(type);

		ObservableList<String> sem = FXCollections.observableArrayList("1st", "2nd");
		semCMB.setItems(sem);

		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			dateTF.setText(LocalDateTime.now().format(formatter));
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();

		// Initialize TableView and TableColumn for subjects
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		subCodeColumn.setCellValueFactory(new PropertyValueFactory<>("subCode"));
		unitsColumn.setCellValueFactory(new PropertyValueFactory<>("units"));
		subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));

		// Bind the ObservableList to the TableView
		subjectsTableView.setItems(subjectsList);

		// Fetch subjects data from the database and populate the ObservableList
		fetchSubjectsFromDatabase();
		
		System.out.println(subjectsList);
	}

	private int getGeneratedId(Connection con) throws SQLException {
		int generatedId = 0;

		try (Statement statement = con.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()")) {

			if (resultSet.next()) {
				generatedId = resultSet.getInt(1);
			}
		}

		return generatedId;
	}

	@FXML
	private void enrollButtonClicked() {
		UserSession session = UserSession.getInstance();
		String username = session.getUsername();
		String selectedCourse = courseCMB.getValue();
		String enrollmentDate = dateTF.getText();
		String firstName = fNameTF.getText();
		String gender = genderCMB.getValue();
		String location = locCMB.getValue();
		String lastName = lNameTF.getText();
		String middleName = mNameTF.getText();
		String section = secCMB.getValue();
		String year = "2023";

		try (Connection con = DatabaseManager.getConnection();
				PreparedStatement preparedStatement = con.prepareStatement(
						"INSERT INTO student (course, date, First_name, gender, location, last_name, Middle_name, section, year, encoder, scode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS)) {

			int generatedId = getGeneratedId(con);
			String formattedSid = String.format("%s%04d", year, generatedId);

			preparedStatement.setString(1, selectedCourse);
			preparedStatement.setString(2, enrollmentDate);
			preparedStatement.setString(3, firstName);
			preparedStatement.setString(4, gender);
			preparedStatement.setString(5, location);
			preparedStatement.setString(6, lastName);
			preparedStatement.setString(7, middleName);
			preparedStatement.setString(8, section);
			preparedStatement.setString(9, year);
			preparedStatement.setString(10, username);
			preparedStatement.setString(11, formattedSid);

			int rowsInserted = preparedStatement.executeUpdate();

			if (rowsInserted > 0) {
				// Use Platform.runLater() for UI updates
				Platform.runLater(() -> {
					sidTF.setText(formattedSid);

					// Clear other UI components
					clearFields();
				});

				System.out.println("Enrollment successful!");
			}
		} catch (SQLException e) {
			// Use Platform.runLater() for UI updates
			Platform.runLater(() -> {
				e.printStackTrace(); // Log the error or show a user-friendly message
			});
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

		// getters and setters
	}

	void showSubjects() {
		
		
	}
}
