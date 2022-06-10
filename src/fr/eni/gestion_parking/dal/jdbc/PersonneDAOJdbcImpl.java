package fr.eni.gestion_parking.dal.jdbc;

import fr.eni.gestion_parking.bo.Personne;
import fr.eni.gestion_parking.dal.DALException;
import fr.eni.gestion_parking.dal.PersonneDAO;
import fr.eni.gestion_parking.utils.MonLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PersonneDAOJdbcImpl implements PersonneDAO {

    private static final String INSERT = "INSERT INTO Personnes VALUES (?, ?)";
    private static final String UPDATE = "UPDATE Personnes SET nom = ?, prenom = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM Personnes WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM Personnes";
    private static final String SELECT_BY_ID = "SELECT * FROM Personnes WHERE id = ?";
    private static final String SELECT_ALL_WITHOUT_VOITURE = "SELECT * FROM Personnes WHERE id NOT IN (SELECT DISTINCT(fkPersonne) FROM Voitures WHERE fkPersonne IS NOT NULL)";

    private static final Logger logger = MonLogger.getLogger(PersonneDAOJdbcImpl.class.getSimpleName());

    /**
     * Ajouter une personne
     * @param personne la personne à ajouter
     * @return la nouvelle personne
     * @throws DALException
     */
    @Override
    public Personne insert(final Personne personne) throws DALException {
        Personne newPersonne = null;
        logger.info("PersonneDAOJdbcImpl --> insert");

        try (Connection connection = JdbcTools.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, personne.getNom().toUpperCase());
            preparedStatement.setString(2, personne.getPrenom());

            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newPersonne = new Personne(personne.getNom(), personne.getPrenom());
                newPersonne.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new DALException(e.getMessage());
        }

        return newPersonne;
    }

    /**
     * Modifier une personne
     * @param personne la personner à modifier
     * @return la personne modifié
     * @throws DALException
     */
    @Override
    public Personne update(final Personne personne) throws DALException {
        Personne updatedPersonne = null;
        logger.info("PersonneDAOJdbcImpl --> update");

        try (Connection connection = JdbcTools.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, personne.getNom().toUpperCase());
            preparedStatement.setString(2, personne.getPrenom());
            preparedStatement.setInt(3, personne.getId());

            if (preparedStatement.execute()) {
                updatedPersonne = new Personne(personne.getNom(), personne.getPrenom());
                updatedPersonne.setId(personne.getId());
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new DALException(e.getMessage());
        }

        return updatedPersonne;
    }

    /**
     * Supprimer une personne
     * @param personne la personne à supprimer
     * @return true -> Supprimer; false -> Erreur
     * @throws DALException
     */
    @Override
    public boolean delete(final Personne personne) throws DALException {
        logger.info("PersonneDAOJdbcImpl --> delete");
        try (Connection connection = JdbcTools.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, personne.getId());

            return preparedStatement.execute();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new DALException(e.getMessage());
        }
    }

    /**
     * Retourne toutes les personnes
     * @return la listes des personnes
     * @throws DALException
     */
    @Override
    public List<Personne> selectAll() throws DALException {
        logger.info("PersonneDAOJdbcImpl --> selectAll");
        return customSelectAll(SELECT_ALL);
    }

    /**
     * Retourne une personnes par id
     * @return la personne
     * @throws DALException
     */
    @Override
    public Personne selectById(int id) throws DALException {
        Personne personne = null;
        logger.info("PersonneDAOJdbcImpl --> selectById");

        try (Connection connection = JdbcTools.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                personne = itemBuilder(resultSet);
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
        return personne;
    }


    /**
     * Retourne toutes les personnes sans voitures
     * @return
     * @throws DALException
     */
    @Override
    public List<Personne> selectAllWithoutVoiture() throws DALException {
        logger.info("PersonneDAOJdbcImpl --> selectAllWithoutVoiture");
        return customSelectAll(SELECT_ALL_WITHOUT_VOITURE);

    }

    /**
     * execute custon select ALL
     * @param requete La requete SQL
     * @return la liste des personnes
     * @throws DALException
     */
    private List<Personne> customSelectAll(String requete) throws DALException {
        List<Personne> personnes = new ArrayList<>();
        logger.info("PersonneDAOJdbcImpl --> customSelectAll");

        try (Connection connection = JdbcTools.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS)){
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                personnes.add(itemBuilder(resultSet));
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new DALException(e.getMessage());
        }

        return personnes;
    }

    /**
     * Créer une Personne depuis le resultSet
     * @param resultSet le resultSet
     * @return la personnes
     * @throws SQLException
     */
    private Personne itemBuilder(ResultSet resultSet) throws SQLException {
        logger.info("PersonneDAOJdbcImpl --> itemBuilder");
        Personne personne = new Personne(resultSet.getString(2).toUpperCase(), resultSet.getString(3));
        personne.setId(resultSet.getInt(1));
        return personne;
    }
}
