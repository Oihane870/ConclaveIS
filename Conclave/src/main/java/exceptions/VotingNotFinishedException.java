package exceptions;

public class VotingNotFinishedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public VotingNotFinishedException(String mensaje) {
		super(mensaje);
	}

}
