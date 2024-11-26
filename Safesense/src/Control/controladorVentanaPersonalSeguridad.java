package Control;


//import Model.Mensaje;

import Model.Conexion;
import Model.Solicitud;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class controladorVentanaPersonalSeguridad {
	
	class Par{
    	private String usuario;
    	private int id;
    	
    	Par(String u, int i){
    		this.usuario = u;
    		this.id = i;
    	}
    	public String GetUser() {
    		return this.usuario;
    	}
    	public int GetId() {
    		return this.id;
    	}
    	
    	
    }
	

    @FXML
    private Label LabelSolicitudesAcceso;
    
    @FXML
    private Label label_nombre_vigilante;

    @FXML
    private VBox vboxSolicitudes;
    
    @FXML
    private TextField Texto_Mensaje3;

    @FXML
    private TextField Texto_Titulo3;
    
    @FXML
    private ScrollPane ResultadosSeguridad;
    
    @FXML 
    private MenuButton Trabajadores;
    
    @FXML ListView<String> ListaAccesos;
    
    private String nombreUsuario; // Añade una variable para almacenar el nombre de usuario

    private int ID_Empresa;
    
    private ArrayList<Par> usuarios;
    
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        label_nombre_vigilante.setText(nombreUsuario); // Actualiza el label cuando se establece el nombre de usuario
    }
    public void setIDEmpresa(int id) {
        this.ID_Empresa = id;
        System.out.println("ID COnfigurado a " + this.ID_Empresa);
        
    }
    
    public controladorVentanaPersonalSeguridad(int ID_EMpresa) {
    	this.ID_Empresa = ID_EMpresa;
    }
    
    
    

    @FXML
    void initialize() {
        mostrarSolicitudesDeAcceso();
        CargarBotonUsuarios();
    }

    void CargarBotonUsuarios() {
    	ArrayList<Par> usuario = new ArrayList<Par>();
    	
    	try {
	        Connection cone = Conexion.Conectar();
	        String query = "SELECT username,ID_Usuario FROM Usuario WHERE ID_Empresa = " + this.ID_Empresa + ";";
	        try (PreparedStatement stmt = cone.prepareStatement(query)) {
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                usuario.add(new Par(rs.getString("username"),rs.getInt("ID_Usuario")));
	            }
	            
	        }
	        cone.close();
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
    	
    	this.usuarios = usuario;
    	System.out.println(this.ID_Empresa);
    	System.out.println(usuarios.size());
    	 for (Par u : usuarios) {
             System.out.println("Añadiendo " + u.GetUser());
             MenuItem menuItem = new MenuItem(u.GetUser());
             menuItem.setOnAction(event -> {
	                MenuItem source = (MenuItem) event.getSource();
	                Trabajadores.setText(source.getText());
	                ActualizarListaAccesosAUsuario(u.GetId());
	            });
             Trabajadores.getItems().add(menuItem);	
         }
    }
    
    public void ActualizarListaAccesosAUsuario(int ID) {
    	ArrayList<String> accesos = new ArrayList<String>();
    	try {
	        Connection cone = Conexion.Conectar();
	        String query = "SELECT nombre FROM Zona JOIN Acceso ON Acceso.ID_Zona = Zona.ID_Zona JOIN Solicita ON Solicita.ID_Acceso = Acceso.ID_Acceso WHERE ID_Usuario = "+ ID+ " AND Estado = 'aceptada';";
	        try (PreparedStatement stmt = cone.prepareStatement(query)) {
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                accesos.add(rs.getString("nombre"));
	            }
	        }
	        cone.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
    	 ObservableList<String> observableList = FXCollections.observableArrayList(accesos);
    	 ListaAccesos.setItems(observableList);
    }
    
    public void mostrarSolicitudesDeAcceso() {
       // Conexion conexion = new Conexion(); // Asegúrate de inicializar esto adecuadamente, posiblemente pasándolo como parámetro o usando un singleton
        List<Solicitud> detallesSolicitudes = Conexion.obtenerDetallesSolicitudesAcceso();
        
        for (Solicitud detalle : detallesSolicitudes) {
            agregarSolicitudAVBox(detalle);
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
    

    private void agregarSolicitudAVBox(Solicitud detalle) {
        VBox contenedorSolicitud = new VBox(5); // Espaciado vertical entre elementos del VBox
        contenedorSolicitud.setAlignment(Pos.CENTER_LEFT); 

        Label idAccesoLabel = new Label("ID Acceso: " + detalle.getIdAcceso());
        Label estadoLabel = new Label("Estado: " + detalle.getEstado());
        Label idZonaLabel = new Label("ID Zona: " + detalle.getUbicacion());
        Label idUsuarioLabel = new Label("Usuario: " + detalle.getTrabajador());

        Button btnAceptar = new Button("Aceptar");
        Button btnDenegar = new Button("Denegar");

        // Evento para el botón Aceptar
        btnAceptar.setOnAction(event -> {
            Conexion.actualizarEstadoAcceso(detalle.getIdAcceso(), "aceptada");
            actualizarEstiloBoton(btnAceptar, "aceptada");
            vboxSolicitudes.getChildren().remove(contenedorSolicitud); // Elimina esta solicitud de la UI
        });

        // Evento para el botón Denegar
        btnDenegar.setOnAction(event -> {
            Conexion.actualizarEstadoAcceso(detalle.getIdAcceso(), "denegada");
            actualizarEstiloBoton(btnDenegar, "denegada");
            vboxSolicitudes.getChildren().remove(contenedorSolicitud); // Elimina esta solicitud de la UI
        });


        HBox botonesContainer = new HBox(10, btnAceptar, btnDenegar); // Espaciado de 10 píxeles entre botones
        botonesContainer.setAlignment(Pos.CENTER_LEFT);

        contenedorSolicitud.getChildren().addAll(idAccesoLabel, estadoLabel, idZonaLabel, idUsuarioLabel, botonesContainer);
        vboxSolicitudes.getChildren().add(contenedorSolicitud);
       
    }
    // Método para actualizar el estilo del botón
    private void actualizarEstiloBoton(Button boton, String estado) {
        if (estado.equals("aceptada")) {
            boton.setStyle("-fx-background-color: #00FF00;"); // Verde para aceptada
        } else if (estado.equals("denegada")) {
            boton.setStyle("-fx-background-color: #FF0000;"); // Rojo para denegada
        }
    }
    
   


    


}

