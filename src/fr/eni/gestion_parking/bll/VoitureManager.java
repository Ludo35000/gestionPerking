package fr.eni.gestion_parking.bll;

import fr.eni.gestion_parking.bo.Personne;
import fr.eni.gestion_parking.bo.Voiture;
import fr.eni.gestion_parking.dal.DALException;
import fr.eni.gestion_parking.dal.DAOFactory;
import fr.eni.gestion_parking.dal.PersonneDAO;
import fr.eni.gestion_parking.dal.VoitureDAO;
import fr.eni.gestion_parking.utils.MonLogger;

import java.util.List;
import java.util.logging.Logger;

/**
 * Classe CatalogueManager
 *
 * @author lrabu
 */
public class VoitureManager {

    private static final Logger logger = MonLogger.getLogger(VoitureManager.class.getSimpleName());
    private static volatile VoitureManager instance;
    private VoitureDAO voitureDAO;

    /**
     * Constructeur CatalogueManager
     */
    private VoitureManager(){
        this.voitureDAO = DAOFactory.getVoitureDao();
    }

    /**
     * Retourne l'instance de CatalogueManager
     * @return l'instance
     */
    public static synchronized VoitureManager getInstance() {
        if (instance == null)
            instance = new VoitureManager();
        return instance;
    }

    /**
     * Retourne la liste des voitures
     * @return liste des voitures
     * @throws BLLException
     */
    public List<Voiture> getListVoiture() throws BLLException {
        try {
            logger.info("VoitureManager --> getListVoiture");
            return voitureDAO.selectAll();
        } catch (DALException e) {
            logger.severe(e.getMessage());
            throw new BLLException(e.getMessage(), BLLExceptionType.OTHER);
        }
    }

    /**
     * Ajoute une voiture
     * @param voiture la voiture à ajouter
     * @return la voiture ajoutée
     * @throws BLLException
     */
    public Voiture addVoiture(final Voiture voiture) throws BLLException {
        try {
            logger.info("VoitureManager --> addVoiture");
            validerVoiture(voiture);

            if (voiture.getId() != null) {
                String error = "La voiture est déjà présente en base de données";
                logger.severe(error);
                throw new BLLException(error, BLLExceptionType.ERROR_VOITURE_PRESENTE_BASE);
            }

            return voitureDAO.insert(voiture);
        } catch (DALException e) {
            logger.severe(e.getMessage());
            throw new BLLException(e.getMessage(), BLLExceptionType.OTHER);
        }
    }

    /**
     * Modifier une voiture
     * @param voiture la voiture à modifier
     * @return la voiture modifier
     * @throws BLLException
     */
    public Voiture updateVoiture(final Voiture voiture) throws BLLException {
        try {
            logger.info("VoitureManager --> updateVoiture");
            validerVoiture(voiture);

            if (voiture.getId() == null) {
                String error = "La voiture n'est pas présente en base de données";
                logger.severe(error);
                throw new BLLException(error, BLLExceptionType.ERROR_VOITURE_NON_PRESENTE_BASE);
            }

            return voitureDAO.update(voiture);
        } catch (DALException e) {
            logger.severe(e.getMessage());
            throw new BLLException(e.getMessage(), BLLExceptionType.OTHER);
        }
    }

    /**
     * Supprime une voiture
     * @param voiture la voiture a supprimer
     * @return true -> Supprimer; false -> Erreur
     * @throws BLLException
     */
    public boolean deleteVoiture(final Voiture voiture) throws BLLException {
        try {
            logger.info("VoitureManager --> deleteVoiture");
            validerVoiture(voiture);

            if (voiture.getId() == null) {
                String error = "La voiture n'est pas présente en base de données";
                logger.severe(error);
                throw new BLLException(error, BLLExceptionType.ERROR_VOITURE_NON_PRESENTE_BASE);
            }

            return voitureDAO.delete(voiture);
        } catch (DALException e) {
            logger.severe(e.getMessage());
            throw new BLLException(e.getMessage(), BLLExceptionType.OTHER);
        }
    }

    /**
     * Valide une voiture avant n'import quelle requête
     * @param voiture la voiture à valider
     * @throws BLLException
     */
    private void validerVoiture(Voiture voiture) throws BLLException {
        logger.info("VoitureManager --> validerVoiture");
        if (voiture.getNom() == null || voiture.getNom().isBlank()) {
            String error = "Le nom d'une voiture ne peut pas être vide ou initilisé à rien";
            logger.severe(error);
            throw new BLLException(error, BLLExceptionType.ERROR_VOITURE_NOM_VIDE);
        }
        if (voiture.getPlaqueImmatriculation() == null || voiture.getPlaqueImmatriculation().isBlank()) {
            String error = "La plaque d'immatriculation d'une voiture ne peut pas être vide ou initilisé à rien";
            logger.severe(error);
            throw new BLLException(error, BLLExceptionType.ERROR_VOITURE_PLAQUE_VIDE);
        }
        if (!voiture.getPlaqueImmatriculation().toUpperCase().matches("[A-Z]{2}[-][0-9]{3}[-][A-Z]{2}")) {
            String error = "La plaque d'immatriculation ne correspond pas à ce qui est attendu : AA-000-AA";
            logger.severe(error);
            throw new BLLException(error, BLLExceptionType.ERROR_VOITURE_PLAQUE_FORMAT);
        }
        if (voiture.getPlaqueImmatriculation().length() != 9) {
            String error = "La plaque d'immatriculation d'une voiture doit avoir 7 caractères";
            logger.severe(error);
            throw new BLLException(error, BLLExceptionType.ERROR_VOITURE_PLAQUE_TAILLE);
        }
    }
}
