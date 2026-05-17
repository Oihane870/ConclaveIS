package exceptions;

public class ConclaveAlreadyOpenException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ConclaveAlreadyOpenException(String mensaje) {
		super(mensaje);
	}

}
