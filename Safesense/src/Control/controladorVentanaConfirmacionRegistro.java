package Control;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class controladorVentanaConfirmacionRegistro {

    @FXML
    private Pane confirmacionPane;

    @FXML
    private Label confirmacionLabel;

    public void inicializar(String mensaje) {
        confirmacionLabel.setText(mensaje);
    }
    
    
}
