package it.polito.tdp.tesi;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.tesi.model.Giocatore;
import it.polito.tdp.tesi.model.Model;
import it.polito.tdp.tesi.model.Vertice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private ComboBox<String> boxQualita;

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
    	
    	txtRisultato.setStyle("-fx-font-family: monospace");
    	txtRisultato.clear();
    	
    	List<Vertice> squadra = model.getSquadra();
    	
    	if (squadra.size() == 0) {
    		this.txtRisultato.appendText("SQUADRA NON TROVATA \nProvare a cambiare uno dei 4 giocatori o aumentare il budget");
    		return;
    	}
    	
    	this.txtRisultato.clear();
    	this.txtRisultato.appendText("SQUADRA TROVATA CON VALORE = " + (model.getIntesaFinale()+model.getOverallFinale()) + 
    			"\n\nOVERALL: " + model.getOverallFinale() + "\tINTESA: " + model.getIntesaFinale() + "\tCOSTO TOTALE SQUADRA: " +
    			model.getCostoFinale()+ " crediti\n");

    	for(Vertice v : squadra) {
    		
    		txtRisultato.appendText(String.format("\n%-30s %-5s %d %s  ",v.getRuolo().getNome().toUpperCase(), 
    				v.getGiocatore().getPosizione(), v.getGiocatore().getOverall(), v.getGiocatore().getNome().toUpperCase()));
    	}

    }

    @FXML
    void doVerifica(ActionEvent event) {
    	
    	txtRisultato.setStyle("-fx-font-family: monospace");
    	txtRisultato.clear();
    	
    	String modulo = this.boxModulo.getValue();
    	
    	if (modulo == null ) {
    		txtRisultato.appendText("ERRORE: \nSelezionare il modulo che si vuole usare");
    		return;
    	}
    	
    	int budget;
    	try {
    		budget = Integer.parseInt(this.budget.getText());
    	} catch (NumberFormatException e) {
    		txtRisultato.setText("ERRORE: \nInserire un numero intero");
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
    		txtRisultato.appendText("ERRORE: \nSelezionare la qualità e i 4 giocatori");
    		return;
    	}
    	
    
    	String risultato = model.verificaParametri(modulo, budget, giocatori);
    	if (risultato.equals("Giusto")) {
    		risultato = "Parametri inseriti in modo corretto "
					+ "\nI giocatori selezionati rispettano i vincoli di intesa e prezzo \nCliccare sul bottone 'Cerca Squadra' "
					+ "per costruire la migliore squadra possibile \nBudget rimasto di: "
					+ model.getBudgetRimasto() + " crediti" ;
    		this.btnCerca.setDisable(false);
    	}
    	txtRisultato.appendText(risultato);
    	

    }
    
    @FXML
    void impostaGiocatori(ActionEvent event) {
    	
    	this.boxGiocatore1.getItems().clear();
		this.boxGiocatore2.getItems().clear();
		this.boxGiocatore3.getItems().clear();
		this.boxGiocatore4.getItems().clear();
    	
    	if(this.boxQualita.getValue().equals("TOP (ov.>84)")) {
    		this.boxGiocatore1.getItems().addAll(model.getGiocatoriTop());
    		this.boxGiocatore2.getItems().addAll(model.getGiocatoriTop());
    		this.boxGiocatore3.getItems().addAll(model.getGiocatoriTop());
    		this.boxGiocatore4.getItems().addAll(model.getGiocatoriTop());
    	}  
    	if(this.boxQualita.getValue().equals("ALTO (86>ov.>83)")) {
    		this.boxGiocatore1.getItems().addAll(model.getGiocatoriAlto());
    		this.boxGiocatore2.getItems().addAll(model.getGiocatoriAlto());
    		this.boxGiocatore3.getItems().addAll(model.getGiocatoriAlto());
    		this.boxGiocatore4.getItems().addAll(model.getGiocatoriAlto());
    	}  
    	if(this.boxQualita.getValue().equals("MEDIO (84>ov.>81)")) {
    		this.boxGiocatore1.getItems().addAll(model.getGiocatoriMedio());
    		this.boxGiocatore2.getItems().addAll(model.getGiocatoriMedio());
    		this.boxGiocatore3.getItems().addAll(model.getGiocatoriMedio());
    		this.boxGiocatore4.getItems().addAll(model.getGiocatoriMedio());
    	}
    	if(this.boxQualita.getValue().equals("BASSO (82>ov.>79)")) {
    		this.boxGiocatore1.getItems().addAll(model.getGiocatoriBasso());
    		this.boxGiocatore2.getItems().addAll(model.getGiocatoriBasso());
    		this.boxGiocatore3.getItems().addAll(model.getGiocatoriBasso());
    		this.boxGiocatore4.getItems().addAll(model.getGiocatoriBasso());
    	}
    	if(this.boxQualita.getValue().equals("BASE (80>ov.>77)")) {
    		this.boxGiocatore1.getItems().addAll(model.getGiocatoriBase());
    		this.boxGiocatore2.getItems().addAll(model.getGiocatoriBase());
    		this.boxGiocatore3.getItems().addAll(model.getGiocatoriBase());
    		this.boxGiocatore4.getItems().addAll(model.getGiocatoriBase());
    	}  

    }

    @FXML
    void initialize() {
        assert budget != null : "fx:id=\"budget\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxModulo != null : "fx:id=\"boxModulo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiocatore1 != null : "fx:id=\"boxGiocatore1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiocatore2 != null : "fx:id=\"boxGiocatore2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiocatore3 != null : "fx:id=\"boxGiocatore3\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiocatore4 != null : "fx:id=\"boxGiocatore4\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxQualita != null : "fx:id=\"boxQualita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnVeriifica != null : "fx:id=\"btnVeriifica\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCerca != null : "fx:id=\"btnCerca\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtRisultato != null : "fx:id=\"txtRisultato\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
        this.boxModulo.getItems().addAll(model.getModuli());
    	
    	this.boxQualita.getItems().addAll("TOP (ov.>84)", "ALTO (86>ov.>83)", "MEDIO (84>ov.>81)",
    			"BASSO (82>ov.>79)", "BASE (80>ov.>77)");
    	
    }
}
