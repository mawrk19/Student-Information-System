package students;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import application.DatabaseManager;
import application.MainFrameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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

    private ObservableList<Students> studentList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setStudents();
        configureTable();
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        fullnameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
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

                    String fullname = firstName + " " + middleName + " " + lastName;

                    Students studentObj = new Students(fullname, course1, year1, section1, location1, scode1, date1, sid1, gender1);
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

    public static class Students {

        private String fullname;
        private String course;
        private String year;
        private String section;
        private String location;
        private int scode;
        private String date;
        private int sid;
        private String gender;

        public Students(String fullname, String course, String year, String section, String location, int scode,
                String date, int sid, String gender) {
            this.fullname = fullname;
            this.course = course;
            this.year = year;
            this.section = section;
            this.location = location;
            this.scode = scode;
            this.date = date;
            this.sid = sid;
            this.gender = gender;
        }

        public String getFullname() {
            return fullname;
        }

        public String getCourse() {
            return course;
        }

        public String getYear() {
            return year;
        }

        public String getSection() {
            return section;
        }

        public String getLocation() {
            return location;
        }

        public int getScode() {
            return scode;
        }

        public String getDate() {
            return date;
        }

        public int getSid() {
            return sid;
        }

        public String getGender() {
            return gender;
        }
    }
    
    private void configureTable() {
        // Make the TableView editable
        studentTableView.setEditable(true);

        // Make specific columns editable
        fullnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        courseColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        sectionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        locationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        genderColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }
    
    
    @FXML
    private void handleUpdateButton() {
        boolean confirmed = showConfirmationDialog("Update Confirmation", "Are you sure you want to update these students?");
        if (confirmed) {
            try (Connection con = DatabaseManager.getConnection()) {
                StringBuilder updateQuery = new StringBuilder("UPDATE student SET First_name = CASE sid ");

                for (Students student : studentList) {
                    updateQuery.append("WHEN ").append(student.getSid()).append(" THEN ?");
                }

                updateQuery.append("END, Middle_name = CASE sid ");
                for (Students student : studentList) {
                    updateQuery.append("WHEN ").append(student.getSid()).append(" THEN ?");
                }

                updateQuery.append("END, last_name = CASE sid ");
                for (Students student : studentList) {
                    updateQuery.append("WHEN ").append(student.getSid()).append(" THEN ?");
                }

                updateQuery.append(", course = CASE sid ");
                for (Students student : studentList) {
                    updateQuery.append("WHEN ").append(student.getSid()).append(" THEN ?");
                }

                updateQuery.append(", year = CASE sid ");
                for (Students student : studentList) {
                    updateQuery.append("WHEN ").append(student.getSid()).append(" THEN ?");
                }

                updateQuery.append(", section = CASE sid ");
                for (Students student : studentList) {
                    updateQuery.append("WHEN ").append(student.getSid()).append(" THEN ?");
                }

                updateQuery.append(", location = CASE sid ");
                for (Students student : studentList) {
                    updateQuery.append("WHEN ").append(student.getSid()).append(" THEN ?");
                }

                updateQuery.append(", scode = CASE sid ");
                for (Students student : studentList) {
                    updateQuery.append("WHEN ").append(student.getSid()).append(" THEN ?");
                }

                updateQuery.append(", date = CASE sid ");
                for (Students student : studentList) {
                    updateQuery.append("WHEN ").append(student.getSid()).append(" THEN ?");
                }

                updateQuery.append(", gender = CASE sid ");
                for (Students student : studentList) {
                    updateQuery.append("WHEN ").append(student.getSid()).append(" THEN ?");
                }

                updateQuery.append("END WHERE sid IN (");
                for (Students student : studentList) {
                    updateQuery.append(student.getSid()).append(",");
                }
                updateQuery.deleteCharAt(updateQuery.length() - 1); // Remove the trailing comma
                updateQuery.append(")");

                try (PreparedStatement stmt = con.prepareStatement(updateQuery.toString())) {
                    int parameterIndex = 1;
                    for (Students student : studentList) {
                        String[] names = student.getFullname().split("\\s+");
                        stmt.setString(parameterIndex++, names.length > 0 ? names[0] : null);
                        stmt.setString(parameterIndex++, names.length > 1 ? names[1] : null);
                        stmt.setString(parameterIndex++, names.length > 2 ? names[2] : null);
                        stmt.setString(parameterIndex++, student.getCourse());
                        stmt.setString(parameterIndex++, student.getYear());
                        stmt.setString(parameterIndex++, student.getSection());
                        stmt.setString(parameterIndex++, student.getLocation());
                        stmt.setInt(parameterIndex++, student.getScode());
                        stmt.setString(parameterIndex++, student.getDate());
                        stmt.setString(parameterIndex++, student.getGender());
                        // Add more set statements for other columns if needed
                    }

                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void handleDeleteButton() {
        // Handle the delete button action
        Students selectedStudent = studentTableView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            boolean confirmed = showConfirmationDialog("Delete Confirmation", "Are you sure you want to delete this student?");
            if (confirmed) {
                deleteStudentFromDatabase(selectedStudent);
                studentList.remove(selectedStudent);
            }
        }
    }
    
    private void updateStudentInDatabase(Students student) {
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement stmt = con.prepareStatement("UPDATE student SET " +
                     "First_name = ?, Middle_name = ?, last_name = ?, course = ?, " +
                     "year = ?, section = ?, location = ?, scode = ?, date = ?, gender = ? " +
                     "WHERE sid = ?")) {

            String[] names = student.getFullname().split("\\s+");
            stmt.setString(1, names.length > 0 ? names[0] : null);
            stmt.setString(2, names.length > 1 ? names[1] : null);
            stmt.setString(3, names.length > 2 ? names[2] : null);
            stmt.setString(4, student.getCourse());
            stmt.setString(5, student.getYear());
            stmt.setString(6, student.getSection());
            stmt.setString(7, student.getLocation());
            stmt.setInt(8, student.getScode());
            stmt.setString(9, student.getDate());
            stmt.setString(10, student.getGender());
            stmt.setInt(11, student.getSid());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); 
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
    
}
