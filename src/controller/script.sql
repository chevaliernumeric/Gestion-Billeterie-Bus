-- Création de la base de données
CREATE DATABASE IF NOT EXISTS MICDAVoyages;
USE MICDAVoyages;

-- Création de la table 'Bus'
CREATE TABLE IF NOT EXISTS Bus (
    idBus INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255),
    capacite INT,
    etat VARCHAR(100)
);

-- Création de la table 'Trajet'
CREATE TABLE IF NOT EXISTS Trajet (
    idTrajet INT AUTO_INCREMENT PRIMARY KEY,
    villeDepart VARCHAR(100),
    villeArrivee VARCHAR(100),
    dateDepart DATE,
    heureDepart TIME
);

-- Création de la table 'Client'
CREATE TABLE IF NOT EXISTS Client (
    idClient INT AUTO_INCREMENT PRIMARY KEY,
    prenom VARCHAR(100),
    nom VARCHAR(100),
    numeroIdentite VARCHAR(100),
    telephone VARCHAR(100)
);

-- Création de la table 'Reservation'
CREATE TABLE IF NOT EXISTS Reservation (
    idReservation INT AUTO_INCREMENT PRIMARY KEY,
    idClient INT,
    idTrajet INT,
    numeroSiege INT,
    dateReservation DATE,
    codePaiement VARCHAR(100),
    FOREIGN KEY (idClient) REFERENCES Client(idClient),
    FOREIGN KEY (idTrajet) REFERENCES Trajet(idTrajet),
    FOREIGN KEY (idBus) REFERENCES Bus(idBus)
);

-- Création de la table 'EtatPlaceBus'
CREATE TABLE IF NOT EXISTS EtatPlaceBus (
    idEtatPlaces INT AUTO_INCREMENT PRIMARY KEY,
    idBus INT,
    dateTrajet DATE,
    placesRestantes INT,
    FOREIGN KEY (idBus) REFERENCES Bus(idBus)
);

CREATE TABLE IF NOT EXISTS EtatPlacesBus (
    idEtatPlaces INT AUTO_INCREMENT PRIMARY KEY,
    idBus INT,
    dateTrajet DATE,
    placesRestantes INT,
    FOREIGN KEY (idBus) REFERENCES Bus(idBus)
);
