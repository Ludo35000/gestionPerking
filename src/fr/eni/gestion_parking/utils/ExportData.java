package fr.eni.gestion_parking.utils;

import fr.eni.gestion_parking.bo.Personne;
import fr.eni.gestion_parking.bo.Voiture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Classe ExportData
 *
 * @author lrabu
 */
public class ExportData {

    private static final Logger logger = MonLogger.getLogger(ExportData.class.getSimpleName());

    /**
     * Export les données en XML
     * @param voitureList la liste des voitures
     * @param personneList la liste des personnes sans voitures
     */
    public static void exportDataXml(List<Voiture> voitureList, List<Personne> personneList) {
        try {
            logger.info("ExportData --> exportDataXml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("parking");
            doc.appendChild(rootElement);

            Element voitures = doc.createElement("voitures");
            rootElement.appendChild(voitures);

            for (Voiture voiture : voitureList) {
                exportDataXmlVoiture(doc, voitures, voiture);
            }

            Element personnes = doc.createElement("personnes");
            rootElement.appendChild(personnes);

            for (Personne personne : personneList) {
                exportDataXmlPersonne(doc, personnes, personne);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("D:\\TP\\GestionParking\\export.xml"));
            transformer.transform(source, result);

        } catch (Exception e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Export les données en CSV
     * @param voitureList la liste des voitures
     * @param personneList la listes de perssonages sans voitures
     */
    public static void exportDataCsv(List<Voiture> voitureList, List<Personne> personneList) {
        StringBuilder sb = new StringBuilder();
        logger.info("ExportData --> exportDataCsv");

        //Header
        sb.append("id").append(";");
        sb.append("nom").append(";");
        sb.append("prenom").append(";");
        sb.append("id").append(";");
        sb.append("nom").append(";");
        sb.append("plaqueImmatriculation").append(";").append("\n");

        //Voiture
        for (Voiture voiture : voitureList) {
            if (voiture.getPersonne() == null) {
                sb.append(";").append(";").append(";");
            } else {
                sb.append(voiture.getPersonne().getId()).append(";");
                sb.append(voiture.getPersonne().getNom()).append(";");
                sb.append(voiture.getPersonne().getPrenom()).append(";");
            }
            sb.append(voiture.getId()).append(";");
            sb.append(voiture.getNom()).append(";");
            sb.append(voiture.getPlaqueImmatriculation()).append(";").append("\n");
        }

        //Personne
        for (Personne personne : personneList) {
            sb.append(personne.getId()).append(";");
            sb.append(personne.getNom()).append(";");
            sb.append(personne.getPrenom()).append(";");
            sb.append(";").append(";").append(";").append("\n");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\TP\\GestionParking\\export.csv"));
            writer.write(sb.toString());

            writer.close();
        } catch (IOException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert Personne en XML
     * @param doc le document
     * @param parent l'element parent
     * @param personne la personne
     */
    private static void exportDataXmlPersonne(Document doc, Element parent, Personne personne) {
        logger.info("ExportData --> exportDataXmlPersonne");
        Element personneElem = doc.createElement("personne");
        constructHeaderXML(doc, parent, personne.getId(), personne.getNom(), personneElem);

        Element prenomElem = doc.createElement("prenom");
        prenomElem.appendChild(doc.createTextNode(personne.getPrenom()));
        personneElem.appendChild(prenomElem);
    }

    /**
     * Convert Voiture en XML
     * @param doc le document
     * @param parent l'element parent
     * @param voiture la voiture
     */
    private static void exportDataXmlVoiture(Document doc, Element parent, Voiture voiture) {
        logger.info("ExportData --> exportDataXmlVoiture");
        Element voitureElem = doc.createElement("voiture");
        constructHeaderXML(doc, parent, voiture.getId(), voiture.getNom(), voitureElem);

        Element voiturePi = doc.createElement("plaqueImmatriculation");
        voiturePi.appendChild(doc.createTextNode(voiture.getPlaqueImmatriculation()));
        voitureElem.appendChild(voiturePi);

        if (voiture.getPersonne() != null) {
            exportDataXmlPersonne(doc, voitureElem, voiture.getPersonne());
        }
    }

    /**
     * Création du header commun
     * @param doc le document
     * @param id l'identifiant
     * @param nom le nom
     * @param element l'element parent
     */
    private static void constructHeaderXML(Document doc, Element parent, int id, String nom, Element element) {
        logger.info("ExportData --> constructHeaderXML");
        parent.appendChild(element);

        Element idElem = doc.createElement("id");
        idElem.appendChild(doc.createTextNode(String.valueOf(id)));
        element.appendChild(idElem);

        Element voitureName = doc.createElement("nom");
        voitureName.appendChild(doc.createTextNode(nom));
        element.appendChild(voitureName);
    }
}