package services.Image;

import entities.Image;
import entities.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface DroitCrud<T> {

    void add(T el) throws SQLException;
    void delete(int id) throws SQLException;
    ArrayList<T> showAll() throws SQLException;

    //share image
    void deleteShraedRoleById(int id) throws SQLException;
    void addRoleToImage(int idimage , List<Integer> ids ) throws SQLException;
    ArrayList<User> getRadwithGeustRole(int imageid) throws SQLException;
    ArrayList<User> getRadwithoutGuest(int imageid) throws SQLException;





}
