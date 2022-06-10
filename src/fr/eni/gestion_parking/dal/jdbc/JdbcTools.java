package fr.eni.gestion_parking.dal.jdbc;

import fr.eni.gestion_parking.dal.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe JdbcTools
 *
 * @author lrabu
 */
public class JdbcTools {

    private static final String URL_DB = "urlDb";
    private static final String USER_DB = "userDb";
    private static final String PASSWORD_DB = "passwordDb";

    /**
     * Retourne la connexion à la base de données
     * @return la connexion
     * @throws SQLException l'exception
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(Settings.getProperty(URL_DB), Settings.getProperty(USER_DB), Settings.getProperty(PASSWORD_DB));
    }

}
