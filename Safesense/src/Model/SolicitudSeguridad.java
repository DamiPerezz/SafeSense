package Model;
import Model.camara;

import java.util.List;

public class SolicitudSeguridad {
    private String usuario;
    private List<camara> solicitudes;

    // Constructor, getters y setters
    public SolicitudSeguridad(String usuario,List<camara> solicitudes) {
        this.usuario = usuario;
        this.solicitudes = solicitudes;
        
    }

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public List<camara> getSolicitudes() {
		return solicitudes;
	}

	public void setSolicitudes(List<camara> solicitudes) {
		this.solicitudes = solicitudes;
	}
	
	 // Constructor, getters y setters aquí...

    // Método para verificar si hay solicitudes pendientes
    public boolean tieneSolicitudesPendientes() {
        for (camara camara : solicitudes) {
            if ("Pendiente".equals(camara.getEstado())) {
                return true;
            }
        }
        return false;
    }
   
}
