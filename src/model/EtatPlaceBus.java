package model;

import java.util.Date;

public class EtatPlaceBus {
    private int idEtatPlaces;
    private int idBus;
    private Date dateTrajet;
    private int placesRestantes;

    // Constructeur
    public EtatPlaceBus(int idEtatPlaces, int idBus, Date dateTrajet, int placesRestantes) {
        this.idEtatPlaces = idEtatPlaces;
        this.idBus = idBus;
        this.dateTrajet = dateTrajet;
        this.placesRestantes = placesRestantes;
    }

    // Getters
    public int getIdEtatPlaces() {
        return idEtatPlaces;
    }

    public int getIdBus() {
        return idBus;
    }

    public Date getDateTrajet() {
        return dateTrajet;
    }

    public int getPlacesRestantes() {
        return placesRestantes;
    }

    // Setters
    public void setIdEtatPlaces(int idEtatPlaces) {
        this.idEtatPlaces = idEtatPlaces;
    }

    public void setIdBus(int idBus) {
        this.idBus = idBus;
    }

    public void setDateTrajet(Date dateTrajet) {
        this.dateTrajet = dateTrajet;
    }

    public void setPlacesRestantes(int placesRestantes) {
        this.placesRestantes = placesRestantes;
    }

    // Méthode pour afficher les informations de l'objet (optionnelle)
    @Override
    public String toString() {
        return "EtatPlaceBus{" +
                "idEtatPlaces=" + idEtatPlaces +
                ", idBus=" + idBus +
                ", dateTrajet=" + dateTrajet +
                ", placesRestantes=" + placesRestantes +
                '}';
    }
}
