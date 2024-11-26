package Control;
import Model.Conexion;
import Model.Solicitud;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class controladorVentanaBaseDatos {
	
	int ID_Desliz;
	
	int ID_Empresa;
	
	private int IndexIDs;
	
	public void setID_Empresa(int id) {
		this.ID_Empresa = id;
	}

	@FXML
    private ImageView Imagen;
	
    @FXML
    private TextArea textArea_Base_Datos;

    @FXML
    private MenuButton DesplegableCaps;
    
    @FXML
    private Label CampoActualizable;

    @FXML
    private Button btnIzq;

    @FXML
    private Button BtnDrch;
    
    private ArrayList<Fecha_ID> IDs;

    @FXML
    void PasarDrch(ActionEvent event) {
    	if(IndexIDs+1 >= 0 && IndexIDs+1 < IDs.size()) {
    		this.ID_Desliz = IDs.get(IndexIDs+1).getID();
    		this.IndexIDs++;
    		ActualizarAImagen(this.ID_Desliz);
    		CampoActualizable.setText(IDs.get(IndexIDs).getFecha());
    		
    	}
    	
    }

    @FXML
    void PasarIzq(ActionEvent event) {
    	if(IndexIDs-1 >= 0 && IndexIDs-1 < IDs.size()) {
    		this.ID_Desliz = IDs.get(IndexIDs-1).getID();
    		this.IndexIDs--;
    		ActualizarAImagen(this.ID_Desliz);
    		CampoActualizable.setText(IDs.get(IndexIDs).getFecha());
    	}
    	
    }
    public Image DecodificarFotoB64(String b64) {
		
		 byte[] imageBytes = Base64.getDecoder().decode(b64);

		    // Crear un ByteArrayInputStream con los bytes de la imagen
		    ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);

		    // Crear una imagen de JavaFX a partir del ByteArrayInputStream
		    Image image = new Image(bis);

		    // Cerrar el ByteArrayInputStream
		    try {
		        bis.close();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    // Retornar la imagen
		    return image;
		
	}
    public String SacarB64DeDB(int ID) {
    	String foto = "";
		try {
			Connection cone = Conexion.Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT Contenido from Datos WHERE ID_Dato = "+ ID +";";
			ResultSet rs = 	stmt.executeQuery(query);
			while(rs.next()) {
				foto = rs.getString("Contenido");
				//comprobar si el emisor es el usuario uno lo imprima por la derecha y sino por la izquierda
			}
			rs.close();
			stmt.close();
			cone.close();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return foto;
    }
    
    void ActualizarAImagen(int id){
    	Image imagen = DecodificarFotoB64(SacarB64DeDB(id));
    	Imagen.setImage(imagen); 
    }
    private ArrayList<Fecha_ID> SacarIDDeCapturas(){ //Cambiar por TimeStamps
    	ArrayList<Fecha_ID> ids = new ArrayList<Fecha_ID>();
    	System.out.println("Sacando trabajadores de la DB");
    	
    	try {
			Connection cone = Conexion.Conectar();
			Statement stmt = cone.createStatement();
			String query = "SELECT ID_Dato, fecha FROM Datos WHERE TipoDato = 'Foto';";
			ResultSet rs = 	stmt.executeQuery(query);
			
			while(rs.next()) {
				ids.add(new Fecha_ID(rs.getString("fecha"), rs.getInt("ID_Dato")));
			}
			
			rs.close();
			stmt.close();
			cone.close();
	  }catch(Exception e) {
			System.out.println(e.getMessage());
			//mostrarMensajeDeError("Error, revisa la consola para obtener mas detalles");
		}
    	
    	return ids;
    }

    
    void InstanciarMenuButtoms() {
    	ArrayList<Fecha_ID> capturas = SacarIDDeCapturas();
    	for(Fecha_ID t : capturas) {
    		System.out.println("Añadiendo " +t);
    		 MenuItem menuItem = new MenuItem(String.valueOf(t.getID()));
	           
	            menuItem.setOnAction(event -> {
	                MenuItem source = (MenuItem) event.getSource();
	                DesplegableCaps.setText(source.getText());
	                ActualizarAImagen(t.getID());
	                this.ID_Desliz = t.getID();
	                CampoActualizable.setText(t.getFecha());
	            });
	            
	            DesplegableCaps.getItems().add(menuItem);	
    	}
    	
    }
    
    @FXML
    void initialize() {
    	IDs = SacarIDDeCapturas();
    	this.ID_Desliz = IDs.get(0).getID();
    	this.IndexIDs = 0;
    	ActualizarAImagen(this.ID_Desliz);
    	CampoActualizable.setText(IDs.get(0).getFecha());
    	InstanciarMenuButtoms();
    	
    }
  
//    public void setHistorialAccesos(List<Solicitud> historialAccesos) {
//        StringBuilder information = new StringBuilder();
//        for (Solicitud acceso : historialAccesos) {
//            information.append("ID Acceso: ").append(acceso.getIdAcceso())
//                        .append(", Estado: ").append(acceso.getEstado())
//                        .append(", Zona: ").append(acceso.getZona())
//                        .append(", Usuario: ").append(acceso.getTrabajador())
//                        .append("\n"); // Agrega una nueva línea para separar los accesos
//        }
//        
//        // Establece el texto del TextArea y hace que no sea editable
//        textArea_Base_Datos.setText(information.toString());
//        textArea_Base_Datos.setEditable(false);
//    }
}