package students;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StudentsController {

    @FXML
    private Button DeleteBTN;

    @FXML
    private Button UpdateBTN;

    @FXML
    private TableColumn<Students, String> courseColumn;

    @FXML
    private TableColumn<Students, String> sidColumn;
    
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
        System.out.println(studentList);
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
                    int year1 = resultSet.getInt("year");
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
        private int year;
        private String section;
        private String location;
        private int scode;
        private String date;
        private int sid;
		private String gender;

        public Students(String fullname, String course, int year, String section, String location, int scode,
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

        // Add getters if needed
        public String getFullname() {
            return fullname;
        }

        public String getCourse() {
            return course;
        }

        public int getYear() {
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
        
        public int getSID() {
            return sid;
        }
        
        public String getGender() {
            return gender;
        }
    }
}
