package model;

public class Client {
    private int idClient;
    private String prenom;
    private String nom;
    private String numeroIdentite;
    private String telephone;

    public Client(int idClient, String prenom, String nom, String numeroIdentite, String telephone) {
        this.prenom = prenom;
        this.nom = nom;
        this.numeroIdentite = numeroIdentite;
        this.telephone = telephone;
        this.idClient= idClient;
    }

    // Getters
    public int getIdClient() {
        return idClient;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getNumeroIdentite() {
        return numeroIdentite;
    }

    public String getTelephone() {
        return telephone;
    }
    public String toString() {
        return prenom + " " + nom ;
    }


    // Setters
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNumeroIdentite(String numeroIdentite) {
        this.numeroIdentite = numeroIdentite;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

}

