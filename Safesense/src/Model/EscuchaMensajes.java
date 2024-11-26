package Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Control.ControladorMsj;
import javafx.application.Platform;


public class EscuchaMensajes extends Thread{

	int msjRecv;
    int acumRecv;
    String oyente;
    String emisor;
    ControladorMsj Papa;
    
    // Constructor modificado para incluir los nombres de usuario
    public EscuchaMensajes(String oyente, String emisor, ControladorMsj controladorPapa) {
        super();
        this.oyente = oyente;
        this.emisor = emisor;
        this.msjRecv = SacarNMsjsConv(oyente, emisor);
        this.acumRecv = SacarUltNMsjsIndex(oyente, emisor);
        this.Papa = controladorPapa;
    }
	public void EscucharMensajes(String oyente, String emisor) {
		try {
	        if (SacarNMsjsConv(oyente, emisor) > msjRecv) {
	            ImprimirAPartirDeIndex(oyente, emisor, this.acumRecv);
	        } else {
	            Thread.sleep(200);
	        }
	    } catch (InterruptedException e) {
	        // Aquí manejamos la interrupción durante el sueño
	        //System.out.println("Hilo de escucha interrumpido durante la espera.");
	        Thread.currentThread().interrupt(); // Restaurar el estado interrumpido
	    }
	}
	
	public static int SacarNMsjsConv(String usuario1,String usuario2) {
		int us1 = GestorMensajes.SacarIdDeUsuario(usuario1);
		int us2 = GestorMensajes.SacarIdDeUsuario(usuario2);
		int cont = 0;
		try {
			Connection cone = Conexion.Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT emisor_id FROM Chatea WHERE emisor_id = " + us2 + " AND receptor = " + us1 + " ORDER BY ID_Chat; ";
			ResultSet rs = 	stmt.executeQuery(query);
			while(rs.next()) {
				rs.getString("emisor_id");
				cont++;
			}
			rs.close();
			stmt.close();
			cone.close();
			//System.out.println(cont + " mensajes enviados de" + us2 + " a " + us1);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return cont;
	}
	public static int SacarUltNMsjsIndex(String usuario1,String usuario2) {
        int us1 = GestorMensajes.SacarIdDeUsuario(usuario1);
        int us2 = GestorMensajes.SacarIdDeUsuario(usuario2);
        int acum = -1;
        try {
            Connection cone = Conexion.Conectar();
            Statement stmt = cone.createStatement();
            String query = "SELECT ID_Chat FROM Chatea WHERE (emisor_id = " + us1 + " AND receptor = " + us2 + ") OR (emisor_id = " + us2 + " AND receptor = " + us1 + ") ORDER BY ID_Chat; ";
            ResultSet rs =     stmt.executeQuery(query);
            while(rs.next()) {
                acum = rs.getInt("ID_Chat");
            }
            rs.close();
            stmt.close();
            cone.close();
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return acum;
    }
	public void ImprimirAPartirDeIndex(String oyente, String emisor,int n) {
		int us1 = GestorMensajes.SacarIdDeUsuario(oyente);
		int us2 = GestorMensajes.SacarIdDeUsuario(emisor);
		try {
			Connection cone = Conexion.Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT ID_Chat,mensaje FROM Chatea WHERE emisor_id = " + us2 + " AND receptor = " + us1 + " ORDER BY ID_Chat; ";
			ResultSet rs = 	stmt.executeQuery(query);
			while(rs.next()) {
				int id = rs.getInt("ID_Chat");
				if(id > n) {
					n = id;
					String msj = rs.getString("mensaje");
					 Platform.runLater(() -> Papa.AñadirCajitaIzq(msj));
					
				}
			}
			rs.close();
			stmt.close();
			cone.close();
			this.acumRecv =n;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void run() {
	    try {
	        while (!Thread.interrupted()) {
	            EscucharMensajes(oyente, emisor);
	        }
	    } finally {
	        // Cualquier limpieza final
	        System.out.println("Hilo de escucha terminado.");
	    }
	}

	
	/*
	public void run(String usuario1,String usuario2) {
		this.msjRecv = SacarNMsjsConv(usuario1,usuario2);
		this.acumRecv = SacarUltNMsjsIndex(usuario1,usuario2);
		System.out.println("Mensajes de conversacion entre" + usuario1 + " y " + usuario2 + " -->" + this.msjRecv);
		while(true) {
			EscucharMensajes(usuario1,usuario2);
		}
	}
	
	*/
	 public void iniciar() {
	        if (!this.isAlive()) { // Only start if the thread is not already alive
	            this.start();
	        }
	    }

	    public void detener() {
	        if (this.isAlive()) {
	            this.interrupt();
	        }
	    }
	


}
