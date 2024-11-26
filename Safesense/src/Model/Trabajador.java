package Model;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;
public class Trabajador {
	
	String nombre;
	String apellidos;
	String usuario;
	String contraseña;
	String dni;
	String correo;
	String telefono;
	String tarjetaUID;
	String contraseñaHasheada;
	String cargo;
	String Empresa;
	ArrayList <String> Ubicaciones = new ArrayList <String>();
	
	public static void AñadirUbicacion(String NombreUsuario, String Ubicacion ) {
		ArrayList<Trabajador> arr = new ArrayList<Trabajador>();
		try {
			 arr = SacarTrabajadoresDelRegistro();
		}catch(Exception e) {
			e.getMessage();
		}
		for(Trabajador t : arr) {
			if(t.usuario.equals(NombreUsuario)) {
				t.Ubicaciones.add(Ubicacion);
				System.out.println("Ubicacion" + Ubicacion + "añadida a " + t.usuario);
			}
		}
		 Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        try (Writer writer = new FileWriter("registroUsuarios.json")) {
	            gson.toJson(arr, writer);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
	}
	
	public static ArrayList<Trabajador> SacarTrabajadoresDelRegistro() throws JsonSyntaxException, JsonIOException, FileNotFoundException{
		ArrayList<Trabajador> aux = new ArrayList<Trabajador>();
		
	Gson gson = new Gson();		  
    Trabajador[] usuariosArray = gson.fromJson(new FileReader("registroUsuarios.json"), Trabajador[].class);
    
	for (Trabajador t : usuariosArray) {
		aux.add(t);
		System.out.println(t.cargo);
	}
		return aux;
		
	}
	
	public void AñadirUbicacionATrabajador(String Ubicacion) {
		this.Ubicaciones.add(Ubicacion);
	}
	public Trabajador() {}
	public void VerSolicitudes() {
		ArrayList<String> Solicitudes = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		Gson gson = new Gson();
		 try {
			 File archivo = new File("solicitudes.json");
	            Scanner scanner = new Scanner(archivo);
	            while (scanner.hasNextLine()) {
	                String linea = scanner.nextLine();
	                System.out.println(linea);
	                Solicitud s = gson.fromJson(linea,Solicitud.class);
	                if(s.UID == this.tarjetaUID) {
	                	Solicitudes.add(s.UID);
	                	
	                }
	            }
	            sc.close();
	        } catch (Exception e) {
	            System.out.println("Error al leer camaras");
	            e.printStackTrace();
	        }
		 System.out.println("Tus solicitudes");
		 for(String sol : Solicitudes) {
			 System.out.println(sol);
		 }
		 
	}
	public void CrearSolicitud() {
		Scanner sc = new Scanner(System.in);
		ArrayList<String> Ubicaciones = SacarUbicacionesDisponibles();
		System.out.println("Introduce ubicacion a solicitar");
		for(String u : Ubicaciones) {
			System.out.println(u);
		}
		String ubicacion = sc.next();
		boolean existe = false;
		for(String u : Ubicaciones) {
			if (ubicacion == u) {
				existe = true;
			}
		}
		if(existe == true) {
			Solicitud sol = new Solicitud(this.nombre, this.tarjetaUID,ubicacion,"En espera");
			Convertir c = new Convertir();
			String solJSON = c.ConvertirEjemplo(sol);
			try {
				c.PasarATxt(solJSON, "solicitudes");
				System.out.println("Solicitud añadida con exito, espera de confirmacion de solicitud");
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		}else {
			System.out.println("Ubicacion inexsistente, intenta otra vez");
		}
		
		
	}
	public ArrayList<String> SacarUbicacionesDisponibles() {
		ArrayList<String> Ubicaciones = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		Gson gson = new Gson();
		 try {
			 File archivo = new File("Camaras.json");
	            Scanner scanner = new Scanner(archivo);
	            while (scanner.hasNextLine()) {
	                String linea = scanner.nextLine();
	                System.out.println(linea);
	                camara c = gson.fromJson(linea,camara.class);
	                Ubicaciones.add(c.GetUbicacion());
	            }
	            sc.close();
	        } catch (Exception e) {
	            System.out.println("Error al leer camaras");
	            e.printStackTrace();
	        }
		 
		
		return Ubicaciones;
	}
	public Trabajador(String nombre, String apellidos, String dni, String correo, String telefono, String tarjetaUID,
			String contraseñaHasheada, String cargo) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.correo = correo;
		this.telefono = telefono;
		this.tarjetaUID = tarjetaUID;
		this.contraseñaHasheada = contraseñaHasheada;
		this.cargo = cargo;
	}
	public Trabajador(String nombre, String apellidos, String usuario, String contraseña) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.usuario = usuario;
		this.contraseña = contraseña;
	
	}
	
	public Trabajador(String nombre, String apellidos, String usuario, String contraseña, String rol) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.cargo = rol;
	}
	public Trabajador(String nombre, String apellidos, String usuario, String contraseña, String rol,String empresa) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.cargo = rol;
	}
	public Trabajador(String nombre, String apellidos, String usuario, String contraseña, String rol,ArrayList<String>Ubic) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.cargo = rol;
		this.Ubicaciones = Ubic;
	}
	public Trabajador(String nombre, String apellidos, String usuario, String contraseña, String rol,String empresa,ArrayList<String>Ubic) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.cargo = rol;
		this.Empresa = empresa;
		this.Ubicaciones = Ubic;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getTarjetaUID() {
		return tarjetaUID;
	}
	public void setTarjetaUID(String tarjetaUID) {
		this.tarjetaUID = tarjetaUID;
	}
	public String getContraseñaHasheada() {
		return contraseñaHasheada;
	}
	public void setContraseñaHasheada(String contraseñaHasheada) {
		this.contraseñaHasheada = contraseñaHasheada;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getUsuario() {
		return usuario;
	}
	
	public String getCargo() {
		return cargo;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getContraseña() {
		return contraseña;
	}
	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	
	
	
	

}
