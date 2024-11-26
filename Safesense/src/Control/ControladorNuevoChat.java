package Control;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Model.Conexion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.control.TextField;


public class ControladorNuevoChat {

    @FXML
    private TextField nombreUsuarioField;
    
    private int ID_Empresa;
    
    private String usuarioActual;

    public void setUsuarioActual(String usuarioActual) {
		this.usuarioActual = usuarioActual;
	}
    
    public void setID_Empresa(int iD_Empresa) {
		ID_Empresa = iD_Empresa;
	}

	@FXML
    private void iniciarConversacion() {
        String nombreUsuario = nombreUsuarioField.getText();
        System.out.println(nombreUsuario);

        if (nombreUsuario.isEmpty()) {
            showAlert("Error", "El nombre de usuario no puede estar vacío.");
            return;
        }
        
        // Utilizar la clase Conexion para verificar si el usuario existe
        boolean existeUsuario = Conexion.existeUsuario(nombreUsuario); // Cambiado para utilizar el nuevo método
        boolean esdemiempresa = esDeMiEmpresa(nombreUsuario);
    
        System.out.println(existeUsuario);
        System.out.println(esdemiempresa);
        if (existeUsuario && esdemiempresa) {
        	
            showAlert("Éxito", "El usuario existe, puedes iniciar una conversación.");
            abrirVentanaDeConversacion(nombreUsuario); // Abre la ventana de conversación
        } else {
            showAlert("Error", "El usuario no existe o no está en tu empresa!");
        }
    }
	
	private boolean esDeMiEmpresa(String usuario) {
		boolean existe = false;
		try {
			Connection cone = Conexion.Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT ID_Empresa FROM Usuario WHERE username = '" + usuario + "';" ;
			ResultSet rs = 	stmt.executeQuery(query);
			
			if(rs.next()) {
				int resultado=  rs.getInt("ID_Empresa");
				System.out.println(ID_Empresa);
			    System.out.println(resultado);
				if(resultado == ID_Empresa) {
					existe = true;
				}
				//System.out.println(rs.getString("emisor_id") + " ->" + rs.getString("mensaje"));
				//comprobar si el emisor es el usuario uno lo imprima por la derecha y sino por la izquierda
			}
			rs.close();
			stmt.close();
			cone.close();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return existe;
		
	}

    private void abrirVentanaDeConversacion(String usuario) {
        try {
            // Carga el FXML para la nueva ventana de conversación
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MensajesView.fxml")); // Asegúrate de proporcionar la ruta correcta
            Parent root = loader.load();

        
            ControladorMsj controladorMsj = loader.getController();
            controladorMsj.ConfigurarDatos(usuarioActual, usuario);

            // Muestra la nueva ventana
            Stage stage = new Stage();
            stage.setTitle("Conversación");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir la ventana de conversación.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
