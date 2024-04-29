package controller;

import model.Reservation;
import model.ReservationDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservationDAO {

    private static final String INSERT_RESERVATION_SQL = "INSERT INTO Reservation (idReservation,idBus, idTrajet, idClient, numeroSiege, dateReservation,codePaiment) VALUES (?, ?, ?, ?, ?,?,?)";
    private static final String SELECT_RESERVATION_BY_ID = "SELECT * FROM Reservation WHERE idReservation = ?";
    private static final String SELECT_ALL_RESERVATIONS = "SELECT * FROM Reservation";
    private static final String UPDATE_RESERVATION_SQL = "UPDATE Reservation SET idBus = ?, idTrajet = ?, idClient = ?, numeroSiege = ?, dateReservation = ? WHERE idReservation = ?";
    private static final String DELETE_RESERVATION_SQL = "DELETE FROM Reservation WHERE idReservation = ?";

   public static void insertReservation(Reservation reservation) throws SQLException {
        try (Connection connection =DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_RESERVATION_SQL)) {
            preparedStatement.setInt(1, reservation.getIdBus());
            preparedStatement.setInt(2, reservation.getIdTrajet());
            preparedStatement.setInt(3, reservation.getIdClient());
            preparedStatement.setInt(4, reservation.getNumeroSiege());
            preparedStatement.setDate(5, reservation.getDateReservation());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void enregistrerReservation(Reservation reservation, int idBus,int idTrajet,int idClient) {
        // Création de la réservation
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_RESERVATION_SQL)) {
           pstmt.setInt(1,reservation.getIdReservation());
            pstmt.setInt(2, idClient);
            pstmt.setInt(3, idTrajet);
            pstmt.setInt(4, idBus);
            pstmt.setInt(5,reservation.getNumeroSiege());
            pstmt.setDate(6,reservation.getDateReservation());
            pstmt.setString(7,reservation.getCodePaiement());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'erreur
        }
    }



    public static ReservationDetail selectReservation(int idReservation) {
        Reservation reservation = null;
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RESERVATION_BY_ID)) {
            preparedStatement.setInt(1, idReservation);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int numeroSiege = rs.getInt("numeroSiege");
                Date dateReservation = rs.getDate("dateReservation");
                String codePaiment = rs.getString("codePaiment");


                // Construisez l'objet Reservation en incluant bus, trajet et client
                reservation = new Reservation(idReservation,"","","","", "","",numeroSiege, dateReservation, codePaiment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }


    public static List<Reservation> selectAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_RESERVATIONS)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int idReservation = rs.getInt("idReservation");
                int numeroSiege = rs.getInt("numeroSiege");
                Date dateReservation = rs.getDate("dateReservation");
                String codePaiment = rs.getString("codePaiment");
                reservations.add(new Reservation(idReservation,"","","","","", "",numeroSiege, dateReservation,codePaiment));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public static boolean deleteReservation(int idReservation) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(DELETE_RESERVATION_SQL)) {
            statement.setInt(1, idReservation);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }



    public boolean updateReservation(Reservation reservation) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(UPDATE_RESERVATION_SQL)) {
            statement.setInt(1, reservation.getIdBus());
            statement.setInt(2, reservation.getIdTrajet());
            statement.setInt(3, reservation.getIdClient());
            statement.setInt(4, reservation.getNumeroSiege());
            statement.setDate(5, reservation.getDateReservation());
            statement.setInt(6, reservation.getIdReservation());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    public static List<ReservationDetail> getReservationDetails() throws SQLException {
        List<ReservationDetail> reservationDetailsList = new ArrayList<>();
        String sql = "SELECT r.idReservation,r.numeroSiege,r.dateReservation,r.codePaiment,c.prenom as prenomClient, c.nom as nomClient,c.telephone ,t.villeDepart, t.villeArrivee,t.dateDepart,t.heureDepart, b.description FROM Reservation r JOIN Client c ON r.idClient = c.idClient JOIN Trajet t ON r.idTrajet = t.idTrajet JOIN Bus b ON r.idBus = b.idBus";
        Date date = new Date(13/12/2023);
        Time time = new Time(date.getTime());
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ReservationDetail detail = new ReservationDetail("","","","","","",0, date,date,time,"");
                detail.setIdReservation(rs.getInt("idReservation"));
                detail.setPrenomClient(rs.getString("prenomClient"));
                detail.setNomClient(rs.getString("nomClient"));
                detail.setTelephone(rs.getString("telephone"));
                detail.setVilleDepart(rs.getString("villeDepart"));
                detail.setVilleArrivee(rs.getString("villeArrivee"));
                detail.setDateDepart(rs.getDate("dateDepart"));
                detail.setHeureDepart(rs.getTime("heureDepart"));
                detail.setTypeBus(rs.getString("description"));
                detail.setNumeroSiege(rs.getInt("numeroSiege"));
                detail.setDateReservation(rs.getDate("dateReservation"));
                detail.setCodePaiment(rs.getString("codePaiment"));
                reservationDetailsList.add(detail);
            }
        }

        return  reservationDetailsList;
    }

    public static Date getDateReservationParId(int idReservation) {
        String sql = "SELECT dateReservation FROM Reservation WHERE idReservation = ?";
        Date dateReservation = null;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idReservation);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                dateReservation = rs.getDate("dateReservation");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'erreur
        }

        return dateReservation;
    }

    public static String genererCodePaiement(String modePaiement) {
        String prefix = modePaiement.equals("cash") ? "cp_" : "mp_";
        return prefix + UUID.randomUUID().toString();
    }

    public static int getNombreDeReservations() {
        String sql = "SELECT COUNT(*) FROM Reservation"; // Remplacez 'Reservation' par le nom réel de votre table de réservations
        int nombreDeReservations = 0;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                nombreDeReservations = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du nombre de réservations: " + e.getMessage());
            // Gérer l'erreur
        }

        return nombreDeReservations;
    }




}

