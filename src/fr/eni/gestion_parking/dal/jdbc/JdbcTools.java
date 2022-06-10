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

    private static String urlDb = "urlDb";
    private static String userDb = "userDb";
    private static String passwordDb = "passwordDb";

    /**
     * Retourne la connexion à la base de données
     * @return la connexion
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(Settings.getProperty(urlDb), Settings.getProperty(userDb), Settings.getProperty(passwordDb));
    }

}
