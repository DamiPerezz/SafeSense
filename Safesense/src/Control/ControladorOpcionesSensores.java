package Control;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControladorOpcionesSensores {

	@FXML
    private Button btnReg;

    @FXML
    private Button btnReg1;
    
    private int ID_Empresa;
    
    
    public void setID_Empresa(int iD_Empresa) {
		ID_Empresa = iD_Empresa;
	}

	@FXML
    void AbrirCrearSensor(ActionEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/VentanaCrearCamaras.fxml"));
            ControladorSensoresConf control = new ControladorSensoresConf();
            control.setIDEmpresa(ID_Empresa);
            loader.setController(control);
            Parent root = loader.load();
            
            // Obtiene la ventana actual y la cierra antes de mostrar la nueva ventana
            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.close(); // Cierra la ventana actual

            Stage stage = new Stage();
            stage.setTitle("Configuración de camaras en el sistema"); // Establece el título aquí
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) (event.getSource())).getScene().getWindow());
            
         // Obtener la ventana actual (la ventana que se va a cerrar)
            Stage ventanaActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
         // Listener para cuando la nueva ventana se oculte
            stage.setOnCloseRequest(e -> {
                // Aquí puedes reabrir la ventana inicial si es necesario
                ventanaActual.show(); // Muestra la ventana inicial nuevamente
            });
            
         // Cerrar la ventana actual antes de mostrar la nueva
            ventanaActual.hide();
            
            stage.show(); // Mostramos ventana Siguiente

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void AbrirModificarSensor(ActionEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Ventana_Modificar_Sensores.fxml"));
            controladorModificarSensores control = new controladorModificarSensores();
            control.setID_Empresa(ID_Empresa);
            loader.setController(control);
            Parent root = loader.load();
            
            // Obtiene la ventana actual y la cierra antes de mostrar la nueva ventana
            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.close(); // Cierra la ventana actual

            Stage stage = new Stage();
            stage.setTitle("Configuración de camaras en el sistema"); // Establece el título aquí
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) (event.getSource())).getScene().getWindow());
            
         // Obtener la ventana actual (la ventana que se va a cerrar)
            Stage ventanaActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
         // Listener para cuando la nueva ventana se oculte
            stage.setOnCloseRequest(e -> {
                // Aquí puedes reabrir la ventana inicial si es necesario
                ventanaActual.show(); // Muestra la ventana inicial nuevamente
            });
            
         // Cerrar la ventana actual antes de mostrar la nueva
            ventanaActual.hide();
            
            stage.show(); // Mostramos ventana Siguiente

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
