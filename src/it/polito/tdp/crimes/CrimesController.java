/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
//import java.util.List;
import java.util.HashSet;
//import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CrimesController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	//String input = boxAnno.getValue();
    	//try {
    		Integer anno = boxAnno.getValue();
    		model.creaGrafo(anno);
    		txtResult.appendText("Grafo creato\n");
    		txtResult.appendText("vertici: "+model.nVertici()+"\n");
    		txtResult.appendText("archi: "+model.nArchi()+"\n\n");
    		txtResult.appendText(model.getVicini());
    		
        	
    	
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	Integer anno, mese, giorno, n;
    	
    	try {
    		n = Integer.parseInt(txtN.getText());
    	} catch (NumberFormatException e) {
    		txtResult.clear();
    		txtResult.appendText("Inserire un numero intero.\n");
    		return;
    	}
    	
    	if(n<1 || n>10) {
    		txtResult.clear();
    		txtResult.appendText("N deve essere compreso tra 1 e 10\n");
    		return;
    	}
    	
    	anno = boxAnno.getValue();
    	mese = boxMese.getValue();
    	giorno = boxGiorno.getValue();
    	
    	if(anno == null || mese == null || giorno == null) {
        	this.txtResult.clear();
    		txtResult.appendText("Seleziona tutti i campi!\n");
    		return;
    	}
    	
    	try {
    		LocalDate.of(anno, mese, giorno);
    	} catch (DateTimeException e) {
        	this.txtResult.clear();
    		txtResult.appendText("Data non corretta\n");
    	}
    	
    	txtResult.appendText("Simulo con " + n + " agenti");
    	txtResult.appendText("\nCRIMINI MAL GESTITI: " + this.model.simula(anno, mese, giorno, n));
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxAnno.getItems().addAll(model.getAnni());
    	boxMese.getItems().addAll(model.getMesi());
    	boxGiorno.getItems().addAll(model.getGiorni());
    }
}
