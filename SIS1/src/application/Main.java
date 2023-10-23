package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file
    	//Parent root = FXMLLoader.load(getClass().getResource("/com/login/MainLogin.fxml"));  
    	Parent root = FXMLLoader.load(getClass().getResource("MainFrame.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        
//      //scene.getStylesheets().add(getClass().getResource("mainframe.css").toExternalForm());s      
//      String mfcss = this.getClass().getResource("mainframe.css").toExternalForm();
//      scene.getStylesheets().add(mfcss);
    }

    public static void main(String[] args) {
        launch(args);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
       
    }
}
