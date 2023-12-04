package enrollment;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import application.DatabaseManager;
import application.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TransactionController {

    @FXML
    private ComboBox<String> MOPCMB;
    
    @FXML
    private ComboBox<String> schemeCMB;
    
    @FXML
    private ComboBox<String> lateCMB;

    @FXML
    private Label amtDueLBL;

    @FXML
    private Label amtLBL;

    @FXML
    private CheckBox athCB;

    @FXML
    private Label balanceLBL;

    @FXML
    private CheckBox comCB;
   
    @FXML
    private CheckBox libCB;

    @FXML
    private CheckBox medCB;

    @FXML
    private CheckBox mediaCB;

    @FXML
    private Label miscLBL;

    @FXML
    private CheckBox sciCB;

    @FXML
    private Label totalLBL;

    @FXML
    private TextField transacIDTF;

    @FXML
    private Label tuitionLBL;
    
    @FXML
    private Button saveAndPrint;
    
    private static TransactionController instance;

    private String studCode;

    private int tuition = 1000;

    public void initialize() {
        ObservableList<String> mop = FXCollections.observableArrayList("Scholar", "Full");
        MOPCMB.setItems(mop);

        ObservableList<String> late = FXCollections.observableArrayList("Yes", "No");
        lateCMB.setItems(late);

        ObservableList<String> scheme = FXCollections.observableArrayList("1", "2");
        schemeCMB.setItems(scheme);
    }

    @FXML
    private void saveAndPrintClicked(ActionEvent event) {
    	TransactionController trans = TransactionController.getInstance();
    	String studCode1 = trans.getStudCode();
    			
        if (studCode1 == null || studCode1.isEmpty()) {
            System.out.println("StudCode is not available. Please check your implementation.");
            System.out.println("eto scode: " + studCode1);
            return;
        }

        if (!hasTransaction(studCode1)) {
            System.out.println("eto scode: " + studCode1);
            System.out.println("Please make a transaction before enrolling.");
            return;
        }
        saveAndPrint();
    }

    public static TransactionController getInstance() {
        if (instance == null) {
            instance = new TransactionController();
        }
        return instance;
    }
    
    private boolean hasTransaction(String studentID) {
        try (Connection con = DatabaseManager.getConnection()) {
            String sql = "SELECT COUNT(*) FROM transaction WHERE scode = ?";

            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                preparedStatement.setString(1, studCode);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int transactionCount = resultSet.getInt(1);
                        return transactionCount > 0;
                    }
                }
            }
        } catch (SQLException e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();
            return false;
        }
		return false;
    }


    private void saveAndPrint() {
        String mop = MOPCMB.getValue();
        String transacID = transacIDTF.getText();
        String late = lateCMB.getValue();
        String scheme = schemeCMB.getValue();

        if (mop == null || transacID.isEmpty() || late == null || scheme == null) {
            System.out.println("Please fill in all required fields.");
            return;
        }

        double tuitionTotal;
        double miscTotal = calculateMiscAmount();

        // Set tuitionTotal based on the selected scheme
        if ("1".equals(scheme)) {
            tuitionTotal = 1000.0;
        } else if ("2".equals(scheme)) {
            tuitionTotal = 3500.0;
        } else {
            System.out.println("Invalid scheme selected.");
            return;
        }

        // Adjust tuitionTotal based on the payment mode (mop)
        if ("Scholar".equals(mop)) {
            tuitionTotal -= 1000.0;
        }

        // Set labels and text fields
        tuitionLBL.setText(String.valueOf(tuitionTotal));

        // Set total amount
        double totalAmount = tuitionTotal + miscTotal;
        totalLBL.setText(String.valueOf(totalAmount));

        // Adjust balance based on late enrollment
        double balanceTotal = totalAmount;
        if ("Yes".equals(late)) {
            balanceTotal += 50.0;
        }

        // Set labels for total and balance
        amtLBL.setText(String.valueOf(totalAmount));
        balanceLBL.setText(String.valueOf(balanceTotal));

        UserSession session = UserSession.getInstance();
        String encoder = session.getUsername();

        try (Connection con = DatabaseManager.getConnection()) {
        	
            String sql = "INSERT INTO transaction ( payment_mode, amt_due, amount, late, total, balance, misc_total, tuition_total, encoder, date, scheme, scode) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                
                preparedStatement.setString(1, mop);
                preparedStatement.setString(2, amtDueLBL.getText());
                preparedStatement.setString(3, amtLBL.getText());
                preparedStatement.setString(4, late);
                preparedStatement.setString(5, totalLBL.getText());
                preparedStatement.setString(6, balanceLBL.getText());
                preparedStatement.setDouble(7, miscTotal);
                preparedStatement.setString(8, tuitionLBL.getText());
                preparedStatement.setString(9, encoder);
                preparedStatement.setString(10, getCurrentDate());
                preparedStatement.setString(11, scheme);
                preparedStatement.setString(12, studCode);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            // Retrieve the generated transaction_ID
                            int generatedTransactionID = generatedKeys.getInt(1);

                            // Print the generated transaction_ID
                            System.out.println("Generated Transaction ID: " + generatedTransactionID);
                            
                            // You can use the generatedTransactionID as needed in your application
                        }
                    }
                } else {
                    System.out.println("No rows affected. Insertion failed.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // for development only, replace with proper logging
            System.out.println("An error occurred. Please contact support.");
        }
    }

    private double calculateMiscAmount() {
        double miscTotal = 0.0;

        // Define fees for each checkbox
        double libFee = 30.0;
        double medFee = 40.0;
        double sciFee = 100.0;
        double comFee = 100.0;
        double athFee = 30.0;
        double mediaFee = 40.0;

        // Check which checkboxes are selected and accumulate the fees
        if (libCB.isSelected()) {
            miscTotal += libFee;
        }
        if (medCB.isSelected()) {
            miscTotal += medFee;
        }
        if (sciCB.isSelected()) {
            miscTotal += sciFee;
        }
        if (comCB.isSelected()) {
            miscTotal += comFee;
        }
        if (athCB.isSelected()) {
            miscTotal += athFee;
        }
        if (mediaCB.isSelected()) {
            miscTotal += mediaFee;
        }

        return miscTotal;
    }

    private String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }
    
    public void setStudCode(String studCode) {
        this.studCode = studCode;
    }

    public String getStudCode() {
        return studCode;
    }
}
