package businessLogic;
import java.io.File;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dataAccess.DataAccess;
import domain.Cardenal;
import domain.Conclave;
import domain.Papa;
import domain.Persona;
import domain.Sale;
import domain.Votacion;
import domain.Voto;
import exceptions.AlreadyVotedException;
import exceptions.ConclaveAlreadyOpenException;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.NoActiveVotingException;
import exceptions.SaleAlreadyExistException;
import exceptions.VotingNotFinishedException;

import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.IOException;


/**
 * It implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation  implements BLFacade {
	 private static final int baseSize = 160;

		private static final String basePath="src/main/resources/images/";
	DataAccess dbManager;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		dbManager=new DataAccess();		
	}
	
    public BLFacadeImplementation(DataAccess da)  {
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		dbManager=da;		
	}
    

	/**
	 * {@inheritDoc}
	 */
   @WebMethod
	public Sale createSale(String title, String description,int status, float price, Date pubDate, String sellerEmail, File file) throws  FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException {
		dbManager.open();
		Sale product=dbManager.createSale(title, description, status, price, pubDate, sellerEmail, file);		
		dbManager.close();
		return product;
   };
	
   /**
    * {@inheritDoc}
    */
	@WebMethod 
	public List<Sale> getSales(String desc){
		dbManager.open();
		List<Sale>  rides=dbManager.getSales(desc);
		dbManager.close();
		return rides;
	}
	
	/**
	    * {@inheritDoc}
	    */
		@WebMethod 
		public List<Sale> getPublishedSales(String desc, Date pubDate) {
			dbManager.open();
			List<Sale>  rides=dbManager.getPublishedSales(desc,pubDate);
			dbManager.close();
			return rides;
		}
	/**
	    * {@inheritDoc}
	    */
	@WebMethod public BufferedImage getFile(String fileName) {
		return dbManager.getFile(fileName);
	}

    
	public void close() {
		DataAccess dB4oManager=new DataAccess();
		dB4oManager.close();

	}

	/**
	 * {@inheritDoc}
	 */
    @WebMethod	
	 public void initializeBD(){
    	dbManager.open();
		dbManager.initializeDB();
		dbManager.close();
	}
    /**
	 * {@inheritDoc}
	 */
    @WebMethod public Image downloadImage(String imageName) {
        File image = new File(basePath+imageName);
        try {
            return ImageIO.read(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //conclave

    @WebMethod
	public List<Cardenal> getAllCardenales() {
    	dbManager.open();
		List<Cardenal>  cardenales=dbManager.getAllCardenales();
		dbManager.close();
		return cardenales;
		
	}

    @WebMethod
	public Conclave getConclaveActivo() {
    	dbManager.open();
    	Conclave c = dbManager.getConclaveActivo();
    	dbManager.close();
		return c;
	}

    @WebMethod
	public Conclave abrirConclave(Cardenal maestro, List<Cardenal> electores) throws ConclaveAlreadyOpenException {
    	dbManager.open();
    	Conclave c = dbManager.abrirConclave(maestro, electores);
    	dbManager.close();
    	return c;
	}

    @WebMethod
    public Votacion abrirVotacion() throws NoActiveVotingException {
		dbManager.open();
		Votacion votacion = dbManager.abrirVotacion();
		dbManager.close();
		return votacion;
	}

    @WebMethod
	public void emitirVoto(int electorId, String candidato) throws AlreadyVotedException, NoActiveVotingException {
    	dbManager.open();
    	dbManager.emitirVoto(electorId, candidato);
    	dbManager.close();
	}

    @WebMethod
	public String cerrarVotacion() throws VotingNotFinishedException, NoActiveVotingException {
    	dbManager.open();
    	String ganador = dbManager.cerrarVotacion();
    	dbManager.close();
    	return ganador;
    	
	}

    @WebMethod
	public Papa registrarPapa(String nombre, Date fechaNac, String nombreEscogido, int numPapa)
			throws NoActiveVotingException {
    	dbManager.open();
    	Papa papa = dbManager.registrarPapa(nombre, fechaNac, nombreEscogido, numPapa);
    	dbManager.close();
		return papa;
	}

    @WebMethod
	public void rechazarPapa() throws NoActiveVotingException {
		dbManager.open();
		dbManager.rechazarPapa();
		dbManager.close();
	}

    @WebMethod
	public Persona encontrarPersonaPorNombre(String nombre) {
    	dbManager.open();
    	Persona p = dbManager.encontrarPersonaPorNombre(nombre);
    	dbManager.close();
		return p;
	}

    
}

