package controller;

import model.Bus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {

    private static final String INSERT_BUS_SQL = "INSERT INTO Bus (description, etat, capacite) VALUES (?, ?, ?)";
    private static final String SELECT_BUS_BY_ID = "SELECT * FROM Bus WHERE idBus = ?";
    private static final String SELECT_ALL_BUSES_SQL = "SELECT idBus,description, etat, capacite FROM Bus";
    private static final String DELETE_BUS_SQL = "DELETE FROM Bus WHERE idBus = ?";
    private static final String UPDATE_BUS_SQL = "UPDATE Bus SET description = ?, etat = ?, capacite = ? WHERE idBus = ?";
    private static final String getBus = "SELECT description FROM Bus";


    // Method to insert a bus
    public static void insertBus(Bus bus) throws SQLException {
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BUS_SQL)) {
            preparedStatement.setString(1, bus.getDescription());
            preparedStatement.setString(2, bus.getEtat());
            preparedStatement.setInt(3, bus.getCapacite());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public static List<Bus> selectAllBuses() {
        List<Bus> buses = new ArrayList<>();
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BUSES_SQL)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idBus");
                String description = rs.getString("description");
                String etat = rs.getString("etat");
                int capacite = rs.getInt("capacite");

                buses.add(new Bus(id,description, etat, capacite));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  buses;

    }
    public static boolean deleteBus(int idBus) throws SQLException {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BUS_SQL)) {
            preparedStatement.setInt(1, idBus);

            boolean rowDeleted = preparedStatement.executeUpdate() > 0;
            return rowDeleted;
        }
    }

    public static boolean updateBus(Bus bus) throws SQLException {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BUS_SQL)) {
            preparedStatement.setString(1, bus.getDescription());
            preparedStatement.setString(2, bus.getEtat());
            preparedStatement.setInt(3, bus.getCapacite());
            preparedStatement.setInt(4, bus.getIdBus());

            boolean rowUpdated = preparedStatement.executeUpdate() > 0;
            return rowUpdated;
        }
    }
    public static Bus selectBusById(int idBus) {
        Bus bus = null;
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BUS_BY_ID)) {
            preparedStatement.setInt(1, idBus);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String description = resultSet.getString("description");
                String etat = resultSet.getString("etat");
                int capacite = resultSet.getInt("capacite");

                bus = new Bus(idBus,description, etat, capacite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bus;
    }

    // Méthode pour décrémenter la capacité d'un bus
    public static boolean decrementerCapaciteBus(int busId) {
        String sql = "SELECT capacite FROM bus WHERE bus_id = ?";
        String updateSql = "UPDATE bus SET capacite = capacite - 1 WHERE bus_id = ? AND capacite > 0";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            // Vérifier la capacité actuelle
            pstmt.setInt(1, busId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int capacite = rs.getInt("capacite");
                if (capacite > 0) {
                    // Décrémenter la capacité
                    updateStmt.setInt(1, busId);
                    int affectedRows = updateStmt.executeUpdate();
                    return affectedRows > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Méthode pour vérifier si un siège est pris
    public static boolean isSiegePris(int busId, int numeroSiege) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE bus_id = ? AND numero_siege = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, busId);
            pstmt.setInt(2, numeroSiege);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Si count > 0, alors le siège est déjà pris
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static List<Bus> getTousLesBus() {
        List<Bus> busList = new ArrayList<>();
        String sql = "SELECT * FROM bus"; // Assurez-vous que cette requête correspond à votre schéma de base de données

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int idBus = rs.getInt("idBus");;
                String description = rs.getString("description");
                String etat = rs.getString("etat");
                int capacite = rs.getInt("capacite");
                // Assurez-vous que ces méthodes set correspondent aux attributs de votre classe Bus
                Bus bus = new Bus(idBus,description,etat,capacite);

                busList.add(bus);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'exception comme nécessaire
        }
        return busList;
    }


    public static List<Bus> getBusesPourTrajet(int idTrajet) throws SQLException {
        List<Bus> buses = new ArrayList<>();
        String sql = "SELECT b.idBus, b.description, b.capacite FROM Bus b INNER JOIN Trajet t ON b.idBus = t.idBus WHERE t.idTrajet = ?";
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idTrajet); // Assurez-vous que cela correspond à un paramètre dans la requête
            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                int idBus = res.getInt("idBus");
                String description = res.getString("description");
                int capacite = res.getInt("capacite");

                Bus bus = new Bus(idBus, description, capacite);
                buses.add(bus);
            }
        }

        return buses;
    }

    public static int getNombreDeBus() {
        String sql = "SELECT COUNT(*) FROM Bus"; // Remplacez 'Bus' par le nom réel de votre table de bus
        int nombreDeBus = 0;

        try (Connection conn = DatabaseConnection.connect(); // Assurez-vous que cette méthode établit une connexion à votre base de données
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                nombreDeBus = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'erreur
        }

        return nombreDeBus;
    }

    public static int getCapaciteBus(int idBus) {
        int capacite = 0;
        String sql = "SELECT capacite FROM Bus WHERE idBus = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idBus);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                capacite = rs.getInt("capacite");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'erreur
        }

        return capacite;
    }


    // ... autres méthodes de BusDAO




    // Autres méthodes liées aux bus...
}




