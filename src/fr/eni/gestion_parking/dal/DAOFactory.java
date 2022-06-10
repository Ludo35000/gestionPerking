package fr.eni.gestion_parking.dal;

import fr.eni.gestion_parking.dal.jdbc.PersonneDAOJdbcImpl;
import fr.eni.gestion_parking.dal.jdbc.VoitureDAOJdbcImpl;

/**
 * CLasse DAOFactory
 */
public class DAOFactory {

    /**
     * Retourne l'instance pour editer les voiture
     * @return l'instance
     */
    public static VoitureDAO getVoitureDao() {
        return new VoitureDAOJdbcImpl();
    }

    /**
     * Retourne l'instance pour editer les personnes
     * @return l'instance
     */
    public static PersonneDAO getPersonneDao() {
        return new PersonneDAOJdbcImpl();
    }
}
