package Model;

import com.google.gson.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class camara {

    private String nombre; // Nombre identificatorio de la camara
    private String IP; // IP asociada a cada camara
    private String ubicacion; // Ubicacion en la que se encuentra la camara instalada
    private String estado;

    // Constructor parametrizado de la clase Camara
    public camara(String n, String i, String ub) {
        nombre = n;
        IP = i;
        ubicacion = ub;
    }

    public camara(String n, String i, String ub, String e) {
        nombre = n;
        IP = i;
        ubicacion = ub;
        estado = e;
    }
    public static void main(String[] args) {
        CrearCamara();
    }

    public static void CrearCamara() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el nombre de la Cámara");
        String camara = sc.nextLine();  // Utilizar nextLine() para permitir espacios en el nombre
        System.out.println("Introduce la IP de la Cámara");
        String IP = sc.next();
        sc.nextLine();  // Consumir el salto de línea pendiente después de next()
        System.out.println("Introduce la ubicación de la Cámara");
        String ubicacion = sc.nextLine();  // Utilizar nextLine() para permitir espacios en la ubicación
        
        camara c = new camara(camara, IP, ubicacion);
        c.MeterObjetoEnJSON(c);
        System.out.println(c.toString() + " metida en .json como nuevo objeto de cámara");
    }


    private void MeterObjetoEnJSON(camara cam) {
        JsonArray camarasArray = leerCamarasJson(); // Leer el array actual de cámaras

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String JSON = gson.toJson(cam);

        camarasArray.add(JsonParser.parseString(JSON)); // Agregar la nueva cámara al array

        try (FileWriter writer = new FileWriter("Camaras.json", false)) {
            gson.toJson(camarasArray, writer); // Escribir el array completo al archivo
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String GetUbicacion() {
        return ubicacion;
    }
    
    

    public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}


	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	private JsonArray leerCamarasJson() {
        try (FileReader reader = new FileReader("Camaras.json")) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            if (jsonElement.isJsonArray()) {
                return jsonElement.getAsJsonArray();
            } else {
                return new JsonArray();
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo 'Camaras.json': " + e.getMessage());
            return new JsonArray();
        }
    }
}
