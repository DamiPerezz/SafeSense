package Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.PreparedStatement;

public class GestorMensajes extends Thread{

	//1 Prueba Insertar Mensaje
	
	
	public static void ImprimirConversacion(String usuario1,String usuario2) {
		int us1 = SacarIdDeUsuario(usuario1);
		int us2 = SacarIdDeUsuario(usuario2);
		try {
			Connection cone = Conexion.Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT emisor_id,mensaje FROM Chatea WHERE (emisor_id = " + us1 + " AND receptor = " + us2 + ") OR (emisor_id = " + us2 + " AND receptor = " + us1 + ") ORDER BY ID_Chat; ";
			ResultSet rs = 	stmt.executeQuery(query);
			while(rs.next()) {
				System.out.println(rs.getString("emisor_id") + " ->" + rs.getString("mensaje"));
				//comprobar si el emisor es el usuario uno lo imprima por la derecha y sino por la izquierda
			}
			rs.close();
			stmt.close();
			cone.close();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}	
	public static int SacarIdDeUsuario(String username) {
		int id = -1;
		try {
			Connection cone = Conexion.Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT ID_Usuario FROM Usuario WHERE username = '" + username + "';";
			ResultSet rs = 	stmt.executeQuery(query);
			
			if(rs.next()) {
				 id = rs.getInt("ID_Usuario");
				 System.out.println(id);
			}
			rs.close();
			stmt.close();
			cone.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage() + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return id;	
	}
	
	public static String SacarUsuarioDeId(int ID) {
		String username = "";
		try {
			Connection cone = Conexion.Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT username FROM Usuario WHERE ID_Usuario = '" + ID + "';";
			ResultSet rs = 	stmt.executeQuery(query);
			
			if(rs.next()) {
				 username = rs.getString("username");
			}
			rs.close();
			stmt.close();
			cone.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return username;	
	}
	public static void EnviarMensaje(String emisorUsername, String receptorUsername, String mensaje) {
		Connection cone = null;
        PreparedStatement pstmt = null;
        int id_emisor = SacarIdDeUsuario(emisorUsername);
        int id_receptor = SacarIdDeUsuario(receptorUsername);
        
        if((id_emisor != -1) && (id_receptor != -1)) {
        	try {
                cone = Conexion.Conectar();
                String query = "INSERT INTO Chatea(emisor_id, receptor, mensaje, fecha) VALUES (?, ?, ?, ?)";
                pstmt = cone.prepareStatement(query);
                // Establecer los valores de los parámetros
                pstmt.setInt(1, id_emisor); // emisor_id
                pstmt.setInt(2, id_receptor); // receptor
                pstmt.setString(3, mensaje); // mensaje
                pstmt.setDate(4, Date.valueOf(LocalDate.now())); // fecha
                
                // Ejecutar la consulta
                pstmt.executeUpdate();
                System.out.println("Mensaje enviado con exito");
                pstmt.close();
                cone.close();
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }else{
        	System.out.println("Error en los usuarios");
        }
        
        
		
	}
	public static void EnviarMensajePorID(int emisorId, int receptorId, String mensaje) {
		Connection cone = null;
        PreparedStatement pstmt = null;
        
        if((emisorId != -1) && (receptorId != -1)) {
        	try {
                cone = Conexion.Conectar();
                String query = "INSERT INTO Chatea(emisor_id, receptor, mensaje, fecha) VALUES (?, ?, ?, ?)";
                pstmt = cone.prepareStatement(query);
                // Establecer los valores de los parámetros
                pstmt.setInt(1, emisorId); // emisor_id
                pstmt.setInt(2, receptorId); // receptor
                pstmt.setString(3, mensaje); // mensaje
                pstmt.setDate(4, Date.valueOf(LocalDate.now())); // fecha
                
                // Ejecutar la consulta
                pstmt.executeUpdate();
                System.out.println("Mensaje enviado con exito");
                pstmt.close();
                cone.close();
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }else{
        	System.out.println("Error en los usuarios");
        }
        
        
		
	}
//	public static void main(String[] args) {
//		String oyente = "Paquito123";
//		String emisor = "dpmSeguridad";
//		EscuchaMensajes listener = new EscuchaMensajes(oyente,emisor);
//		listener.start(); 
//		try {
//			Thread.sleep(200);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		EnviarMensaje(emisor, oyente,"Prueba escucha 3");
//		
//	}
	

	
	
}
