package model;

import java.sql.Date;

public class Reservation extends ReservationDetail {
    private int idReservation;
    private int idBus;
    private int idTrajet;
    private int idClient;
    private int numeroSiege;
    private Date dateReservation;
    private String codePaiement;


    public Reservation(int idReservation,String nomClient, String prenomClient, String villeDepart, String villeArrivee ,String telephone, String description, int numeroSiege, Date dateReservation,String codePaiement) {
        super(nomClient,prenomClient,villeDepart,villeArrivee,telephone,description,numeroSiege,dateReservation,null,null,codePaiement);
        this.idReservation = idReservation;
        this.numeroSiege = numeroSiege;
        this.dateReservation = dateReservation;
        this.codePaiement = codePaiement;
    }

    // Getters
    public int getIdReservation() {
        return idReservation;
    }

    public int getIdBus() {
        return idBus;
    }

    public int getIdTrajet() {
        return idTrajet;
    }

    public int getIdClient() {
        return idClient;
    }

    public int getNumeroSiege() {
        return numeroSiege;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    // Setters
    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public void setIdBus(int idBus) {
        this.idBus = idBus;
    }

    public void setIdTrajet(int idTrajet) {
        this.idTrajet = idTrajet;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setNumeroSiege(int numeroSiege) {
        this.numeroSiege = numeroSiege;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }
    public String getCodePaiement() {
        return codePaiement;
    }

    public void setCodePaiement(String codePaiement) {
        this.codePaiement = codePaiement;
    }



}

