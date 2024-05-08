package tests;

import utils.MyDataBase;
import entities.*;
import services.*;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        ServiceDonateur serviceDonateur= new ServiceDonateur();
        ServiceGratification serviceGratification = new ServiceGratification();


        MyDataBase myDB = MyDataBase.getInstance();
        Connection connection = myDB.getConnection();

        if (connection != null) {
            System.out.println("Connection etabli.");
            // You can perform further operations with the connection here if needed.
        } else {
            System.out.println("error de connection.");
        }

        Gratification g1 = new Gratification(500,"Sub","hello","Monnaitaire");
        Donateur d1  = new Donateur(28475145,"Bairem","Walid","WB@hotmail.fr","Particulier");

            /*try {
             serviceDonateur.ajouter(d1);

         } catch (SQLException e) {
             System.out.println(e.getMessage());
        }

             */

            try {
                System.out.println(serviceGratification.afficher());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }




    }


    }


