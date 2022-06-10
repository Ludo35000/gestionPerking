package fr.eni.gestion_parking.dal;

import fr.eni.gestion_parking.bo.Personne;

import java.util.List;

/**
 * Interface PersonneDAO
 *
 * @author lrabu
 */
public interface PersonneDAO {

    /**
     * Ajouter une personne
     * @param personne la personne à ajouter
     * @return la nouvelle personne
     * @throws DALException la DALException
     */
    Personne insert(final Personne personne) throws DALException;

    /**
     * Modifier une personne
     * @param personne la personne à modifier
     * @return la personne modifiée
     * @throws DALException la DALException
     */
    Personne update(final Personne personne) throws DALException;

    /**
     * Supprimer une personne
     * @param personne la personne à supprimer
     * @return true -> Supprimer; false -> Erreur
     * @throws DALException la DALException
     */
    boolean delete(final Personne personne) throws DALException;

    /**
     * Retourne toutes les personnes
     * @return la listes des personnes
     * @throws DALException la DALException
     */
    List<Personne> selectAll() throws DALException;

    /**
     * Retourne une personne par id
     * @return la personne
     * @throws DALException la DALException
     */
    Personne selectById(int id) throws DALException;

    /**
     * Retourne toutes les personnes sans voitures
     * @return la liste des personnes sans voitures
     * @throws DALException la DALException
     */
    List<Personne> selectAllWithoutVoiture() throws DALException;
}
