package controllers.blog;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class CommentaireItem {
    public HBox commenterHbox;
    public TextField inputTextComment;
    public ImageView commenterArt;
    public ImageView userimg;
    public Text username;
    public Text userComment;
    public Label createdAT;
    public Label createdAtLabelLocal;

    public void commenterArt(MouseEvent mouseEvent) {
    }
}
