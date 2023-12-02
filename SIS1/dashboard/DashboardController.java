package dashboard;

import application.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

//    @FXML
//    private Label adminName;
//
//    @FXML
//    private BarChart<String, Number> barChart;
//
//    @FXML
//    private CategoryAxis xAxis;
//
//    @FXML
//    private NumberAxis yAxis;
//
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        setUsernameLabel();
//        populateBarChart();
//    }
//
//    private void setUsernameLabel() {
//        UserSession session = UserSession.getInstance();
//        String username = session.getUsername();
//        adminName.setText(username);
//    }
//
//    private void populateBarChart() {
//        // Assuming you have a method to retrieve enrollment data per year level
//        // Replace this with your logic to fetch the data
//        // For example, let's say you have an EnrollmentData class with a method getEnrollmentDataPerYearLevel()
//
//        EnrollmentData enrollmentData = new EnrollmentData();
//        // Fetching enrollment data per year level
//        // Replace this with your actual logic to fetch the data
//        // For example, assume you have a Map<String, Integer> where the key is the year level (e.g., "Freshman", "Sophomore", etc.)
//        // and the value is the number of enrollees for that year level
//        // Map<String, Integer> enrollmentPerYearLevel = enrollmentData.getEnrollmentDataPerYearLevel();
//
//        // Sample data (replace this with your fetched data)
//        // Sample data represents the number of enrollees for each year level
//        // For example, "Freshman": 50, "Sophomore": 70, "Junior": 60, "Senior": 80
//        // Add this data to the bar chart
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        series.setName("Enrollment per Year Level");
//        series.getData().add(new XYChart.Data<>("1st yr", 50));
//        series.getData().add(new XYChart.Data<>("2nd yr", 70));
//        series.getData().add(new XYChart.Data<>("3rd yr", 60));
//        series.getData().add(new XYChart.Data<>("4th yr", 80));
//
//        // Set the data to the bar chart
//        barChart.getData().add(series);
//    }
}
