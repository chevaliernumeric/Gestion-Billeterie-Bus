package model;

import java.sql.Time;
import java.util.*;

public class ReservationDetail{
    private int idReservation;
    private String nomClient;
    private String prenomClient;
    private String villeDepart;
    private String villeArrivee;
    private String description;
    private int numeroSiege;
    private Date dateReservation;
    private java.sql.Date dateDepart;
    private Time heureDepart;
    private String codePaiment;
    private  String telephone;


    // Getters, setters et constructeur
    public ReservationDetail(String nomClient, String prenomClient, String villeDepart, String villeArrivee, String telephone,String description, int numeroSiege, Date dateReservation, java.sql.Date dateDepart, Time heureDepart, String codePaiment) {
        this.nomClient = nomClient;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.description = description;
        this.numeroSiege = numeroSiege;
        this.dateReservation = dateReservation;
        this.prenomClient = prenomClient;
        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.codePaiment = codePaiment;
        this.telephone = telephone;
    }


    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    // Getters et Setters pour nomClient
    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }


    // Getters et Setters pour villeDepart
    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    // Getters et Setters pour villeArrivee
    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    // Getters et Setters pour typeBus
    public String getDescription() {
        return description;
    }

    public void setTypeBus(String typeBus) {
        this.description = typeBus;
    }

    public int getNumeroSiege() {
        return numeroSiege;
    }

    public void setNumeroSiege(int numeroSiege) {
        this.numeroSiege = numeroSiege;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public java.sql.Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(java.sql.Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Time getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(Time heureDepart) {
        this.heureDepart = heureDepart;
    }

    public String getCodePaiment() {
        return codePaiment;
    }

    public void setCodePaiment(String codePaiment) {
        this.codePaiment = codePaiment;
    }
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


}

