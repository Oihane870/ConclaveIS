package dataAccess;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Seller;
import domain.Votacion;
import domain.Voto;
import domain.Cardenal;
import domain.Conclave;
import domain.Papa;
import domain.Persona;
import domain.Sale;
import exceptions.AlreadyVotedException;
import exceptions.ConclaveAlreadyOpenException;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.NoActiveVotingException;
import exceptions.SaleAlreadyExistException;
import exceptions.VotingNotFinishedException;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess  {
	private  EntityManager  db;
	private  EntityManagerFactory emf;
    private static final int baseSize = 160;

	private static final String basePath="src/main/resources/images/";
	private static final String dbServerDir = "src/main/resources/db/";


	ConfigXML c=ConfigXML.getInstance();

     public DataAccess()  {
		if (c.isDatabaseInitialized()) {
			String fileName=c.getDbFilename();

			if (!c.isDatabaseLocal()) fileName=dbServerDir+fileName;
			
			File fileToDelete= new File(fileName);
			if(fileToDelete.delete()){
				File fileToDeleteTemp= new File(fileName+"$");
				fileToDeleteTemp.delete();
				System.out.println("File deleted");
			 } else {
				 System.out.println("Operation failed");
				}
		}
		open();
		if  (c.isDatabaseInitialized()) 
			initializeDB();
		System.out.println("DataAccess created => isDatabaseLocal: "+c.isDatabaseLocal()+" isDatabaseInitialized: "+c.isDatabaseInitialized());

		close();

	}
     
    public DataAccess(EntityManager db) {
    	this.db=db;
    }

	
	
	/**
	 * This method  initializes the database with some products and sellers.
	 * This method is invoked by the business logic (constructor of BLFacadeImplementation) when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	public void initializeDB(){
		
		db.getTransaction().begin();

		try { 
	       
		    //Create sellers 
			Seller seller1=new Seller("seller1@gmail.com","Aitor Fernandez");
			Seller seller2=new Seller("seller22@gmail.com","Ane Gaztañaga");
			Seller seller3=new Seller("seller3@gmail.com","Test Seller");

			
			//Create products
			Date today = UtilDate.trim(new Date());
		
			
			seller1.addSale("futbol baloia", "oso polita, gutxi erabilita", 2, 10,  today, null);
			seller1.addSale("salomon mendiko botak", "44 zenbakia, 3 ateraldi",2, 20,  today, null);
			seller1.addSale("samsung 42\" telebista", "berria, erabili gabe", 2, 175,  today, null);


			seller2.addSale("imac 27", "7 urte, dena ondo dabil", 1, 200,today, null);
			seller2.addSale("iphone 17", "oso gutxi erabilita", 2, 400, today, null);
			seller2.addSale("orbea mendiko bizikleta", "29\" 10 urte, mantenua behar du", 3,225, today, null);
			seller2.addSale("polar kilor erlojua", "Vantage M, ondo dago", 3, 30, today, null);

			seller3.addSale("sukaldeko mahaia", "1.8*0.8, 4 aulkiekin. Prezio finkoa", 3,45, today, null);

			
			db.persist(seller1);
			db.persist(seller2);
			db.persist(seller3);
			
			
			//cargar cardenales
			Calendar cal = Calendar.getInstance();
			cal.set(1942, Calendar.SEPTEMBER, 20);
			Cardenal cardenal1 = new Cardenal("Jose Cobo Cano", cal.getTime(), "Arzobispo de Madrid" );
			
			cal.set(1943, Calendar.APRIL, 21);
			Cardenal cardenal2 = new Cardenal("Juan Jose Omella", cal.getTime(), "Arzobispo de Barcelona");
			
			cal.set(1945, Calendar.OCTOBER, 15);
			Cardenal cardenal3 = new Cardenal("Antonio Cañizares LLovera", cal.getTime(), "Arzobispo emerito de Valencia");
			
			cal.set(1943, Calendar.MAY, 16);
			Cardenal cardenal4 = new Cardenal("Carlos Osoro Sierra", cal.getTime(), "Arzobispo emerito de Madrid");
			
			cal.set(1955, Calendar.JANUARY, 17);
			Cardenal cardenal5 = new Cardenal("Pietro Parolin", cal.getTime(), "Secretario de Estado del Vaticano");
			
			cal.set(1943, Calendar.OCTOBER, 11);
			Cardenal cardenal6 = new Cardenal("Matteo Zuppi", cal.getTime(), "Arzobispo de Bolonia y presidente de la Conferencia Episcopal Italiana");
			
			cal.set(1944, Calendar.JANUARY, 4);
			Cardenal cardenal7 = new Cardenal("Angelo de Donatis", cal.getTime(),"Penitenciario Mayor de la Santa Sede" );
			
			cal.set(1943, Calendar.NOVEMBER, 3);
			Cardenal maestro = new Cardenal("Guido Marini", cal.getTime(), "Maestro de Ceremonias Pontificias");
			
			
			db.persist(cardenal1);
			db.persist(cardenal2);
			db.persist(cardenal3);
			db.persist(cardenal4);
			db.persist(cardenal5);
			db.persist(cardenal6);
			db.persist(cardenal7);
			db.persist(maestro);

	
			db.getTransaction().commit();
			System.out.println("Db initialized");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This method creates/adds a product to a seller
	 * 
	 * @param title of the product
	 * @param description of the product
	 * @param status 
	 * @param selling price
	 * @param category of a product
	 * @param publicationDate
	 * @return Product
 	 * @throws SaleAlreadyExistException if the same product already exists for the seller
	 */
	public Sale createSale(String title, String description, int status, float price,  Date pubDate, String sellerEmail, File file) throws  FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException {
		

		System.out.println(">> DataAccess: createProduct=> title= "+title+" seller="+sellerEmail);
		try {
		

			if(pubDate.before(UtilDate.trim(new Date()))) {
				throw new MustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.ErrorSaleMustBeLaterThanToday"));
			}
			if (file==null)
				throw new FileNotUploadedException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.ErrorFileNotUploadedException"));

			db.getTransaction().begin();
			
			Seller seller = db.find(Seller.class, sellerEmail);
			if (seller.doesSaleExist(title)) {
				db.getTransaction().commit();
				throw new SaleAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.SaleAlreadyExist"));
			}

			Sale sale = seller.addSale(title, description, status, price, pubDate, file);
			//next instruction can be obviated

			db.persist(seller); 
			db.getTransaction().commit();
			 System.out.println("sale stored "+sale+ " "+seller);

			return sale;
		} catch (NullPointerException e) {
			   e.printStackTrace();
			// TODO Auto-generated catch block
			db.getTransaction().commit();
			return null;
		}
		
		
	}
	
	/**
	 * This method retrieves all the products that contain a desc text in a title
	 * 
	 * @param desc the text to search
	 * @return collection of products that contain desc in a title
	 */
	public List<Sale> getSales(String desc) {
		System.out.println(">> DataAccess: getProducts=> from= "+desc);

		List<Sale> res = new ArrayList<Sale>();	
		TypedQuery<Sale> query = db.createQuery("SELECT s FROM Sale s WHERE s.title LIKE ?1",Sale.class);   
		query.setParameter(1, "%"+desc+"%");
		
		List<Sale> sales = query.getResultList();
	 	 for (Sale sale:sales){
		   res.add(sale);
		  }
	 	return res;
	}
	
	/**
	 * This method retrieves the products that contain a desc text in a title and the publicationDate today or before
	 * 
	 * @param desc the text to search
	 * @return collection of products that contain desc in a title
	 */
	public List<Sale> getPublishedSales(String desc, Date pubDate) {
		System.out.println(">> DataAccess: getProducts=> from= "+desc);

		List<Sale> res = new ArrayList<Sale>();	
		TypedQuery<Sale> query = db.createQuery("SELECT s FROM Sale s WHERE s.title LIKE ?1 AND s.pubDate <=?2",Sale.class);   
		query.setParameter(1, "%"+desc+"%");
		query.setParameter(2,pubDate);
		
		List<Sale> sales = query.getResultList();
	 	 for (Sale sale:sales){
		   res.add(sale);
		  }
	 	return res;
	}

public void open(){
		
		String fileName=c.getDbFilename();
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);
			  db = emf.createEntityManager();
    	   }
		System.out.println("DataAccess opened => isDatabaseLocal: "+c.isDatabaseLocal());

		
	}

	public BufferedImage getFile(String fileName) {
		File file=new File(basePath+fileName);
		BufferedImage targetImg=null;
		try {
             targetImg = rescale(ImageIO.read(file));
        } catch (IOException ex) {
            //Logger.getLogger(MainAppFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
		return targetImg;

	}
	
	public BufferedImage rescale(BufferedImage originalImage)
    {
		System.out.println("rescale "+originalImage);
        BufferedImage resizedImage = new BufferedImage(baseSize, baseSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, baseSize, baseSize, null);
        g.dispose();
        return resizedImage;
    }
	
	
	//metodos conclave
	public List<Cardenal> getAllCardenales(){
		System.out.println(">> DataAccess: getAllCardenales");
		TypedQuery<Cardenal> consulta = db.createQuery("SELECT c FROM Cardenal c", Cardenal.class);
		return consulta.getResultList();

	}
	public Conclave getConclaveActivo() {
		System.out.println(">> DataAccess: getConclaveActivo");
		TypedQuery<Conclave> consulta = db.createQuery("SELECT c FROM Conclave c WHERE c.papa IS NULL" , Conclave.class);
		if (consulta.getResultList().isEmpty()) {
			return null;
		}
		else {
			 return consulta.getResultList().get(0); 
		}

	}
	
	
	public Conclave abrirConclave(Cardenal maestro, List<Cardenal> electores)
	throws ConclaveAlreadyOpenException  {
		
		System.out.println(">> DataAccess: abrirConclave");
		if(this.getConclaveActivo() != null) {
			throw new ConclaveAlreadyOpenException(
				ResourceBundle.getBundle("Etiquetas").getString("Exceptions.ConclaveAlreadyOpen"));

		}
		db.getTransaction().begin();
		Conclave c = new Conclave(new Date(), maestro);
		for (Cardenal e: electores) {
			Cardenal managed = db.find(Cardenal.class, e.getId());
			c.addElector(managed);
		} 
		
		db.persist(c);
		db.getTransaction().commit();

		System.out.println("Conclave abierto: " + c);
		return c;
		
	}
	
	public Votacion abrirVotacion() 
			throws NoActiveVotingException{
		System.out.println(">> DataAccess: abrirVotacion");
		
		db.getTransaction().begin();
		Conclave c = this.getConclaveActivo();
		if(c.getVotacionActual()!= null) {
			db.getTransaction().commit();
			throw new NoActiveVotingException(
					ResourceBundle.getBundle("Etiquetas").getString("Exceptions.NoActiveVoting"));

		}
		
		Votacion v = c.abrirVotacion();
		db.persist(c);
		db.getTransaction().commit();

		System.out.println("Votacion abierta: " + v);
		return v;
		
	}
	public void emitirVoto(int electorId, String candidato)
			throws AlreadyVotedException, NoActiveVotingException {
		System.out.println(">> DataAccess: emitirVoto elector=" + electorId + " candidato=" + candidato);
		db.getTransaction().begin();
		Conclave c = this.getConclaveActivo();
		Votacion v = c.getVotacionActual();
		
		if(v == null) {
			db.getTransaction().commit();
			throw new NoActiveVotingException
			(ResourceBundle.getBundle("Etiquetas").getString("Exceptions.NoActiveVoting"));
		}
		Cardenal elector = db.find(Cardenal.class, electorId);
		if(v.yaVoto(elector)) {
			db.getTransaction().commit();
			throw new AlreadyVotedException(
					ResourceBundle.getBundle("Etiquetas").getString("Exceptions.AlreadyVoted"));

		}
		
		Voto voto = v.emitirVoto(elector, candidato);
		db.persist(voto);
		db.persist(c);
		db.getTransaction().commit();
		System.out.println("Se voto: " + voto);
	}
	
	public String cerrarVotacion() throws VotingNotFinishedException, NoActiveVotingException {
		System.out.println(">> DataAccess: cerrarVotacion");

		db.getTransaction().begin();
		Conclave c = this.getConclaveActivo();
		Votacion v = c.getVotacionActual();
		if(v == null) {
			db.getTransaction().commit();
			throw new NoActiveVotingException
			
			(ResourceBundle.getBundle("Etiquetas").getString("Exceptions.NoActiveVoting"));

		}
		
		if (!v.puedeSerCerrada()) {
			db.getTransaction().commit();
			throw new VotingNotFinishedException					
			(ResourceBundle.getBundle("Etiquetas").getString("Exceptions.VotingNotFinished"));

		}
		
		v.setHoraCierre(new Date());
		String ganador  = v.getCandidatoGanador();		
		 
		if (ganador == null) {
			v.setResultado("negra");
			Pantalla.enviarMensaje("Fumata negra");
		}
		db.persist(c);
		db.getTransaction().commit();

		return ganador; 
	}
	
	public Papa registrarPapa(String nombre, Date fechaNac, String nombreEscogido, int numPapa)throws NoActiveVotingException  {
		System.out.println(">> DataAccess: registrarPapa => " + nombreEscogido);
		db.getTransaction().begin();
		Conclave c = this.getConclaveActivo();
		List<Votacion> votaciones = c.getVotaciones();
		Votacion v = votaciones.get(votaciones.size() - 1);		
		if (v == null) {
			db.getTransaction().commit();
			throw new NoActiveVotingException
			(ResourceBundle.getBundle("Etiquetas").getString("Exceptions.NoActiveVoting"));

		}
		
		
		Papa p = new Papa(nombre, fechaNac, nombreEscogido, new Date(), numPapa  );
		db.persist(p);
		c.setPapa(p);
		v.setResultado("blanca");
		Pantalla.enviarMensaje("Fumata blanca");
		db.persist(c);
		db.getTransaction().commit();
		System.out.println("Nuevo papa registrado: " + p);

		return p;
	}
	
	public void rechazarPapa() throws NoActiveVotingException{
		System.out.println(">> DataAccess: rechazarPapa");
		db.getTransaction().begin();
		Conclave c = this.getConclaveActivo();
		List<Votacion> votaciones = c.getVotaciones();
		Votacion v = votaciones.get(votaciones.size() - 1);		
		if (v == null) {
			db.getTransaction().commit();
			throw new NoActiveVotingException
			(ResourceBundle.getBundle("Etiquetas").getString("Exceptions.NoActiveVoting"));
			

		}
		
		v.setResultado("negra");
		Pantalla.enviarMensaje("Fumata negra");
		db.persist(c);
		db.getTransaction().commit();
		System.out.println("No se ha podido registrar al nuevo papa: ");

		
	}
	
	public Persona encontrarPersonaPorNombre(String nombre) {
		System.out.println(">> DataAccess: encontrarPersonaPorNombre => " + nombre);

		TypedQuery<Persona> consulta = db.createQuery("SELECT p FROM Persona p where p.nombre = ?1", Persona.class);
		consulta.setParameter(1, nombre);
		List<Persona> result = consulta.getResultList();
		if (result.isEmpty()) return null;
		else {
			return result.get(0);
		}

	}
	
	
	
	
	public void close(){
		db.close();
		System.out.println("DataAcess closed");
	}
	
}
