package services.interpretation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface InterpretationService<T> {

    void addInterpretation(T a) throws SQLException;
    void deleteInterpretation(T a) throws SQLException;
    void EditInterpretation(T a) throws SQLException;
    ArrayList<T> showInterpreationByImage(int id) throws SQLException;
    ArrayList<T> showInterpreationByUser(int id) throws SQLException;



}
