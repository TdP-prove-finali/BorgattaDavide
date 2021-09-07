package it.polito.tdp.tesi;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.tesi.model.Giocatore;
import it.polito.tdp.tesi.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CheckBox checkQualita;

    @FXML
    private TextField budget;

    @FXML
    private ComboBox<String> boxModulo;

    @FXML
    private ComboBox<Giocatore> boxGiocatore1;

    @FXML
    private ComboBox<Giocatore> boxGiocatore2;

    @FXML
    private ComboBox<Giocatore> boxGiocatore3;

    @FXML
    private ComboBox<Giocatore> boxGiocatore4;

    @FXML
    private Button btnVeriifica;

    @FXML
    private Button btnCerca;

    @FXML
    private TextArea txtRisultato;

    @FXML
    void doCerca(ActionEvent event) {

    }

    @FXML
    void doVerifica(ActionEvent event) {
    	
    	txtRisultato.clear();
    	
    	String modulo = this.boxModulo.getValue();
    	
    	if (modulo == null ) {
    		txtRisultato.appendText("Selezionare il modulo che si vuole usare");
    		return;
    	}
    	
    	int budget;
    	try {
    		budget = Integer.parseInt(this.budget.getText());
    	} catch (NumberFormatException e) {
    		txtRisultato.setText("Inserire un numero intero");
    		return;
    	}
    	
    	List<Giocatore> giocatori = new ArrayList<>();
    	
    	Giocatore giocatore1 = this.boxGiocatore1.getValue();
    	giocatori.add(giocatore1);
    	Giocatore giocatore2 = this.boxGiocatore2.getValue();
    	giocatori.add(giocatore2);
    	Giocatore giocatore3 = this.boxGiocatore3.getValue();
    	giocatori.add(giocatore3);
    	Giocatore giocatore4 = this.boxGiocatore4.getValue();
    	giocatori.add(giocatore4);
    	
    	if (giocatore1 == null || giocatore2 == null || giocatore3 == null || giocatore4 == null ) {
    		txtRisultato.appendText("Selezionare i 4 giocatori");
    		return;
    	}
    	
    
    	String risultato = model.verificaParametri(modulo, budget, giocatori);
    	txtRisultato.appendText(risultato);
    	

    }

    @FXML
    void initialize() {
        assert checkQualita != null : "fx:id=\"checkQualita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert budget != null : "fx:id=\"budget\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxModulo != null : "fx:id=\"boxModulo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiocatore1 != null : "fx:id=\"boxGiocatore1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiocatore2 != null : "fx:id=\"boxGiocatore2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiocatore3 != null : "fx:id=\"boxGiocatore3\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiocatore4 != null : "fx:id=\"boxGiocatore4\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnVeriifica != null : "fx:id=\"btnVeriifica\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCerca != null : "fx:id=\"btnCerca\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtRisultato != null : "fx:id=\"txtRisultato\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
        this.boxModulo.getItems().addAll(model.getModuli());
    	
    	this.boxGiocatore1.getItems().addAll(model.getGiocatori());
    	this.boxGiocatore2.getItems().addAll(model.getGiocatori());
    	this.boxGiocatore3.getItems().addAll(model.getGiocatori());
    	this.boxGiocatore4.getItems().addAll(model.getGiocatori());
    }
}
