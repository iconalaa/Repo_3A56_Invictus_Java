package services.user;
import entities.Patient;
import services.ICrud;

import java.sql.SQLException;
import java.util.ArrayList;

public class PatientService implements ICrud<Patient>{


    @Override
    public void add(Patient el) throws SQLException {

    }

    @Override
    public void update(Patient el,int id) throws SQLException {

    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public ArrayList<Patient> showAll() throws SQLException {
        return null;
    }
}
