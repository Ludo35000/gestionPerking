package fr.eni.gestion_parking.dal;

import fr.eni.gestion_parking.bo.Voiture;

import java.util.List;

/**
 * Interface VoitureDAO
 *
 * @author lrabu
 */
public interface VoitureDAO {

    /**
     * Ajouter une voiture
     * @param voiture la voiture à ajouer
     * @return la nouvelle voiture
     * @throws DALException
     */
    Voiture insert(final Voiture voiture) throws DALException;

    /**
     * Modifier une voiture
     * @param voiture la voiture à modifier
     * @return la voiture modifier
     * @throws DALException
     */
    Voiture update(final Voiture voiture) throws DALException;

    /**
     * Supprimer une voiture
     * @param voiture la voiture supprimer
     * @return true -> Supprimer; false -> Erreur
     * @throws DALException
     */
    boolean delete(final Voiture voiture) throws DALException;

    /**
     * Retourne toutes les voiture
     * @return la liste des voitures
     * @throws DALException
     */
    List<Voiture> selectAll() throws DALException;
}
