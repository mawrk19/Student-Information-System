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
import javafx.geometry.Rectangle2D;
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

import com.mysql.cj.jdbc.Blob;

import application.DatabaseManager;
import application.UserSession;

public class OldEnrollmentController implements Initializable {

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

	String searchedCode = SearchBarSingleton.getInstance().getSearchbarText();

	Students student = setCredentials();
	InputStream imageStream;
	
	private Connection connection;
	private Statement statement;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeDatabase();
		loadComboBoxValues();
		setupComboBoxListeners();
		
		try {
			imageStream = getUserImageFromDatabase();
			if (imageStream != null) {
				// Load the user image from the database
				Image userImage = new Image(imageStream);
				imageView.setImage(userImage);
				centerImage(imageView);
			} else {
				System.out.println("Input stream is null. Skipping image loading.");
			}
		} catch (SQLException e) {
			// Handle SQLException
			e.printStackTrace();
		}

		if (student != null) {
			fNameTF.setText(student.getFirstName());
			mNameTF.setText(student.getMiddleName());
			lNameTF.setText(student.getLastName());
			dateTF.setText(student.getDate());
			genderCMB.setValue(student.getGender());
			courseCMB.setValue(student.getCourse());
			yrCMB.setValue(student.getYear());
			secCMB.setValue(student.getSection());
			semCMB.setValue(student.getSem());
			locCMB.setValue(student.getLocation());

			// gawa ka method para sa image
		}
		
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

