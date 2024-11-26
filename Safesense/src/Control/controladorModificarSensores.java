package Control;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Conexion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class controladorModificarSensores {
	
	private int ID_Empresa;
	
	

	  public void setID_Empresa(int iD_Empresa) {
		ID_Empresa = iD_Empresa;
	}

	@FXML
	    private Button btnReg;

	    @FXML
	    private MenuButton MenuSensores;

	    @FXML
	    private MenuButton MenuZonas;
	    
	    @FXML
	    private TextField name_input;

	    @FXML
	    private TextField ip_input;

	    @FXML
	    private ListView<String> TablaSensoresEnZona;

	    @FXML
	    private MenuButton ZonaDelSensor;

	    @FXML
	    private MenuButton TipoSensor;
	    
	    private int PK_SensorActual;

	    @FXML
	    void ActualizarSensor(ActionEvent event) {
	    	int id_zona = Conexion.obtenerIdZona(ZonaDelSensor.getText());
	    	if(PK_SensorActual>0) {
	    	
	    		try {
					Connection cone = Conexion.Conectar();
					Statement stmt = cone.createStatement();
					String query = "UPDATE Sensor SET nombre = '" + name_input.getText() + "', ip = '" + ip_input.getText() +"', tipo = '" + TipoSensor.getText() +"', ID_Zona = " + id_zona + " WHERE ID_Sensor = " + PK_SensorActual +";";
					ResultSet rs = 	stmt.executeQuery(query);
					
					rs.close();
					stmt.close();
					cone.close();
					mostrarMensajeDeExito("Se han actualizado los campos correctamente");
					
				}catch(Exception e) {
					System.out.println(e.getMessage());
				}
	    		
	    	}
	    }
	    
	    private void mostrarMensajeDeExito(String mensaje) {
		    Alert alert = new Alert(AlertType.INFORMATION);
		    alert.setTitle("Operacion exitosa");
		    alert.setHeaderText(null); // No header
		    alert.setContentText(mensaje);
		    alert.showAndWait(); // Muestra el diálogo y espera a que el usuario lo cierre
		}
	    
	    @FXML
	    void MostrarSensoresEnZona(ActionEvent event) {
	    	int id_zona = Conexion.obtenerIdZona(MenuZonas.getText());
	    	ArrayList<String> SensoresEnZona = SacarSensoresDeZona(id_zona);
	    	
	    	ObservableList<String> items = FXCollections.observableArrayList(SensoresEnZona);
	        
	    	TablaSensoresEnZona.setItems(items);
	    	
	    }
	    
	    ArrayList<String> SacarSensoresDeZona(int ID_Zona){
			ArrayList<String> sensores = new ArrayList<String>();
			
			try {
				Connection cone = Conexion.Conectar();
				Statement stmt = cone.createStatement();
				String query = "SELECT nombre FROM Sensor where ID_Zona = " + ID_Zona +";";
				ResultSet rs = 	stmt.executeQuery(query);
				
				while(rs.next()) {
					sensores.add(rs.getString("nombre"));
				}
				
				rs.close();
				stmt.close();
				cone.close();
				System.out.print("Se han sacado las siguientes zonas asociadas a tu empresa " );
				
				
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
			return sensores;
	    }
	    int SacarIDDeSensor(String nombre){
			int n = -1;
			try {
				Connection cone = Conexion.Conectar();
				Statement stmt = cone.createStatement();
				String query = "SELECT ID_Sensor FROM Sensor where nombre = '" + nombre +"';";
				ResultSet rs = 	stmt.executeQuery(query);
				
				while(rs.next()) {
					n = rs.getInt("ID_Sensor");
				}
				
				rs.close();
				stmt.close();
				cone.close();
				System.out.print("Se han sacado las siguientes zonas asociadas a tu empresa " );
				
				
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
			return n;
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
	    ArrayList<String> SacarTodosSensores(){
	    		ArrayList<String> sensores = new ArrayList<String>();
			
			try {
				Connection cone = Conexion.Conectar();
				Statement stmt = cone.createStatement();
				String query = "SELECT nombre FROM Sensor;";
				ResultSet rs = 	stmt.executeQuery(query);
				
				while(rs.next()) {
					sensores.add(rs.getString("nombre"));
				}
				
				rs.close();
				stmt.close();
				cone.close();
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
			return sensores;
	    }
	    
	    void ActualizarCamposSensor(String sensor) {
	    	 int id_zona = -1;
	    	try {
				Connection cone = Conexion.Conectar();
				Statement stmt = cone.createStatement();
				String query = "SELECT ID_Sensor,nombre,ip,Tipo,ID_Zona FROM Sensor where nombre = '" + sensor +"';";
				ResultSet rs = 	stmt.executeQuery(query);
				
				while(rs.next()) {
					PK_SensorActual = rs.getInt("ID_Sensor");
					 name_input.setText(rs.getString("nombre"));
					 ip_input.setText(rs.getString("ip"));
					 TipoSensor.setText(rs.getString("Tipo"));
					 id_zona = rs.getInt("ID_Zona");
					 
				}
				
				rs.close();
				stmt.close();
				cone.close();
				System.out.print("Se han sacado las siguientes zonas asociadas a tu empresa " );
				
				ZonaDelSensor.setText(Conexion.obtenerZonadeID(id_zona));
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
	    	
	    }
	    
	    @FXML
	    void initialize() {
	    	
	    	//COnfiguraciones de los menus y botones iniciales
	    	
	    	ArrayList<String> zonas = SacarZonasDeDB();
	    	ArrayList<String> sensores = SacarTodosSensores();
	    	 for (String zona : zonas) {
	    	        MenuItem item = new MenuItem(zona);
	    	        item.setOnAction(event -> {
	    	            MenuZonas.setText(zona);
	    	            // Aquí puedes añadir más lógica que se ejecutará cuando se seleccione este ítem
	    	        });
	    	        MenuZonas.getItems().add(item);
	    	    }
	    	 for (String sensor : sensores) {
	    	        MenuItem item = new MenuItem(sensor);
	    	        item.setOnAction(event -> {
	    	        	MenuSensores.setText(sensor);
	    	        	ActualizarCamposSensor(sensor);
	    	            // Aquí puedes añadir más lógica que se ejecutará cuando se seleccione este ítem
	    	        });
	    	        MenuSensores.getItems().add(item);
	    	    }
	    	 for (String zona : zonas) {
	    	        MenuItem item = new MenuItem(zona);
	    	        item.setOnAction(event -> {
	    	        	ZonaDelSensor.setText(zona);
	    	            // Aquí puedes añadir más lógica que se ejecutará cuando se seleccione este ítem
	    	        });
	    	        ZonaDelSensor.getItems().add(item);
	    	    }
	    	 MenuItem item = new MenuItem("Camara");
 	        item.setOnAction(event -> {
 	        	TipoSensor.setText("Camara");
 	            // Aquí puedes añadir más lógica que se ejecutará cuando se seleccione este ítem
 	        });
 	       TipoSensor.getItems().add(item);
 	      MenuItem item2 = new MenuItem("Controlador");
	        item.setOnAction(event -> {
	        	TipoSensor.setText("Controlador");
	            // Aquí puedes añadir más lógica que se ejecutará cuando se seleccione este ítem
	        });
	       TipoSensor.getItems().add(item2);
	    	 
	    	 
	    }
	
}
