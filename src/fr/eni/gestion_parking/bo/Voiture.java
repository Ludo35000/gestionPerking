package fr.eni.gestion_parking.bo;

/**
 * Classe Voiture
 *
 * @author lrabu
 */
public class Voiture {

    private Integer id;
    private String nom;
    private String plaqueImmatriculation;
    private Personne personne;

    /**
     * Constructeur de la voiture
     * @param nom le nom
     * @param plaqueImmatriculation la plaque d'immatriculation
     */
    public Voiture(String nom, String plaqueImmatriculation) {
        this.nom = nom;
        this.plaqueImmatriculation = plaqueImmatriculation;
        this.personne = null;
    }

    /**
     * Retourne l'identifiant
     * @return l'identifiant
     */
    public Integer getId() {
        return id;
    }

    /**
     * Change l'identifiant
     * @param id l'identifiant
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retourne le nom
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Change le nom de la voiture
     * @param nom le nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne la plaque d'immatriculation
     * @return la plaque d'immatriculation
     */
    public String getPlaqueImmatriculation() {
        return plaqueImmatriculation;
    }

    /**
     * Change la plaque d'immatriculation
     * @param plaqueImmatriculation la plaque d'immatriculation
     */
    public void setPlaqueImmatriculation(String plaqueImmatriculation) {
        this.plaqueImmatriculation = plaqueImmatriculation;
    }

    /**
     * Retourne la personne en charge de la voiture
     * @return la personne
     */
    public Personne getPersonne() {
        return personne;
    }

    /**
     * Change la personne responsable de la voiture
     * @param personne la personne
     */
    public void setPersonne(Personne personne) {
        this.personne = personne;
    }
}
