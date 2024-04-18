package tests;

import services.diagnostic.ReportService;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        ReportService reportService = new ReportService();
        System.out.println(reportService.displayAll());
    }
}
