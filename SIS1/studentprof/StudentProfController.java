package studentprof;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DatabaseManager;

public class StudentProfController {
    @FXML
    private Label address;

    @FXML
    private Label age;

    @FXML
    private Label courses;

    @FXML
    private Label email;

    @FXML
    private Label section;

    @FXML
    private Label yr;

    @FXML
    private TextField searchh;

    @FXML
    private Button search;  // Added the Button declaration

    // Corrected the method name to match the one specified in the FXML file
    @FXML
    private void searchActionPerformed(ActionEvent e) {
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM sis_database WHERE scode = ?")) {

            String scode = searchh.getText();
            if (scode.length() > 0) {
                stmt.setString(1, scode);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        String course = resultSet.getString("course");
                        String year = resultSet.getString("year");
                        String section = resultSet.getString("section");
                        String address = resultSet.getString("location");

                        // Update your labels with the retrieved data
                        courses.setText(course);
                        yr.setText(year);
                        this.section.setText(section);
                        this.address.setText(address);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Handle the exception according to your needs
        }
    }
}
