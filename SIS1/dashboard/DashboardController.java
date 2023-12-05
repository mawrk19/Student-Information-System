package dashboard;

import application.DatabaseManager;
import application.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Label adminName;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    
    @FXML
    private PieChart pieChart;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUsernameLabel();
        populateBarChart();
        populatePieChart();
        pieChart.setLabelsVisible(false);
    }

    private void setUsernameLabel() {
        UserSession session = UserSession.getInstance();
        String username = session.getUsername();
        adminName.setText(username);
    }

    private void populateBarChart() {
        try (Connection barcon = DatabaseManager.getConnection()) {

            String firstYearQuery = "SELECT year, COUNT(sid) AS count_sid FROM student WHERE year = '1st' GROUP BY year";
            populateSeries(barcon, firstYearQuery, "1st Year");

            String secondYearQuery = "SELECT year, COUNT(sid) AS count_sid FROM student WHERE year = '2nd' GROUP BY year";
            populateSeries(barcon, secondYearQuery, "2nd Year");
            
            String thirdYearQuery = "SELECT year, COUNT(sid) AS count_sid FROM student WHERE year = '3rd' GROUP BY year";
            populateSeries(barcon, thirdYearQuery, "3rd Year");
            
            String fourthYearQuery = "SELECT year, COUNT(sid) AS count_sid FROM student WHERE year = '4th' GROUP BY year";
            populateSeries(barcon, fourthYearQuery, "4th Year");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateSeries(Connection connection, String query, String seriesName) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(seriesName);

            while (resultSet.next()) {
                String year = resultSet.getString("year");
                int sidCount = resultSet.getInt("count_sid");

                series.getData().add(new XYChart.Data<>(year, sidCount));
            }

            barChart.getData().add(series);
        }
    }
    
    private void populatePieChart() {
        try (Connection piecon = DatabaseManager.getConnection()) {

        	String courseCountsQuery = "SELECT " +
                    "CASE WHEN course = 'BSCS' THEN 'BSCS' " +
                         "WHEN course = 'BSIT' THEN 'BSIT' " +
                         "WHEN course = 'BSIS' THEN 'BSIS' " +
                         "WHEN course = 'BSEMC' THEN 'BSEMC' " +
                         "ELSE 'Others' END AS course, " +
                    "COUNT(sid) AS count_sid " +
                    "FROM student " +
                    "WHERE course IN ('BSCS', 'BSIT', 'BSIS', 'BSEMC') " +
                    "GROUP BY course";
        	
        	populatePie(piecon, courseCountsQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void populatePie(Connection connection, String query) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            int totalCount = 0;

            while (resultSet.next()) {
                String course = resultSet.getString("course");
                int sidCount = resultSet.getInt("count_sid");
                totalCount += sidCount;

                pieChartData.add(new PieChart.Data(course, sidCount));
            }

            for (PieChart.Data data : pieChartData) {
                double percentage = (data.getPieValue() / totalCount) * 100;
                data.setName(data.getName() + " (" + String.format("%.2f", percentage) + "%)");
            }

            pieChart.setData(pieChartData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}