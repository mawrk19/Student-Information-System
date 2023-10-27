package enrollment;


	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;

	import javax.swing.JOptionPane;
	import javax.swing.filechooser.FileNameExtensionFilter;

	import application.DatabaseManager;
	import application.UserSession;
	import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.scene.control.Button;
	import javafx.scene.control.Label;
	import javafx.scene.control.PasswordField;
	import javafx.scene.control.TextField;
	import javafx.scene.paint.Color;
	import javafx.stage.Stage;
	import javafx.stage.StageStyle;

	public class EnrollmentController {		
		
		@FXML
		TextField sytxtbox,studidtxtbox,coursetxtbox,yrtxtbox,agetxtbox,addtxtbox,emailtxtbox;
		
		@FXML
		TextField emailField, regFname, regLname, regUser, regPass;
		@FXML
		PasswordField passField;
		@FXML
		Button signInBTN;
		@FXML
		Label messLabel,tbox;

		public void exportdata(ActionEvent e) {
		DatabaseManager connectNow = new DatabaseManager();
		Connection con = connectNow.getConnection();

		String sy = sytxtbox.getText();
		String studid = studidtxtbox.getText();
		String course = coursetxtbox.getText();
		String yr = yrtxtbox.getText();
	    String age = agetxtbox.getText();
	    String address = addtxtbox.getText();
	    String email = emailtxtbox.getText();
	 	String infos = "INSERT INTO info (Sy, Studid, Course, Yr, Age ,Address,Email) VALUE(?,?,?,?)";

		try {
			PreparedStatement stmt = con.prepareStatement(infos);

			stmt.setString(1, sy);
			stmt.setString(2, studid);
			stmt.setString(3, course);
			stmt.setString(4, yr);
			stmt.setString(5, age);
			stmt.setString(6, address);
			stmt.setString(7, email);
			if (sy.isEmpty() || studid.isEmpty() || course.isEmpty() || yr.isEmpty() || age.isEmpty() || 
					address.isEmpty() || email.isEmpty()) {
				tbox.setText("Kulang kulang ka ba?");
				return;
			}
			int rs = stmt.executeUpdate();
			if (rs == 1) {
				tbox.setText("Succesfully Enrolled");
				sytxtbox.setText("");
				studidtxtbox.setText("");
				coursetxtbox.setText("");
				yrtxtbox.setText("");
				agetxtbox.setText("");
				addtxtbox.setText("");
				emailtxtbox.setText("");
				
				
				

			} else {
				tbox.setText("Failed Registration");

			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			
		}
		}
	}

}
