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
import application.MainFrameController;
import application.UserSession;
import javafx.application.Platform;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import students.Students;

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
    private TextField amtTF;

    @FXML
    private Label tuitionLBL;

    @FXML
    private Button saveAndPrint;
    
    private double tuitionTotal;
    private double miscTotal;

    private static TransactionController instance;

    private String studCode;

    private double tuition = 1000; // Assuming tuition is a double
    
    private boolean continuousUpdate = false;

    public void initialize() {
        ObservableList<String> mop = FXCollections.observableArrayList("Scholar", "Full");
        MOPCMB.setItems(mop);

        ObservableList<String> late = FXCollections.observableArrayList("Yes", "No");
        lateCMB.setItems(late);

        ObservableList<String> scheme = FXCollections.observableArrayList("1", "2");
        schemeCMB.setItems(scheme);

        MOPCMB.setOnAction(event -> setTransactionBasedOnSelection());
        schemeCMB.setOnAction(event -> setTransactionBasedOnSelection());
        lateCMB.setOnAction(event -> setTransactionBasedOnSelection());
        transacIDTF.setOnAction(event -> setTransactionBasedOnSelection());
        amtTF.setOnAction(event -> setTransactionBasedOnSelection());
        libCB.setOnAction(event -> setTransactionBasedOnSelection());
        medCB.setOnAction(event -> setTransactionBasedOnSelection());
        sciCB.setOnAction(event -> setTransactionBasedOnSelection());
        comCB.setOnAction(event -> setTransactionBasedOnSelection());
        athCB.setOnAction(event -> setTransactionBasedOnSelection());
        mediaCB.setOnAction(event -> setTransactionBasedOnSelection());
        saveAndPrint.setOnAction(arg0 -> {
			try {
				saveAndPrintClicked(arg0);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        
        saveAndPrint.setOnAction(event -> {
            continuousUpdate = false; // Stop continuous updates
            try {
				saveAndPrintClicked(event);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
        new Thread(() -> {
            continuousUpdate = true;
            while (continuousUpdate) {
                setTransactionBasedOnSelection();
                try {
                    Thread.sleep(1000); // Adjust the sleep duration as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
        

    private void setTransactionBasedOnSelection() {
        Platform.runLater(() -> {
            String mop = MOPCMB.getValue();
            String scheme = schemeCMB.getValue();
            String late = lateCMB.getValue();
            String transacID = transacIDTF.getText();
            String amt = amtTF.getText();

            double balance = 0.0;  // Default balance value
            double total = tuitionTotal + miscTotal;
            if (!amt.isEmpty()) {
                balance = total - Double.parseDouble(amt);
            }

            tuitionTotal = calculateTuitionTotal(mop, scheme, late);
            miscTotal = calculateMiscAmount();

            totalLBL.setText(String.valueOf("Total: " + total));
            balanceLBL.setText(String.valueOf("Balance: " + balance));
            tuitionLBL.setText(String.valueOf("Tuition " + tuitionTotal));
            miscLBL.setText(String.valueOf("Miscellaneous " + miscTotal));
        });
    }

    private double calculateTuitionTotal(String mop, String scheme, String late) {
        double tuitionTotal = tuition;

        if ("Scholar".equals(mop)) {
            tuitionTotal -= 1000.0;
        }

        if ("Yes".equals(late)) {
            tuitionTotal += 50.0;
        }

        if ("2".equals(scheme)) {
            tuitionTotal = 3500.0;
        }

        return tuitionTotal;
    }

    // Assuming this method calculates the miscellaneous total based on selected checkboxes
    private double calculateMiscAmount() {
        double miscTotal = 0.0;

        double libFee = 30.0;
        double medFee = 40.0;
        double sciFee = 100.0;
        double comFee = 100.0;
        double athFee = 30.0;
        double mediaFee = 40.0;

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


	@FXML
	private void setStudents() {
		try (Connection con = DatabaseManager.getConnection();
				PreparedStatement stmt = con.prepareStatement("SELECT * FROM student")) {

			try (ResultSet resultSet = stmt.executeQuery()) {
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
					String sem = resultSet.getString("sem");

					Students studentObj = new Students(firstName, middleName, lastName, course1, year1, section1,
							location1, scode1, date1, sid1, gender1, null, sid1, sid1, sem);
					// studentList.add(studentObj);
				}
				// studentTableView.setItems(studentList);

			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception as needed
		}
	}
	
	@FXML
	private void saveAndPrintClicked(ActionEvent event) throws IOException {
	    continuousUpdate = false; // Stop continuous updates
	    TransactionController trans = TransactionController.getInstance();
	    String studCode1 = trans.getStudCode();

	    if (studCode1 == null || studCode1.isEmpty()) {
	        System.out.println("StudCode is not available. Please check your implementation.");
	        System.out.println("eto scode: " + studCode1);
	        return;
	    }

	    if (validateInputs()) {
	        saveAndPrint();
	        returnToEnrollment(event );
	    }
	}
	
	private boolean validateInputs() {
	    String mop = MOPCMB.getValue();
	    String transacID = transacIDTF.getText();
	    String late = lateCMB.getValue();
	    String scheme = schemeCMB.getValue();
	    String amt = amtTF.getText();

	    if (mop == null || transacID.isEmpty() || late == null || scheme == null || amt.isEmpty()) {
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Invalid Inputs");
	        alert.setContentText("Please fill in all required fields.");
	        alert.showAndWait();
	        return false;
	    }

	    // Additional validation if needed

	    return true;
	}


	public static TransactionController getInstance() {
		if (instance == null) {
			instance = new TransactionController();
		}
		return instance;
	}

	private double calculateTotalAmount() {
		TransactionController trans = TransactionController.getInstance();
		String mop = MOPCMB.getValue();
		String late = lateCMB.getValue();
		String scheme = schemeCMB.getValue();

		double miscTotal = calculateMiscAmount();

		double tuitionTotal;

		// Set tuitionTotal based on the selected scheme
		if ("1".equals(scheme)) {
			tuitionTotal = 1000.0;
		} else if ("2".equals(scheme)) {
			tuitionTotal = 3500.0;
		} else {
			throw new IllegalArgumentException("Invalid scheme selected.");
		}

		// Adjust tuitionTotal based on the payment mode (mop)
		if ("Scholar".equals(mop)) {
			tuitionTotal -= 1000.0;
		}

		// Adjust balance based on late enrollment
		if ("Yes".equals(late)) {
			tuitionTotal += 50.0;
		}

		return tuitionTotal + miscTotal;

	}

	private void saveAndPrint() {
	    TransactionController trans = TransactionController.getInstance();
	    String studCode1 = trans.getStudCode();
	    String mop = MOPCMB.getValue();
	    String transacID = transacIDTF.getText();
	    String late = lateCMB.getValue();
	    String scheme = schemeCMB.getValue();

	    System.out.println("has Transaction scode " + studCode1);

	    if (mop == null || transacID.isEmpty() || late == null || scheme == null) {
	        System.out.println("Please fill in all required fields.");
	        return;
	    }

	    double totalAmount = calculateTotalAmount();

<<<<<<< HEAD
			try (Connection con = DatabaseManager.getConnection()) {
				String sql;
		
					sql = "INSERT INTO transaction (transaction_ID, payment_mode, amt_due, amount, late, total, balance, misc_total, tuition_total, encoder, date) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
=======
	    UserSession session = UserSession.getInstance();
	    String encoder = session.getUsername();

	    try (Connection con = DatabaseManager.getConnection()) {
	        String sql = "UPDATE transaction SET payment_mode=?, amount=?, late=?, total=?, balance=?, misc_total=?, tuition_total=?, encoder=?, date=?, scheme=? WHERE scode=?";
>>>>>>> a42a7e0689459afcd5741c539341a9860d0036c5

	        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
	            preparedStatement.setString(1, mop);
	            preparedStatement.setString(2, amtTF.getText());
	            preparedStatement.setString(3, late);
	            preparedStatement.setString(4, totalLBL.getText());
	            preparedStatement.setString(5, balanceLBL.getText());
	            preparedStatement.setDouble(6, calculateMiscAmount());
	            preparedStatement.setString(7, tuitionLBL.getText());
	            preparedStatement.setString(8, encoder);
	            preparedStatement.setString(9, getCurrentDate());
	            preparedStatement.setString(10, scheme);
	            preparedStatement.setString(11, studCode1);

	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Transaction record updated successfully.");
	            } else {
	                System.out.println("No rows affected. Update failed.");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // for development only, replace with proper logging
	        System.out.println("An error occurred. Please contact support.");
	    }
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
	
	private void returnToEnrollment(ActionEvent event) throws IOException {
		BackgroundFill ube = new BackgroundFill(Color.web("#3c5199"), null, null);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/enrollment/Enrollment.fxml"));
		Parent timetable = loader.load();

		AnchorPane.setLeftAnchor(timetable, 10.0);
		AnchorPane.setRightAnchor(timetable, 10.0);
		AnchorPane.setTopAnchor(timetable, 10.0);
		AnchorPane.setBottomAnchor(timetable, 20.0);

		FXMLLoader mainFrameLoader = new FXMLLoader(getClass().getResource("/application/MainFrame.fxml"));
		Parent mainFrame = mainFrameLoader.load();
		MainFrameController mainFrameController = mainFrameLoader.getController();

		mainFrameController.Profileicn.setStyle("-fx-background-color: #5d76dc; -fx-border-radius: 50; -fx-background-radius: 25;");

		mainFrameController.Dashboard.setStyle("-fx-border-radius: 25 0 0 25;");
		mainFrameController.Dashboard.setBackground(new Background(ube));
		mainFrameController.Dashboard.setTextFill(Color.WHITE);
		mainFrameController.StudentProf.setStyle("-fx-border-radius: 25 0 0 25;");
		mainFrameController.StudentProf.setBackground(new Background(ube));
		mainFrameController.StudentProf.setTextFill(Color.WHITE);
//	        Timetable.setStyle("-fx-border-radius: 25 0 0 25;");
//	        Timetable.setBackground(new Background(ube));
//	        Timetable.setTextFill(Color.WHITE);
		mainFrameController.Schedule.setStyle("-fx-border-radius: 25 0 0 25;");
		mainFrameController.Schedule.setBackground(new Background(ube));
		mainFrameController.Schedule.setTextFill(Color.WHITE);
		mainFrameController.Evaluation.setStyle("-fx-border-radius: 25 0 0 25;");
		mainFrameController.Evaluation.setBackground(new Background(ube));
		mainFrameController.Evaluation.setTextFill(Color.WHITE);
//	        Grading.setStyle("-fx-border-radius: 25 0 0 25;");
//	        Grading.setBackground(new Background(ube));
//	        Grading.setTextFill(Color.WHITE);
		mainFrameController.Enrollment.setStyle("-fx-border-radius: 25 0 0 25;");
		mainFrameController.Enrollment.setBackground(new Background(ube));
		mainFrameController.Enrollment.setTextFill(Color.WHITE);
		mainFrameController.oldEnrollment.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		mainFrameController.oldEnrollment.setTextFill(Color.BLACK);
		mainFrameController.Students.setStyle("-fx-border-radius: 25 0 0 25;");
		mainFrameController.Students.setBackground(new Background(ube));
		mainFrameController.Students.setTextFill(Color.WHITE);

		mainFrameController.setContent(timetable);

		Scene scene = new Scene(mainFrame);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();

		double windowWidth = stage.getWidth();
		double windowHeight = stage.getHeight();

		stage.setWidth(windowWidth);
		stage.setHeight(windowHeight);
	}
}
