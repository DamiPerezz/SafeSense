package Control;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.Conexion;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class controlCamaras {

    @FXML
    private WebView espview;
    @FXML
    private TabPane tabPane;

    
    private int ID_Empresa;

    public void setID_Empresa(int iD_Empresa) {
		ID_Empresa = iD_Empresa;
	}

   
    public void initialize() {
        // Puedes llamar este método en el método initialize o después de cargar los datos
        ArrayList<String> nombreZonas = SacarZonasDeDB();
        // Asumiendo que nombresTabs ya está lleno de strings con los nombres para cada tab

        crearTabsDinamicamente(nombreZonas);
    }

    private void crearTabsDinamicamente(ArrayList<String> nombresTabs) {
    	int idTemp;
    	ArrayList<String> IPs = null;
        for (String nombreTab : nombresTabs) {
            Tab nuevoTab = new Tab(nombreTab);
            idTemp = Conexion.obtenerIdZona(nombreTab);
            IPs = SacarIPsDeZona(idTemp);
            
            // Usamos un VBox para organizar los WebView verticalmente
            VBox vbox = new VBox();
            vbox.setSpacing(5); // Espacio entre los WebView

            for (String ip : IPs) {
                WebView webView = new WebView();
                webView.getEngine().load("http://"+ip); // Carga la URL deseada, asumiendo que la IP es parte de la URL
                vbox.getChildren().add(webView);
                System.out.println("Creando web view de camara " + ip + " en zona " + nombreTab);
            }
            
            // Configuración del AnchorPane
            AnchorPane contenidoTab = new AnchorPane();
            AnchorPane.setTopAnchor(vbox, 0.0);
            AnchorPane.setRightAnchor(vbox, 0.0);
            AnchorPane.setBottomAnchor(vbox, 0.0);
            AnchorPane.setLeftAnchor(vbox, 0.0);
            
            contenidoTab.getChildren().add(vbox);
            
            nuevoTab.setContent(contenidoTab);
            
            tabPane.getTabs().add(nuevoTab);
        }
    }
    
    private ArrayList<String> SacarIPsDeZona(int Id_Zona) {
    	ArrayList<String> ips = new ArrayList<String>();

        try {
            Connection cone = Conexion.Conectar();
            Statement stmt = cone.createStatement();
            String query = "SELECT ip FROM Sensor where ID_Zona = " + Id_Zona +" AND tipo = 'Camara';";
            ResultSet rs =     stmt.executeQuery(query);

            while(rs.next()) {
                ips.add(rs.getString("ip"));
            }

            rs.close();
            stmt.close();
            cone.close();

        }catch(Exception e) {
            System.out.println(e.getMessage());
        }

        return ips;
    }
    
    ArrayList<String> SacarZonasDeDB(){
        ArrayList<String> zonas = new ArrayList<String>();

        try {
            Connection cone = Conexion.Conectar();
            Statement stmt = cone.createStatement();
            String query = "SELECT nombre FROM Zona where ID_Empresa = " + ID_Empresa +";";
            ResultSet rs =     stmt.executeQuery(query);

            while(rs.next()) {
                zonas.add(rs.getString("nombre"));
            }

            rs.close();
            stmt.close();
            cone.close();
            System.out.print("Se han sacado las siguientes zonas asociadas a tu empresa " );

            for(String e : zonas) {
                System.out.print(e+ ", ");
            }

        }catch(Exception e) {
            System.out.println(e.getMessage());
        }

        return zonas;
    }

    public void loadPage(WebEngine engine) {
        engine.load("http://192.168.80.173");
    }
}


