package enrollment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.ResourceBundle;

import application.DatabaseManager;
import application.MainFrameController;
import application.UserSession;
import encoderui.EncoderController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import students.Students;

public class TransactionController {
	
	private String firstName;
    private String middleName;
    private String lastName;
    private String course;
    private String year;
    private String section;
    private String location;
    private String sem;
    private int scode;
    private String date;
    private String sy;
    private int sid;
    private String gender;
    private Blob studentImage;
    private int start;
    private int end;

    @FXML
    private ComboBox<String> MOPCMB;

    @FXML
    private ComboBox<String> schemeCMB;

    @FXML
    private ComboBox<String> lateCMB;
    
    @FXML
    private TextField libTF;
    
    @FXML
    private TextField medTF;
    
    @FXML
    private TextField sciTF;
    
    @FXML
    private TextField compTF;
    
    @FXML
    private TextField athTF;
    
    @FXML
    private TextField mediaTF;

    @FXML
    private Label amtDueLBL;

    @FXML
    private Label balanceLBL;

    @FXML
    private Label totalLBL;

    @FXML
    private TextField amtTF;

    @FXML
    private Label tuitionLBL;
    
    @FXML
    private Label miscLBL;

    @FXML
    private Button saveAndPrint;
    
    @FXML
    private Button addbtn;
    
    @FXML
    private Button deletebtn1;
    
    @FXML
    private Button deletebtn2;
    
    @FXML
    private Button deletebtn3;
    
    @FXML
    private Button deletebtn4;
    
    @FXML
    private Button deletebtn5;
    
    @FXML
    private Button deletebtn6;
    
    @FXML
    private VBox vboxcontainer;
    
    private TextField price;
    
    
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
        
        price = new TextField();

