package controllers.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

public class StatisticController {
    @FXML
    private PieChart pieChart;
    public void setData(int numberOfDoctors, int numberOfRadiologists, int numberOfSimpleUsers) {
        // Create data for the pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Doctors", numberOfDoctors),
                new PieChart.Data("Radiologists", numberOfRadiologists),
                new PieChart.Data("Simple Users", numberOfSimpleUsers)
        );

        // Set the data to the pie chart
        pieChart.setData(pieChartData);
    }
}
