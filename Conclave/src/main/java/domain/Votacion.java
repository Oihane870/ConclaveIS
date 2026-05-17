package domain;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;

import java.util.List;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)

@Entity
public class Votacion {
	@Id
	private int numVotacion;
	private Date horaComienzo;
	private Date horaCierre;
	private String resultado;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	HashMap<String, Integer> votos = new HashMap<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
    private List<Cardenal> electoresQueYaVotaron = new ArrayList<Cardenal>();
	
	@ManyToOne
	private Conclave conclave;

	
	
	public Votacion(int numVotacion, Date horaComienzo, Conclave conclave) {
		this.numVotacion = numVotacion;
		this.horaComienzo = horaComienzo;
	    this.conclave = conclave;

		
	} 
	
	
	public Votacion(int numVotacion, Date horaComienzo, Date horaCierre, String resultado) {
		this.numVotacion = numVotacion;
		this.horaComienzo = horaComienzo;
		this.horaCierre = horaCierre;
		this.resultado = resultado;
	}
	
	
	//getters
	public int getNumVotacion() {
		return this.numVotacion;
	}
	public Date getHoraComienzo() {
		return this.horaComienzo;
		
	}
	public Date getHoraCierre() {
		return this.horaCierre;
	}
	public String getResultado() {
		return this.resultado;
	}
	public List<Cardenal> getElectoresQueYaVotaron(){
		return electoresQueYaVotaron;
	}
	public HashMap<String, Integer> getVotos() {
		return votos;
	}
	//setters
	
	public void setNumVotacion(int numVotacion) {
		this.numVotacion = numVotacion;
		
	}
	
	public void setHoraComienzo(Date horaComienzo) {
		this.horaComienzo = horaComienzo;
		
	}
	
	public void setHoraCierre(Date horaCierre) {
		this.horaCierre = horaCierre;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	
	
	
	public Voto emitirVoto(Cardenal elector, String candidato) {
		int votosActuales;

		if (votos.containsKey(candidato)) {
		    votosActuales = votos.get(candidato);
		} else {
		    votosActuales = 0;
		}

		votos.put(candidato, votosActuales + 1);
		
		electoresQueYaVotaron.add(elector);
		return new Voto(candidato);
		
	}
	
	 public boolean yaVoto(Cardenal elector) {
	        return electoresQueYaVotaron.contains(elector);
	 }
	 
	 public boolean puedeSerCerrada() {
	        if (horaComienzo == null) return false;
	        long diffMs = new Date().getTime() - horaComienzo.getTime();
	        return diffMs >= 60L * 60 * 1000; // 1 hora en ms
	        //para probar 1 min
	        //return diffMs >= 60 * 1000;
	        
	    }
	
	
	public String getCandidatoGanador() {
		String candidatoGanador = null;
		if (this.puedeSerCerrada() == true) {
			int maxVotos = -1;
			int totalVotos = 0;
			int totalElectores = conclave.getElectores().size();
			
			
			for (String candidato : votos.keySet()) {
				int numVotos = votos.get(candidato);
				totalVotos += votos.get(candidato);
				if (numVotos > maxVotos) {
					candidatoGanador = candidato;
					maxVotos = numVotos;
				}
				
			}
			if(totalElectores * 2/3 >= totalVotos) {
				candidatoGanador = null;
			}
			
			
			
		}
		return candidatoGanador;
	}
	
	
}
