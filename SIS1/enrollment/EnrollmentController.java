package enrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import application.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class EnrollmentController {

	@FXML
	TextField fname, mname, lname, sid;
	@FXML
	PasswordField passField;
	@FXML
	Button signInBTN;
	@FXML
	Label messLabel, tbox;
	@FXML
	ComboBox<String> yrLvl, section, course, gender, location;

	public void exportdata(ActionEvent e) {
		DatabaseManager connectNow = new DatabaseManager();
		Connection con = connectNow.getConnection();

		String fname1 = fname.getText();
		String mname1 = mname.getText();
		String lname1 = lname.getText();
		String sid1 = sid.getText();
		String yr = yrLvl.getValue();
		String sectionValue = section.getValue();
		String courseValue = course.getValue();
		String genderValue = gender.getValue();
		String locationValue = location.getValue();

		if (fname1.isEmpty() || mname1.isEmpty() || lname1.isEmpty() || sid1.isEmpty() || yr == null
				|| sectionValue == null || courseValue == null || genderValue == null || locationValue == null) {
			tbox.setText("Incomplete information");
			return;
		}

		String infos = "INSERT INTO students (First_name, Middle_name, last_name, SID, course, Year, gender, location, section) VALUES (?,?,?,?,?,?,?,?,?)";

		try {
			PreparedStatement stmt = con.prepareStatement(infos);

			stmt.setString(1, fname1);
			stmt.setString(2, mname1);
			stmt.setString(3, lname1);
			stmt.setString(4, sid1);
			stmt.setString(5, courseValue);
			stmt.setString(6, yr);
			stmt.setString(7, genderValue);
			stmt.setString(8, locationValue);
			stmt.setString(9, sectionValue);

			int rs = stmt.executeUpdate();
			if (rs == 1) {
				tbox.setText("Successfully Enrolled");
			} else {
				tbox.setText("Failed Registration");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			tbox.setText("Error: " + ex.getMessage());
		}
	}

}
