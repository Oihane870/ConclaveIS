package exceptions;

public class NoActiveVotingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoActiveVotingException(String mensaje) {
		super(mensaje);
	}

}
