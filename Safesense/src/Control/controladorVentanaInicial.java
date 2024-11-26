package Control;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class controladorVentanaInicial {

    @FXML
    private Button Boton_registro_usuario;

    @FXML
    private Button Boton_iniciar_sesion;

    @FXML
    void abrir_ventana_inicio_sesion(ActionEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Ventana_Inicio_Sesion.fxml"));
            controladorVentanaInicioSesion control = new controladorVentanaInicioSesion();
            loader.setController(control);
            Parent root = loader.load();


            // Obtiene la ventana actual y la cierra antes de mostrar la nueva ventana
            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.close(); // Cierra la ventana actual
            
            Stage stage = new Stage();
            stage.setTitle("Iniciar Sesión");
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

    // Método para cargar los registros de usuario desde el archivo JSON
    private JsonArray cargarUsuarios() {
        try (FileReader reader = new FileReader("registroUsuarios.json")) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            
            // JsonParser de la biblioteca Gson para analizar el contenido del archivo JSON que se está leyendo (reader)
            //JsonParser.parseReader(reader) analiza el flujo de entrada (reader) y devuelve un Json array. Este json array es el que contiene los registros 
            
            
            return jsonArray;
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonArray();
        }
    } 
    

    @FXML
    void abrir_ventana_registro(ActionEvent event) {
    	
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ventana_registro_usuario.fxml"));
            controladorVentanaRegistro control = new controladorVentanaRegistro();
            loader.setController(control);
            Parent root = loader.load();
            

            // Obtiene la ventana actual y la cierra antes de mostrar la nueva ventana
            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.close(); // Cierra la ventana actual

            Stage stage = new Stage();
            stage.setTitle("Regiatro de Nuevo Usuario");
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
