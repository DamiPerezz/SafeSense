package Control;

import Model.Conexion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Sistema extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			//Llamamos al codigo que hemos esto en FXML con la funcion loader para teenr la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ventana_inicial.fxml"));
			controladorVentanaInicial control = new controladorVentanaInicial(); //Llamamos al controlador en el main creando un controlador
			loader.setController(control); // Relacionamos la vista con el controlador
			
			Parent root = loader.load(); //Con esto llamamos a la funcion load de loader
	
		    primaryStage.setTitle("Home"); // Asignamos el título "HOME" a la ventana 
			primaryStage.setScene(new Scene(root)); //LLamamos a lo que hemos cargado con la funcion load
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		//System.out.println(Conexion.getEmpresaUsuario("as"));
		//System.out.println(Hashear.sha256("1234"));
	}
}
