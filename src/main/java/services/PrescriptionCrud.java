package services;


import entities.Report;

import java.sql.SQLException;
import java.util.List;

public interface PrescriptionCrud<T> {
    void add(T entity, Report report) throws SQLException;
    void add2(T entity, int re) throws SQLException;
    void delete(int id) throws SQLException;
    void update(T entity,int id) throws SQLException;

    List<T> displayAll() throws SQLException;
}
