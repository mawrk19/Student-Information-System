package schedule;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.Dialog;
import java.awt.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.*;

import application.DatabaseManager;
import application.MainFrameController;
import enrollment.Subject;




public class ScheduleController implements Initializable{

		
		ZonedDateTime dateFocus;
	    ZonedDateTime today;

	    
	    @FXML
	    private ComboBox<String> courseCMB;
	    
	    @FXML
	    private ComboBox<String> yearCMB;
	    
	    @FXML
	    private ComboBox<String> sectionCMB;
	    
	    @FXML
	    private ComboBox<String> semesterCMB;
	    
	    @FXML
	    private Text month;
	    
	    @FXML
	    private Text year;
	    
	    @FXML
	    private Button forward;
	    
	    @FXML
	    private Button prev;
	    
//	    @FXML
//	    private Button BtnTimetable;
	    
	    @FXML
	    private FlowPane calendar;
		   
	    private boolean dataLoaded = false;
	    
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			dateFocus = ZonedDateTime.now();
	        today = ZonedDateTime.now();
	        
	        drawCalendar();
		
	        ObservableList<String> course = FXCollections.observableArrayList("BSCS", "BSIT", "BSIS", "BSEMC");
	        courseCMB.setItems(course);
	        
	        ObservableList<String> year = FXCollections.observableArrayList("1st YEAR", "2nd YEAR", "3rd YEAR", "4th YEAR");
	        yearCMB.setItems(year);
	        
	        ObservableList<String> section = FXCollections.observableArrayList("A", "B");
	        sectionCMB.setItems(section);
	        
		    ObservableList<String> semester = FXCollections.observableArrayList("1st SEMESTER", "2nd SEMESTER");
	        semesterCMB.setItems(semester);
	            
	        courseCMB.setOnAction(event -> setSubjectsBasedOnSelection());
	        yearCMB.setOnAction(event -> setSubjectsBasedOnSelection());
	        sectionCMB.setOnAction(event -> setSubjectsBasedOnSelection());
	        semesterCMB.setOnAction(event -> setSubjectsBasedOnSelection());
	        
			subcodeCLMNColumn.setCellValueFactory(new PropertyValueFactory<>("subcodeCLMN"));
			credunitCLMNColumn.setCellValueFactory(new PropertyValueFactory<>("credunitCLMN"));
		    descriptionCLMNColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionCLMN"));
			sdidCLMNColumn.setCellValueFactory(new PropertyValueFactory<>("sdidCLMN"));
			
		
			submitBTN.setOnAction(ActionEvent -> handleShowDataButtonClick());
		

}
		
		 @FXML
		    void backOneMonth(ActionEvent event) {
		        dateFocus = dateFocus.minusMonths(1);
		        calendar.getChildren().clear();
		        drawCalendar();
		    }

		    @FXML
		    void forwardOneMonth(ActionEvent event) {
		        dateFocus = dateFocus.plusMonths(1);
		        calendar.getChildren().clear();
		        drawCalendar();
		    }
		
		private void drawCalendar(){
	        year.setText(String.valueOf(dateFocus.getYear()));
	        month.setText(String.valueOf(dateFocus.getMonth()));

	        double calendarWidth = calendar.getPrefWidth();
	        double calendarHeight = calendar.getPrefHeight();
	        double strokeWidth = 1;
	        double spacingH = calendar.getHgap();
	        double spacingV = calendar.getVgap();

	        int monthMaxDate = dateFocus.getMonth().maxLength();
	        //Check for leap year
	        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
	            monthMaxDate = 28;
	        }
	        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();

	        for (int i = 0; i < 6; i++) {
	            for (int j = 0; j < 7; j++) {
	                StackPane stackPane = new StackPane();

	                Rectangle rectangle = new Rectangle();
	                rectangle.setFill(Color.TRANSPARENT);
	                
	                double rectangleWidth =(calendarWidth/7) - strokeWidth - spacingH;
	                rectangle.setWidth(rectangleWidth);
	                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
	                double cornerRadius = 10;
	                rectangle.setArcWidth(cornerRadius);
	                rectangle.setArcHeight(cornerRadius);
	                rectangle.setHeight(rectangleHeight);
	                stackPane.getChildren().add(rectangle);

	                int calculatedDate = (j+1)+(7*i);
	                if(calculatedDate > dateOffset){
	                    int currentDate = calculatedDate - dateOffset;
	                    if(currentDate <= monthMaxDate){
	                        Text date = new Text(String.valueOf(currentDate));
	                        double textTranslationY = - (rectangleHeight / 10) * 0.50;
	                        date.setTranslateY(textTranslationY);
	                        stackPane.getChildren().add(date);
	                    }
	                    if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
	                    	Color ube = Color.web("#3c5199");
	                    	for (Node node : stackPane.getChildren()) {
	                            if (node instanceof Text) {
	                                Text textNode = (Text) node;
	                                textNode.setFill(Color.WHITE);
	                            }
	                        }
	                        rectangle.setStroke(ube);
	                        rectangle.setFill(ube);
	                    }
	                }
	                FlowPane.setMargin(stackPane, new Insets(0, 0, 0, 0));
	                calendar.getChildren().add(stackPane);
	            }
	        }
	    }
		
