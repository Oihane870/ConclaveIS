package domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


@XmlAccessorType(XmlAccessType.FIELD)
@Entity

public class Cardenal extends Persona implements Serializable{
	
	
	
	
	private static final long serialVersionUID = 1L;

	private String cargo;
	
	
	public Cardenal() {
        super();
    }
	public Cardenal(String nombre, Date fechaNac, String cargo) {
		super(nombre, fechaNac);
		this.cargo = cargo;
	}
	
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
        this.cargo = cargo;
    }
	
	
	public boolean esElegible() {
		Date hoy = new Date();
		long diffMs = hoy.getTime() - this.getFechaNac().getTime();
		long anios = diffMs / (1000L * 60 *60 * 24 * 365);
		if (anios > 80 ) {
			return true;
		}
		else {
			return false;
		}
	}
	@Override
    public String toString() {
        return getId() + ";" + getNombre() + ";" + cargo;
    }
}
