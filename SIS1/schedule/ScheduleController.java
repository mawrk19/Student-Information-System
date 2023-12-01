package schedule;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;

import application.MainFrameController;

public class ScheduleController implements Initializable{

		ZonedDateTime dateFocus;
	    ZonedDateTime today;
	    
	    @FXML
	    private Text month;
	    
	    @FXML
	    private Text year;
	    
	    @FXML
	    private Button forward;
	    
	    @FXML
	    private Button prev;
	    
	    @FXML
	    private Button BtnTimetable;
	    
	    @FXML
	    private FlowPane calendar;
		   
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			dateFocus = ZonedDateTime.now();
	        today = ZonedDateTime.now();
	        
	        drawCalendar();
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
		
		public void gotoTimetable(ActionEvent event) throws IOException {
		    try {
		    	
		        FXMLLoader loader = new FXMLLoader(getClass().getResource("/timetable/Timetable.fxml"));
		        Parent timetable = loader.load();
		        
		        AnchorPane.setLeftAnchor(timetable, 10.0);
				AnchorPane.setRightAnchor(timetable, 10.0);
				AnchorPane.setTopAnchor(timetable, 10.0);
				AnchorPane.setBottomAnchor(timetable, 20.0);

		        FXMLLoader mainFrameLoader = new FXMLLoader(getClass().getResource("/application/MainFrame.fxml"));
		        Parent mainFrame = mainFrameLoader.load();
		        MainFrameController mainFrameController = mainFrameLoader.getController();
		        
		        mainFrameController.Dashboard.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		    	mainFrameController.Dashboard.setTextFill(Color.WHITE);
		    	mainFrameController.StudentProf.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		    	mainFrameController.StudentProf.setTextFill(Color.WHITE);
		    	mainFrameController.Timetable.setStyle("-fx-background-color: #eff0f3; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		    	mainFrameController.Timetable.setTextFill(Color.BLACK);
		    	mainFrameController.Schedule.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		    	mainFrameController.Schedule.setTextFill(Color.WHITE);
		    	mainFrameController.Enrollment.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		    	mainFrameController.Enrollment.setTextFill(Color.WHITE);
		    	mainFrameController.oldEnrollment.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		    	mainFrameController.oldEnrollment.setTextFill(Color.WHITE);
		    	mainFrameController.Students.setStyle("-fx-background-color: #3c5199; -fx-border-radius: 25 0 0 25; -fx-background-radius: 25 0 0 25;");
		    	mainFrameController.Students.setTextFill(Color.WHITE);
		        

		        mainFrameController.setContent(timetable);

		        Scene scene = new Scene(mainFrame);
		        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		        stage.setScene(scene);
		        stage.show();
		        
		        double windowWidth = stage.getWidth();
		        double windowHeight = stage.getHeight();

		        // ... (your code to load the new FXML view)

		        // After loading the new view, set the window's dimensions back
		        stage.setWidth(windowWidth);
		        stage.setHeight(windowHeight);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}

	       
	   }