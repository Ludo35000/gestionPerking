package fr.eni.gestion_parking.bll;

/**
 * Classe BLLException
 *
 * @author lrabu
 */
public class BLLException extends Exception{

    private final BLLExceptionType exceptionType;

    /**
     * Constructeur BLLException
     */
    public BLLException(BLLExceptionType exceptionType) {
        super();
        this.exceptionType = exceptionType;
    }

    /**
     * Constructeur BLLException
     * @param message message
     */
    public BLLException(String message, BLLExceptionType exceptionType) {
        super(message);
        this.exceptionType = exceptionType;
    }

    /**
     * Constructeur BLLException
     * @param message message
     * @param exc exception
     */
    public BLLException(String message, Throwable exc, BLLExceptionType exceptionType) {
        super(message, exc);
        this.exceptionType = exceptionType;
    }

    /**
     * Retourne le message
     * @return le message
     */
    @Override
    public String getMessage() {
        return "Couche BLL - " + super.getMessage();
    }

    /**
     * Retourne le type de l'exception
     * @return le type
     */
    public BLLExceptionType getExceptionType() {
        return exceptionType;
    }
}
