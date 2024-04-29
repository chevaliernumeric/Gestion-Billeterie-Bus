package model;

public class Bus {
    private int idBus;
    private String description;
    private String etat;
    private int capacite;


    public Bus(int idBus,String description, String etat, int capacite) {
        this.description = description;
        this.etat = etat;
        this.capacite = capacite;
        this.idBus = idBus;

    }

    public Bus(int idBus, String description, int capacite) {
        this.idBus = idBus;
        this.description = description;
        this.capacite = capacite;
    }


    // Getters and Setters
    public int getIdBus() {
        return idBus;
    }

    public void setIdBus(int idBus) {
        this.idBus = idBus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }
    @Override
    public String toString() {
        return  description;
    }


}

