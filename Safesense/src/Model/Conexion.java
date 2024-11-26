package Model;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class Conexion {

	static String usuario = "";
	static String contraseña = "";
	
	public static Connection Conectar(){
		System.out.println("Conectando a la base de datos...");
		Connection con = null;
    	try {
			Class.forName("org.mariadb.jdbc.Driver");
			 try {
				con = DriverManager.getConnection(
				            "jdbc:mariadb://IP/piisafesense", usuario, contraseña);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error en conexion a la base de datos");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Error en conexion a la base de datos " + e.getMessage() );
			// TODO Auto-generated catch block
			
		}
		return con;
	}
	
	public static boolean login(String username, String hashPass) {
		boolean existeUsuario = false;
		
		
		try {
			Connection cone = Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT username, pass FROM Usuario WHERE username = '"+ username + "' AND pass = '" + hashPass + "';";
			ResultSet rs = 	stmt.executeQuery(query);
			
			if(rs.next()){
				existeUsuario = true;
			}
			rs.close();
			stmt.close();
			cone.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return existeUsuario;
	}
	
	public static boolean registroUsuario(String nombre, String apellido, String username, String hashPass, String rol, int idEmpresa) {
	    boolean usuarioRegistrado = false;
	    
	    try {
	        Connection cone = Conectar();
	        Statement stmt = cone.createStatement();

	        // Insertando usuario en la tabla Usuarios
	        String insertQuery = "INSERT INTO Usuario (nombre, apellido, username, pass,Rol, ID_Empresa) VALUES ('" 
	                             + nombre + "', '" + apellido + "', '" + username + "', '" + hashPass + "','" + rol+ "','" + idEmpresa + "');";
	        int affectedRows = stmt.executeUpdate(insertQuery, Statement.RETURN_GENERATED_KEYS);
	        
	        //affectedRows se utiliza para almacenar el número de filas afectadas por una operación de base de datos, como INSERT, UPDATE o DELETE.
	        
	        /** Statement.RETURN_GENERATED_KEYS, indica que se deben devolver las claves generadas automáticamente por la base de datos al insertar un nuevo registro.
	        En este caso, se refiere al id_usuario que la base de datos asigna automáticamente al nuevo usuario.**/

	        if(affectedRows > 0){ //Si affectedRows es mayor que 0, significa que la operación de base de datos afectó a ese número de filas.Si insertas un nuevo registro en la base de datos, affectedRows será 1 porque se ha añadido una fila.
	            // Obteniendo la clave primaria del usuario recién insertado
	            ResultSet rs = stmt.getGeneratedKeys(); 
	            /**Después de ejecutar la consulta INSERT, esta línea recupera las claves generadas automáticamente. 
	            Estas claves se devuelven como un objeto ResultSet.**/
	            if(rs.next()){
	                int idUsuario = rs.getInt(1); //, esta línea recupera el valor de la columna 1 de la fila actual del objeto ResultSet, que es el id_usuario del usuario recién insertado.

	                // Insertando username y password en la tabla Login con el id_usuario asociado
	                String loginInsertQuery = "INSERT INTO Login (id_usuario, username, pass) VALUES (" 
	                                          + idUsuario + ", '" + username +"', '" + hashPass +"');";
	                stmt.executeUpdate(loginInsertQuery);
	                
	                usuarioRegistrado = true;
	            }
	            rs.close();
	        }

	        stmt.close();
	        cone.close();
	    } catch (SQLException e) {
	       e.printStackTrace();
	    }
	    
	    return usuarioRegistrado;
	}
	
	public static int registroEmpresa(String empresa) {
	    int idEmpresa = -1;
	    
	    try {
	        Connection cone = Conectar();
	        Statement stmt = cone.createStatement();

	        // Comprobando si la empresa ya existe en la tabla Empresa
	        String checkQuery = "SELECT ID_Empresa FROM Empresa WHERE nombre = '" + empresa + "';";
	        ResultSet rs = stmt.executeQuery(checkQuery);

	        if (rs.next()) {
	            // Si la empresa ya existe, obtenemos su ID_Empresa
	            idEmpresa = rs.getInt("ID_Empresa");
	        } else {
	            // Si la empresa no existe, la insertamos en la tabla Empresa
	            String insertQuery = "INSERT INTO Empresa (nombre) VALUES ('" + empresa + "');";
	            stmt.executeUpdate(insertQuery, Statement.RETURN_GENERATED_KEYS);
	            
	            // Obteniendo el ID_Empresa generado
	            rs = stmt.getGeneratedKeys();
	            if (rs.next()) {
	                idEmpresa = rs.getInt(1);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return idEmpresa;
	}

	
	
	public static boolean registroSolicitud(int idUsuario, ArrayList<Integer> idsZonasSeleccionadas) {
	    boolean solicitudRegistrada = false;

	    try {
	        Connection cone = Conectar();
	        cone.setAutoCommit(false); // Desactiva el auto-commit para manejar transacciones

	        // Itera sobre cada ID_Zona seleccionado y realiza la inserción
	        for (Integer idZona : idsZonasSeleccionadas) {
	            // Prepara la consulta con el ID_Zona específico
	            String accesoInsertQuery = "INSERT INTO Acceso (estado, ID_Zona) VALUES ('pendiente', " + idZona + ")";
	            try (Statement stmt = cone.createStatement()) {
	                int affectedRowsAcceso = stmt.executeUpdate(accesoInsertQuery, Statement.RETURN_GENERATED_KEYS);

	                if (affectedRowsAcceso > 0) {
	                    try (ResultSet rsAcceso = stmt.getGeneratedKeys()) {
	                        if (rsAcceso.next()) {
	                            int idAcceso = rsAcceso.getInt(1);

	                            // Realiza la inserción en la tabla Solicita
	                            String solicitaInsertQuery = "INSERT INTO Solicita (ID_Usuario, ID_Acceso) VALUES (" + idUsuario + ", " + idAcceso + ")";
	                            stmt.executeUpdate(solicitaInsertQuery);
	                        }
	                    }
	                }
	            }
	        }

	        cone.commit(); // Confirma todas las inserciones como una transacción
	        solicitudRegistrada = true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return solicitudRegistrada;
	}

	public static List<Solicitud> obtenerDetallesSolicitudesAcceso() {
	    List<Solicitud> detallesSolicitudes = new ArrayList<>();

	    try {
	        Connection cone = Conectar();
	        String query = "SELECT Usuario.username, Zona.nombre, Acceso.ID_Acceso, Acceso.Estado, Acceso.ID_Zona, Solicita.ID_Usuario FROM Acceso JOIN Solicita ON Acceso.ID_Acceso = Solicita.ID_Acceso JOIN Usuario ON Solicita.ID_Usuario = Usuario.ID_Usuario JOIN Zona ON Zona.ID_Zona = Acceso.ID_Zona WHERE Acceso.Estado = 'pendiente'";
	        try (PreparedStatement stmt = cone.prepareStatement(query)) {
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                detallesSolicitudes.add(new Solicitud(
	                	rs.getString("Usuario.username"),
	                	rs.getString("Zona.nombre"),
	                    rs.getInt("ID_Acceso"),
	                    rs.getString("Estado"),
	                    rs.getInt("ID_Zona"),
	                    rs.getInt("ID_Usuario")
	                ));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return detallesSolicitudes;
	}

	 public static boolean actualizarEstadoAcceso(int idAcceso, String nuevoEstado) {
		    Connection cone = null;
		    PreparedStatement pstmt = null;
		    PreparedStatement pstmtDelete = null;
		    try {
		        cone = Conectar();
		        // Prepara la consulta para actualizar el estado en la tabla Acceso
		        pstmt = cone.prepareStatement("UPDATE Acceso SET Estado = ? WHERE ID_Acceso = ?");
		        pstmt.setString(1, nuevoEstado);
		        pstmt.setInt(2, idAcceso);
		        
		        // Ejecuta la actualización y verifica si afectó a alguna fila
		        int affectedRows = pstmt.executeUpdate();
		        if (affectedRows == 1 && (nuevoEstado.equals("aceptado") || nuevoEstado.equals("denegado"))) {
		            // Si el estado es aceptado o denegado y la actualización fue exitosa,
		            // prepara y ejecuta la eliminación en la tabla Solicita
		            pstmtDelete = cone.prepareStatement("DELETE FROM Solicita WHERE ID_Acceso = ?");
		            pstmtDelete.setInt(1, idAcceso);
		            pstmtDelete.executeUpdate();
		        }
		        
		        // Retorna true si se actualizó al menos una fila
		        return affectedRows > 0;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    } finally {
		        // Cierra los recursos en el bloque finally para evitar fugas de recursos
		        try {
		            if (pstmtDelete != null) pstmtDelete.close();
		            if (pstmt != null) pstmt.close();
		            if (cone != null) cone.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		}
	 
	 

	 public static List<Solicitud> obtenerHistorialAccesosE(int idEmpresa) {
		    List<Solicitud> historialAccesos = new ArrayList<>();

		    try {
		        Connection cone = Conectar();
		        String query = "SELECT Acceso.ID_Acceso, Acceso.Estado, Zona.nombre AS NombreZona, Usuario.nombre AS NombreUsuario " +
		                       "FROM Acceso " +
		                       "JOIN Zona ON Acceso.ID_Zona = Zona.ID_Zona " +
		                       "JOIN Empresa ON Zona.ID_Empresa = Empresa.ID_Empresa " +
		                       "JOIN Solicita ON Acceso.ID_Acceso = Solicita.ID_Acceso " +
		                       "JOIN Usuario ON Solicita.ID_Usuario = Usuario.ID_Usuario " +
		                       "WHERE Empresa.ID_Empresa = ?";
		        try (PreparedStatement stmt = cone.prepareStatement(query)) {
		            stmt.setInt(1, idEmpresa); // Establece el ID_Empresa en el parámetro de la consulta
		            ResultSet rs = stmt.executeQuery();
		            while (rs.next()) {
		                historialAccesos.add(new Solicitud(
		                    rs.getInt("ID_Acceso"),
		                    rs.getString("Estado"),
		                    rs.getString("NombreZona"),
		                    rs.getString("NombreUsuario")
		                ));
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return historialAccesos;
		}

		
	 
	public static boolean existeUsuario(String username) {
	    String query = "SELECT COUNT(*) AS count FROM Usuario WHERE username = ?;";
	    try (Connection conn = Conectar();
	         PreparedStatement pstmt = conn.prepareStatement(query)) {
	        
	        pstmt.setString(1, username); // Establecer el nombre de usuario en el primer parámetro de la consulta
	        
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            int count = rs.getInt("count");
	            return count > 0; // Retorna true si count es mayor que 0, es decir, el usuario existe
	        }
	    } catch (SQLException e) {
	        System.out.println("Error al verificar la existencia del usuario: " + e.getMessage());
	        e.printStackTrace();
	    }
	    
	    return false; // Retorna false si no se encontró el usuario o si ocurrió un error
	}

	
	public static int obtenerIdUsuario(String nombreUsuario) {
	    int idUsuario = -1; // Valor predeterminado que indica que no se encontró el usuario

	    try {
	        Connection cone = Conectar();
	        Statement stmt = cone.createStatement();

	        // Consulta para obtener el idUsuario a partir del nombreUsuario
	        String usuarioSelectQuery = "SELECT id_usuario FROM Usuario WHERE username = '" + nombreUsuario + "';";
	        ResultSet rsUsuario = stmt.executeQuery(usuarioSelectQuery);

	        if(rsUsuario.next()){
	            idUsuario = rsUsuario.getInt("ID_Usuario");
	        }

	        rsUsuario.close();
	        stmt.close();
	        cone.close();
	    } catch (SQLException e) {
	       e.printStackTrace();
	    }

	    return idUsuario;
	}

	public static int obtenerIdZona(String nombreZona) {
	    int idZona = -1; // Valor predeterminado que indica que no se encontró la zona

	    try {
	        Connection cone = Conectar();
	        Statement stmt = cone.createStatement();

	        // Consulta para obtener el idZona a partir del nombreZona
	        String zonaSelectQuery = "SELECT ID_Zona FROM Zona WHERE nombre = '" + nombreZona + "';";
	        ResultSet rsZona = stmt.executeQuery(zonaSelectQuery);

	        if(rsZona.next()){
	            idZona = rsZona.getInt("ID_Zona");
	        }

	        rsZona.close();
	        stmt.close();
	        cone.close();
	    } catch (SQLException e) {
	       e.printStackTrace();
	    }

	    return idZona;
	}
	public static String obtenerZonadeID(int id_Zona) {
	    String zona = ""; // Valor predeterminado que indica que no se encontró la zona

	    try {
	        Connection cone = Conectar();
	        Statement stmt = cone.createStatement();

	        // Consulta para obtener el idZona a partir del nombreZona
	        String zonaSelectQuery = "SELECT nombre FROM Zona WHERE ID_Zona = " + id_Zona + ";";
	        ResultSet rsZona = stmt.executeQuery(zonaSelectQuery);

	        if(rsZona.next()){
	        	zona = rsZona.getString("nombre");
	        }

	        rsZona.close();
	        stmt.close();
	        cone.close();
	    } catch (SQLException e) {
	       e.printStackTrace();
	    }

	    return zona;
	}


	
	public static String getRolUsuario(String nombreUsuario) { 
		String rolUsuario = "";
		
		try {
			Connection cone = Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT Rol FROM Usuario WHERE username = '" + nombreUsuario + "';";
			ResultSet rs = 	stmt.executeQuery(query);
			
			while(rs.next()){
				rolUsuario = rs.getString("Rol");
			}
			rs.close();
			stmt.close();
			cone.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("rol de Usuario: " + rolUsuario);
		return rolUsuario;			
	}
	public static int getEmpresaUsuario(String nombreUsuario) { 
		int empresa = -1;
		
		try {
			Connection cone = Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT ID_Empresa FROM Usuario WHERE username = '"+ nombreUsuario +"' ;";
			ResultSet rs = 	stmt.executeQuery(query);
			while(rs.next()){
				empresa = rs.getInt("ID_Empresa");
			}
			rs.close();
			stmt.close();
			cone.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ID Empresa del usuario = " + empresa);
		return empresa;			
	}
	public static String getEmpresaNombreUsuario(int id) { 
		String n = "";
		
		try {
			Connection cone = Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT nombre FROM Empresa WHERE ID_Empresa = "+ id +" ;";
			ResultSet rs = 	stmt.executeQuery(query);
			while(rs.next()){
				n = rs.getString("nombre");
			}
			rs.close();
			stmt.close();
			cone.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Nombre de empresa con ID " +id +  " = " + n );
		return n;			
	}
	
	public static String SelectUserPass(String nombreUsuario, String hashPass) { //Funcion de prueba
		String todo = null;
		String usuario = null;
		String pass = null;
		
		try {
			Connection cone = Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT username, pass FROM Usuario WHERE username = 'ikerTrabajador';";
			ResultSet rs = 	stmt.executeQuery(query);
			
			while(rs.next()){
				//todo = rs.getString("username");
				usuario = rs.getString("username");
				pass = rs.getString("pass");
			}
			rs.close();
			stmt.close();
			cone.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(usuario + " "+ pass);
		return todo;			
	}
	
	public static void main(String[] args) {
//		SelectUserPass("","");
//		System.out.println(login("ikerTrabajador", "494414ded24da13c451b13b424928821351c78fce49f93d9e1b55f102790c206"));
//		System.out.println(login("ikerTrabajador", "494414ded24da13c451b13b424528821351c78fce49f93d9e1b55f102790c206"));
		//System.out.println(getRolUsuario("ikerTrabajador"));
		//System.out.println(registroUsuario("Maria", "Juarez", "mariamaria", "12345", "Trabajador"));
		
	}
	
	
}
