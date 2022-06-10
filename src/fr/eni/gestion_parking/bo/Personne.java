package fr.eni.gestion_parking.bo;

/**
 * Classe Personne
 *
 * @author lrabu
 */
public class Personne {

    private Integer id;
    private String nom;
    private String prenom;

    /**
     * Constructeur
     * @param nom nom
     * @param prenom prenom
     */
    public Personne(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    /**
     * Retourne l'identifiant
     * @return l'id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Change l'identifiant
     * @param id l'id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retourne le nom
     * @return le nom
     */
    public String getNom() {
        return nom.toUpperCase();
    }

    /**
     * Change le nom
     * @param nom le nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le prénom
     * @return le prénom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Change le prénom
     * @param prenom le prénom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * La personne sous forme de string
     * @return la personne
     */
    @Override
    public String toString() {
        return this.getNom().toUpperCase() + " " + this.getPrenom();
    }
}
