package controllers.blog;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DeleteArticleController {
    public Label lblConfirmation;
    public Button btnConfirmDelete;
    public Button btnCancel;
    private int articleId;
    private boolean confirmed = false;

    public void confirmDelete(ActionEvent actionEvent) {
        confirmed = true;
        closeWindow();
    }

    public void cancelDelete(ActionEvent actionEvent) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getArticleId() {
        return articleId;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
