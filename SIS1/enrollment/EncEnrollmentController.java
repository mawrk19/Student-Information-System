package enrollment;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import students.Students;
import javafx.scene.layout.StackPane;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import application.DatabaseManager;
import application.UserSession;

public class EncEnrollmentController implements Initializable {

	@FXML
	private ComboBox<String> courseCMB, genderCMB, locCMB, secCMB, yrCMB, semCMB;

	@FXML
	private TextField dateTF, fNameTF, lNameTF, mNameTF, sidTF;

	@FXML
	private Button enrollBTN;

	@FXML
	private ImageView insertIMG;

	@FXML
	private TableView<Subject> subjectsTableView;

	@FXML
	private TableColumn<Subject, Integer> idColumn;

	@FXML
	private TableColumn<Subject, String> subCodeColumn;

	@FXML
	private TableColumn<Subject, Integer> unitsColumn;

	@FXML
	private TableColumn<Subject, String> subjectColumn;

	@FXML
	private AnchorPane newContentAnchorPane;

	@FXML
	private ImageView imageView;

	@FXML
	AnchorPane contentarea;

	private ObservableList<Subject> subjectsList = FXCollections.observableArrayList();

	private Image image;

	private String studCode;

	private Connection connection;
	private Statement statement;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeDatabase();
		loadComboBoxValues();
		setupComboBoxListeners();
		
		
		fNameTF.addEventFilter(KeyEvent.KEY_TYPED, event -> {
		    if (!event.getCharacter().matches("[a-zA-Z\\s]")) {
		        event.consume();
		    }
		});

		lNameTF.addEventFilter(KeyEvent.KEY_TYPED, event -> {
		    if (!event.getCharacter().matches("[a-zA-Z\\s]")) {
		        event.consume();
		    }
		});
		
