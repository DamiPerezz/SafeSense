package Control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;

import Model.Conexion;

public class ControladorConfiguracionTarjetas {

	private int ID_Empresa;
	
	
	
	public void setID_Empresa(int iD_Empresa) {
		ID_Empresa = iD_Empresa;
	}
	@FXML
    private TextField input1;
    @FXML
    private Button btnReg;
    @FXML
    private MenuButton TrabajadoresMenu;
    private static boolean FormatoInputUID(String UID) {
    	String patronRegex = "^[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}::[0-9A-Fa-f]{2}$";
    	return Pattern.matches(patronRegex, UID);
    }
    private void ActualizarUIDATrabajador(String username,String UID) {
    	
    	try {
			Connection cone = Conexion.Conectar();
			Statement stmt = cone.createStatement();
			String query = "UPDATE Usuario SET UID = '" + UID +"' WHERE username = '" + username +"';";
			ResultSet rs = 	stmt.executeQuery(query);
			rs.close();
			stmt.close();
			cone.close();
			mostrarMensajeDeExito("La tarjeta de acceso de " + username + " ha sido asignada a " + UID);
	  }catch(Exception e) {
			System.out.println(e.getMessage());
			mostrarMensajeDeError("Error, revisa la consola para obtener mas detalles");
		}
    	
    }
    
    private void mostrarMensajeDeExito(String mensaje) {
	    Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setTitle("Operacion exitosa");
	    alert.setHeaderText(null); // No header
	    alert.setContentText(mensaje);
	    alert.showAndWait(); // Muestra el diálogo y espera a que el usuario lo cierre
	}	    
    private void mostrarMensajeDeError(String mensaje) {
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle("Error");
	    alert.setHeaderText(null); // No header
	    alert.setContentText(mensaje);
	    alert.showAndWait(); // Muestra el diálogo y espera a que el usuario lo cierre
	}
    
    private ArrayList<String> SacarTrabajadoresEmpresaDB(){
    	ArrayList<String> trabajadores = new ArrayList<String>();
    	System.out.println("Sacando trabajadores de la DB");
    	
    	try {
			Connection cone = Conexion.Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT username FROM Usuario WHERE ID_Empresa = " + ID_Empresa +";";
			ResultSet rs = 	stmt.executeQuery(query);
			
			while(rs.next()) {
				trabajadores.add(rs.getString("username"));
			}
			
			rs.close();
			stmt.close();
			cone.close();
	  }catch(Exception e) {
			System.out.println(e.getMessage());
			mostrarMensajeDeError("Error, revisa la consola para obtener mas detalles");
		}
    	
    	return trabajadores;
    }
    
    @FXML
    void initialize() {
    	ArrayList<String> trabajadores = SacarTrabajadoresEmpresaDB();
    	for(String t : trabajadores) {
    		System.out.println("Añadiendo " +t);
    		 MenuItem menuItem = new MenuItem(t);
	           
	            menuItem.setOnAction(event -> {
	                MenuItem source = (MenuItem) event.getSource();
	                TrabajadoresMenu.setText(source.getText());
	            });
	            
	            TrabajadoresMenu.getItems().add(menuItem);	
    	}
    }
    
    @FXML
    void Registrar(ActionEvent event) {
    	String tarjeta = input1.getText();
    	if(FormatoInputUID(tarjeta)) {
    		ActualizarUIDATrabajador(TrabajadoresMenu.getText(),tarjeta);
    	}else {
    		mostrarMensajeDeError("Error, revisa el formato de la tarjeta");
    	}
    		
    }
}
