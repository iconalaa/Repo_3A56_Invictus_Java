package controllers.dons;

import services.ServiceGratification;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class StatsController implements Initializable {

    @FXML
    private LineChart<String, Number> chart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ServiceGratification serviceGratification = new ServiceGratification();
            Map<LocalDate, Integer> gratificationsPerDay = serviceGratification.getGratificationsPerDay();
            showLineChart(gratificationsPerDay);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load gratification statistics: " + e.getMessage());
        }
    }

    private void showLineChart(Map<LocalDate, Integer> data) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Gratifications Per Day");

        for (Map.Entry<LocalDate, Integer> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }

        chart.getData().add(series);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}