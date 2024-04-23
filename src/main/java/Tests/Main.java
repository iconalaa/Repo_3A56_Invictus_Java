package Tests;

import Utils.MyDataBase;
import Entities.*;
import Services.*;

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

        gratification g1 = new gratification(500,"Sub","hello","Monnaitaire");
        donateur d1  = new donateur(28475145,"Bairem","Walid","WB@hotmail.fr","Particulier");

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


