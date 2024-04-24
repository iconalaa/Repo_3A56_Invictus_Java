package services.diagnostic;

import entities.Image;
import services.user.PatientService;
import services.user.UserService;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageService {

    private Connection connection;

    public ImageService(){
        connection = MyDataBase.getInstance().getConnection();
    }





}

