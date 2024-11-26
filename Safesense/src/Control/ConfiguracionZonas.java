package Control;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Conexion;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ConfiguracionZonas {

	  @FXML
	  private TextField input1;

	  @FXML
	  private Button btnReg;
	  
	  private int ID_Empresa;
	  
	  private String nombre_Empresa;
	  
	  public void setIDEmpresa(int id) {
	        this.ID_Empresa = id;
	        this.nombre_Empresa = Conexion.getEmpresaNombreUsuario(id);
	    }
	
	  @FXML
	  void Registrar() {
		  System.out.println(input1.getText());
		  RegistrarZonaEnDB(input1.getText());
		  
	  }
	  
	  private void mostrarMensajeDeExito(String mensaje) {
		    Alert alert = new Alert(AlertType.INFORMATION);
		    alert.setTitle("Operacion exitosa");
		    alert.setHeaderText(null); // No header
		    alert.setContentText(mensaje);
		    alert.showAndWait(); // Muestra el diálogo y espera a que el usuario lo cierre
		}
	  
	  void RegistrarZonaEnDB(String input1) {
		  //Guardar en DB
		  System.out.println("Registrando zona");
		  try {
				Connection cone = Conexion.Conectar();
				Statement stmt = cone.createStatement();
				String query = "INSERT INTO Zona(nombre,ID_Empresa) VALUES ('" + input1+"',"+ID_Empresa+")";
				ResultSet rs = 	stmt.executeQuery(query);
				
				rs.close();
				stmt.close();
				cone.close();
				mostrarMensajeDeExito("Has insertado la zona " + input1 +" en el sistema de " + nombre_Empresa + " con éxito");
		  }catch(Exception e) {
				System.out.println(e.getMessage());
			}
		  int idN = Conexion.obtenerIdZona(input1);
		  AñadirConfiguracionRegistrosAccesosInicialesAZonaDB(idN);
	  }
	  
	  void AñadirConfiguracionRegistrosAccesosInicialesAZonaDB(int ID_Zona) {
		  //Guardar en DB
		  System.out.println("Creando estados pendiente, denegado, aceptado");
		  try {
				Connection cone = Conexion.Conectar();
				Statement stmt = cone.createStatement();
				String query = "INSERT INTO Acceso(Estado,ID_Zona) VALUES ('aceptado',"+ID_Zona+")";
				ResultSet rs = 	stmt.executeQuery(query);
				query = "INSERT INTO Acceso(Estado,ID_Zona) VALUES ('pendiente',"+ID_Zona+")";
				rs = 	stmt.executeQuery(query);
				query = "INSERT INTO Acceso(Estado,ID_Zona) VALUES ('denegado',"+ID_Zona+")";
				rs = 	stmt.executeQuery(query);
				rs.close();
				stmt.close();
				cone.close();
		  }catch(Exception e) {
				System.out.println(e.getMessage());
			}
	  }
	  
}
