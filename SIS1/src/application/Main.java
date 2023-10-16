package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
	import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFrame.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 900);
//        //scene.getStylesheets().add(getClass().getResource("mainframe.css").toExternalForm());s      
//        String mfcss = this.getClass().getResource("mainframe.css").toExternalForm();
//        scene.getStylesheets().add(mfcss);
        primaryStage.setTitle("Student Information System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
