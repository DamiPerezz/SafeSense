package Control;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Conexion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;


public class ControladorSensoresConf {

	     @FXML
	    private TextField input1;

	    @FXML
	    private TextField input2;

	    @FXML
	    private Button btnReg;

	    @FXML
	    private MenuButton MenuZonas;
	    
	    @FXML
	    private MenuButton MenuTiposSensor;

	    private ArrayList<String> zonas;
	    
	    private int ID_Empresa;
	    
	    public void setIDEmpresa(int id) {
	    	this.ID_Empresa = id;
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
	    public void initialize() {
	        // Agregar elementos al ArrayList (puedes hacerlo desde donde sea apropiado en tu código)
	        
	    	zonas = SacarZonasDeDB();

	        // Limpiar el MenuZonas antes de agregar nuevos elementos
	        MenuZonas.getItems().clear();

	        // Agregar los elementos del ArrayList al MenuZonas
	        for (String zona : zonas) {
	            MenuItem menuItem = new MenuItem(zona);
	           
	            menuItem.setOnAction(event -> {
	                MenuItem source = (MenuItem) event.getSource();
	                MenuZonas.setText(source.getText());
	            });
	            
	            MenuZonas.getItems().add(menuItem);
	            
	            
	        }
	        
	        MenuItem menuItem = new MenuItem("Camara");
	           
            menuItem.setOnAction(event -> {
                MenuItem source = (MenuItem) event.getSource();
                MenuTiposSensor.setText(source.getText());
            });
            
            MenuTiposSensor.getItems().add(menuItem);
            
            
            MenuItem menuItem2 = new MenuItem("Controlador");   
            menuItem2.setOnAction(event -> {
                MenuItem source = (MenuItem) event.getSource();
                MenuTiposSensor.setText(source.getText());
            });
            MenuTiposSensor.getItems().add(menuItem2);
	        
	        
	    }
	    @FXML
	    void RegistrarSensor(ActionEvent event) {
	    	if (input1.getText().isEmpty() || input2.getText().isEmpty() || MenuZonas.getText().equals("MenuZonas") || MenuTiposSensor.getText().equals("MenuTiposSensor")) {
	    		mostrarMensajeDeExito("Todos los campos deben ser llenados.");
	        } else {
	            // Si todos los campos están llenos, proceder con el registro en la base de datos
	            RegistrarSensorEnDB(input1.getText(), input2.getText(), MenuZonas.getText(), MenuTiposSensor.getText());
	        }
	    	
	    }
	    void RegistrarSensorEnDB(String nombre, String ip,String Zona, String tipo) {
	    	int id_Zona = Conexion.obtenerIdZona(Zona);
	    	try {
				Connection cone = Conexion.Conectar();
				Statement stmt = cone.createStatement();
				String query = "INSERT INTO Sensor(nombre,tipo,ip,ID_Zona) VALUES ('" + nombre+"','"+tipo+"','" + ip + "',"+ id_Zona +");";
				ResultSet rs = 	stmt.executeQuery(query);
				rs.close();
				stmt.close();
				cone.close();
				mostrarMensajeDeExito("Has insertado el sensor " + nombre +" tipo " + tipo+ " en la zona " + Zona + " con IP asociada" + ip);
		  }catch(Exception e) {
				System.out.println(e.getMessage());
				mostrarMensajeDeError("Error, revisa la consola para obtener mas detalles");
			}
	    	
	    }
	    ArrayList<String> SacarZonasDeDB(){
			ArrayList<String> zonas = new ArrayList<String>();
			
			try {
				Connection cone = Conexion.Conectar();
				Statement stmt = cone.createStatement();
				String query = "SELECT nombre FROM Zona where ID_Empresa = " + ID_Empresa +";";
				ResultSet rs = 	stmt.executeQuery(query);
				
				while(rs.next()) {
					zonas.add(rs.getString("nombre"));
				}
				
				rs.close();
				stmt.close();
				cone.close();
				System.out.print("Se han sacado las siguientes zonas asociadas a tu empresa " );
				
				for(String e : zonas) {
					System.out.print(e+ ", ");
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
			return zonas;
	    }
}


   
