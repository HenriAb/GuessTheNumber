package it.polito.tdp.GuessTheNumber;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList; //
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class FXMLController {
	
	private int NMAX;
	private int TMAX;
	int NMAX_F = 100;
	int TMAX_F = 10;
	int NMAX_M = 100;
	int TMAX_M = 8;
	int NMAX_D = 100;
	int TMAX_D = 5;
	private boolean inGioco = false;
	private int segreto;
	private int tentativiFatti;
	int min = 1;
	int max = TMAX;//TMAX;
	
	LinkedList<Integer> ltentativiFatti = new LinkedList<>();
	
	ObservableList<String> list = FXCollections.observableArrayList("Facile", "Medio", "Difficile");
	

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnNewGame;

    @FXML
    private TextField tentativiRimasti;

    @FXML
    private HBox layout;

    @FXML
    private TextField inputTentativo;

    @FXML
    private Button btnTry;

    @FXML
    private TextArea output;
    
    @FXML
    private Button btnEndGame;
    
    @FXML
    private ProgressBar status;
    
    @FXML
    private HBox layoutMode;

    @FXML
    private CheckBox checkBoxHelp;

    @FXML
    private ComboBox<String> choice;

    @FXML
    void doNewGame(ActionEvent event) {
    	
    	//logica
    	this.segreto = (int) (Math.random() * NMAX ) +1;
    	System.out.println("Il segreto vale: " + segreto);
    	this.tentativiFatti = 0;
    	
    	//interfaccia
    	this.inGioco = true;
    	this.layoutMode.setDisable(false);
    	this.choice.setDisable(false);
    	this.status.setVisible(true);
    	this.status.setProgress(-1.0);
    	
    	this.tentativiRimasti.setText("" );//+ TMAX);
    	
    	
    	
    }
    
    @FXML
    void doEndGame(ActionEvent event) {

    	this.inGioco = false;
    	this.inputTentativo.clear();
    	this.layout.setDisable(true);
    	this.layoutMode.setDisable(true);
    	this.output.clear();
    	this.tentativiRimasti.setText("");
    	this.choice.setValue("scegli difficoltà");
    	this.checkBoxHelp.setSelected(false);
    	this.btnNewGame.setDisable(false);
    }

    @FXML
    void doHelp(ActionEvent event) {

    }
    
    @FXML
    void doTry(ActionEvent event) {
    	
    	//Lettura e test input
    	String tent = this.inputTentativo.getText();
    	int tentativo;
    	try {
    		tentativo = Integer.parseInt(tent);
    	}catch(NumberFormatException nfe) {
    		nfe.printStackTrace();
    		this.output.setText("Errore! Devi inserire un numero.");
    		return;
    	}
    	
    	//interfaccia
    	this.btnNewGame.setDisable(true);
    	this.choice.setDisable(true);
    	this.layoutMode.setDisable(true);
    	
    	//gestione
    	if(this.ltentativiFatti.contains(tentativo)) {
			output.setText("Numero gia' inserito!");
			this.inputTentativo.setText("");
			return;
		}
    	
    	if(tentativo > max || tentativo < min) {
    		this.output.setText("Errore! inserisci un valore tra [" + min + " - " + max + "]");
    		this.inputTentativo.setText("");
    		return;
    	}
    	
    	this.tentativiFatti++;
    	this.tentativiRimasti.setText(Integer.toString(TMAX - tentativiFatti));
    	this.status.setProgress((double) Integer.parseInt(tentativiRimasti.getText()) / TMAX);
    	this.ltentativiFatti.add(tentativo);
    	this.tenta(tentativo);
    	this.inputTentativo.setText("");
    	
    }

    @FXML
    void doChange(ActionEvent event) {

    	this.layout.setDisable(false);
    	this.layoutMode.setDisable(false);
    	switch(this.choice.getValue()) {
    	case "Facile":
    		this.NMAX = NMAX_F;
    		this.TMAX = TMAX_F;
    		break;
    	
    	case "Medio":
    		this.NMAX = NMAX_M;
    		this.TMAX = TMAX_M;
    		break;
    	
    	default:
    		this.NMAX = NMAX_D;
    		this.TMAX = TMAX_D;
    		break;	
    	}
    	
    	min = 1;
    	max = NMAX;
    	
    	if(this.inGioco) {
    		this.tentativiRimasti.setText("" + TMAX);// mostra 0
    	}
    	
    }
    
    public void tenta(int t) {
    	
    	//devo distinguere due casi: modalita assistita o no
    	if(this.checkBoxHelp.isSelected()) {
    		//distinguo se sono ancora in gara o meno
    		if(Integer.parseInt(this.tentativiRimasti.getText()) > 0) {
    			if(t == segreto) {
    				this.output.setText("Hai vinto! Il numero era: " + this.segreto + ". Hai utilizzato " + this.tentativiFatti + " tentativi.");
            		this.layout.setDisable(true);
            		this.inGioco = false;
            		return;
    			}
    			else {
    				if(t < segreto) {
    					if(t > min) {
    						min = t;
    					}
    					this.output.setText("Troppo basso! Inserisci un numero tra [" + min + " - " + max + "]. Suggerimento:" + ((int)(min+max)/2));
    				}
    				else {
    					// tentativo troppo alto
    					if(t > segreto) {
    						if(t < max) {
    							max = t;
    						}
    						this.output.setText("Troppo alto! Inserisci un numero tra [" + min + " - " + max + "]. Suggerimento:" + ((int)(min+max)/2));
    					}
    				}
    			}
    		}
    		else {
    			// ho perso
    			this.output.setText("Hai perso! Il numero era: " + this.segreto);
        		this.layout.setDisable(true);
        		this.inGioco = false;
        		return;
    		}
    	}
    	else {
    		// Modalita normale
    		//distinguo se sono ancora in gara o meno
    		if(Integer.parseInt(this.tentativiRimasti.getText()) > 0) {
    			if(t == segreto) {
    				this.output.setText("Hai vinto! Il numero era: " + this.segreto + ". Hai utilizzato " + this.tentativiFatti + " tentativi.");
            		this.layout.setDisable(true);
            		this.inGioco = false;
            		return;
    			}
    			else {
    				if(t < segreto) {
    					this.output.setText("Troppo basso!");
    				}
    				else {
    					// tentativo troppo alto
    					this.output.setText("Troppo alto!");
    				}
    			}
    		}
    		else {
    			// ho perso
    			this.output.setText("Hai perso! Il numero era: " + this.segreto);
        		this.layout.setDisable(true);
        		this.inGioco = false;
        		return;
    		}
    	}
    	
    }
    
    @FXML
    void initialize() {
    	this.choice.setItems(list);
        assert btnNewGame != null : "fx:id=\"btnNewGame\" was not injected: check your FXML file 'Scene.fxml'.";
        assert choice != null : "fx:id=\"choice\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tentativiRimasti != null : "fx:id=\"tentativiRimasti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert layout != null : "fx:id=\"layout\" was not injected: check your FXML file 'Scene.fxml'.";
        assert inputTentativo != null : "fx:id=\"inputTentativo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTry != null : "fx:id=\"btnTry\" was not injected: check your FXML file 'Scene.fxml'.";
        assert output != null : "fx:id=\"output\" was not injected: check your FXML file 'Scene.fxml'.";

    }
}

