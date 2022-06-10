package fr.eni.gestion_parking.ihm;

import fr.eni.gestion_parking.bll.BLLException;
import fr.eni.gestion_parking.bll.BLLExceptionType;
import fr.eni.gestion_parking.bll.PersonneManager;
import fr.eni.gestion_parking.bll.VoitureManager;
import fr.eni.gestion_parking.bo.Personne;
import fr.eni.gestion_parking.bo.Voiture;
import fr.eni.gestion_parking.utils.ExportData;
import fr.eni.gestion_parking.utils.MonLogger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.logging.Logger;

/**
 * classe Controller
 *
 * @author lrabu
 */
public class Controller {

    @FXML
    public TableView<Voiture> tableViewVoitures;
    @FXML
    public TableColumn<Voiture, String> tableColumnVoitureNom;
    @FXML
    public TableColumn<Voiture, String> tableColumnVoiturePi;
    @FXML
    public TableColumn<Voiture, String> tableColumnVoiturePersonne;
    public TableView<Personne> tableViewPersonne;
    @FXML
    public TableColumn<Personne, String> tableColumnPersonneNom;
    @FXML
    public TableColumn<Personne, String> tableColumPersonnePrenom;
    @FXML
    public TextField textFieldVoitureNom;
    @FXML
    public TextField textFieldVotiruePlaque;
    @FXML
    public ComboBox<Personne> comboBoxVoiturePersonne;
    @FXML
    public Label errorLabelVoitureNom;
    @FXML
    public Label errorLabelVoiturePi;
    @FXML
    public TextField textFieldPersonneNom;
    @FXML
    public Label errorLabelPersonneNom;
    @FXML
    public TextField textFieldPersonnePrenom;
    @FXML
    public Label errorLabelPersonnePrenom;

    private PersonneManager personneManager;
    private VoitureManager voitureManager;

    private Voiture selectedVoiture;
    private Personne selectedPersonne;
    private static final Logger logger = MonLogger.getLogger(Controller.class.getSimpleName());


    /**
     * Fonction d'initialisation
     */
    public void initialize() {
        logger.info("Controller --> initialize");
        try {
            personneManager = PersonneManager.getInstance();
            voitureManager = VoitureManager.getInstance();

            // Partie tableau voiture
            tableColumnVoitureNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            tableColumnVoiturePi.setCellValueFactory(new PropertyValueFactory<>("plaqueImmatriculation"));
            tableColumnVoiturePersonne.setCellValueFactory(voiture -> {
                Personne personne = voiture.getValue().getPersonne();
                return new SimpleStringProperty(personne == null ? "" : personne.toString());
            });

            reloadTableVoiture();

            tableViewVoitures.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                textFieldVoitureNom.setText(newSelection == null ? null : newSelection.getNom());
                textFieldVotiruePlaque.setText(newSelection == null ? null : newSelection.getPlaqueImmatriculation());
                comboBoxVoiturePersonne.setValue(newSelection == null ? null : newSelection.getPersonne());
                selectedVoiture = newSelection;
                removeError();
            });

            // Partie edition voiture
            comboBoxVoiturePersonne.getItems().add(null);

