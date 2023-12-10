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
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import students.Students;

public class EncOldTransactionController {
	
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
    private Label miscLBL;

    @FXML
    private CheckBox sciCB;

    @FXML
    private Label totalLBL;
    
    @FXML
    private Label balanceLBL;

    @FXML
    private TextField amtTF;

    @FXML
    private Label tuitionLBL;

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
    private TextField category;
    
    private double tuitionTotal;
    private double miscTotal;
    
    private List<String> additionalCategoryValues = new ArrayList<>();
    private List<String> additionalPriceValues = new ArrayList<>();

    private static EncOldTransactionController instance;

    private String studCode;

    private double tuition = 1000; // Assuming tuition is a double
    
    private boolean continuousUpdate = false;
    
	private String searchedCode;

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
        amtTF.setOnAction(event -> setTransactionBasedOnSelection());
        deletebtn1.setOnAction(event -> deleteRow(event));
        deletebtn2.setOnAction(event -> deleteRow(event));
        deletebtn3.setOnAction(event -> deleteRow(event));
        deletebtn4.setOnAction(event -> deleteRow(event));
        deletebtn5.setOnAction(event -> deleteRow(event));
        deletebtn6.setOnAction(event -> deleteRow(event));
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
    	double total = 0.0;

    	for (Node node : vboxcontainer.getChildren()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;
                TextField priceField = (TextField) hbox.getChildren().get(1); // Assuming price TextField is at index 1

                double priceValue = parseTextFieldValue(priceField.getText());
                total += priceValue;
            }
        }

        return total;
    }
    
    private double parseTextFieldValue(String text) {
        if (text.isEmpty()) {
            return 0.0;
        } else {
            return Double.parseDouble(text);
        }
    }
    
    @FXML
    private void addrows() {
    	HBox hbox = createRow(); // Create a new row
        vboxcontainer.getChildren().add(hbox);
        setTransactionBasedOnSelection();
        
        TextField categoryField = (TextField) hbox.getChildren().get(0); // Assuming category TextField is at index 0
        TextField priceField = (TextField) hbox.getChildren().get(1); // Assuming price TextField is at index 1
        String categoryValue = categoryField.getText();
        String priceValue = priceField.getText();

        // Store the values for the newly added row
        additionalCategoryValues.add(categoryValue);
        additionalPriceValues.add(priceValue);
    }
    
    private HBox createRow() {
        HBox hbox = new HBox();
        hbox.setPrefSize(314, 50);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(20);

        category = new TextField();
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
	    setTransactionBasedOnSelection();
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
	    searchedCode = SearchBarSingleton.getInstance().getSearchbarText();
	    
	    saveAndPrintClicked(event);
	  
	    System.out.println("rich baby daddy gang: " + searchedCode);
	    OldEnrollmentController enroll = new OldEnrollmentController();

	    if (searchedCode == null || searchedCode.isEmpty()) {
	        System.out.println("StudCode is not available. Please check your implementation.");
	        System.out.println("eto scode: " + searchedCode);
	        return;
	    }

	    if (validateInputs()) {
	        saveAndPrint();

	        // Update the UI with the new values
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


	public static EncOldTransactionController getInstance() {
		if (instance == null) {
			instance = new EncOldTransactionController();
		}
		return instance;
	}

	private double calculateTotalAmount() {
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
	    // Get transaction details from UI
	    TransactionController trans = TransactionController.getInstance();
	    String studCode1 = trans.getStudCode();
	    String mop = MOPCMB.getValue();
	    String late = lateCMB.getValue();
	    String scheme = schemeCMB.getValue();

	    // Check if essential details are present
	    if (mop == null || late == null || scheme == null) {
	        System.out.println("Please fill in all required fields.");
	        return;
	    }

	    // Generate a 6-digit transaction ID
	    String transactID = generateRandomTransactionID();
	    
	    double totalAmount = calculateTotalAmount();

	    // Get user session details
	    UserSession session = UserSession.getInstance();
	    String encoder = session.getUsername();

	    try (Connection con = DatabaseManager.getConnection()) {
	        // SQL query to update transaction details
	        String updateSql = "UPDATE transaction SET transaction_id=?, payment_mode=?, amount=?, late=?, total=?, "
	                + "balance=?, misc_total=?, tuition_total=?, encoder=?, date=?, scheme=? WHERE scode=?";

	        try (PreparedStatement updateStatement = con.prepareStatement(updateSql)) {
	            // Set parameters for the update query
	            updateStatement.setString(1, transactID);
	            updateStatement.setString(2, mop);
	            updateStatement.setString(3, amtTF.getText());
	            updateStatement.setString(4, late);
	            updateStatement.setString(5, totalLBL.getText());
	            updateStatement.setString(6, balanceLBL.getText());
	            updateStatement.setDouble(7, calculateMiscAmount());
	            updateStatement.setString(8, tuitionLBL.getText());
	            updateStatement.setString(9, encoder);
	            updateStatement.setString(10, getCurrentDate());
	            updateStatement.setString(11, scheme);
	            updateStatement.setString(12, searchedCode);

	            // Execute the update query
	            int rowsAffected = updateStatement.executeUpdate();

	            // Check if the update was successful
	            if (rowsAffected > 0) {
	                System.out.println("Transaction record updated successfully.");
	            } else {
	                System.out.println("No rows affected. Update failed.");
	                return;
	            }

	            // Retrieve student details for PDF generation
	            String selectSql = "SELECT * FROM student WHERE scode = ?";
	            try (PreparedStatement selectStmt = con.prepareStatement(selectSql)) {
	                selectStmt.setString(1, searchedCode);

	                try (ResultSet resultSet = selectStmt.executeQuery()) {
	                    if (resultSet.next()) {
	                        String firstName = resultSet.getString("First_name");
	                        String middleName = resultSet.getString("Middle_name");
	                        String lastName = resultSet.getString("last_name");
	                        String course = resultSet.getString("course");
	                        String year = resultSet.getString("year");
	                        String section = resultSet.getString("section");
	                        String location = resultSet.getString("location");
	                        int scode = resultSet.getInt("scode");
	                        String date = resultSet.getString("date");
	                        int sid = resultSet.getInt("sid");
	                        String gender = resultSet.getString("gender");
	                        String sem = resultSet.getString("sem");
	                        String sy = resultSet.getString("sy");
	                        int start = resultSet.getInt("eSubjectsStart");
	                        int end = resultSet.getInt("eSubjectsEnd");

	                        // Create a Students object with retrieved data
	                        Students studentObj = new Students(firstName, middleName, lastName, course, year, sy, section,
	                                location, scode, date, sid, gender, null, start, end, sem);

	                        // Generate PDF with student details
	                        generateAndOpenPDF(encoder, transactID, date, studentObj);
	                    }
	                }
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace(); // Log or handle the exception as needed
	        System.out.println("An error occurred. Please contact support.");
	    }
	}

	private void generateAndOpenPDF(String encoder, String transactID, String date2, Students studentObj) throws com.itextpdf.io.exceptions.IOException, IOException {
	    // Ensure firstName, middleName, and lastName are populated before using them
		String categoryValue = category.getText();
        String priceValue = price.getText();
	    String firstNameStr = studentObj.getFirstName();
	    String middleNameStr = studentObj.getMiddleName();
	    String lastNameStr = studentObj.getLastName();

	    Itext PDFgenerator = new Itext();
	    try {
	    	String path = "C:\\Users\\SHEAL\\git\\Student-Information-System\\transaction print\\sample2.pdf";

	        PDFgenerator.generatePDF(
                    encoder,
                    transactID.toString(),
                    totalLBL.getText(),
                    balanceLBL.getText(),
                    date2,
                    firstNameStr,
                    middleNameStr,
                    lastNameStr,
                    libTF.getText(),
                    medTF.getText(),
                    sciTF.getText(),
                    compTF.getText(),
                    athTF.getText(),
                    mediaTF.getText(),
                    categoryValue,
                    priceValue
            );
	        PDFgenerator.openReceipt(path);
	    } catch (FileNotFoundException e) {
	        // Handle the file not found exception
	        e.printStackTrace();
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

	private void returnToEnrollment(ActionEvent event) throws IOException {
		BackgroundFill ube = new BackgroundFill(Color.web("#3c5199"), null, null);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/enrollment/EncBeforeOldEnrollment.fxml"));
		Parent timetable = loader.load();

		AnchorPane.setLeftAnchor(timetable, 10.0);
		AnchorPane.setRightAnchor(timetable, 10.0);
		AnchorPane.setTopAnchor(timetable, 10.0);
		AnchorPane.setBottomAnchor(timetable, 20.0);

		FXMLLoader mainFrameLoader = new FXMLLoader(getClass().getResource("/encoderui/Encoder.fxml"));
		Parent mainFrame = mainFrameLoader.load();
		EncoderController encodercontroller = mainFrameLoader.getController();

		encodercontroller.Profileicn.setStyle("-fx-background-color: #5d76dc; -fx-border-radius: 50; -fx-background-radius: 25;");

		encodercontroller.Dashboard.setStyle("-fx-border-radius: 25 0 0 25;");
		encodercontroller.Dashboard.setBackground(new Background(ube));
		encodercontroller.Dashboard.setTextFill(Color.WHITE);
		encodercontroller.StudentProf.setStyle("-fx-border-radius: 25 0 0 25;");
		encodercontroller.StudentProf.setBackground(new Background(ube));
		encodercontroller.StudentProf.setTextFill(Color.WHITE);
//	        Timetable.setStyle("-fx-border-radius: 25 0 0 25;");
//	        Timetable.setBackground(new Background(ube));
//	        Timetable.setTextFill(Color.WHITE);
//	        Grading.setStyle("-fx-border-radius: 25 0 0 25;");
//	        Grading.setBackground(new Background(ube));
//	        Grading.setTextFill(Color.WHITE);
		encodercontroller.Enrollment.setStyle("-fx-border-radius: 25 0 0 25;");
		encodercontroller.Enrollment.setBackground(new Background(ube));
		encodercontroller.Enrollment.setTextFill(Color.WHITE);
		encodercontroller.oldEnrollment.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		encodercontroller.oldEnrollment.setTextFill(Color.BLACK);
		encodercontroller.Students.setStyle("-fx-border-radius: 25 0 0 25;");
		encodercontroller.Students.setBackground(new Background(ube));
		encodercontroller.Students.setTextFill(Color.WHITE);

		encodercontroller.setContent(timetable);

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