		mNameTF.addEventFilter(KeyEvent.KEY_TYPED, event -> {
		    if (!event.getCharacter().matches("[a-zA-Z\\s]")) {
		        event.consume();
		    }
		});

		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			dateTF.setText(LocalDateTime.now().format(formatter));
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();

		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		subCodeColumn.setCellValueFactory(new PropertyValueFactory<>("subCode"));
		unitsColumn.setCellValueFactory(new PropertyValueFactory<>("units"));
		subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));

		courseCMB.setOnAction(event -> setSubjectsBasedOnSelection());
		yrCMB.setOnAction(event -> setSubjectsBasedOnSelection());
		secCMB.setOnAction(event -> setSubjectsBasedOnSelection());
		semCMB.setOnAction(event -> setSubjectsBasedOnSelection());
		imageView.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				// Handle primary (left) mouse click
				insertIMG();
			}
		});
		sidTF.setEditable(false);
		sidTF.setDisable(true);
	}

	private void initializeDatabase() {
		try {
			Connection con = DatabaseManager.getConnection();
			statement = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadComboBoxValues() {
	    loadComboBox("courses", courseCMB);
	    loadComboBox("genders", genderCMB);
	    loadComboBox("locations", locCMB);
	    loadComboBox("sections", secCMB);
	    loadComboBox("semesters", semCMB);
	    loadComboBox("years", yrCMB);    
	}

	private void loadComboBox(String columnName, ComboBox<String> comboBox) {
	    try {
	        ResultSet resultSet = statement.executeQuery("SELECT DISTINCT " + columnName + " FROM school");
	        ObservableList<String> items = FXCollections.observableArrayList();
	        while (resultSet.next()) {
	            items.add(resultSet.getString(columnName));
	        }
	        comboBox.setItems(items);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private void setupComboBoxListeners() {
	}

	private void insertValue(String columnName, String value) {
	    try {
	        statement.executeUpdate("INSERT INTO school (" + columnName + ") VALUES ('" + value + "')");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@FXML
	private void enrollButtonClickedAction(ActionEvent event) throws SQLException, IOException {
		String studCode = enrollButtonClicked(convertImageToInputStream(image), createStudentsObject());
		System.out.println("Enrolled student with studCode: " + studCode);
		// setTransaction();
	}


	private void replaceTableViewContent() {
	    try {
	        // Load Transaction.fxml
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/enrollment/EncTransaction.fxml"));
	        AnchorPane newTableAnchorPane = loader.load();

	        // Assuming subjectsTableView is defined somewhere in your code.
	        // Example:
	        // TableView<?> subjectsTableView = new TableView<>();

	        // Get the parent of subjectsTableView
	        Pane tableViewParent = (Pane) subjectsTableView.getParent();

	        // Get the anchor constraints of subjectsTableView
	        Double topAnchor = AnchorPane.getTopAnchor(subjectsTableView);
	        Double bottomAnchor = AnchorPane.getBottomAnchor(subjectsTableView);
	        Double leftAnchor = AnchorPane.getLeftAnchor(subjectsTableView);
	        Double rightAnchor = AnchorPane.getRightAnchor(subjectsTableView);

	        // Remove subjectsTableView from its parent
	        tableViewParent.getChildren().remove(subjectsTableView);

	        // Set anchor constraints for newTableAnchorPane
	        AnchorPane.setTopAnchor(newTableAnchorPane, topAnchor);
	        AnchorPane.setBottomAnchor(newTableAnchorPane, bottomAnchor);
	        AnchorPane.setLeftAnchor(newTableAnchorPane, leftAnchor);
	        AnchorPane.setRightAnchor(newTableAnchorPane, rightAnchor);

	        // Add newTableAnchorPane to the parent
	        tableViewParent.getChildren().add(newTableAnchorPane);

	        EncTransactionController transactionController = loader.getController();
	        transactionController.setStudCode(this.studCode);

	        System.out.println("Stud code from enrollment: " + studCode);

	        // If needed, re-add subjectsTableView to the parent
	        // tableViewParent.getChildren().add(subjectsTableView);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void newEnrollment() throws IOException {
		Pane enrollmentstage = FXMLLoader.load(getClass().getResource("/enrollment/EncEnrollment.fxml"));
		contentarea.getChildren().removeAll();
		contentarea.getChildren().setAll(enrollmentstage);
		AnchorPane.setLeftAnchor(enrollmentstage, 10.0);
		AnchorPane.setRightAnchor(enrollmentstage, 10.0);
		AnchorPane.setTopAnchor(enrollmentstage, 10.0);
		AnchorPane.setBottomAnchor(enrollmentstage, 20.0);
	}

	public void setSubjectsBasedOnSelection() {
		String selectedCourse = courseCMB.getValue();
		String selectedYear = yrCMB.getValue();
		String selectedSection = secCMB.getValue();
		String selectedSemester = semCMB.getValue();

		int startId;
		int endId;

		if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);

			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);

			startId = 10;
			endId = 17;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);

			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 10;
			endId = 17;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 18;
			endId = 25;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 18;
			endId = 25;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 18;
			endId = 25;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 18;
			endId = 25;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 89;
			endId = 96;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 89;
			endId = 96;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 89;
			endId = 96;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 89;
			endId = 96;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 60;
			endId = 66;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 60;
			endId = 66;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 60;
			endId = 66;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 60;
			endId = 66;

		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);

			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);

			startId = 10;
			endId = 17;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);

			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);

			startId = 10;
			endId = 17;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 26;
			endId = 32;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 26;
			endId = 32;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 26;
			endId = 32;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 26;
			endId = 32;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 97;
			endId = 104;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 97;
			endId = 104;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 97;
			endId = 104;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 97;
			endId = 104;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 67;
			endId = 74;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 67;
			endId = 74;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 67;
			endId = 74;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 67;
			endId = 74;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 48;
			endId = 54;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 48;
			endId = 54;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 48;
			endId = 54;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 48;
			endId = 54;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 33;
			endId = 40;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 33;
			endId = 40;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 33;
			endId = 40;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 33;
			endId = 40;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 105;
			endId = 113;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 105;
			endId = 113;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 105;
			endId = 113;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 105;
			endId = 113;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 75;
			endId = 82;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 75;
			endId = 82;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 75;
			endId = 82;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 75;
			endId = 82;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 55;
			endId = 59;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 55;
			endId = 59;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 55;
			endId = 59;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 55;
			endId = 59;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 41;
			endId = 47;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 41;
			endId = 47;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 41;
			endId = 47;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 41;
			endId = 47;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 114;
			endId = 9;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 114;
			endId = 120;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 114;
			endId = 120;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 114;
			endId = 120;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 83;
			endId = 88;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 83;
			endId = 88;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 83;
			endId = 88;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 83;
			endId = 88;
		} else {
			subjectsTableView.setVisible(false);
//	        irregularTableView.setVisible(true);
			clearSubjectsTable();
			return;
		}

		// Set subjects for the specified conditions
		setSubjectsForSemester(createStudentsObject());
	}
	

	private Students createStudentsObject() {
		String selectedCourse = courseCMB.getValue();
		String selectedYear = yrCMB.getValue();
		String selectedSection = secCMB.getValue();
		String selectedSemester = semCMB.getValue();

		int startId = 1; // Default values
		int endId = 9; // Default values

		if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 10;
			endId = 17;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 10;
			endId = 17;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 18;
			endId = 25;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 18;
			endId = 25;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 18;
			endId = 25;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 18;
			endId = 25;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 89;
			endId = 96;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 89;
			endId = 96;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 89;
			endId = 96;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 89;
			endId = 96;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 60;
			endId = 66;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 60;
			endId = 66;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 60;
			endId = 66;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 60;
			endId = 66;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 10;
			endId = 17;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 10;
			endId = 17;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 26;
			endId = 32;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 26;
			endId = 32;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 26;
			endId = 32;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 26;
			endId = 32;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 97;
			endId = 104;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 97;
			endId = 104;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 97;
			endId = 104;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 97;
			endId = 104;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 67;
			endId = 74;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 67;
			endId = 74;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 67;
			endId = 74;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 67;
			endId = 74;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 48;
			endId = 54;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 48;
			endId = 54;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 48;
			endId = 54;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 48;
			endId = 54;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 33;
			endId = 40;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 33;
			endId = 40;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 33;
			endId = 40;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 33;
			endId = 40;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 105;
			endId = 113;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 105;
			endId = 113;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 105;
			endId = 113;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 105;
			endId = 113;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 75;
			endId = 82;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 75;
			endId = 82;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 75;
			endId = 82;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 75;
			endId = 82;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 55;
			endId = 59;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 55;
			endId = 59;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 55;
			endId = 59;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 55;
			endId = 59;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 41;
			endId = 47;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 41;
			endId = 47;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 41;
			endId = 47;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 41;
			endId = 47;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 114;
			endId = 9;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 114;
			endId = 120;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 114;
			endId = 120;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 114;
			endId = 120;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 83;
			endId = 88;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 83;
			endId = 88;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 83;
			endId = 88;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester) ) {
			subjectsTableView.setVisible(true);
	        
			startId = 83;
			endId = 88;
		}

		return new Students(selectedYear, selectedYear, selectedSection, selectedYear, selectedYear, selectedYear,
				selectedYear, selectedYear, endId, selectedYear, endId, selectedYear, null, startId, endId, selectedYear);
	}

	private void clearSubjectsTable() {
		subjectsTableView.getItems().clear();
	}

	private void setSubjectsForSemester(Students student) {
		int startId = student.getStart();
		int endId = student.getEnd();

		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM subjects WHERE id between ? and ?")) {

			preparedStatement.setInt(1, startId);
			preparedStatement.setInt(2, endId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				clearSubjectsTable(); // Clear existing items before adding new ones
				while (resultSet.next()) {
					int id = resultSet.getInt("id");
					String subCode = resultSet.getString("sub_code");
					int units = resultSet.getInt("units");
					String subject = resultSet.getString("subject");

					Subject subjectObj = new Subject(id, subCode, units, subject);
					subjectsList.add(subjectObj);
				}
				subjectsTableView.setItems(subjectsList);
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception as needed
		}
	}

	private InputStream convertImageToInputStream(Image image) throws IOException {
		if (image != null && image.getPixelReader() != null) {
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} else {
			InputStream defaultImageStream = getClass().getResourceAsStream("/img_icons/NoPeep.png");
			return defaultImageStream;
		}
	}

	private String enrollButtonClicked(InputStream imageStream, Students student) throws SQLException, IOException {
		String selectedCourse = courseCMB.getValue();
		String enrollmentDate = dateTF.getText();
		String firstName = fNameTF.getText();
		String gender = genderCMB.getValue();
		String location = locCMB.getValue();
		String lastName = lNameTF.getText();
		String middleName = mNameTF.getText();
		String section = secCMB.getValue();
		String year1 = yrCMB.getValue();
		String sy = "2023";
		String sy1 = "2023-2024";
		String startID = String.valueOf(student.getStart());
		String endID = String.valueOf(student.getEnd());
		String sem = semCMB.getValue();
		String status = "enrolled";
		String studCode = null;

		UserSession session = UserSession.getInstance();
		String username = session.getUsername();

		if (isFormFilled()) {
			// Form is filled, proceed with replacing content and database operations
			replaceTableViewContent();

			try (Connection con = DatabaseManager.getConnection()) {
				String sql;

				if (image != null) {
					sql = "INSERT INTO student (course, date, First_name, gender, location, last_name, Middle_name, section, sy, year, sem, status, eSubjectsStart, eSubjectsEnd, image, encoder) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				} else {
					sql = "INSERT INTO student (course, date, First_name, gender, location, last_name, Middle_name, section, sy, year, sem, status, eSubjectsStart, eSubjectsEnd, encoder) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				}

				try (PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					preparedStatement.setString(1, selectedCourse);
					preparedStatement.setString(2, enrollmentDate);
					preparedStatement.setString(3, firstName);
					preparedStatement.setString(4, gender);
					preparedStatement.setString(5, location);
					preparedStatement.setString(6, lastName);
					preparedStatement.setString(7, middleName);
					preparedStatement.setString(8, section);
					preparedStatement.setString(9, sy1);
					preparedStatement.setString(10, year1);
					preparedStatement.setString(11, sem);
					preparedStatement.setString(12, status);

					if (image != null) {
						preparedStatement.setString(13, startID);
						preparedStatement.setString(14, endID);
						preparedStatement.setBlob(15, imageStream);
						preparedStatement.setString(16, username);
					} else {
						preparedStatement.setString(13, startID);
						preparedStatement.setString(14, endID);
						preparedStatement.setString(15, username);
					}

					int rowsAffected = preparedStatement.executeUpdate();

					if (rowsAffected > 0) {
						try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
							if (generatedKeys.next()) {
								int generatedId = generatedKeys.getInt(1);
								System.out.println("Student with ID " + generatedId + " inserted successfully.");

								String formattedId = String.format("%04d", generatedId);
								studCode = sy + formattedId; // Set studCode here

								System.out.println(studCode + " vs. alfredy");

								EncTransactionController transacCon = EncTransactionController.getInstance();
								transacCon.setStudCode(studCode);

								String sql1 = "UPDATE student SET scode = ? WHERE sid = ?";
								try (PreparedStatement scodeStatement = con.prepareStatement(sql1)) {
									scodeStatement.setString(1, studCode);
									scodeStatement.setInt(2, generatedId);
									int scodeRowsAffected = scodeStatement.executeUpdate();

									if (scodeRowsAffected > 0) {
										System.out.println("Scode inserted successfully.");

										// Insert studCode into the transaction table
										String transactionSql = "INSERT INTO transaction (scode) VALUES (?)";
										try (PreparedStatement transactionStatement = con
												.prepareStatement(transactionSql)) {
											transactionStatement.setString(1, studCode);

											int transactionRowsAffected = transactionStatement.executeUpdate();

											if (transactionRowsAffected > 0) {
												System.out.println("Transaction record inserted successfully.");
											} else {
												System.out.println("Failed to insert transaction record.");
											}
										}
									} else {
										System.out.println("Failed to insert scode.");
									}
								}
							}
						}
					} else {
						System.out.println("No rows affected. Insertion failed.");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			// Form is not filled, display an error message or take appropriate action
			System.out.println("Form is not filled. Please fill in all required fields.");

		}
		return studCode;
	}

	public void clearFields() {
		courseCMB.setValue(null);
		dateTF.clear();
		fNameTF.clear();
		genderCMB.setValue(null);
		locCMB.setValue(null);
		lNameTF.clear();
		mNameTF.clear();
		secCMB.setValue(null);
	}

	@FXML
	private void insertIMG() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			Image newImage = new Image(selectedFile.toURI().toString());
			imageView.setImage(newImage);
			image = newImage;
		}
	}

	public void setStudCode(String studCode) {
		this.studCode = studCode;
	}

	public String getStudCode() {
		return this.studCode;
	}

	private boolean isFormFilled() {
		if (courseCMB.getValue() == null || dateTF.getText().isEmpty() || fNameTF.getText().isEmpty()
				|| genderCMB.getValue() == null || locCMB.getValue() == null || lNameTF.getText().isEmpty()
				|| mNameTF.getText().isEmpty() || secCMB.getValue() == null || semCMB.getValue() == null
				|| yrCMB.getValue() == null) {

			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Incomplete Form");
			alert.setHeaderText(null);
			alert.setContentText("Please fill in all the required fields.");
			alert.showAndWait();

			return false;
		}

		return true;
	}
	
}
