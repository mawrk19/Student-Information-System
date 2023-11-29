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
	                FlowPane.setMargin(stackPane, new Insets(0, 0, 0, 0)); // Adjust the bottom margin as needed
	                calendar.getChildren().add(stackPane);
	            }
	        }
	    }

	       
	   }