package services;

import entities.Doctor;
import entities.Image;
import entities.User;

import java.sql.SQLException;
import java.util.List;

public interface ReportCrud <T>{

    void add(T entity, User doctor, Image image) throws SQLException;
    void delete(int id) throws SQLException;
    void update(T entity,int id) throws SQLException;
    List<T> displayAll(int id) throws SQLException;


}
