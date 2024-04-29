package controller;

import model.EtatPlaceBus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class EtatPlaceBusDAO {
    public static int updatePlacesRestantes(int idBus, Date dateTrajet, int decrement) {
        // Vérifie si un enregistrement existe déjà pour ce bus et cette date
        if (getEtatPlacesParBusEtDate(idBus, dateTrajet) != null) {
            // S'il existe, décrémente les places restantes etatplacesbus
             EtatPlaceBus etat = getEtatPlacesParBusEtDate(idBus, dateTrajet);
            int placesRestantes = etat.getPlacesRestantes() - 1;

            String sqlUpdate = "UPDATE EtatPlacesBus SET placesRestantes = placesRestantes - ? WHERE idBus = ? AND dateTrajet = ?";
            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                pstmtUpdate.setInt(1, decrement);
                pstmtUpdate.setInt(2, idBus);
                pstmtUpdate.setDate(3, new java.sql.Date(dateTrajet.getTime()));
                pstmtUpdate.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // Gérer l'erreur
            }
        } else {
            // S'il n'existe pas, crée un nouvel enregistrement
            String sqlInsert = "INSERT INTO EtatPlacesBus (idBus, dateTrajet, placesRestantes) VALUES (?, ?, ?)";
            int capaciteInitiale = controller.BusDAO.getCapaciteBus(idBus) - decrement;
            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {

                pstmtInsert.setInt(1, idBus);
                pstmtInsert.setDate(2, new java.sql.Date(dateTrajet.getTime()));
                pstmtInsert.setInt(3, capaciteInitiale);
                pstmtInsert.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // Gérer l'erreur
            }
        }
        return idBus;
    }
    public static EtatPlaceBus getEtatPlacesParBusEtDate(int idBus, Date dateTrajet) {
        String sql = "SELECT idEtatPlaces, placesRestantes FROM EtatPlacesBus WHERE idBus = ? AND dateTrajet = ?";
        EtatPlaceBus etatPlaceBus = null;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idBus);
            pstmt.setDate(2, new java.sql.Date(dateTrajet.getTime()));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int idEtatPlaces = rs.getInt("idEtatPlaces");
                int placesRestantes = rs.getInt("placesRestantes");
                etatPlaceBus = new EtatPlaceBus(idEtatPlaces, idBus, dateTrajet, placesRestantes);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'erreur
        }

        return etatPlaceBus;
    }

    public static int getPlacesRestantes(int idBus, Date dateTrajet) {
        String sql = "SELECT placesRestantes FROM EtatPlacesBus WHERE idBus = ? AND dateTrajet = ?";
        int placesRestantes = -1;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idBus);
            pstmt.setDate(2, new java.sql.Date(dateTrajet.getTime()));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                placesRestantes = rs.getInt("placesRestantes");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'erreur
        }

        return placesRestantes;
    }


}
