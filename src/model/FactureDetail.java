package model;


import java.sql.Time;

public class FactureDetail {
    private String codePaiement;
    private int numeroSiege;
    private int idBus;
    private String villeDepart;
    private String villeArrivee;
    private Time heureDepart;
    private int idClient;
    private String prenomClient;
    private String nomClient;
    private String telephoneClient;
    private int idReservation;
    private String typeBus;

    public FactureDetail(String codePaiement,int numeroSiege,int idBus,String villeDepart,String villeArrivee,Time heureDepart,int idClient,String prenomClient,String nomClient,String telephoneClient,int idReservation,String typeBus){
        this.codePaiement = codePaiement;
        this.numeroSiege = numeroSiege;
        this.idBus = idBus;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.heureDepart = heureDepart;
        this.idClient = idClient;
        this.prenomClient = prenomClient;
        this.nomClient = nomClient;
        this.telephoneClient = telephoneClient;
        this.idReservation = idReservation;
        this.typeBus = typeBus;


    }
    public String getCodePaiement() {
        return codePaiement;
    }

    public void setCodePaiement(String codePaiement) {
        this.codePaiement = codePaiement;
    }
    public int getNumeroSiege() {
        return numeroSiege;
    }

    public void setNumeroSiege(int numeroSiege) {
        this.numeroSiege = numeroSiege;
    }
    public int getIdBus() {
        return idBus;
    }

    public void setIdBus(int idBus) {
        this.idBus = idBus;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public Time getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(Time heureDepart) {
        this.heureDepart = heureDepart;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getTelephoneClient() {
        return telephoneClient;
    }

    public void setTelephoneClient(String telephoneClient) {
        this.telephoneClient = telephoneClient;
    }
    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }
    public String getTypeBus() {
        return typeBus;
    }
    public void setDescription(String description){
        this.typeBus = description;
    }


    // ... Reste des getters et setters
}

