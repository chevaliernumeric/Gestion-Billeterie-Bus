package controller;

import model.Trajet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrajetDAO {

    private static final String INSERT_TRAJET_SQL = "INSERT INTO Trajet (villeDepart, villeArrivee, dateDepart, heureDepart) VALUES (?, ?, ?, ?)";
    private static final String SELECT_TRAJET_BY_ID = "SELECT * FROM Trajet WHERE idTrajet = ?";
    private static final String SELECT_ALL_TRAJETS = "SELECT * FROM Trajet";
    private static final String DELETE_TRAJET_SQL = "DELETE FROM Trajet WHERE idTrajet = ?";
    private static final String UPDATE_TRAJET_SQL = "UPDATE Trajet SET villeDepart = ?, villeArrivee = ?, dateDepart = ?, heureDepart = ? WHERE idTrajet = ?";
    private static final String getTrajet= "SELECT * FROM Trajet";

    public static void insertTrajet(Trajet trajet) throws SQLException {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRAJET_SQL)) {
            preparedStatement.setString(1, trajet.getVilleDepart());
            preparedStatement.setString(2, trajet.getVilleArrivee());
            preparedStatement.setDate(3, trajet.getDateDepart());
            preparedStatement.setTime(4, trajet.getHeureDepart());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Trajet selectTrajet(int idTrajet) {
        Trajet trajet = null;
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TRAJET_BY_ID)) {
            preparedStatement.setInt(1, idTrajet);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                idTrajet = rs.getInt("idTrajet");
                String villeDepart = rs.getString("villeDepart");
                String villeArrivee = rs.getString("villeArrivee");
                Date dateDepart = rs.getDate("dateDepart");
                Time heureDepart = rs.getTime("heureDepart");
                trajet = new Trajet(idTrajet, villeDepart, villeArrivee, dateDepart, heureDepart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trajet;
    }

    public static List<Trajet> selectAllTrajets() {
        List<Trajet> trajets = new ArrayList<>();
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TRAJETS)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int idTrajet = rs.getInt("idTrajet");
                String villeDepart = rs.getString("villeDepart");
                String villeArrivee = rs.getString("villeArrivee");
                Date dateDepart = rs.getDate("dateDepart");
                Time heureDepart = rs.getTime("heureDepart");
                trajets.add(new Trajet(idTrajet, villeDepart, villeArrivee, dateDepart, heureDepart));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trajets;
    }

    public static boolean deleteTrajet(int idTrajet) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(DELETE_TRAJET_SQL)) {
            statement.setInt(1, idTrajet);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public static boolean updateTrajet(Trajet trajet) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TRAJET_SQL)) {
            statement.setString(1, trajet.getVilleDepart());
            statement.setString(2, trajet.getVilleArrivee());
            statement.setDate(3, trajet.getDateDepart());
            statement.setTime(4, trajet.getHeureDepart());
            statement.setInt(5, trajet.getIdTrajet());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    public static List<Trajet> getTousLesTrajets() throws SQLException {
        List<Trajet> trajets = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(getTrajet)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int idTrajet = resultSet.getInt("idTrajet");
                String villeDepart = resultSet.getString("villeDepart");
                String villeArrivee = resultSet.getString("villeArrivee");
                Date dateDepart = resultSet.getDate("dateDepart");
                Time heureDepart = resultSet.getTime("heureDepart");

                Trajet trajet = new Trajet(idTrajet,villeDepart, villeArrivee, dateDepart, heureDepart);
                trajets.add(trajet);
            }
        }

        return trajets;
    }

    public static void enregistrerTrajet(Trajet trajet, int busId) {
        String sql = "INSERT INTO Trajet (idTrajet, villeDepart, villeArrivee, dateDepart, heureDepart, idBus) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, trajet.getIdTrajet());
            pstmt.setString(2, trajet.getVilleDepart());
            pstmt.setString(3, trajet.getVilleArrivee());
            pstmt.setDate(4, trajet.getDateDepart());
            pstmt.setTime(5, trajet.getHeureDepart());
            pstmt.setInt(6, busId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int getIdTrajet(int idTrajet) {
        List<Integer> idsTrajets = new ArrayList<>();

        String sql = "SELECT idTrajet FROM Trajet ";
         idTrajet = 0;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

                while(rs.next()){
                    idTrajet = rs.getInt("idTrajet");
                    idsTrajets.add(idTrajet);
                }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'erreur
        }

        return idTrajet;
    }

    public static int getNombreDeTrajets() {
        String sql = "SELECT COUNT(*) FROM Trajet";
        int nombreDeTrajets = 0;

        try (Connection conn = DatabaseConnection.connect(); // Assurez-vous que cette méthode existe pour établir une connexion à la base de données
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                nombreDeTrajets = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'erreur
        }

        return nombreDeTrajets;
    }

    public static int getIdTrajetParCritere(String villeDepart) {
        String sql = "SELECT idTrajet FROM Trajet WHERE ville_depart = ?";
        int idTrajet = 0;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, villeDepart);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                idTrajet = rs.getInt("idTrajet");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'erreur
        }

        return idTrajet;
    }



    // Reste des méthodes pour TrajetDAO
}