        MOPCMB.setOnAction(event -> setTransactionBasedOnSelection());
        schemeCMB.setOnAction(event -> setTransactionBasedOnSelection());
        lateCMB.setOnAction(event -> setTransactionBasedOnSelection());
        amtTF.setOnAction(event -> setTransactionBasedOnSelection());
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
                    Thread.sleep(1); // Adjust the sleep duration as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        amtTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                // Non-digit characters are not allowed
                amtTF.setText(newValue.replaceAll("[^\\d]", ""));
            }

            // Restrict the maximum value to 10,000
            if (!amtTF.getText().isEmpty()) {
                int amount = Integer.parseInt(amtTF.getText());
                if (amount > 10000) {
                    amtTF.setText("10000");
                }
            }
            setTransactionBasedOnSelection();
        });
    }
        

    private void setTransactionBasedOnSelection() {
    	Platform.runLater(() -> {
            String mop = MOPCMB.getValue();
            String scheme = schemeCMB.getValue();
            String late = lateCMB.getValue();
            String amt = amtTF.getText();

            double tuitionTotal = calculateTuitionTotal(mop, scheme, late);
            double miscTotal = calculateMiscAmount();

            double total = tuitionTotal + miscTotal;
            double balance = 0.0; // Default balance value
            
            if (!amt.isEmpty()) {
                balance = total - Double.parseDouble(amt);
            }

            totalLBL.setText(String.valueOf(total));
            balanceLBL.setText(String.valueOf(balance));
            tuitionLBL.setText(String.valueOf(tuitionTotal));
            miscLBL.setText(String.valueOf(miscTotal));
        });
    }
    
    private double calculateMiscAmount() {
        double libValue = parseTextFieldValue(libTF.getText());
        double medValue = parseTextFieldValue(medTF.getText());
        double sciValue = parseTextFieldValue(sciTF.getText());
        double compValue = parseTextFieldValue(compTF.getText());
        double athValue = parseTextFieldValue(athTF.getText());
        double mediValue = parseTextFieldValue(mediaTF.getText());

        // Retrieve the value from the price field
        double priceValue = parseTextFieldValue(price.getText());

        double total = libValue + medValue + sciValue + compValue + athValue + mediValue + priceValue;

        return total;
    }
    
    private double parseTextFieldValue(String text) {
        if (text.isEmpty()) {
            return 0.0;
        } else {
            return Double.parseDouble(text);
        }
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
    
    @FXML
    private void addrows() {
    	HBox hbox = createRow(); // Create a new row
        vboxcontainer.getChildren().add(hbox);
    }
    
    private HBox createRow() {
        HBox hbox = new HBox();
        hbox.setPrefSize(314, 50);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(20);

        TextField category = new TextField();
        category.setPrefSize(123, 20);
        category.setAlignment(Pos.CENTER);
        category.setFont(new Font(15));

        // Use the class-level 'price' variable instead of creating a new one
        price = new TextField(); // Assign the created price text field to the class-level variable
        price.setPrefSize(78, 23);
        price.setAlignment(Pos.CENTER);

        // Add a listener to update the total when the price changes
        price.textProperty().addListener((observable, oldValue, newValue) -> {
            setTransactionBasedOnSelection(); // Recalculate totals when price changes
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(this::deleteRow); // Assign the deleteRow method

        hbox.getChildren().addAll(category, price, deleteButton);

        return hbox;
    }
    
	
	public void deleteRow(ActionEvent event) {
		Button deleteBtn = (Button) event.getSource(); // Get the delete button that triggered the event
	    HBox hbox = (HBox) deleteBtn.getParent(); // Get the parent HBox containing the delete button
	    clearTextFields(hbox); // Clear the text fields in this row
	    vboxcontainer.getChildren().remove(hbox);
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
					String sy = resultSet.getString("sy");
					int start = resultSet.getInt("eSubjectsStart");
					int end = resultSet.getInt("eSubjectsEnd");
					
					this.firstName = firstName;
		            this.middleName = middleName;
		            this.lastName = lastName;

					Students studentObj = new Students(firstName, middleName, lastName, course1, year1, sy, section1, location1, scode1, date1, sid1, gender1, null, start, end, sem);

					// studentList.add(studentObj);
				}
				// studentTableView.setItems(studentList);

			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception as needed
		}
	}
	
	private void Students(String firstName, String middleName, String lastName, String course1, String year1, String sy,
			String section1, String location1, int scode1, String date1, int sid1, String gender1, Object object,
			int start, int end, String sem) {
		
		this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.course = course;
        this.year = year;
        this.sem = sem; // Added this line to set the 'sem' property
        this.section = section;
        this.location = location;
        this.scode = scode;
        this.date = date;
        this.sid = sid;
        this.gender = gender;
        this.studentImage = studentImage;
        this.start = start;
        this.end = end;
        this.sy = sy;
		
	}


	@FXML
	private void saveAndPrintClicked(ActionEvent event) throws IOException {
	    continuousUpdate = false; // Stop continuous updates
	    TransactionController trans = TransactionController.getInstance();
	    String studCode1 = trans.getStudCode();
	    EnrollmentController enroll = new EnrollmentController();

	    if (studCode1 == null || studCode1.isEmpty()) {
	        System.out.println("StudCode is not available. Please check your implementation.");
	        System.out.println("eto scode: " + studCode1);
	        return;
	    }

	    if (validateInputs()) {
	        saveAndPrint();

	        // Update the UI with the new values
//	        Itext printshit = new Itext();
//	        printshit.generatePDF(studCode1, miscTotal, end, null); // <- Missing semicolon
//
//	        try {
//	            printshit.generatePDF(transacID.toString(), totalAmount, balanceTotal, localdate);
//	        } catch (FileNotFoundException e) {
//	            e.printStackTrace(); // <- Log the exception or handle it appropriately
//	        }

	        setTransactionBasedOnSelection();
	        returnToEnrollment(event);

	        // Re-enable continuous updates after save operation
	        continuousUpdate = true;
	    }
	}

	
	private boolean validateInputs() {
	    String mop = MOPCMB.getValue();
	    String late = lateCMB.getValue();
	    String scheme = schemeCMB.getValue();
	    String amt = amtTF.getText();

	    if (mop == null || late == null || scheme == null || amt.isEmpty()) {
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Invalid Inputs");
	        alert.setContentText("Please fill in all required fields.");
	        alert.showAndWait();
	        return false;
	    }

	    double balance = Double.parseDouble(balanceLBL.getText().replace("Balance: ", ""));
	    if (balance < 0) {
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("Warning");
	        alert.setHeaderText("Overpayment");
	        alert.setContentText("You have entered more than the total amount. Please review your payment.");
	        alert.showAndWait();
	        return false;
	    }

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

	private void saveAndPrint() throws com.itextpdf.io.exceptions.IOException, IOException {
        TransactionController trans = TransactionController.getInstance();
        String studCode1 = trans.getStudCode();
        String mop = MOPCMB.getValue();
        String late = lateCMB.getValue();
        String scheme = schemeCMB.getValue();

        if (mop == null || late == null || scheme == null) {
            System.out.println("Please fill in all required fields.");
            return;
        }

        // Generate a 6-digit transaction ID
        String transactID = generateRandomTransactionID();

        System.out.println("has Transaction scode " + studCode1);

        double totalAmount = calculateTotalAmount();

        UserSession session = UserSession.getInstance();
        String encoder = session.getUsername();

        try (Connection con = DatabaseManager.getConnection()) {
            String sql = "UPDATE transaction SET transaction_id =?, payment_mode=?, amount=?, late=?, total=?, balance=?, misc_total=?, tuition_total=?, encoder=?, date=?, scheme=? WHERE scode=?";

            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                preparedStatement.setString(1, transactID);
                preparedStatement.setString(2, mop);
                preparedStatement.setString(3, amtTF.getText());
                preparedStatement.setString(4, late);
                preparedStatement.setString(5, totalLBL.getText());
                preparedStatement.setString(6, balanceLBL.getText());
                preparedStatement.setDouble(7, calculateMiscAmount());
                preparedStatement.setString(8, tuitionLBL.getText());
                preparedStatement.setString(9, encoder);
                preparedStatement.setString(10, getCurrentDate());
                preparedStatement.setString(11, scheme);
                preparedStatement.setString(12, studCode1);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Transaction record updated successfully.");
                } else {
                    System.out.println("No rows affected. Update failed.");
                }
                setStudents();
                
                
                
                LocalDate localdate = LocalDate.now();
                
                if (validateInputs()) {
                    // Existing code...
                    // Ensure firstName, middleName, and lastName are populated before using them
                    String firstNameStr = (firstName != null && !firstName.isEmpty()) ? firstName : "DefaultFirstName";
                    String middleNameStr = (middleName != null && !middleName.isEmpty()) ? middleName : "DefaultMiddleName";
                    String lastNameStr = (lastName != null && !lastName.isEmpty()) ? lastName : "DefaultLastName";
                    

                    ItextEnroll PDFgenerator = new ItextEnroll();
                    try {
                    	String path = "C:\\Users\\user\\git\\Student-Information-System\\transaction print\\sample2.pdf";
                    	
                    	PDFgenerator.titeGeneratePDF(
                    		    encoder,
                    		    transactID.toString(),
                    		    totalLBL.getText(),
                    		    balanceLBL.getText(),
                    		    localdate,
                    		    firstNameStr,
                    		    middleNameStr,
                    		    lastNameStr,
                    		    libTF.getText(), // Replace with the appropriate text field
                    		    medTF.getText(), // Replace with the appropriate text field
                    		    sciTF.getText(), // Replace with the appropriate text field
                    		    compTF.getText(), // Replace with the appropriate text field
                    		    athTF.getText(), // Replace with the appropriate text field
                    		    mediaTF.getText() // Replace with the appropriate text field
                    		);
                        
                        PDFgenerator.openReceipt(path);
                    } catch (FileNotFoundException e) {
                        // Handle the file not found exception
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // for development only, replace with proper logging
            System.out.println("An error occurred. Please contact support.");
        }
    }
	
	private String generateRandomTransactionID() {
        Random random = new Random();
        int transactionID = random.nextInt(900000) + 100000; // Generates a random number between 100000 and 999999
        return String.valueOf(transactionID);
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
		mainFrameController.Enrollment.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		mainFrameController.Enrollment.setTextFill(Color.BLACK);
		mainFrameController.oldEnrollment.setStyle("-fx-border-radius: 25 0 0 25;");
		mainFrameController.oldEnrollment.setBackground(new Background(ube));
		mainFrameController.oldEnrollment.setTextFill(Color.WHITE);
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
	
	private void clearTextFields(HBox hbox) {
	    for (Node node : hbox.getChildren()) {
	        if (node instanceof TextField) {
	            ((TextField) node).setText(""); // Clear the text field
	        }
	    }
	}
	
}
