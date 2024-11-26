package Model;

public class Solicitud {
	
	String Trabajador;
	String UID;
	String Ubicacion;
	String Estado;
    private String zona;
    private String nombreCamara;
    private String ipCamara;
    private int idAcceso;
    private int idZona;
    private int idUsuario;
    
	public Solicitud(String trabajador, String uID, String ubicacion, String estado) {
		super();
		Trabajador = trabajador;
		UID = uID;
		Ubicacion = ubicacion;
		Estado = estado;
	}
	
	 public Solicitud(int idAcceso, String estado, int idZona, int idUsuario) {
	        this.idAcceso = idAcceso;
	        this.Estado = estado;
	        this.idZona = idZona;
	        this.idUsuario = idUsuario;
	    }
	 public Solicitud(String usuario, String zona, int idAcceso, String estado, int idZona, int idUsuario) {
		 this.Trabajador = usuario;
		 this.Ubicacion = zona;	
	        this.idAcceso = idAcceso;
	        this.Estado = estado;
	        this.idZona = idZona;
	        this.idUsuario = idUsuario;
	    }
	
	 // Constructor, getters y setters
    public Solicitud(String Trabajador, String zona, String nombreCamara, String ipCamara, String ubicacion, String estado) {
        this.Trabajador = Trabajador;
        this.zona = zona;
        this.nombreCamara = nombreCamara;
        this.ipCamara = ipCamara;
        this.Ubicacion = ubicacion;
        this.Estado = estado;
    }
    
    public Solicitud(int idAcceso, String estado, String zona, String trabajador) {
        this.idAcceso = idAcceso;
        this.Estado = estado;
        this.zona = zona;
        this.Trabajador = trabajador;
    }

	public String getTrabajador() {
		return Trabajador;
	}

	public void setTrabajador(String trabajador) {
		Trabajador = trabajador;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public String getUbicacion() {
		return Ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		Ubicacion = ubicacion;
	}

	public String getEstado() {
		return Estado;
	}

	public void setEstado(String estado) {
		Estado = estado;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getNombreCamara() {
		return nombreCamara;
	}

	public void setNombreCamara(String nombreCamara) {
		this.nombreCamara = nombreCamara;
	}

	public String getIpCamara() {
		return ipCamara;
	}

	public void setIpCamara(String ipCamara) {
		this.ipCamara = ipCamara;
	}
	
	 public int getIdAcceso() {
	        return idAcceso;
	    }
	 
	 public int getIdZona() {
	        return idZona;
	    }

	    public int getIdUsuario() {
	        return idUsuario;
	    }
    
    
	

}