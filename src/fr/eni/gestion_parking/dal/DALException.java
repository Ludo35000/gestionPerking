package fr.eni.gestion_parking.dal;

/**
 * Classe DALException
 *
 * @author lrabu
 */
public class DALException extends Exception {

	/**
	 * Constructeur DALException
	 */
	public DALException() {
		super();
	}

	/**
	 * Constructeur DALException
	 * @param message le message
	 */
	public DALException(String message) {
		super(message);
	}

	/**
	 * Constructeur DALException
	 * @param message le message
	 * @param exception l'exception
	 */
	public DALException(String message, Throwable exception) {
		super(message, exception);
	}

	/**
	 * Retourne le message
	 * @return le message
	 */
	@Override
	public String getMessage() {
		StringBuffer sb = new StringBuffer("Couche DAL - ");
		sb.append(super.getMessage());
		
		return sb.toString() ;
	}
	
	
}
