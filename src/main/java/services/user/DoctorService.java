package services.user;

import entities.Doctor;
import services.ICrud;

import java.sql.SQLException;
import java.util.ArrayList;

public class DoctorService implements ICrud<Doctor> {

    @Override
    public void add(Doctor el) throws SQLException {

    }

    @Override
    public void update(Doctor el,int id) throws SQLException {

    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public ArrayList<Doctor> showAll() throws SQLException {
        return null;
    }
}