	private InputStream getUserImageFromDatabase() throws SQLException {

		Connection con = DatabaseManager.getConnection();
		try (PreparedStatement stmt = con.prepareStatement("SELECT image FROM student WHERE scode = ?")) {
			stmt.setString(1, SearchBarSingleton.getInstance().getSearchbarText());
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getBinaryStream("image");
			}
		}
		return null; // Return null if no image is found
	}

	private void centerImage(ImageView imageView) {
		double imageViewWidth = imageView.getFitWidth();
		double imageViewHeight = imageView.getFitHeight();

		Image image = imageView.getImage();

		if (image != null) {
			double imageWidth = image.getWidth();
			double imageHeight = image.getHeight();

			double offsetX = 0;
			double offsetY = 0;

			if (imageWidth > imageViewWidth) {
				offsetX = (imageWidth - imageViewWidth) / 2;
			} else {
				offsetX = (imageViewWidth - imageWidth) / 2;
			}

			if (imageHeight > imageViewHeight) {
				offsetY = (imageHeight - imageViewHeight) / 2;
			} else {
				offsetY = (imageViewHeight - imageHeight) / 2;
			}

			imageView.setViewport(new Rectangle2D(offsetX, offsetY, imageViewWidth, imageViewHeight));
		}
	}

	@FXML
	private void enrollButtonClickedAction(ActionEvent event) throws SQLException, IOException {
		String studCode = enrollButtonClicked(createStudentsObject());
		System.out.println("Enrolled student with studCode: " + studCode);
		// setTransaction();
	}

	private void replaceTableViewContent() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/enrollment/OldTransaction.fxml"));
			AnchorPane newTableAnchorPane = loader.load();

			StackPane tableViewParent = (StackPane) subjectsTableView.getParent();
			Double topAnchor = AnchorPane.getTopAnchor(subjectsTableView);
			Double bottomAnchor = AnchorPane.getBottomAnchor(subjectsTableView);
			Double leftAnchor = AnchorPane.getLeftAnchor(subjectsTableView);
			Double rightAnchor = AnchorPane.getRightAnchor(subjectsTableView);

			tableViewParent.getChildren().remove(subjectsTableView);

			AnchorPane.setTopAnchor(newTableAnchorPane, topAnchor);
			AnchorPane.setBottomAnchor(newTableAnchorPane, bottomAnchor);
			AnchorPane.setLeftAnchor(newTableAnchorPane, leftAnchor);
			AnchorPane.setRightAnchor(newTableAnchorPane, rightAnchor);

			tableViewParent.getChildren().add(newTableAnchorPane);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public void newEnrollment() throws IOException {
//		Pane enrollmentstage = FXMLLoader.load(getClass().getResource("/enrollment/Enrollment.fxml"));
//		contentarea.getChildren().removeAll();
//		contentarea.getChildren().setAll(enrollmentstage);
//		AnchorPane.setLeftAnchor(enrollmentstage, 10.0);
//		AnchorPane.setRightAnchor(enrollmentstage, 10.0);
//		AnchorPane.setTopAnchor(enrollmentstage, 10.0);
//		AnchorPane.setBottomAnchor(enrollmentstage, 20.0);
//	}

	public void setSubjectsBasedOnSelection() {
		String selectedCourse = courseCMB.getValue();
		String selectedYear = yrCMB.getValue();
		String selectedSection = secCMB.getValue();
		String selectedSemester = semCMB.getValue();

		int startId;
		int endId;

		if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 10;
			endId = 17;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 10;
			endId = 17;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 18;
			endId = 25;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 18;
			endId = 25;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 18;
			endId = 25;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 18;
			endId = 25;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 89;
			endId = 96;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 89;
			endId = 96;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 89;
			endId = 96;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 89;
			endId = 96;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 60;
			endId = 66;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 60;
			endId = 66;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 60;
			endId = 66;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 60;
			endId = 66;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 10;
			endId = 17;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 10;
			endId = 17;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 26;
			endId = 32;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 26;
			endId = 32;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 26;
			endId = 32;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 26;
			endId = 32;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 97;
			endId = 104;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 97;
			endId = 104;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 97;
			endId = 104;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 97;
			endId = 104;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 67;
			endId = 74;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 67;
			endId = 74;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 67;
			endId = 74;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 67;
			endId = 74;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 48;
			endId = 54;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 48;
			endId = 54;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 48;
			endId = 54;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 48;
			endId = 54;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 33;
			endId = 40;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 33;
			endId = 40;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 33;
			endId = 40;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 33;
			endId = 40;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 105;
			endId = 113;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 105;
			endId = 113;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 105;
			endId = 113;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 105;
			endId = 113;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 75;
			endId = 82;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 75;
			endId = 82;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 75;
			endId = 82;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 75;
			endId = 82;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 55;
			endId = 59;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 55;
			endId = 59;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 55;
			endId = 59;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 55;
			endId = 59;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 41;
			endId = 47;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 41;
			endId = 47;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 41;
			endId = 47;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 41;
			endId = 47;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 114;
			endId = 9;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 114;
			endId = 120;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 114;
			endId = 120;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 114;
			endId = 120;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 83;
			endId = 88;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 83;
			endId = 88;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 83;
			endId = 88;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 83;
			endId = 88;
		} else {
			subjectsTableView.setVisible(false);
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
				&& "1st".equals(selectedSemester)) {
			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 10;
			endId = 17;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 10;
			endId = 17;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 18;
			endId = 25;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 18;
			endId = 25;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 18;
			endId = 25;
		} else if ("BSIT".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 18;
			endId = 25;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 89;
			endId = 96;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 89;
			endId = 96;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 89;
			endId = 96;
		} else if ("BSIS".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 89;
			endId = 96;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 60;
			endId = 66;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 60;
			endId = 66;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 60;
			endId = 66;
		} else if ("BSEMC".equals(selectedCourse) && "1st".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 60;
			endId = 66;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 10;
			endId = 17;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 1;
			endId = 9;
		} else if ("BSCS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 10;
			endId = 17;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 26;
			endId = 32;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 26;
			endId = 32;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 26;
			endId = 32;
		} else if ("BSIT".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 26;
			endId = 32;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 97;
			endId = 104;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 97;
			endId = 104;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 97;
			endId = 104;
		} else if ("BSIS".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 97;
			endId = 104;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 67;
			endId = 74;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 67;
			endId = 74;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 67;
			endId = 74;
		} else if ("BSEMC".equals(selectedCourse) && "2nd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 67;
			endId = 74;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 48;
			endId = 54;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 48;
			endId = 54;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 48;
			endId = 54;
		} else if ("BSCS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 48;
			endId = 54;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 33;
			endId = 40;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 33;
			endId = 40;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 33;
			endId = 40;
		} else if ("BSIT".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 33;
			endId = 40;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 105;
			endId = 113;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 105;
			endId = 113;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 105;
			endId = 113;
		} else if ("BSIS".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 105;
			endId = 113;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 75;
			endId = 82;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 75;
			endId = 82;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 75;
			endId = 82;
		} else if ("BSEMC".equals(selectedCourse) && "3rd".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 75;
			endId = 82;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 55;
			endId = 59;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 55;
			endId = 59;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 55;
			endId = 59;
		} else if ("BSCS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 55;
			endId = 59;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 41;
			endId = 47;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 41;
			endId = 47;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 41;
			endId = 47;
		} else if ("BSIT".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 41;
			endId = 47;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 114;
			endId = 9;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 114;
			endId = 120;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 114;
			endId = 120;
		} else if ("BSIS".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 114;
			endId = 120;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 83;
			endId = 88;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "A".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 83;
			endId = 88;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "1st".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 83;
			endId = 88;
		} else if ("BSEMC".equals(selectedCourse) && "4th".equals(selectedYear) && "B".equals(selectedSection)
				&& "2nd".equals(selectedSemester)) {
			subjectsTableView.setVisible(true);

			startId = 83;
			endId = 88;
		}

		return new Students(selectedYear, selectedYear, selectedSection, selectedYear, selectedYear, selectedYear,
				selectedYear, selectedYear, endId, selectedYear, endId, selectedYear, null, startId, endId,
				selectedYear);
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

	private String enrollButtonClicked(Students student) throws SQLException, IOException {
	    String searchedCode = SearchBarSingleton.getInstance().getSearchbarText();
	    System.out.println("old enroll click " + searchedCode);
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

	    // add mo yung sa images
	    UserSession session = UserSession.getInstance();
	    String username = session.getUsername();

	    if (isFormFilled()) {
	        // Form is filled, proceed with replacing content and database operations
	        replaceTableViewContent();

	        try (Connection con = DatabaseManager.getConnection()) {
	            // SELECT query to retrieve ID
	            String selectIdSql = "SELECT id FROM transaction WHERE scode = ?";

	            // Update existing student record
	            String updateSql = "UPDATE student SET course=?, date=?, First_name=?, gender=?, location=?, "
	                    + "last_name=?, Middle_name=?, section=?, sy=?, year=?, sem=?, status=?, eSubjectsStart=?, "
	                    + "eSubjectsEnd=?, encoder=? WHERE scode=?";
	            try (PreparedStatement updateStatement = con.prepareStatement(updateSql)) {
	                updateStatement.setString(1, selectedCourse);
	                updateStatement.setString(2, enrollmentDate);
	                updateStatement.setString(3, firstName);
	                updateStatement.setString(4, gender);
	                updateStatement.setString(5, location);
	                updateStatement.setString(6, lastName);
	                updateStatement.setString(7, middleName);
	                updateStatement.setString(8, section);
	                updateStatement.setString(9, sy1);
	                updateStatement.setString(10, year1);
	                updateStatement.setString(11, sem);
	                updateStatement.setString(12, status);
	                updateStatement.setString(13, startID);
	                updateStatement.setString(14, endID);
	                updateStatement.setString(15, username);
	                updateStatement.setString(16, searchedCode);

	                int rowsAffected = updateStatement.executeUpdate();

	                if (rowsAffected > 0) {
	                    System.out.println("Student record updated successfully.");

	                    // Update scode in the student table
	                    String updateScodeSql = "UPDATE student SET scode = ? WHERE scode = ?";
	                    try (PreparedStatement updateScodeStatement = con.prepareStatement(updateScodeSql)) {
	                        updateScodeStatement.setString(1, searchedCode);
	                        updateScodeStatement.setString(2, searchedCode);

	                        int scodeRowsAffected = updateScodeStatement.executeUpdate();

	                        if (scodeRowsAffected > 0) {
	                            System.out.println("Scode updated successfully.");

	                            // Insert studCode into the transaction table
	                            String transactionSql = "INSERT INTO transaction (scode) VALUES (?)";
	                            try (PreparedStatement transactionStatement = con.prepareStatement(transactionSql)) {
	                                transactionStatement.setString(1, searchedCode);

	                                System.out.println("The Queens: " + searchedCode);

	                                int transactionRowsAffected = transactionStatement.executeUpdate();

	                                if (transactionRowsAffected > 0) {
	                                    System.out.println("Transaction record inserted successfully.");

	                                    // Retrieve id from transaction
	                                    try (PreparedStatement selectIdStatement = con.prepareStatement(selectIdSql)) {
	                                        selectIdStatement.setString(1, searchedCode);
	                                        ResultSet resultSet = selectIdStatement.executeQuery();

	                                        GeneratedIdSingleton idSingleton = GeneratedIdSingleton.getInstance();

	                                        if (resultSet.next()) {
	                                            int transactionId = resultSet.getInt("id");
	                                            idSingleton.setGeneratedId(transactionId);
	                                            System.out.println("printed transact ID: " + transactionId);
	                                        } else {
	                                            System.out.println("Failed to retrieve transaction record.");
	                                        }
	                                    }
	                                } else {
	                                    System.out.println("Failed to update scode.");
	                                }
	                            }
	                        } else {
	                            System.out.println("No rows affected. Update failed.");
	                        }
	                    }
	                } else {
	                    System.out.println("Transaction ID not found for scode: " + searchedCode);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    } else {
	        // Form is not filled, display an error message or take appropriate action
	        System.out.println("Form is not filled. Please fill in all required fields.");
	    }
	    return searchedCode;
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

//	@FXML
//	private void insertIMG() {
//		FileChooser fileChooser = new FileChooser();
//		fileChooser.getExtensionFilters()
//				.add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
//		File selectedFile = fileChooser.showOpenDialog(null);
//
//		if (selectedFile != null) {
//			Image newImage = new Image(selectedFile.toURI().toString());
//			imageView.setImage(newImage);
//			image = newImage;
//		}
//	}

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

	private Students setCredentials() {
		Students student = null;
		String searchedCode = SearchBarSingleton.getInstance().getSearchbarText();

		System.out.println("show searched code: " + searchedCode);

		try (Connection con = DatabaseManager.getConnection()) {
			String sql = "SELECT * FROM student WHERE scode = ?";

			try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
				preparedStatement.setString(1, searchedCode);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						// Retrieve values from the database
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
						String sy = resultSet.getString("sy");
						String sem = resultSet.getString("sem");
						int start = resultSet.getInt("eSubjectsStart");
						int end = resultSet.getInt("eSubjectsEnd");
						Blob image = (Blob) resultSet.getBlob("image");
						// Create a new Students object with retrieved values
						student = new Students(firstName, middleName, lastName, course1, year1, sy, section1, location1,
								scode1, date1, sid1, gender1, image, start, end, sem);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return student;
	}

}
