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
import javafx.fxml.FXML;
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
    private ComboBox<String> lateCMB;

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
    private TextField totalTF;

    @FXML
    private TextField transacIDTF;

    @FXML
    private Label tuitionLBL;

    private int tuition = 1000;

    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> mop = FXCollections.observableArrayList("Scholar", "Full");
        MOPCMB.setItems(mop);

        ObservableList<String> late = FXCollections.observableArrayList("Yes", "No");
        lateCMB.setItems(late);
        
        ObservableList<String> scheme = FXCollections.observableArrayList("1", "2");
        schemeCMB.setItems(scheme);
    }


    private void print(Transaction transac) {
    
    }

    private void saveAndPrintClicked(Transaction transac) {
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
        totalTF.setText(String.valueOf(totalAmount));

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
            String sql = "INSERT INTO transaction (transaction_ID, payment_mode, amt_due, amount, late, total, balance, misc_total, tuition_total, encoder, date, scheme) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, transacID);
                preparedStatement.setString(2, mop);
                preparedStatement.setString(3, amtDueLBL.getText());
                preparedStatement.setString(4, amtLBL.getText());
                preparedStatement.setString(5, late);
                preparedStatement.setString(6, totalTF.getText());
                preparedStatement.setString(7, balanceLBL.getText());
                preparedStatement.setDouble(8, miscTotal); // Use setDouble for numeric values
                preparedStatement.setString(9, tuitionLBL.getText());
                preparedStatement.setString(10, encoder);
                preparedStatement.setString(11, getCurrentDate());
                preparedStatement.setString(12, scheme); // Assuming scheme is a string in your database

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            // Do something with the generated keys
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
}
