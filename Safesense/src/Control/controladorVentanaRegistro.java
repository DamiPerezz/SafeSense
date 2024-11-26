package Control;
import Model.Hashear;
import Model.Conexion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class controladorVentanaRegistro {

	    private String rolSeleccionado = "Rol no seleccionado";

	    @FXML
	    private Button Boton_Registrar;

	    @FXML
	    private TextField Label_Nombre_Usuario;

	    @FXML
	    private TextField Label_Contraseña;

	    @FXML
	    private TextField Label_Apellido;

	    @FXML
	    private TextField Label_Empresa;

	    @FXML
	    private TextField Label_nombre;

	    @FXML
	    private MenuButton boton_roles;

	    @FXML
	    private MenuItem boton_trabajador;

	    @FXML
	    private MenuItem boton_personal_seguridad;

	    @FXML
	    private MenuItem boton_encargado_vigilancia;
	    
	    @FXML
	    private Label label_mensaje_registro;
	    
	    @FXML
	    void initialize() {
	        // Configurar manejadores de eventos para los MenuItems
	        boton_trabajador.setOnAction(e -> onMenuItemSelected(boton_trabajador));
	        boton_personal_seguridad.setOnAction(e -> onMenuItemSelected(boton_personal_seguridad));
	        boton_encargado_vigilancia.setOnAction(e -> onMenuItemSelected(boton_encargado_vigilancia));
	    }

	    @FXML
	    void RegistrarUsuario(ActionEvent event) {
	        // Obtener datos de los campos de texto
	        String nombre = Label_nombre.getText();
	        String apellido = Label_Apellido.getText();
	        String usuario = Label_Nombre_Usuario.getText();
	        String contraseña = Label_Contraseña.getText();
	        String empresa = Label_Empresa.getText();
	        
	        // Verificar si alguno de los campos está vacío
	        if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() || contraseña.isEmpty() || empresa.isEmpty() || rolSeleccionado.equals("Rol no seleccionado")) {
	            label_mensaje_registro.setText("Debe rellenar todos los apartados para poder realizar el registro.");
	            return; // Salir del método para evitar registrar un usuario incompleto
	        }

	        
	        contraseña = Hashear.sha256(contraseña); // Hasheamos la contraseña
	        // Obtener el rol seleccionado
	        String rolSeleccionado = obtenerRolSeleccionado();
	        
	        int id_empresa = Conexion.registroEmpresa ( empresa);

	        // Registrar usuario en la base de datos remota
	        boolean usuarioRegistrado = Conexion.registroUsuario(nombre, apellido, usuario, contraseña, rolSeleccionado, id_empresa);

	        if (usuarioRegistrado) {
	            label_mensaje_registro.setText("Usuario registrado exitosamente.");
	            // Restablecer el rol seleccionado
	            this.rolSeleccionado = "Rol no seleccionado";
	            
	            try {
	      			
	      			//Llamamos al codigo que hemos hecho en FXML con la funcion loader para tener la vista
	      	
	      			FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/View/Ventana_Confirmación_Registro.fxml"));
	      			
	      			controladorVentanaDenegacionInicioSesion control2 = new controladorVentanaDenegacionInicioSesion(); //Llamamos al controlador2 
	      			loader3.setController(control2); // Relacionamos la vista2 con el controlador2
	      			
	      			Parent root2 = loader3.load(); //Con esto llamamos a la funcion load de loader
	      			
	      			Stage stage = new Stage ();
	      			stage.setTitle("Regstro de Nuevo Usuario");
	      			stage.setScene(new Scene(root2)); //LLamamos a lo que hemos cargado con la funcion load
	      			
	      			//Estas dos lineas son para impedir que despues de que se habra una ventana con el texto introducido
	      			//Impedir que se abran mas ventanas al darle al boton mostrar
	      			
	      			stage.initModality(Modality.WINDOW_MODAL);
	      			stage.initOwner(((Node) (event.getSource())).getScene().getWindow());
	      			
	      			stage.show();
	      		} catch(Exception e) {
	      			e.printStackTrace();
	      		}

	            // Limpiar los campos después del registro
	            Label_nombre.clear();
	            Label_Apellido.clear();
	            Label_Nombre_Usuario.clear();
	            
            Label_Contraseña.clear();
    } else {
        label_mensaje_registro.setText("Error al registrar el usuario.");
    }
    }
  
    private void onMenuItemSelected(MenuItem menuItem) {
        rolSeleccionado = menuItem.getText();
    }

    private String obtenerRolSeleccionado() {
        return rolSeleccionado;
    }
      

}


