package Control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Conexion;
import Model.GestorMensajes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ControladorPanel1 {
	
	String usuarioActual;

    @FXML
    private Button nuevoMensajeButton;

    @FXML
    private VBox chatList;
    
    private int ID_Empresa;
    private Thread hiloDeEscucha; // Hilo para escuchar los mensajes
    private Stage chatStage; // Guardar la referencia del Stage del chat
    
    public void setID_Empresa(int iD_Empresa) {
		ID_Empresa = iD_Empresa;
	}
    
	@FXML
    private void NuevoMensaje() {
        try {
            // Cargar la nueva ventana de conversación
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/nuevoChat.fxml"));
            Parent root = loader.load();
            ControladorNuevoChat controlador = loader.getController();
            controlador.setUsuarioActual(usuarioActual);
            controlador.setID_Empresa(ID_Empresa);
            
            Stage stage = new Stage();
            stage.setTitle("Nueva Conversación");
            stage.setScene(new Scene(root));
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
           
        }
    }
    public void setUsuario(String username) {
    	this.usuarioActual = username;
    }
    
	public static ArrayList <String> SacarContactosDeMisMensajes(String usuario1) {
		//int us1 = GestorMensajes.SacarIdDeUsuario(usuario1);
		ArrayList <String> usuarios = new ArrayList <String> ();
		int us1 = GestorMensajes.SacarIdDeUsuario(usuario1);

		System.out.println("Entradno a ejecutar query");
		try {
			Connection cone = Conexion.Conectar();
			String query = "SELECT emisor.username AS emisor_username, receptor.username AS receptor_username " +
		               "FROM Chatea " +
		               "JOIN Usuario AS emisor ON Chatea.emisor_id = emisor.ID_Usuario " +
		               "JOIN Usuario AS receptor ON Chatea.receptor = receptor.ID_Usuario " +
		               "WHERE Chatea.emisor_id = ? OR Chatea.receptor = ? " +
		               "ORDER BY Chatea.ID_Chat;";
			PreparedStatement pstmt = cone.prepareStatement(query);
			pstmt.setInt(1, us1);
			pstmt.setInt(2, us1);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				//usuarios.add(GestorMensajes.SacarUsuarioDeId(rs.getInt("emisor_id")));
				String emisor = rs.getString("emisor_username");
				String receptor = rs.getString("receptor_username");
				System.out.println(emisor + " "+ receptor);
				if(usuario1.equals(emisor) == true) {
					if(usuarios.contains(receptor)== false) {
						usuarios.add(receptor);
					}
				}else {
					if(usuarios.contains(emisor)== false) {
						usuarios.add(emisor);
					}
				}
			}
			rs.close();
			pstmt.close();
			cone.close();
		}catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return usuarios;
		 
	}

//	public static void main(String[]args) {
//	        
//	    	ArrayList <Integer> usuarios = SacarIdDeMisMensajes("dpmSeguridad");
//	    	for(Integer u : usuarios ) {
//	    		System.out.println(u);
//	    	}
//	    }
//	
	
	private void cargarChats() {
        // Simula obtener conversaciones de un método estático de GestorMensajes
        
		ArrayList <String> usuarios = SacarContactosDeMisMensajes(usuarioActual);
		
        for (String usuario : usuarios) {
            HBox hboxChat = new HBox(10);
            hboxChat.setStyle("-fx-background-color: #CCCCCC; -fx-padding: 10;");

            
            Label lblNombreUsuario = new Label(usuario);
            lblNombreUsuario.setFont(new Font("Arial", 16));

            Label lblUltimoMensaje = new Label("Último mensaje...");
            lblUltimoMensaje.setFont(new Font("Arial", 12));

            Label lblFecha = new Label("22/03/24"); // Poner aquí la fecha real del último mensaje

            Button btnAbrirChat = new Button("Abrir Chat");
            
            btnAbrirChat.setOnAction(event -> {
                try {
                    // Cargar la ventana de chat
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MensajesView.fxml"));
                    Parent root = loader.load();
                    
                    // Si necesitas pasar datos al controlador, lo haces así:
                    ControladorMsj controladorChat = loader.getController();
                    
                    controladorChat.ConfigurarDatos(usuarioActual,usuario); // Ejemplo de método para configurar datos
                    
                    if (chatStage != null) {
                        chatStage.close(); // Cierra la ventana anterior si estaba abierta
                    }
                    
                    chatStage = new Stage();
                    chatStage.setTitle("Chat con " + usuario);
                    chatStage.setScene(new Scene(root));
                    
                   chatStage.setOnCloseRequest(e -> controladorChat.cerrarVentana());
                   	
                   
                   

                    
                    chatStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
           

            hboxChat.getChildren().addAll(lblNombreUsuario, lblUltimoMensaje, lblFecha, btnAbrirChat);
            chatList.getChildren().add(hboxChat);
        }
    }
	
	
	 


	@FXML
	public void initialize() {
		cargarChats();
	}
	
}

    
    
