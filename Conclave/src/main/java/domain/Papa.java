package domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Papa extends Persona implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombreEscogido;
	private Date fechaEleccion;
	private int numPapa;
	
	
	
	public Papa(String nombre, Date fechaNac, String nombreEscogido, Date fechaEleccion, int numPapa) {
		super(nombre, fechaNac);
		this.nombreEscogido = nombreEscogido;
		this.fechaEleccion = fechaEleccion;
		this.numPapa = numPapa;
		
	}
	
	//getters
	public Date getFechaEleccion() {
		return this.fechaEleccion;
	}
	public int getNumPapa() {
		return this.numPapa;
	}
	public String  getNombreEscogido() {
		return this.nombreEscogido;
	}
	
	
	//setters
	public void setFechaEleccion(Date fechaEleccion) {
		this.fechaEleccion = fechaEleccion;
	}
	public void setNumPapa(int numPapa) {
		this.numPapa = numPapa;
	}
	public void setNombreEscogido(String nombreEscogido) {
		this.nombreEscogido = nombreEscogido;
	}
}
