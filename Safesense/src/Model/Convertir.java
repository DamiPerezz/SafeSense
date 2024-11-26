package Model;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileWriter;
import java.util.Scanner;

public class Convertir {
	
	 Gson gson;
	 Convertir(){
		 this.gson = new Gson();
		 
	 }
	 
	public String ConvertirEjemplo(camara objeto) { //Metodo para convertir a String en JSON el objeto
		String json = gson.toJson(objeto);
		return json;
	}
	public String ConvertirEjemplo(Solicitud objeto) { //Metodo para convertir a String en JSON el objeto
		String json = gson.toJson(objeto);
		return json;
	}
	 
	public String ConvertirEjemplo(Trabajador objeto) { //Metodo para convertir a String en JSON el objeto
		String json = gson.toJson(objeto);
		return json;
	}
	
	public String ConvertirEjemplo(Registro objeto) { //Metodo para convertir a String en JSON el objeto
		String json = gson.toJson(objeto);
		return json;
	}
	 
	public void PasarATxt(String texto, String ubi) { //Metodo para pasar al archivo dato.json el texto sin sobrescribir
		System.out.println("Metiendo " + texto + " en directorio actual" );
		String archivo = ubi + ".json";
		try {
			FileWriter escritor = new FileWriter(archivo,true);
			escritor.write(texto + "\n");
			System.out.println("Escitura de " + texto + " con exito en " + archivo );
			escritor.close();
		}catch(Exception e) {
			System.out.println("Error al escribir en archivo" + e.getMessage());
		}
		
	}
	
	 /* COMENTADA TEMPORALMENTE PARA QUE NO HAYA ERRORES
	  * HAY QUE QUITAR LA CLASE Ejemplo
	public static void main(String []args) {
		Convertir convertir = new Convertir();
		Ejemplo n1 = new Ejemplo("dato", "dato2", "dato3");	
		Ejemplo n2 = new Ejemplo("dato4", "dato5", "dato6");	
		String r = convertir.ConvertirEjemplo(n1);
		System.out.println(r);
		convertir.PasarATxt(r, "ejemplo1");
		
		} */
}

