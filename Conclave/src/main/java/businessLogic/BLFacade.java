package businessLogic;

import java.io.File;
import java.util.Date;
import java.util.List;

import domain.Cardenal;
import domain.Conclave;
import domain.Papa;
import domain.Persona;
import domain.Sale;
import domain.Votacion;
import exceptions.AlreadyVotedException;
import exceptions.ConclaveAlreadyOpenException;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.NoActiveVotingException;
import exceptions.SaleAlreadyExistException;
import exceptions.VotingNotFinishedException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.awt.image.BufferedImage;
import java.awt.Image;

import gui.*;
/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BLFacade  {
	  

	/**
	 * This method creates/adds a product to a seller
	 * 
	 * @param title of the product
	 * @param description of the product
	 * @param status 
	 * @param selling price
	 * @param category of a product
	 * @param publicationDate
	 * @return Sale
	 */
   @WebMethod
	public Sale createSale(String title, String description, int status, float price, Date pubDate, String sellerEmail, File file) throws  FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException;
	
	
	/**
	 * This method retrieves the products that contain desc
	 * 
	 * @param desc the text to search
	 * @return collection of sales that contain desc 
	 */
	@WebMethod public List<Sale> getSales(String desc);
	
	/**
	 * 	 * This method retrieves the products that contain a desc text in a title and the publicationDate today or before
	 * 
	 * @param desc the text to search
	 * @param pubDate the date  of the publication date
	 * @return collection of sales that contain desc and published before pubDate
	 */
	@WebMethod public List<Sale> getPublishedSales(String desc, Date pubDate);

	
	/**
	 * This method calls the data access to initialize the database with some sellers and products.
	 * It is only invoked  when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	@WebMethod public void initializeBD();
	
		
	@WebMethod public Image downloadImage(String imageName);
	
	
	//conclave
	
	
	@WebMethod public List<Cardenal> getAllCardenales();
	@WebMethod public Conclave getConclaveActivo();
	@WebMethod public Conclave abrirConclave(Cardenal maestro, List<Cardenal> electores)
			throws ConclaveAlreadyOpenException;
	
	@WebMethod 	public Votacion abrirVotacion() throws NoActiveVotingException;

	@WebMethod public void emitirVoto(int electorId, String candidato)
			throws AlreadyVotedException, NoActiveVotingException;
	
	@WebMethod public String cerrarVotacion() throws VotingNotFinishedException, NoActiveVotingException;
	
	@WebMethod public Papa registrarPapa(String nombre, Date fechaNac, String nombreEscogido, int numPapa)throws NoActiveVotingException;
	
	
	@WebMethod public void rechazarPapa() throws NoActiveVotingException;
	
	@WebMethod public Persona encontrarPersonaPorNombre(String nombre);


	

	
}
