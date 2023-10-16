package application;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MainFrameController implements Initializable {


    @FXML
    private Label Menu;

    @FXML
    private Label exitMenu;

    @FXML
    private AnchorPane slidenav;
    
    public void Borderpane(){
    	
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
        slidenav.setTranslateX(-270);
        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slidenav);

            slide.setToX(0);
            slide.play();

            slidenav.setTranslateX(-270);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(false);
                exitMenu.setVisible(true);
            });
        });

        exitMenu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slidenav);

            slide.setToX(-270);
            slide.play();

            slidenav.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(true);
                exitMenu.setVisible(false);
            });
        });
    }
}
