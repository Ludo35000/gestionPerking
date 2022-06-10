package fr.eni.gestion_parking.dal;

import fr.eni.gestion_parking.utils.MonLogger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Classe Settings
 *
 * @author lrabu
 */
public class Settings {
    private static Properties properties;

    public static final Logger logger = MonLogger.getLogger(Settings.class.getSimpleName());

    static {
        try (InputStream input = Settings.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties = new Properties();
            properties.load(input);
        } catch (FileNotFoundException e) {
            logger.severe(e.getMessage());
        } catch (IOException e) {
            logger.severe(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Retourne la valeur de la propriété associé à la clé
     * @param key la clé
     * @return la valeur
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
