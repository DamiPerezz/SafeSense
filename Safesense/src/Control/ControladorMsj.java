package Control;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Model.Conexion;
import Model.EscuchaMensajes;
import Model.GestorMensajes;
import View.CajitaMensaje;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

	public class ControladorMsj {
		
		private String UsuarioActual;
		private int UsId;
		private String DestinatorioActual;
		private int DestId;
		private EscuchaMensajes HiloEscucha;
		
		public void cargarConversacion() {
			System.out.println(UsuarioActual + " " + UsId + " - " + DestinatorioActual + " " + DestId);
			System.out.println("Cargando conversaciones");
			
			int IDTemp;
			String msjTemp;
			//Ojo pq esta funcion es improtante
			try {
				Connection cone = Conexion.Conectar();
				Statement stmt = cone.createStatement();
				String query = "SELECT emisor_id,mensaje FROM Chatea WHERE (emisor_id = " + UsId + " AND receptor = " + DestId + ") OR (emisor_id = " + DestId + " AND receptor = " + UsId + ") ORDER BY ID_Chat; ";
				ResultSet rs = 	stmt.executeQuery(query);
				while(rs.next()) {
					IDTemp = rs.getInt("emisor_id");
					msjTemp = rs.getString("mensaje");
					System.out.println(IDTemp + " " + msjTemp);
					if(IDTemp == UsId) {
						AñadirCajitaDrch(msjTemp);
					}else {
						AñadirCajitaIzq(msjTemp);
					}
					//comprobar si el emisor es el usuario uno lo imprima por la derecha y sino por la izquierda
				}
				rs.close();
				stmt.close();
				cone.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		
		
		private void ajustarAlturaVBox() {
		    double alturaTotal = vboxMensajes.getChildren().stream()
		        .mapToDouble(node -> node.getBoundsInParent().getHeight())
		        .sum();
		    vboxMensajes.setPrefHeight(alturaTotal);
		    System.out.println(alturaTotal);
		    vboxMensajes.applyCss();
		    vboxMensajes.layout();
		}
		
		public void AñadirCajitaIzq(String Texto) {
			System.out.println("Imprimiendo" + Texto + " a la izquierda");
			CajitaMensaje mensaje = new CajitaMensaje(Texto, false);
		    Platform.runLater(() -> {
		        vboxMensajes.getChildren().add(mensaje);
		        ajustarAlturaVBox();
		        vboxMensajes.applyCss();
		        vboxMensajes.layout();
		        scrollPane.setVvalue(1.0); // Desplazar al final
		    });
			
		}
		public void AñadirCajitaDrch(String Texto) {
			System.out.println("Imprimiendo" + Texto + " a la derecha");
			CajitaMensaje mensaje = new CajitaMensaje(Texto, true);
		    Platform.runLater(() -> {
		        vboxMensajes.getChildren().add(mensaje);
		        ajustarAlturaVBox();
		        vboxMensajes.applyCss();
		        vboxMensajes.layout();
		        scrollPane.setVvalue(1.0); // Desplazar al final
		    });
			
			
		}
	    public void ConfigurarDatos(String usuarioActual,  String destinatorioActual) {
			UsuarioActual = usuarioActual;
			UsId = GestorMensajes.SacarIdDeUsuario(usuarioActual);
			DestinatorioActual = destinatorioActual;
			DestId = GestorMensajes.SacarIdDeUsuario(destinatorioActual);
			cargarConversacion();
			//Lanzar hilo de escucha
			HiloEscucha = new EscuchaMensajes(UsuarioActual,DestinatorioActual,this);
			HiloEscucha.start();
			
			
		}
		
	    
	    public void abrirVentana() {
	        // Configurar y mostrar la ventana de chat aquí (puedes usar FXMLLoader si es una aplicación JavaFX)

	        // Iniciar hilo de escucha
	        HiloEscucha = new EscuchaMensajes(UsuarioActual, DestinatorioActual, this);
	        HiloEscucha.iniciar();
	    }

	    public void cerrarVentana() {
	        // Detener hilo de escucha cuando se cierra la ventana
	        if (HiloEscucha != null) {
	            HiloEscucha.detener();
	         //   System.out.println("se ejecuta detenerhilo");
	        }
	    }
	    
	    @FXML
	    private TextArea InputTexto;

	    @FXML
	    private Button BotonEnviarMsj;
	    
	    @FXML
	    private VBox vboxMensajes;
	    
	    @FXML
	    private ScrollPane scrollPane;
	    
	    @FXML
	    private void EnviarMensaje(ActionEvent event) {
	    	System.out.println("Entra");
	    	System.out.println(UsuarioActual + " " + UsId + ", " + DestinatorioActual + " " + DestId);
	    	 String mensaje = InputTexto.getText();
	    	    if (!mensaje.isEmpty()) {
	    	    	GestorMensajes.EnviarMensajePorID(UsId,DestId,mensaje);
	    	    	AñadirCajitaDrch(mensaje);
	    	        InputTexto.clear();
	    	        
	    	        // Aquí también podrías agregar el código para enviar el mensaje a través de tu backend
	    	    }
	    	
	    }
	    
	    
	    

}

