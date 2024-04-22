package tests;

import entities.Image;
import entities.Interpretation;
import services.diagnostic.ImageService;
import services.interpretation.InterpreationServices;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        ImageService ps = new ImageService();
        System.out.println(ps.getSharedImageById(8));

    }
}
