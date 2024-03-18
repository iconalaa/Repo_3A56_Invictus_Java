package services;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ICrud<T> {
    void add(T el) throws SQLException;
    void update(T el) throws SQLException;
    void delete(int id) throws SQLException;
    ArrayList<T> showAll() throws SQLException;

}
