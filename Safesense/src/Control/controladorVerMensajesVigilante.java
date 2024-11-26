package Control;


import java.io.FileNotFoundException;


import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;


public class controladorVerMensajesVigilante {

	
	    @FXML
	    private ScrollPane ResultadosVigilante;
	    
	    @FXML
	    void ActualizarMensajesSeguridad() throws FileNotFoundException {
	    	System.out.println("Entrado");
	    	TextArea textArea = new TextArea();
	    	//textArea.setText(Mensaje.LeerInbox("Vigilante"));
	    	
	    	ResultadosVigilante.setContent(textArea);
	    }

	    
}
