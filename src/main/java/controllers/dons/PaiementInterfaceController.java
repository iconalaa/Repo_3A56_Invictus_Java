package controllers.dons;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class PaiementInterfaceController {

    @FXML
    public WebView webView;

    public void initialize() {
        // Load the checkout URL into the WebView
        webView.getEngine().load("about:blank"); // Load a blank page initially
    }

    public void loadCheckoutUrl(String url) {
        webView.getEngine().load(url);
    }


}
