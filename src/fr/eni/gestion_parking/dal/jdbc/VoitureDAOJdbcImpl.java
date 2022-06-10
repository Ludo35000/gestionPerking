package fr.eni.gestion_parking.dal.jdbc;

import fr.eni.gestion_parking.bo.Voiture;
import fr.eni.gestion_parking.dal.DALException;
import fr.eni.gestion_parking.dal.DAOFactory;
import fr.eni.gestion_parking.dal.PersonneDAO;
import fr.eni.gestion_parking.dal.VoitureDAO;
import fr.eni.gestion_parking.utils.MonLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Classe VoitureDAOJdbcImpl
 *
 * @author lrabu
 */
public class VoitureDAOJdbcImpl implements VoitureDAO {

    private static final String INSERT = "INSERT INTO Voitures VALUES(?, ?, ?)";
    private static final String UPDATE = "UPDATE Voitures SET nom = ?, plaqueImmatriculation = ?, fkPersonne = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM Voitures WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM Voitures";

    private static final PersonneDAO personneDAO = DAOFactory.getPersonneDao();
    private static final Logger logger = MonLogger.getLogger(VoitureDAOJdbcImpl.class.getSimpleName());

    /**
     * Ajouter une voiture
     * @param voiture la voiture à ajouer
     * @return la nouvelle voiture
     * @throws DALException
     */
    @Override
    public Voiture insert(final Voiture voiture) throws DALException {
        Voiture newVoiture = null;
        logger.info("VoitureDAOJdbcImpl --> insert");

        try (Connection connection = JdbcTools.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, voiture.getNom());
            preparedStatement.setString(2, voiture.getPlaqueImmatriculation().toUpperCase());

            if (voiture.getPersonne() == null) {
                preparedStatement.setNull(3, Types.INTEGER);
            } else {
                preparedStatement.setInt(3, voiture.getPersonne().getId());
            }

            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next()) {
                newVoiture = new Voiture(voiture.getNom(), voiture.getPlaqueImmatriculation());
                newVoiture.setPersonne(voiture.getPersonne());
                newVoiture.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new DALException(e.getMessage());
        }

        return newVoiture;
    }

    /**
     * Modifier une voiture
     * @param voiture la voiture à modifier
     * @return la voiture modifier
     * @throws DALException
     */
    @Override
    public Voiture update(final Voiture voiture) throws DALException {
        Voiture updatedVoiture = null;
        logger.info("VoitureDAOJdbcImpl --> update");

        try (Connection connection = JdbcTools.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, voiture.getNom());
            preparedStatement.setString(2, voiture.getPlaqueImmatriculation().toUpperCase());

            if (voiture.getPersonne() == null) {
                preparedStatement.setNull(3, Types.INTEGER);
            } else {
                preparedStatement.setInt(3, voiture.getPersonne().getId());
            }

            preparedStatement.setInt(4, voiture.getId());

            if(preparedStatement.execute()){
                updatedVoiture = new Voiture(voiture.getNom(), voiture.getPlaqueImmatriculation());
                updatedVoiture.setPersonne(voiture.getPersonne());
                updatedVoiture.setId(voiture.getId());
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }

        return updatedVoiture;
    }

    /**
     * Supprimer une voiture
     * @param voiture la voiture supprimer
     * @return true -> Supprimer; false -> Erreur
     * @throws DALException
     */
    @Override
    public boolean delete(Voiture voiture) throws DALException {
        logger.info("VoitureDAOJdbcImpl --> delete");
        try (Connection connection = JdbcTools.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1, voiture.getId());

            return preparedStatement.execute();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Retourne toutes les voiture
     * @return la liste des voitures
     * @throws DALException
     */
    @Override
    public List<Voiture> selectAll() throws DALException {
        List<Voiture> voitures = new ArrayList<>();
        logger.info("VoitureDAOJdbcImpl --> selectAll");

        try (Connection connection = JdbcTools.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL, Statement.RETURN_GENERATED_KEYS)){
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                voitures.add(itemBuilder(resultSet));
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
        return voitures;
    }

    /**
     * Créer une voiture depuis le resultSet
     * @param resultSet le resultSet
     * @return la voiture
     * @throws SQLException
     */
    private Voiture itemBuilder(ResultSet resultSet) throws SQLException, DALException {
        logger.info("VoitureDAOJdbcImpl --> itemBuilder");
        Voiture voiture = new Voiture(resultSet.getString(2), resultSet.getString(3));
        voiture.setId(resultSet.getInt(1));
        resultSet.getInt(4);
        voiture.setPersonne(personneDAO.selectById(resultSet.getInt(4)));
        return voiture;
    }
}
