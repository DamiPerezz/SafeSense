
package Control;
import Model.Conexion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class controladorVentanaTrabajador {

//    @FXML
//    private RadioButton boton_Solicitud_Entrada;
//
//    @FXML
//    private RadioButton boton_solicitud_Zona1;
//
//    @FXML
//    private RadioButton boton_solicitud_Zona3;
//
//    @FXML
//    private RadioButton boton_solicitud_Zona4;
//
//    @FXML
//    private RadioButton boton_solicitud_Zona2;
//
//    @FXML
//    private RadioButton boton_solicitud_Zona5;

    @FXML
    private Button Boton_Solicitud_Acceso;
    
    @FXML
    private Label label_mensaje_trabajador;
    
    @FXML
    private Label label_elegir_zona;
    
    @FXML
    private Label label_nombre_trabajador;
    
    @FXML
    private ScrollPane scrollPane;
    
    @FXML
    private VBox vbox;
    
    private String nombreUsuario; // Añade una variable para almacenar el nombre de usuario

    private int ID_Empresa;
    
    
    
    //ScrollPane scrollPane = new ScrollPane();
    //VBox vbox = new VBox(); // Esto contendrá tus RadioButtons
    
    
    boolean ExisteAccesoAceptado(String Zona){
    	int id = Conexion.obtenerIdZona(Zona);
    	int id_user = Conexion.obtenerIdUsuario(nombreUsuario);
    	boolean acceso = false;
    	try {
			Connection cone = Conexion.Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT * FROM Zona JOIN Acceso ON Acceso.ID_Zona = Zona.ID_Zona JOIN Solicita ON Solicita.ID_Acceso = Acceso.ID_Acceso WHERE ID_Usuario = "+ id_user +" AND Estado = 'aceptada' AND Zona.ID_Zona =" + id + ";";
			ResultSet rs = 	stmt.executeQuery(query);
			
			if(rs.next()) {
				acceso = true;
			}
			
			rs.close();
			stmt.close();
			cone.close();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
    	
    	return acceso;
    }
    
    @FXML
    void initialize() {  	
//    	System.out.println("ScrollPane is: " + scrollPane); // Esto debería no ser null.
//        if (scrollPane == null) {
//            throw new IllegalStateException("ScrollPane no ha sido inicializado. Revisa el FXML y las anotaciones @FXML.");
//        }
    	System.out.println("ID_Empresa cuando se ejecuta initialize: "+ID_Empresa);//Quitar cuando Esté arreglado (aquí imprima el ID Bien)
    	setIDEmpresa(Conexion.getEmpresaUsuario(nombreUsuario));
    	System.out.println("ID_Empresa tras setearlo manualmente: "+ID_Empresa);
    	
    	ArrayList<String> zonas = SacarZonasDeDB();
        for (String zona : zonas) {
            RadioButton radioButton = new RadioButton(zona);
            radioButton.setOnAction(event -> {
            	System.out.println("Seleccionada la zona: " + zona);
            	if(ExisteAccesoAceptado(zona)) {
            		System.out.println("Ya existe acceso");
            		mostrarAlerta("¡OJO!", "Ya tienes acceso a esta zona");
            		
            	}else {
            		System.out.println("Creando solicitud de acceso a la zona: " + zona);
            		//Hacer acceso
            		ArrayList<Integer> cutre = new ArrayList<Integer>();
            		cutre.add(Conexion.obtenerIdZona(zona));
            		 // Realiza la solicitud de acceso con los IDs de zonas seleccionadas
                    boolean solicitudRegistrada = Conexion.registroSolicitud(Conexion.obtenerIdUsuario(nombreUsuario),cutre);
                    // Actualiza el mensaje al usuario basado en el éxito de la operación
                    if(solicitudRegistrada) {
                    	System.out.println("Solicitud regsitrada");
                    	mostrarAlerta("Éxito", "Solicitud regsitrada, en breve te responderemos.");
                        label_mensaje_trabajador.setText("Solicitud registrada con éxito.");
                        
                    } else {
                        label_mensaje_trabajador.setText("Hubo un problema al registrar la solicitud.");
                        mostrarAlerta("Error", "Hubo un problema al registrar la solicitud.");
                    }
            	}
                
            });
            vbox.getChildren().add(radioButton);
        }
        
        scrollPane.setContent(vbox); // Asegúrate de que este VBox es el contenido de tu ScrollPane
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
				//System.out.println("a: " + rs.getString("nombre"));
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
    
    
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        label_nombre_trabajador.setText(nombreUsuario); // Actualiza el label cuando se establece el nombre de usuario
    }
    
    public void setIDEmpresa(int id) {
        this.ID_Empresa = id;
        
    }
    
    

   // private String nombreUsuario;

    public void setUsuarios(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    
    
    @FXML
    void solicitarAcceso(ActionEvent event) {
        int idUsuario = Conexion.obtenerIdUsuario(nombreUsuario);
        ArrayList<Integer> idsZonasSeleccionadas = new ArrayList<>();

     // Recorrer todos los hijos de vbox 
        for (Node node : vbox.getChildren()) {
            // Verifica si el nodo es una instancia de RadioButton y está seleccionado
            if (node instanceof RadioButton && ((RadioButton) node).isSelected()) {
            	
                // Castea el nodo a RadioButton para poder acceder a sus métodos
                RadioButton radioButton = (RadioButton) node;
                
                // Añade la zona a la lista
                int zonaId = Conexion.obtenerIdZona(radioButton.getText());
                idsZonasSeleccionadas.add(zonaId);
            }
        }
//        // Convirtiendo nombres de zonas a IDs de zonas
//        if (boton_Solicitud_Entrada.isSelected()) idsZonasSeleccionadas.add(Conexion.obtenerIdZona("Entrada"));
//        if (boton_solicitud_Zona1.isSelected()) idsZonasSeleccionadas.add(Conexion.obtenerIdZona("Zona 1"));
//        if (boton_solicitud_Zona2.isSelected()) idsZonasSeleccionadas.add(Conexion.obtenerIdZona("Zona 2"));
//        if (boton_solicitud_Zona3.isSelected()) idsZonasSeleccionadas.add(Conexion.obtenerIdZona("Zona 3"));
//        if (boton_solicitud_Zona4.isSelected()) idsZonasSeleccionadas.add(Conexion.obtenerIdZona("Zona 4"));
//        if (boton_solicitud_Zona5.isSelected()) idsZonasSeleccionadas.add(Conexion.obtenerIdZona("Zona 5"));

        
        
        // Realiza la solicitud de acceso con los IDs de zonas seleccionadas
        boolean solicitudRegistrada = Conexion.registroSolicitud(idUsuario, idsZonasSeleccionadas);

        // Actualiza el mensaje al usuario basado en el éxito de la operación
        if(solicitudRegistrada) {
            label_mensaje_trabajador.setText("Solicitud registrada con éxito.");
        } else {
            label_mensaje_trabajador.setText("Hubo un problema al registrar la solicitud.");
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}