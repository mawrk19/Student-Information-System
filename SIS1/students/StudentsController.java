package students;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import application.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class StudentsController {

	@FXML
	private Button DeleteBTN;

	@FXML
	private Button UpdateBTN;

	@FXML
	private TableColumn<Students, String> courseColumn;

	@FXML
	private TableColumn<Students, Integer> sidColumn;

	@FXML
	private TableColumn<Students, String> dateColumn;

	@FXML
	private TableColumn<Students, String> fullnameColumn;

	@FXML
	private TableColumn<Students, String> genderColumn;

	@FXML
	private TableColumn<Students, String> locationColumn;

	@FXML
	private TableColumn<Students, Integer> scodeColumn;

	@FXML
	private TableColumn<Students, String> sectionColumn;

	@FXML
	private TableColumn<Students, Integer> yearColumn;

	@FXML
	private TableView<Students> studentTableView;

	@FXML
	private TableColumn<Students, String> firstNameColumn;

	@FXML
	private TableColumn<Students, String> middleNameColumn;

	@FXML
	private TableColumn<Students, String> lastNameColumn;
	@FXML
	private TableColumn<Students, String> subject;

	private ObservableList<Students> studentList = FXCollections.observableArrayList();

	@FXML
	private void initialize() {
		setStudents();
		configureTable();
		courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		middleNameColumn.setCellValueFactory(new PropertyValueFactory<>("middleName"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
		sectionColumn.setCellValueFactory(new PropertyValueFactory<>("section"));
		locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
		scodeColumn.setCellValueFactory(new PropertyValueFactory<>("scode"));
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		sidColumn.setCellValueFactory(new PropertyValueFactory<>("sid"));
	}

	@FXML
	private void setStudents() {
		try (Connection con = DatabaseManager.getConnection();
				PreparedStatement stmt = con.prepareStatement("SELECT * FROM student")) {

			try (ResultSet resultSet = stmt.executeQuery()) {
				clearStudentsTable(); // Clear existing items before adding new ones
				while (resultSet.next()) {

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

					Students studentObj = new Students(firstName, middleName, lastName, course1, year1, section1,
							location1, scode1, date1, sid1, gender1, null);
					studentList.add(studentObj);
				}
				studentTableView.setItems(studentList);

			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception as needed
		}
	}

	private void clearStudentsTable() {
		studentList.clear();
	}

	private void configureTable() {
		// Make the TableView editable
		studentTableView.setEditable(true);

		// Make specific columns editable
		firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		middleNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		courseColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		sectionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		locationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		genderColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		dateColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		// Set up event handlers for editing
		setupCellValueFactoryAndEditing(firstNameColumn);
		setupCellValueFactoryAndEditing(middleNameColumn);
		setupCellValueFactoryAndEditing(lastNameColumn);
		setupCellValueFactoryAndEditing(courseColumn);
		setupCellValueFactoryAndEditing(sectionColumn);
		setupCellValueFactoryAndEditing(locationColumn);
		setupCellValueFactoryAndEditing(genderColumn);
		setupCellValueFactoryAndEditing(dateColumn);
	}

	private void setupCellValueFactoryAndEditing(TableColumn<Students, String> column) {
		column.setCellValueFactory(new PropertyValueFactory<>(getPropertyName(column)));

		// Set up editing handlers
		column.setOnEditCommit((TableColumn.CellEditEvent<Students, String> t) -> {
			Students student = t.getTableView().getItems().get(t.getTablePosition().getRow());
			String newValue = t.getNewValue();
			setPropertyValue(column, student, newValue);
		});
	}

	private void setPropertyValue(TableColumn<Students, String> column, Students student, String newValue) {
		String propertyName = getPropertyName(column);
		switch (propertyName) {
		case "firstName" -> student.setFirstName(newValue);
		case "middleName" -> student.setMiddleName(newValue);
		case "lastName" -> student.setLastName(newValue);
		case "course" -> student.setCourse(newValue);
		case "section" -> student.setSection(newValue);
		case "location" -> student.setLocation(newValue);
		case "gender" -> student.setGender(newValue);
		case "date" -> student.setDate(newValue);
		}
	}

	private String getPropertyName(TableColumn<Students, ?> column) {
		String columnName = column.getText().toLowerCase();
		return switch (columnName) {
		case "first name" -> "firstName";
		case "middle name" -> "middleName";
		case "last name" -> "lastName";
		case "course" -> "course";
		case "section" -> "section";
		case "location" -> "location";
		case "gender" -> "gender";
		case "date" -> "date";
		default -> "";
		};
	}

	private void updateStudentsInDatabase(ObservableList<Students> students) {
		try (Connection con = DatabaseManager.getConnection();
				PreparedStatement stmt = con.prepareStatement("UPDATE student SET "
						+ "First_name = ?, Middle_name = ?, last_name = ?, course = ?, "
						+ "year = ?, section = ?, location = ?, scode = ?, date = ?, gender = ? " + "WHERE sid = ?")) {

			for (Students student : students) {

				stmt.setString(1, student.getFirstName());
				stmt.setString(2, student.getMiddleName());
				stmt.setString(3, student.getLastName());
				stmt.setString(4, student.getCourse());
				stmt.setString(5, student.getYear());
				stmt.setString(6, student.getSection());
				stmt.setString(7, student.getLocation());
				stmt.setInt(8, student.getScode());
				stmt.setString(9, student.getDate());
				stmt.setString(10, student.getGender());
				stmt.setInt(11, student.getSid());

				String query = stmt.toString(); // Get the string representation of the prepared statement
				System.out.println("Executing query: " + query); // Print the query

				stmt.addBatch(); // Add each update as a batch operation
			}

			// Execute the batch update
			int[] updateCounts = stmt.executeBatch();

			// Check the update counts or handle success/failure as needed
			for (int i = 0; i < updateCounts.length; i++) {
				if (updateCounts[i] <= 0) {
					// Handle failure for the update at index i
					System.out.println("Update failed for student with SID: " + students.get(i).getSid());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleUpdateButton() {
		boolean confirmed = showConfirmationDialog("Update Confirmation",
				"Are you sure you want to update these students?");
		if (confirmed) {
			updateStudentsInDatabase(studentList);
		}
	}

	@FXML
	private void handleDeleteButton() {
		Students selectedStudent = studentTableView.getSelectionModel().getSelectedItem();
		if (selectedStudent != null) {
			boolean confirmed = showConfirmationDialog("Delete Confirmation",
					"Are you sure you want to delete this student?");
			if (confirmed) {
				deleteStudentFromDatabase(selectedStudent);
				studentList.remove(selectedStudent);
			}
		}
	}

	private void deleteStudentFromDatabase(Students student) {
		try (Connection con = DatabaseManager.getConnection();
				PreparedStatement stmt = con.prepareStatement("DELETE FROM student WHERE sid = ?")) {

			stmt.setInt(1, student.getSid());
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean showConfirmationDialog(String title, String message) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		// Add buttons to the alert
		ButtonType buttonTypeYes = new ButtonType("Yes");
		ButtonType buttonTypeNo = new ButtonType("No");
		alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

		// Show the alert and wait for the user's response
		Optional<ButtonType> result = alert.showAndWait();

		return result.isPresent() && result.get() == buttonTypeYes;
	}
	
	public class Table {

	    private String sub_code;
	    private String subject;
	    private String professor;
	    private String schedule;
	    private String day;
	    private int units;

	    public Table(String sub_code, String subject, String professor, String schedule, String day, int units) {
	        this.sub_code = sub_code;
	        this.subject = subject;
	        this.professor = professor;
	        this.schedule = schedule;
	        this.day = day;
	        this.units = units;
	    }

	    public String getSub_code() {
	        return sub_code;
	    }

	    public void setSub_code(String sub_code) {
	        this.sub_code = sub_code;
	    }

	    public String getSubject() {
	        return subject;
	    }

	    public void setSubject(String subject) {
	        this.subject = subject;
	    }

	    public String getProfessor() {
	        return professor;
	    }

	    public void setProfessor(String professor) {
	        this.professor = professor;
	    }

	    public String getSchedule() {
	        return schedule;
	    }

	    public void setSchedule(String schedule) {
	        this.schedule = schedule;
	    }

	    public String getDay() {
	        return day;
	    }

	    public void setDay(String day) {
	        this.day = day;
	    }

	    public int getUnits() {
	        return units;
	    }

	    public void setUnits(int units) {
	        this.units = units;
	    }
	}
}
