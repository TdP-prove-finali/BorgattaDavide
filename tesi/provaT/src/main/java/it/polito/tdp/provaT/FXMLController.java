package it.polito.tdp.provaT;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CheckBox checkQualita;

    @FXML
    private TextField budget;

    @FXML
    private ComboBox<?> boxModulo;

    @FXML
    private ComboBox<?> boxGiocatore1;

    @FXML
    private ComboBox<?> boxGiocatore2;

    @FXML
    private ComboBox<?> boxGiocatore3;

    @FXML
    private ComboBox<?> boxGiocatore4;

    @FXML
    private Button btnVerifica;

    @FXML
    private Button btnCerca;

    @FXML
    private TextArea txtRisultato;

    @FXML
    void initialize() {
        assert checkQualita != null : "fx:id=\"checkQualita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert budget != null : "fx:id=\"budget\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxModulo != null : "fx:id=\"boxModulo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiocatore1 != null : "fx:id=\"boxGiocatore1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiocatore2 != null : "fx:id=\"boxGiocatore2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiocatore3 != null : "fx:id=\"boxGiocatore3\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiocatore4 != null : "fx:id=\"boxGiocatore4\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnVerifica != null : "fx:id=\"btnVerifica\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCerca != null : "fx:id=\"btnCerca\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtRisultato != null : "fx:id=\"txtRisultato\" was not injected: check your FXML file 'Scene.fxml'.";

    }
}

