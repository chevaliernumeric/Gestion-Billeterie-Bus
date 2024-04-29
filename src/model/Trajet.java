package model;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class Trajet {
    private int idTrajet;
    private int idBus;
    private String villeDepart;
    private String villeArrivee;
    private Date dateDepart;
    private Time heureDepart;

    public Trajet(int idTrajet, String villeDepart, String villeArrivee, Date dateDepart, Time heureDepart) {
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.idTrajet = idTrajet;
    }



    // Getters
    public int getIdTrajet() {
        return idTrajet;
    }
    public int getIdBus() {
        return idBus;
    }


    public String getVilleDepart() {
        return villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public Time getHeureDepart() {
        return heureDepart;
    }

    // Setters
    public void setIdTrajet(int idTrajet) {
        this.idTrajet = idTrajet;
    }

    public  void setIdBus(int idBus){this.idBus=idBus;}

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public void setHeureDepart(Time heureDepart) {
        this.heureDepart = heureDepart;
    }
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return villeDepart + " - " + villeArrivee + " (" + dateFormat.format(dateDepart) + " " + timeFormat.format(heureDepart) + ")";
    }
}

