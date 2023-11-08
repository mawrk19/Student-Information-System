package schedule;



import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

	   public class ScheduleController {
	       @FXML
	       private Button BtnTimetable;

	       public void initialize() {
	           BtnTimetable.setOnMouseClicked(event -> {
	               
	                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/MainFrame.fxml"));
	                Parent root = null;
					try {
						root = loader.load();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                Stage stage = (Stage) BtnTimetable.getScene().getWindow();
	                Scene scene = new Scene(root);
	                stage.setScene(scene);
	           });
	       
	       }
	   }