package Control;
import Model.Conexion;
import Model.Hashear;
import Model.ListaEnlazada;
import Model.Nodo;
import Model.Trabajador;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class controladorVentanaInicioSesion {

    @FXML
    private TextField Label_Usuario;

    @FXML
    private PasswordField Label_Contrasena;
    
    @FXML
    private Label label_mensaje_inicio_sesion;

    private ListaEnlazada<Trabajador> usuarios;

//    @FXML
//    void initialize() {
//        inicializarUsuarios();
//    }
    public static void main(String[] args) {
    	try {
			ListaEnlazada<Trabajador> xd = leerJsonYCrearLista("registroUsuarios.json");
			xd.getPrimero().getDato().getNombre();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
//    private void inicializarUsuarios() {
//    	try {
//            usuarios = leerJsonYCrearLista("registroUsuarios.json");
//    	}catch(Exception e) {
//    		System.out.println(e.getMessage());
//    	}
//    	System.out.println(usuarios);
//    }

    private static ListaEnlazada<Trabajador> leerJsonYCrearLista(String archivoJson) {
        ListaEnlazada<Trabajador> listaUsuarios = new ListaEnlazada<>();
        

        try {
            JsonElement jsonElement = JsonParser.parseReader(new FileReader(archivoJson));
            JsonArray jsonArray = jsonElement.getAsJsonArray();

            for (JsonElement elem : jsonArray) {
                JsonObject jsonObj = elem.getAsJsonObject();
                String nombre = jsonObj.get("nombre").getAsString();
                System.out.println(nombre);
                String apellido = jsonObj.get("apellidos").getAsString();
                String usuario = jsonObj.get("usuario").getAsString();
                String contrasena = jsonObj.get("contraseña").getAsString();
                String rol = jsonObj.get("cargo").getAsString();
                JsonArray ubicacionesJson = jsonObj.getAsJsonArray("Ubicaciones");
                Trabajador usuarioObj;
                if(ubicacionesJson != null) {
                	ArrayList<String> ubicaciones = new ArrayList<>();
                    for (JsonElement ubicacionElement : ubicacionesJson) {
                        ubicaciones.add(ubicacionElement.getAsString());
                    }

                     usuarioObj = new Trabajador(nombre, apellido, usuario, contrasena, rol,ubicaciones);
                }else {
                	 usuarioObj = new Trabajador(nombre, apellido, usuario, contrasena, rol);
                }
          
               
                
                listaUsuarios.insertar(usuarioObj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listaUsuarios;
    }

    
    private String nombreUsuario;
    @FXML
    void IniciarSesion(ActionEvent event) {
        String usuario = Label_Usuario.getText();
        String contraseña = Label_Contrasena.getText();
        

        // Verificar si los campos de usuario o contraseña están vacíos
        if (usuario.isEmpty() || contraseña.isEmpty()) {
            label_mensaje_inicio_sesion.setText("Debe rellenar todos los campos para iniciar sesión.");
            return; // Salir del método para evitar continuar con el inicio de sesión
        }

        // Continuar con el proceso de inicio de sesión si ambos campos están llenos
        contraseña = Hashear.sha256(contraseña); // Hasheamos la contraseña

        boolean usuarioValidoEncontrado = Conexion.login(usuario, contraseña);
        
        String rolUsuario = Conexion.getRolUsuario(usuario);
        int ID_Empresa = Conexion.getEmpresaUsuario(usuario);
//        ListaEnlazada<Trabajador> usuarios = leerJsonYCrearLista("registroUsuarios.json");
//        Nodo<Trabajador> actual = usuarios.getPrimero();
//        while (actual != null) {
//            Trabajador usuarioObj = actual.getDato();
//            if (usuarioObj.getUsuario().equals(usuario) && usuarioObj.getContraseña().equals(contraseña)) {
//                usuarioValidoEncontrado = true;
//                rolUsuario = usuarioObj.getCargo();
//                
//                break;
//            }
//            actual = actual.getEnlace();
//        }
        
        if (usuarioValidoEncontrado) {
        	 nombreUsuario = usuario;  // Asignar el nombre de usuario
            System.out.println("Inicio de sesión realizado con éxito");

            try {

                // Abrir ventana según el rol del usuario
                abrirVentanaSegunRol(nombreUsuario, ID_Empresa, rolUsuario, event);
                
            } catch (IOException e) {
                e.printStackTrace();
                
            }
        } else {
            System.out.println("Usuario o contraseña no válidos");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Ventana_Denegación_Inicio_Sesion.fxml"));
                controladorVentanaDenegacionInicioSesion control = new controladorVentanaDenegacionInicioSesion();
                loader.setController(control);
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Error");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) (event.getSource())).getScene().getWindow());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    
    private void abrirVentanaSegunRol(String username,int ID_Empresa,String rol, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root;
        Stage stage = new Stage();
        

//        // Obtiene la ventana actual y la cierra antes de mostrar la nueva ventana
//        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stageActual.close(); // Cierra la ventana actual

        if (rol.equals("Personal de seguridad")) {
            loader.setLocation(getClass().getResource("/View/Ventana_Personal_Seguridad.fxml"));
            controladorVentanaPersonalSeguridad control = new controladorVentanaPersonalSeguridad(ID_Empresa);
            stage.setTitle("Ventana de Vigilante");
            loader.setController(control);
            root = loader.load();
            control.setNombreUsuario(nombreUsuario);
            
            // Establece el nombre de usuario antes de mostrar la ventana
           // control.setUsuarios(nombreUsuario); // Establece el nombre de usuario antes de mostrar la ventana
        } else if (rol.equals("Encargado de vigilancia")) {
            loader.setLocation(getClass().getResource("/View/Ventada_Encargado_Seguridad.fxml"));
            controladorVentanaEncargadoSeguridad control = new controladorVentanaEncargadoSeguridad();
            loader.setController(control);
            stage.setTitle("Ventana de Encargado de Seguridad");
            root = loader.load();
            control.setNombreUsuario(nombreUsuario);
            control.setIDEmpresa(ID_Empresa);// Establece el nombre de usuario antes de mostrar la ventana
        } else {
        	loader.setLocation(getClass().getResource("/View/Ventana_Trabajador.fxml"));
            controladorVentanaTrabajador control = new controladorVentanaTrabajador();
            loader.setController(control);
            control.setUsuarios(nombreUsuario); // Configurar el nombre de usuario
            root = loader.load();
            stage.setTitle("Ventana del Trabajador");
            control.setNombreUsuario(nombreUsuario);
            control.setIDEmpresa(ID_Empresa);// Establece el nombre de usuario antes de mostrar la ventana
        }

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
    }


}