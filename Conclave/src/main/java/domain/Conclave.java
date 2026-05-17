package domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;



@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Conclave implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    private Integer id;
	private Date fechaInicio;
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Cardenal> electores = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Votacion> votaciones = new ArrayList<Votacion>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Cardenal maestroCeremonias;
	
	@OneToOne(cascade = CascadeType.PERSIST)
	private Papa papa;
	
	
	
	public Conclave() {
        super();
    }
	
	public Conclave(Date fechaInicio, Cardenal maestroCeremonias) {
		this.fechaInicio = fechaInicio;
		this.maestroCeremonias = maestroCeremonias;
		
	}
	
	//getters
    public Integer getId() { 
    	return id; 
    }

	public Date getFechaInicio() {
		return fechaInicio;
	}
	
	
	public Papa getPapa() {
		return this.papa;
	}
    public List<Votacion> getVotaciones() { return votaciones; }

    public List<Cardenal> getElectores() { return electores; }
    
    public Cardenal getMaestroCeremonias() { return maestroCeremonias; }

    
    
    //setters
    public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
    public void setMaestroCeremonias(Cardenal maestroCeremonias) {
        this.maestroCeremonias = maestroCeremonias;
    }
    
    public void setPapa(Papa papa) { this.papa = papa; }

	
	public void addElector(Cardenal cardenal) {
	        electores.add(cardenal);
	}
	
	
	public Votacion abrirVotacion() {
		int numSig = votaciones.size()+1;
		Votacion v = new Votacion(numSig, new Date(), this);
		votaciones.add(v);
		return v;
	}
	
	
	public Votacion getVotacionActual() {
	    if (votaciones.isEmpty()) {
	        return null;
	    }

	    Votacion ultima = votaciones.get(votaciones.size() - 1);

	    if (ultima.getResultado() == null) {
	        return ultima;
	    } else {
	        return null;
	    }
	}
	
	
	public boolean haTerminado() {
		 return papa != null;
	}
	
	
	@Override
    public String toString() {
        return "Conclave#" + id + " inicio=" + fechaInicio;
    }
}
