package Control;

import java.io.FileNotFoundException;
import java.io.IOException;

import Model.GestorMensajes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class controladorVentanaRedactarMensaje {

    @FXML
    private TextField Texto_Mensaje;
    
    @FXML
    private TextField Texto_Titulo;
    
    @FXML
    void Enviar(ActionEvent event) throws IOException {
    	
    	GestorMensajes.EnviarMensaje(Texto_Titulo.getText(), "PersonalSeguridad" , Texto_Mensaje.getText());
    	FXMLLoader loader = new FXMLLoader();
    	Parent root;
    	Stage stage = new Stage();
    	
    	loader.setLocation(getClass().getResource("/View/VentanaCorreoConExito.fxml"));
    	stage.setTitle("Enviar Mensaje al Personal de Seguridad");
    	loader.setController(new ControladorMensajeEnviado());
    	root = loader.load();
    	stage.setScene(new Scene(root));
    	stage.initModality(Modality.WINDOW_MODAL);
    	stage.initOwner(((Node) (event.getSource())).getScene().getWindow());
    	stage.show();
    	
    }
    
    @FXML
    void VerMensajesVigilante(ActionEvent event) throws IOException {
    	try {
    		
        	FXMLLoader loader = new FXMLLoader();
        	Parent root;
        	Stage stage = new Stage();
        	
        	loader.setLocation(getClass().getResource("/View/Ventana_MensajesVigilante.fxml"));
        	loader.setController(new controladorVerMensajesVigilante());
        	stage.setTitle("Enviar Mensaje al Vigilante");
        	root = loader.load();
        	stage.setScene(new Scene(root));
        	stage.initModality(Modality.WINDOW_MODAL);
        	stage.initOwner(((Node) (event.getSource())).getScene().getWindow());
        	stage.show();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
}
