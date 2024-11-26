package Model;

public class Registro {

	String fechaEntrada;
	String horaEntrada;
	String ubicacion;
	String nombre;
	String trabajadorUID;
	String Empresa;
	
	public Registro(String fechaEntrada, String horaEntrada, String ubicacion, String nombre, String trabajadorUID, String empresa) {
		this.fechaEntrada = fechaEntrada;
		this.horaEntrada = horaEntrada;
		this.ubicacion = ubicacion;
		this.nombre = nombre;
		this.trabajadorUID = trabajadorUID;
		this.Empresa = empresa;
	}
	
}
