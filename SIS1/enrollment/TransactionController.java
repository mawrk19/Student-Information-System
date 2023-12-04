package enrollment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import application.DatabaseManager;
import application.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import students.Students;

public class TransactionController {

    @FXML
    private ComboBox<String> MOPCMB;

    @FXML
    private TextField amtDueTF;

    @FXML
    private TextField amtTF;

    @FXML
    private CheckBox athCB;

    @FXML
    private TextField balanceTF;

    @FXML
    private CheckBox comCB;

    @FXML
    private ComboBox<String> lateCMB;

    @FXML
    private CheckBox libCB;

    @FXML
    private CheckBox medCB;

    @FXML
    private CheckBox mediaCB;

    @FXML
    private TextField miscTF;

    @FXML
    private CheckBox sciCB;

    @FXML
    private TextField totalTF;

    @FXML
    private TextField transacIDTF;

    @FXML
    private TextField tuitionTF;
    
    private int tuiton = 1000;

  
	public void initialize(URL url, ResourceBundle rb) {
    	ObservableList<String> mop = FXCollections.observableArrayList("Scholar", "Full", "Installment");
		MOPCMB.setItems(mop);
		
		ObservableList<String> late = FXCollections.observableArrayList("Yes", "No");
		lateCMB.setItems(late);
		
    }
	
	private void saveAndPrint() {
		
	}
    
	private void saveAndPrintClicked( Transaction transac) throws SQLException, IOException {
		String mop = MOPCMB.getValue();
		String transacID = transacIDTF.getText();
		String amtDue = amtDueTF.getText(); // make label
		String amt = amtTF.getText();
		String late = lateCMB.getValue();
		String total = totalTF.getText(); // make label
		String balance = balanceTF.getText(); // make label
		//get all the checkbox value
		//String miscTotal = yrCMB.getValue(); // make label
		String tuition = tuitionTF.getText(); // make label
		
		if (mop == null || transacID.isEmpty() || amt.isEmpty() || late == null) {
	        System.out.println("Please fill in all required fields.");
	        return;
	    }

		UserSession session = UserSession.getInstance();
		String username = session.getUsername();

			try (Connection con = DatabaseManager.getConnection()) {
				String sql;

			
					sql = "INSERT INTO student (transaction_ID, payment_mode, amt_due, amount, late, total, balance, misc_total, tuition_total, encoder, date) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

				try (PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					preparedStatement.setString(1, transacID);
					preparedStatement.setString(2, mop);
					preparedStatement.setString(3, amtDue);
					preparedStatement.setString(4, amt);
					preparedStatement.setString(5, late);
					preparedStatement.setString(6, total);
					preparedStatement.setString(7, balance);
					preparedStatement.setString(8, miscTotal);
					preparedStatement.setString(9, tuition);
					preparedStatement.setString(10, encoder);
					preparedStatement.setString(10, date);
					
					int rowsAffected = preparedStatement.executeUpdate();

					if (rowsAffected > 0) {
						try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
							if (generatedKeys.next()) {
								// blah blah
							}
						}
					} else {
						System.out.println("No rows affected. Insertion failed.");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
}
