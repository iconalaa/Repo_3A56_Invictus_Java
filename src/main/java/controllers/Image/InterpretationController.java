package controllers.Image;

import entities.Interpretation;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import services.interpretation.InterpreationServices;

import java.sql.SQLException;
import java.util.ArrayList;

public class InterpretationController {

    @FXML
    private Text inter;
    Interpretation interpretation;

    private static  int nb=0;

    private InterpreationServices interpreationServices = new InterpreationServices();


    @FXML
    void initialize() throws SQLException {

     ArrayList<Interpretation> ps= interpreationServices.getInterpretationsWithDetails(ImageDashboard.selectedImage.getId());

     if(nb<=ps.size())

     {
         inter.setText(ps.get(nb).getInterpretation());
         nb+=1;

     }
     if(nb==ps.size())
     {
         nb=0;
     }



    }


    public void setInterpretationData(Interpretation interpretation) {


        this.interpretation=interpretation;

    }



}