//		public void gotoTimetable(ActionEvent event) throws IOException {
//		    try {
//		    	
//		        FXMLLoader loader = new FXMLLoader(getClass().getResource("/timetable/Timetable.fxml"));
//		        Parent timetable = loader.load();
//		        
//		        AnchorPane.setLeftAnchor(timetable, 10.0);
//				AnchorPane.setRightAnchor(timetable, 10.0);
//				AnchorPane.setTopAnchor(timetable, 10.0);
//				AnchorPane.setBottomAnchor(timetable, 20.0);
//
//		        FXMLLoader mainFrameLoader = new FXMLLoader(getClass().getResource("/application/MainFrame.fxml"));
//		        Parent mainFrame = mainFrameLoader.load();
//		        MainFrameController mainFrameController = mainFrameLoader.getController();
//		        
//		        mainFrameController.Dashboard.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
//		    	mainFrameController.Dashboard.setTextFill(Color.WHITE);
//		    	mainFrameController.StudentProf.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
//		    	mainFrameController.StudentProf.setTextFill(Color.WHITE);
//		    	mainFrameController.Timetable.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
//		    	mainFrameController.Timetable.setTextFill(Color.BLACK);
//		    	mainFrameController.Schedule.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
//		    	mainFrameController.Schedule.setTextFill(Color.WHITE);
//		    	mainFrameController.Enrollment.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
//		    	mainFrameController.Enrollment.setTextFill(Color.WHITE);
//		    	mainFrameController.oldEnrollment.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
//		    	mainFrameController.oldEnrollment.setTextFill(Color.WHITE);
//		    	mainFrameController.Students.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
//		    	mainFrameController.Students.setTextFill(Color.WHITE);
//		        
//
//		        mainFrameController.setContent(timetable);
//
//		        Scene scene = new Scene(mainFrame);
//		        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//		        stage.setScene(scene);
//		        stage.show();
//		        
//		        double windowWidth = stage.getWidth();
//		        double windowHeight = stage.getHeight();
//
//		        stage.setWidth(windowWidth);
//		        stage.setHeight(windowHeight);
//		    } catch (IOException e) {
//		        e.printStackTrace();
//		    }
//		}
	    
		@FXML
		private Button addBTN;
			 
		@FXML
		private Button deleteBTN;
			 
	    @FXML
	    private Button updateBTN;
	
	    @FXML
	    private Button submitBTN;
	    
	    @FXML
		private TableColumn<Schedule, String> subcodeCLMNColumn;

		@FXML
		private TableColumn<Schedule, String> credunitCLMNColumn;

		@FXML
		private TableColumn<Schedule, String> descriptionCLMNColumn;

		@FXML
		private TableColumn<Schedule, Integer> sdidCLMNColumn;
		
		@FXML
		private TableView<Schedule> scheduleTV;
		
		private ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();

	    
		
		private void setSubjectsBasedOnSelection() {
		    dataLoaded = false; 

		    
		    scheduleList.clear();
		    scheduleTV.getItems().clear();
		}

		private void handleShowDataButtonClick() {
		   
		    if (!dataLoaded) {
		    String selectedCourse = courseCMB.getValue();
		    String selectedYear = yearCMB.getValue();
		    String selectedSection = sectionCMB.getValue();
		    String selectedSemester = semesterCMB.getValue();
		    
		    
		    

		    if ("BSCS".equals(selectedCourse) && "1st YEAR".equals(selectedYear) && "A".equals(selectedSection)
		            && "1st SEMESTER".equals(selectedSemester)) {
		        setSchedule("BSCS1A1stSEMESTER", 1, 9);
		    } else if ("BSCS".equals(selectedCourse) && "1st YEAR".equals(selectedYear) && "A".equals(selectedSection)
		            && "2nd SEMESTER".equals(selectedSemester)) {
		        setSchedule("BSCS1A2ndSEMESTER", 10, 17);
		        fetchDataForOptions(selectedCourse, selectedYear, selectedSemester, selectedSection);
		    }
		    dataLoaded = true;
		}
		}
		private void setSchedule(String semester, int startId, int endId) {
		    try (Connection connection = DatabaseManager.getConnection();
		         PreparedStatement preparedStatement = connection
		                 .prepareStatement("SELECT * FROM subjects WHERE id between ? and ?")) {

		        preparedStatement.setInt(1, startId);
		        preparedStatement.setInt(2, endId);

		        try (ResultSet resultSet = preparedStatement.executeQuery()) {
		            while (resultSet.next()) {
		                String subcode = resultSet.getString("sub_code");
		                String credunit = resultSet.getString("units");
		                String description = resultSet.getString("subject");
		                int sdid = resultSet.getInt("id");

		                Schedule scheduleObj = new Schedule(subcode, credunit, description, sdid);
		                scheduleList.add(scheduleObj);
		            }
		            scheduleTV.setItems(scheduleList);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}

		private ObservableList<String> fetchDataForOptions(String selectedOption1, String selectedOption2,
		        String selectedOption3, String selectedOption4) {
		     
		    return null;
		}
		
		

	}
