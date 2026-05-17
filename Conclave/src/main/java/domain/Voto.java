package domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Voto implements Serializable{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    private Integer id;
	
	private String  candidato;
	
	
	public Voto() {
		super();
	}
	
	public Voto(String candidato) {
		this.candidato = candidato;
	}
	
	public String getCandidato() {
		return this.candidato;
	}
	public Integer getId() {
        return id;
    }
	public void setCandidato(String candidato) {
		this.candidato = candidato;
	}
}
