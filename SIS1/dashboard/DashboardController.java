package dashboard;

import application.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Label adminName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUsernameLabel();
    }

    private void setUsernameLabel() {
        UserSession session = UserSession.getInstance();
        String username = session.getUsername();
        adminName.setText(username);
        System.out.println(username);
    }
}
