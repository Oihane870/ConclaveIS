package exceptions;

public class AlreadyVotedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AlreadyVotedException(String mensaje) {
		super(mensaje);
	}
}
