package view;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.toedter.calendar.JCalendar;
import controller.ClientDAO;
import controller.EtatPlaceBusDAO;
import controller.ReservationDAO;
import controller.TrajetDAO;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static controller.ReservationDAO.selectReservation;

public class ReservationPanel extends JPanel {
    private JTextField textIdBus;
    private JTextField textIdTrajet;
    private JTextField textIdClient;
    private JTextField textNumeroSiege;
    private JTextField textDateReservation;
    private JComboBox<String> comboCodePaiement;


    private JButton addButton;
    private JButton deleteButton;
    private JButton payerButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private JCalendar calendar;
    private JDialog calendarDialog;
    private JComboBox<Trajet> trajetComboBox;
    private JComboBox<Bus> busComboBox;
    private JComboBox<Client> clientComboBox;
    private int capaciteBusActuelle;


    public ReservationPanel() {
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 4));
        trajetComboBox = new JComboBox<>();
        busComboBox = new JComboBox<>();
        clientComboBox = new JComboBox<>();


        List<Bus> busList = controller.BusDAO.getTousLesBus();
        for (Bus bus : busList) {
            busComboBox.addItem(bus);
        }

        chargerTrajets();
        chargerClient();


        // Ajouter les JComboBox au panel
        inputPanel.add(new JLabel("Trajet:"));
        inputPanel.add(trajetComboBox);
        //Bus
        inputPanel.add(new JLabel("Bus:"));
        inputPanel.add(busComboBox);
        //Client
        inputPanel.add(new JLabel("Client:"));
        inputPanel.add(clientComboBox);


        //reservation
        inputPanel.add(new JLabel("Nombre du Siège:"));
        textNumeroSiege = new JTextField(20);
        inputPanel.add(textNumeroSiege);

        //codepaiment

        inputPanel.add(new JLabel("CodePaiment"));
        String[] modesPaiement = {"cash", "mobilemoney"};
        comboCodePaiement = new JComboBox<>(modesPaiement);
        inputPanel.add(comboCodePaiement);
        //nombre de siége brésérvé



        inputPanel.add(new JLabel("Date de Réservation:"));
        textDateReservation = new JTextField(String.valueOf(LocalDate.now()));
        textDateReservation.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                calendarDialog.setVisible(true);
            }
        });
        // Création du JDialog avec BorderLayout
        calendarDialog = new JDialog();
        // Ajout du JCalendar
        calendar = new JCalendar();
        calendarDialog.add(calendar, BorderLayout.CENTER);
        // Création et ajout du JButton dans un JPanel au sud du JDialog
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar selectedDate = calendar.getCalendar();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                textDateReservation.setText(format.format(selectedDate.getTime()));
                calendarDialog.setVisible(false);
            }
        });
        calendarDialog.add(okButton, BorderLayout.SOUTH);

        // Pack après avoir ajouté tous les composants
        calendarDialog.pack();
        calendarDialog.setModal(true);
        inputPanel.add(textDateReservation);
        inputPanel.add(textDateReservation);

        addButton = new JButton("Ajouter");
        addButton.setBackground(Color.green);
        buttonPanel.add(addButton);
        deleteButton = new JButton("Annuler");
        deleteButton.setBackground(Color.red);
        buttonPanel.add(deleteButton);
        payerButton = new JButton("Payer");
        payerButton.setBackground(Color.orange);
        buttonPanel.add(payerButton);
        //Ajouter des gestionnaires d'événements pour les boutons

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                int numeroSiege = Integer.parseInt(textNumeroSiege.getText());
                java.sql.Date sqlDateDepart;
                Date dateDepart;
                try {
                    dateDepart = format.parse(textDateReservation.getText());
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                sqlDateDepart = new java.sql.Date(dateDepart.getTime());
                boolean modeDePaiemen = false;
                String modePaiement = modeDePaiemen ? "mobile money" : "cash";
                String codePaiment = ReservationDAO.genererCodePaiement(modePaiement);
                Reservation reservation = new Reservation(0,"","","","","", "",numeroSiege, sqlDateDepart, codePaiment);
                Bus busSelected = (Bus) busComboBox.getSelectedItem();
                Trajet trajetSelected = (Trajet) trajetComboBox.getSelectedItem();
                Client clientSelected = (Client) clientComboBox.getSelectedItem();
                int idBus = busSelected.getIdBus();


                if (trajetSelected != null && clientSelected != null && busSelected != null) {
                    int idTrajet = trajetSelected.getIdTrajet();
                    int idClient = clientSelected.getIdClient();
                    ReservationDAO.enregistrerReservation(reservation, idClient, idTrajet, idBus);
                    EtatPlaceBusDAO.updatePlacesRestantes(idBus, sqlDateDepart, 1);
                    int placesRestantes = EtatPlaceBusDAO.getPlacesRestantes(idBus,sqlDateDepart);
                    if (placesRestantes >= 0) {
                        JOptionPane.showMessageDialog(ReservationPanel.this, "Il reste " + placesRestantes + " places dans le bus.", "Places Restantes", JOptionPane.INFORMATION_MESSAGE);
                    } else{
                        JOptionPane.showMessageDialog(null, "Le bus est plein. Aucune place supplémentaire ne peut être réservée.", "Bus Plein", JOptionPane.WARNING_MESSAGE);
                    }

                }
                clearTextFields();
                try {
                    refreshTableData();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //annuler une reservation
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    try {
                        String value = String.valueOf(Integer.parseInt(String.valueOf(tableModel.getValueAt(selectedRow, 0))));
                        int idReservation = Integer.parseInt(value);
                        ReservationDetail reservation = selectReservation(idReservation);
                        System.out.println(reservation);
                        if (reservation != null) {
                            ReservationDAO.deleteReservation(idReservation);
                            clearTextFields();
                        }
                    } catch (NumberFormatException | SQLException o) {
                        o.printStackTrace();
                    }

                    try {
                        refreshTableData();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(ReservationPanel.this, "Veuillez sélectionner une résérvation à supprimer.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        //button pour payér
        payerButton.addActionListener(e -> {
            try {
                int selectedRow = table.getSelectedRow();// Méthode pour obtenir l'ID de la réservation sélectionnée
                if (selectedRow >= 0) {
                        String value = String.valueOf(Integer.parseInt(table.getModel().getValueAt(selectedRow, 0).toString()));
                        int idReservation = Integer.parseInt(value);
                        ReservationDetail detail = ReservationDAO.getReservationDetails().get(selectedRow);

                if (detail != null) {
                    String cheminPDF = "src/fichier.pdf";
                   genererFacturePDF(detail, cheminPDF);
                    // Affichage d'un message de confirmation ou ouverture du fichier PDF
                    JOptionPane.showMessageDialog(null, "Paiment éffectuer avec succéss : " + cheminPDF, "Paiment réssi", JOptionPane.INFORMATION_MESSAGE);

                    // Optionnellement, ouvrir le fichier PDF automatiquement
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(new File(cheminPDF));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Détails de réservation introuvables.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de la génération de la facture.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });


        // Configurer le modèle de la table
        tableModel = new DefaultTableModel();
       tableModel.setColumnIdentifiers(new String[]{"ID","Prénom","Client","VDepart","VArrivee","DDepart","HDepart","Bus","NSiege", "DRéservation","MPaiment"});
        table = new JTable(tableModel);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(table), BorderLayout.SOUTH);
        try {
            refreshTableData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    // methode de décrémentation siége bus




//methode pour charger tous les charger dans le combot
    private void chargerTrajets() {
        try {
            List<Trajet> trajets = TrajetDAO.getTousLesTrajets();
            for (Trajet trajet : trajets) {
                trajetComboBox.addItem(trajet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur
        }
    }

    //methode pour charger tous les client dans le combot
    private void chargerClient() {
        try {
            List<Client> clients = ClientDAO.getTousLesTrajets();
            for (Client client : clients) {
                clientComboBox.addItem(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur
        }
    }

    // Méthodes pour interagir avec les données du tableau
    private void refreshTableData() throws SQLException {
        // Effacer les données actuelles de la table
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        loadBusData();
    }
    private void loadBusData() throws SQLException {
        tableModel.setRowCount(0);
        // Récupérer les données des bus et les ajouter à la table
        List<ReservationDetail> reservations = null;
        try {
            reservations = ReservationDAO.getReservationDetails();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (ReservationDetail reservation : reservations) {
            tableModel.addRow(new Object[]{reservation.getIdReservation(), reservation.getPrenomClient(),reservation.getNomClient(),reservation.getVilleDepart(),reservation.getVilleArrivee(),reservation.getDateDepart(),reservation.getHeureDepart(),reservation.getDescription(),reservation.getNumeroSiege(),reservation.getDateReservation(),reservation.getCodePaiment()});
        }
    }

    //methode pour générer une facture
    public static void genererFacturePDF(ReservationDetail detail, String path) {
        Document document = new Document(PageSize.HALFLETTER);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            document.add(new Paragraph("Facture de Réservation"));
            document.add(new Paragraph("Numéro de siége: " + detail.getNumeroSiege()));
            document.add(new Paragraph("Prénom: " + detail.getPrenomClient()));
            document.add(new Paragraph("Nom: " + detail.getNomClient()));
            document.add(new Paragraph("Téléphone: " +detail.getTelephone()));
            document.add(new Paragraph("Ville de départ: " + detail.getVilleDepart()));
            document.add(new Paragraph("Ville d'arrivée: " + detail.getVilleArrivee()));
            document.add(new Paragraph("Heure de départ: " + detail.getHeureDepart()));
            document.add(new Paragraph("Bus: " + detail.getDescription()));
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }


    private void clearTextFields() {
        textNumeroSiege.setText("");
        textDateReservation.setText("");

    }

}