            // Partie tableau personne
            tableColumnPersonneNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            tableColumPersonnePrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));

            reloadTablePersonne();

            tableViewPersonne.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                textFieldPersonneNom.setText(newSelection == null ? null : newSelection.getNom());
                textFieldPersonnePrenom.setText(newSelection == null ? null : newSelection.getPrenom());
                selectedPersonne = newSelection;
                removeError();
            });

        } catch (BLLException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Ajoute une nouvelle voiture
     * @param mouseEvent l'action
     */
    public void addNewVoiture(MouseEvent mouseEvent) {
        logger.info("Controller --> addNewVoiture");
        removeError();
        Voiture newVoiture = new Voiture(textFieldVoitureNom.getText(), textFieldVotiruePlaque.getText());
        newVoiture.setPersonne(comboBoxVoiturePersonne.getValue());

        try {
            selectedVoiture = voitureManager.addVoiture(newVoiture);
            reloadTableVoiture();
            tableViewVoitures.getSelectionModel().select(selectedVoiture);
        } catch (BLLException e) {
            logger.severe(e.getMessage());
            displayError(e.getExceptionType());
        }
    }

    /**
     * Modifie la voiture
     * @param mouseEvent l'action
     */
    public void editVoiture(MouseEvent mouseEvent) {
        logger.info("Controller --> editVoiture");
        removeError();

        if (selectedVoiture != null) {
            Voiture editVoiture = new Voiture(textFieldVoitureNom.getText(), textFieldVotiruePlaque.getText());
            editVoiture.setPersonne(comboBoxVoiturePersonne.getValue());
            editVoiture.setId(selectedVoiture.getId());

            try {
                selectedVoiture = voitureManager.updateVoiture(editVoiture);
                reloadTableVoiture();
                tableViewVoitures.getSelectionModel().select(selectedVoiture);
            } catch (BLLException e) {
                logger.severe(e.getMessage());
                displayError(e.getExceptionType());
            }
        }
    }

    /**
     * Supprime la voiture
     * @param mouseEvent
     */
    public void deleteVoiture(MouseEvent mouseEvent) {
        logger.info("Controller --> initialize");
        removeError();


        if (selectedVoiture != null) {
            try {
                voitureManager.deleteVoiture(selectedVoiture);
                reloadTableVoiture();
                tableViewVoitures.getSelectionModel().select(null);
            } catch (BLLException e) {
                logger.severe(e.getMessage());
                displayError(e.getExceptionType());
            }
        }
    }

    /**
     * Ajouter une nouvelle personne
     * @param mouseEvent l'action
     */
    public void addNewPersonne(MouseEvent mouseEvent) {
        logger.info("Controller --> addNewPersonne");
        removeError();

        Personne newPersonne = new Personne(textFieldPersonneNom.getText().toUpperCase(), textFieldPersonnePrenom.getText());

        try {
            selectedPersonne = personneManager.addPersonne(newPersonne);
            reloadTablePersonne();
            tableViewPersonne.getSelectionModel().select(selectedPersonne);
        } catch (BLLException e) {
            logger.severe(e.getMessage());
            displayError(e.getExceptionType());
        }
    }

    /**
     * Modifie une personne
     * @param mouseEvent l'action
     */
    public void editPersonne(MouseEvent mouseEvent) {
        logger.info("Controller --> editPersonne");
        removeError();

        if (selectedPersonne != null) {
            Personne updatePersonne = new Personne(textFieldPersonneNom.getText().toUpperCase(), textFieldPersonnePrenom.getText());
            updatePersonne.setId(selectedPersonne.getId());

            try {
                selectedPersonne = personneManager.updatePersonne(updatePersonne);
                reloadTablePersonne();
                tableViewPersonne.getSelectionModel().select(selectedPersonne);
            } catch (BLLException e) {
                logger.severe(e.getMessage());
                displayError(e.getExceptionType());
            }
        }
    }

    /**
     * Supprimer une personne
     * @param mouseEvent l'action
     */
    public void deletePersonne(MouseEvent mouseEvent) {
        logger.info("Controller --> deletePersonne");
        removeError();

        if (selectedPersonne != null) {
            try {
                personneManager.deletePersonnse(selectedPersonne);
                reloadTablePersonne();
                tableViewPersonne.getSelectionModel().select(null);
            } catch (BLLException e) {
                logger.severe(e.getMessage());
                displayError(e.getExceptionType());
            }
        }
    }

    /**
     * Export les données en XML
     * @param mouseEvent l'action
     */
    public void exportDataXml(MouseEvent mouseEvent) {
        logger.info("Controller --> exportDataXml");
        try {
            ExportData.exportDataXml(voitureManager.getListVoiture(), personneManager.getListPersonneWithoutVoiture());
        } catch (BLLException e) {
            logger.severe(e.getMessage());
            displayError(e.getExceptionType());
        }
    }

    /**
     * Export les données en CSV
     * @param mouseEvent l'action
     */
    public void exportDataExcel(MouseEvent mouseEvent) {
        logger.info("Controller --> exportDataExcel");
        try {
            ExportData.exportDataCsv(voitureManager.getListVoiture(), personneManager.getListPersonneWithoutVoiture());
        } catch (BLLException e) {
            logger.severe(e.getMessage());
            displayError(e.getExceptionType());
        }
    }

    /**
     * Affiche le message d'erreur selon le type de l'erreur
     * @param exceptionType le type d'erreur
     */
    private void displayError(BLLExceptionType exceptionType) {
        logger.info("Controller --> displayError");
        switch (exceptionType) {
            case ERROR_VOITURE_NOM_VIDE:
                setError(errorLabelVoitureNom, "Le nom est vide");
                break;
            case ERROR_VOITURE_PLAQUE_VIDE:
                setError(errorLabelVoiturePi, "La plaque est vide");
                break;
            case ERROR_VOITURE_PLAQUE_TAILLE:
                setError(errorLabelVoiturePi, "La plaque doit avoir 9 caractères");
                break;
            case ERROR_VOITURE_PLAQUE_FORMAT:
                setError(errorLabelVoiturePi, "La plaque doit être de type : AA-000-AA");
                break;
            case ERROR_PERSONNE_NOM_VIDE:
                setError(errorLabelPersonneNom, "Le nom est vide");
                break;
            case ERROR_PERSONNE_PRENOM_VIDE:
                setError(errorLabelPersonnePrenom, "Le prénom est vide");
                break;
            case ERROR_PERSONNE_ASSOCIE_VOITURE:
                setError(errorLabelPersonnePrenom, "La personne est associé à au moins une voiture");
                break;
            case ERROR_VOITURE_PRESENTE_BASE:
            case ERROR_VOITURE_NON_PRESENTE_BASE:
            case ERROR_PERSONNE_PRESENTE_BASE:
            case ERROR_PERSONNE_NON_PRESENTE_BASE:
            case OTHER:
            default:
                break;
        }
    }

    /**
     * Affiche une erreur
     * @param label le label d'erreur
     * @param message le message d'erreur
     */
    private void setError(Label label, String message) {
        logger.info("Controller --> setError");
        label.setText(message);
        label.setTooltip(new Tooltip(message));
        label.setVisible(true);

    }

    /**
     * Supprime les message d'erreur
     */
    private void removeError() {
        logger.info("Controller --> removeError");
        errorLabelVoitureNom.setVisible(false);
        errorLabelVoitureNom.setTooltip(null);
        errorLabelVoiturePi.setVisible(false);
        errorLabelVoiturePi.setTooltip(null);
        errorLabelPersonneNom.setVisible(false);
        errorLabelPersonneNom.setTooltip(null);
        errorLabelPersonnePrenom.setVisible(false);
        errorLabelPersonnePrenom.setTooltip(null);
    }

    /**
     * Recharge le tableau de voiture
     */
    private void reloadTableVoiture() throws BLLException {
        logger.info("Controller --> reloadTableVoiture");
        tableViewVoitures.setItems(FXCollections.observableArrayList(voitureManager.getListVoiture()));
    }

    /**
     * Recharge le tableau de personne
     */
    private void reloadTablePersonne() throws BLLException {
        logger.info("Controller --> reloadTablePersonne");
        tableViewPersonne.setItems(FXCollections.observableArrayList(personneManager.getListPersonne()));
        comboBoxVoiturePersonne.setItems(FXCollections.observableArrayList(personneManager.getListPersonne()));
    }
}