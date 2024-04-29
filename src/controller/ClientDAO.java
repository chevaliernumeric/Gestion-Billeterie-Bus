package controller;

import model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    private static final String INSERT_CLIENT_SQL = "INSERT INTO client (prenom, nom, numeroIdentite, telephone) VALUES (?, ?, ?, ?)";
    private static final String SELECT_CLIENT_BY_ID = "SELECT * FROM client WHERE idClient = ?";
    private static final String SELECT_ALL_CLIENTS = "SELECT * FROM client";
    private static final String DELETE_CLIENT_SQL = "DELETE FROM client WHERE idClient = ?";
    private static final String UPDATE_CLIENT_SQL = "UPDATE client SET prenom = ?, nom = ?, numeroIdentite = ?, telephone = ? WHERE idClient = ?";
    private static final String getClient= "SELECT * FROM Client";


    public static void insertClient(Client client) throws SQLException {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CLIENT_SQL)) {
            preparedStatement.setString(1, client.getPrenom());
            preparedStatement.setString(2, client.getNom());
            preparedStatement.setString(3, client.getNumeroIdentite());
            preparedStatement.setString(4, client.getTelephone());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static int enregistrerClientSiNecessaire(Client client) {
        int idClient = verifierSiClientExiste(client);
        if (idClient != -1) {
            return idClient;
        }

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_CLIENT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3,client.getNumeroIdentite());
            pstmt.setString(4,client.getTelephone());

            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("La création du client a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return -1;
    }

    private static int verifierSiClientExiste(Client client) {
        String sql = "SELECT idClient FROM Client WHERE nom = ? AND prenom = ? AND numeroIdentite = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getNumeroIdentite());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idClient");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
    public static Client selectClient(int idClient) {
        Client client = null;
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CLIENT_BY_ID)) {
            preparedStatement.setInt(1, idClient);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String prenom = rs.getString("prenom");
                String nom = rs.getString("nom");
                String numeroIdentite = rs.getString("numeroIdentite");
                String telephone = rs.getString("telephone");
                client = new Client(idClient, prenom, nom, numeroIdentite, telephone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    public static List<Client> selectAllClients() {
        List<Client> clients = new ArrayList<>();
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CLIENTS)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int idClient = rs.getInt("idClient");
                String prenom = rs.getString("prenom");
                String nom = rs.getString("nom");
                String numeroIdentite = rs.getString("numeroIdentite");
                String telephone = rs.getString("telephone");
                clients.add(new Client(idClient, prenom, nom, numeroIdentite, telephone));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public static boolean deleteClient(int idClient) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(DELETE_CLIENT_SQL)) {
            statement.setInt(1, idClient);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public static boolean updateClient(Client client) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(UPDATE_CLIENT_SQL)) {
            statement.setString(1, client.getPrenom());
            statement.setString(2, client.getNom());
            statement.setString(3, client.getNumeroIdentite());
            statement.setString(4, client.getTelephone());
            statement.setInt(5, client.getIdClient());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    public static List<Client> getTousLesTrajets() throws SQLException {
        List<Client> clients = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(getClient)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int idClient = resultSet.getInt("idClient");
                String prenom = resultSet.getString("prenom");
                String nom = resultSet.getString("nom");

                Client client = new Client(idClient,prenom,nom,"","");
                clients.add(client);
            }
        }

        return clients;
    }

    // Reste des méthodes pour ClientDAO
}
