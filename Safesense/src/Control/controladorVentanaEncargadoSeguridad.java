package Control;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Conexion;
import Model.Solicitud;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class controladorVentanaEncargadoSeguridad {

    @FXML
    private Button Boton_Mandar_Mensaje;
    
    @FXML
    private Label label_nombre_encargado;

    @FXML
    private Button Boton_Acceso_Base_Datos;

    @FXML
    private Button Boton_Acceso_Camaras;
    
    @FXML
    private PieChart AccesosRecientesHumedadStats;
    
    @FXML
    private PieChart AlarmasStats;
    
    private String nombreUsuario; // Añade una variable para almacenar el nombre de usuario

    private int ID_Empresa;
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        label_nombre_encargado.setText(nombreUsuario); // Actualiza el label cuando se establece el nombre de usuario
    }
    
    public void setIDEmpresa(int id) {
        this.ID_Empresa = id;
        
    }
    
    void CargarAlarmasDeFoto() {
    
    	ObservableList<PieChart.Data> datos = FXCollections.observableArrayList();
    	//Sacamos de la DB y vamos metiendo dinamicamente
    	
    	try {
			Connection cone = Conexion.Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT Zona.nombre, COUNT(*) FROM Datos JOIN Zona ON Zona.ID_Zona = Datos.ID_Zona WHERE TipoDato = 'Foto' GROUP BY Zona.ID_Zona;";
			ResultSet rs = 	stmt.executeQuery(query);
			while(rs.next()) {
				datos.add(new PieChart.Data(rs.getString("nombre"),rs.getInt("COUNT(*)")));
			}
			rs.close();
			stmt.close();
			cone.close();
	  }catch(Exception e) {
			System.out.println(e.getMessage());
		}
    	
    	datos.forEach(data -> 
    		data.nameProperty().bind(
    			Bindings.concat(
    					data.getName(), " ha habido ", data.pieValueProperty(), " alarmas activadas "
   				)
    		)
    	);
    	
    	AlarmasStats.getData().addAll(datos);
    	
        
    	
    }
     
    
    void CargarAccesosRecientesStats() {
    	
    	ObservableList<PieChart.Data> datos = FXCollections.observableArrayList();
    	//Sacamos de la DB y vamos metiendo dinamicamente
    	
    	try {
			Connection cone = Conexion.Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT Zona.nombre, COUNT(*) FROM Historial JOIN Sensor ON Historial.ID_Sensor = Sensor.ID_Sensor JOIN Zona ON Sensor.ID_Zona = Zona.ID_Zona GROUP BY nombre;";
			ResultSet rs = 	stmt.executeQuery(query);
			while(rs.next()) {
				datos.add(new PieChart.Data(rs.getString("nombre"),rs.getInt("COUNT(*)")));
				
			}
			rs.close();
			stmt.close();
			cone.close();
	  }catch(Exception e) {
			System.out.println(e.getMessage());
		}
    	
    	datos.forEach(data -> 
    		data.nameProperty().bind(
    			Bindings.concat(
    					data.getName(), " amount: ", data.pieValueProperty()
   				)
    		)
    	);
    	
    	AccesosRecientesHumedadStats.getData().addAll(datos);
    }
    
    @FXML 
    void initialize(){
    	CargarAccesosRecientesStats();
    	CargarAlarmasDeFoto();
    	//CargarHumedadStats();
    }
    

    @FXML
    void abrirVentanaBaseDatos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Ventana_Base_Datos.fxml"));
            controladorVentanaBaseDatos control = new controladorVentanaBaseDatos();
            control.setID_Empresa(ID_Empresa);
            loader.setController(control);
            Parent root = loader.load();

            // Obtener la lista de historial de accesos con la información completa, filtrada por ID_Empresa
            List<Solicitud> historialAccesos = Conexion.obtenerHistorialAccesosE(this.ID_Empresa); // Usa el ID_Empresa establecido

            // Pasar la lista al controlador de la ventana de base de datos
            //control.setHistorialAccesos(historialAccesos);

            // Configurar y mostrar la ventana de base de datos
            Stage stage = new Stage();
            stage.setTitle("Base de Datos");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) (event.getSource())).getScene().getWindow());

            // Mostrar la ventana de base de datos
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    @FXML
    void ConfiguracionDeZonas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/VentanaCrearZonas.fxml"));
            ConfiguracionZonas control = new ConfiguracionZonas();
            control.setIDEmpresa(ID_Empresa);
            loader.setController(control);
            Parent root = loader.load();
            
            // Obtiene la ventana actual y la cierra antes de mostrar la nueva ventana
            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.close(); // Cierra la ventana actual

            Stage stage = new Stage();
            stage.setTitle("Configuración de zonas en el sistema"); // Establece el título aquí
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
    void ConfiguracionDeCamaras(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Ventana_Opciones_Sensores.fxml"));
            ControladorOpcionesSensores control = new ControladorOpcionesSensores();
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
    
    @FXML
    void abrirVentanaMandarMensaje(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MensajesPanel1.fxml"));
            ControladorPanel1 control = new ControladorPanel1();
            control.setUsuario(nombreUsuario);
            control.setID_Empresa(ID_Empresa);
            loader.setController(control);
            Parent root = loader.load();
            
            // Obtiene la ventana actual y la cierra antes de mostrar la nueva ventana
            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.close(); // Cierra la ventana actual

            Stage stage = new Stage();
            stage.setTitle("Enviar Mensaje al Personal de Seguridad"); // Establece el título aquí
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
    void abrirventanacamaras(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Ventana_CamarasZonas.fxml"));
            controlCamaras control = new controlCamaras();
            control.setID_Empresa(ID_Empresa);
            
            loader.setController(control);
            Parent root = loader.load();
            
            // Obtiene la ventana actual y la cierra antes de mostrar la nueva ventana
            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.close(); // Cierra la ventana actual

            Stage stage = new Stage();
            stage.setTitle("Cámaras de Seguridad"); // Establece el título aquí
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
    void ConfiguracionUIDs(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Ventana_Configuracion_Tarjetas.fxml"));
            ControladorConfiguracionTarjetas control = new ControladorConfiguracionTarjetas();
            loader.setController(control);
            control.setID_Empresa(ID_Empresa);
            Parent root = loader.load();

            // Obtiene la ventana actual y la cierra antes de mostrar la nueva ventana
            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.close(); // Cierra la ventana actual
            
            Stage stage = new Stage();
            stage.setTitle("Configuración de Tarjetas"); // Establece el título aquí
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
