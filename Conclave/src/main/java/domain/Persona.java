package domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)

public class Persona implements Serializable {
    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
    @XmlJavaTypeAdapter(IntegerAdapter.class)
	private int id;
	private String nombre;
	private Date fechaNacimiento;
	
	public Persona() {}
	public Persona(String nombre, Date fechaNacimiento) {
		this.nombre = nombre;
		this.fechaNacimiento = fechaNacimiento;
	}
	
	//getters
	
	public String getNombre() {
		return this.nombre;
	}
	
	public Date getFechaNac() {
		return fechaNacimiento;
	}
	public Integer getId() {
        return id;
    }

   
	
	
	//setters
	
	public void setId(Integer id) {
	        this.id = id;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setFechaNac(Date fechaNac) {
		this.fechaNacimiento = fechaNac;
	}
	
}
