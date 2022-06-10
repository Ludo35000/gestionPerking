package fr.eni.gestion_parking.bll;

import fr.eni.gestion_parking.bo.Personne;
import fr.eni.gestion_parking.bo.Voiture;
import fr.eni.gestion_parking.dal.DALException;
import fr.eni.gestion_parking.dal.DAOFactory;
import fr.eni.gestion_parking.dal.PersonneDAO;
import fr.eni.gestion_parking.dal.VoitureDAO;
import fr.eni.gestion_parking.utils.MonLogger;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Classe CatalogueManager
 *
 * @author lrabu
 */
public class PersonneManager {

    private static final Logger logger = MonLogger.getLogger(PersonneManager.class.getSimpleName());
    private static volatile PersonneManager instance;
    private PersonneDAO personneDAO;
    private VoitureDAO voitureDAO;

    /**
     * Constructeur CatalogueManager
     */
    private PersonneManager(){
        this.personneDAO = DAOFactory.getPersonneDao();
        this.voitureDAO = DAOFactory.getVoitureDao();
    }

    /**
     * Retourne l'instance de CatalogueManager
     * @return l'instance
     */
    public static synchronized PersonneManager getInstance() {
        if (instance == null)
            instance = new PersonneManager();
        return instance;
    }

    /**
     * Retourne le liste des personnes
     * @return liste des personnes
     * @throws BLLException
     */
    public List<Personne> getListPersonne() throws BLLException {
        try {
            logger.info("PersonneManager --> selectAll");
            return personneDAO.selectAll();
        } catch (DALException e) {
            logger.severe(e.getMessage());
            throw new BLLException(e.getMessage(), BLLExceptionType.OTHER);
        }
    }

    /**
     * Retourne le liste des personnes sans voitures
     * @return liste des personnes
     * @throws BLLException
     */
    public List<Personne> getListPersonneWithoutVoiture() throws BLLException {
        try {
            logger.info("PersonneManager --> getListPersonneWithoutVoiture");
            return personneDAO.selectAllWithoutVoiture();
        } catch (DALException e) {
            logger.severe(e.getMessage());
            throw new BLLException(e.getMessage(), BLLExceptionType.OTHER);
        }
    }

    /**
     * Ajouter une personnes
     * @param personne la personne à ajouter
     * @return la personne ajoutée
     * @throws BLLException
     */
    public Personne addPersonne(final Personne personne) throws BLLException {
        try {
            logger.info("PersonneManager --> addPersonne");
            validerPersonne(personne);

            if (personne.getId() != null) {
                String error = "La personne est déjà présente en base de données";
                logger.severe(error);
                throw new BLLException(error, BLLExceptionType.ERROR_PERSONNE_PRESENTE_BASE);
            }
            return personneDAO.insert(personne);
        } catch (DALException e) {
            logger.severe(e.getMessage());
            throw new BLLException(e.getMessage(), BLLExceptionType.OTHER);
        }
    }

    /**
     * Modifier une personne
     * @param personne la personne à modifier
     * @return la personne modifier
     * @throws BLLException
     */
    public Personne updatePersonne(final Personne personne) throws BLLException {
        try {
            logger.info("PersonneManager --> updatePersonne");
            validerPersonne(personne);

            if (personne.getId() == null) {
                String error = "La personne n'est pas présente en base de données";
                logger.severe(error);
                throw new BLLException(error, BLLExceptionType.ERROR_PERSONNE_NON_PRESENTE_BASE);
            }

            return personneDAO.update(personne);
        } catch (DALException e) {
            logger.severe(e.getMessage());
            throw new BLLException(e.getMessage(), BLLExceptionType.OTHER);
        }
    }

    /**
     * Supprimer une personne
     * @param personne la personne
     * @return true -> Supprimer; false -> Erreur
     * @throws BLLException
     */
    public boolean deletePersonnse(final Personne personne) throws BLLException {
        try {
            logger.info("PersonneManager --> deletePersonnse");
            validerPersonne(personne);

            if (personne.getId() == null) {
                String error = "La personne n'est pas présente en base de données";
                logger.severe(error);
                throw new BLLException(error, BLLExceptionType.ERROR_PERSONNE_NON_PRESENTE_BASE);
            }

            if (voitureDAO.selectAll().stream().anyMatch(voiture -> voiture.getPersonne() != null && Objects.equals(voiture.getPersonne().getId(), personne.getId()))) {
                String error = "La personne est associé à au moins une voiture";
                logger.severe(error);
                throw new BLLException(error, BLLExceptionType.ERROR_PERSONNE_ASSOCIE_VOITURE);
            }


            return personneDAO.delete(personne);
        } catch (DALException e) {
            logger.severe(e.getMessage());
            throw new BLLException(e.getMessage(), BLLExceptionType.OTHER);
        }
    }

    /**
     * Valide une personne avant n'import quelle requête
     * @param personne la personne à valider
     * @throws BLLException
     */
    private void validerPersonne(Personne personne) throws BLLException {
        logger.info("PersonneManager --> validerPersonne");
        if (personne.getNom() == null || personne.getNom().isBlank()) {
            String error = "Le nom d'une personne ne peut pas être vide ou initilisé à rien";
            logger.severe(error);
            throw new BLLException(error, BLLExceptionType.ERROR_PERSONNE_NOM_VIDE);
        }
        if (personne.getPrenom() == null || personne.getPrenom().isBlank()) {
            String error = "Le prenom d'une personne ne peut pas être vide ou initilisé à rien";
            logger.severe(error);
            throw new BLLException(error, BLLExceptionType.ERROR_PERSONNE_PRENOM_VIDE);
        }
    }
}
