package view;

import com.toedter.calendar.JCalendar;
import controller.TrajetDAO;
import model.Trajet;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TrajetPanel extends JPanel {
    private JTextField textVilleDepart;
    private JTextField textVilleArrivee;
    private JTextField textDateDepart;
    private JTextField textHeureDepart;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private JCalendar calendar;
    private JDialog calendarDialog;
    private JDialog timeDialog;
    private JSpinner timeSpinner;


    public TrajetPanel() {
        setLayout(new BorderLayout());
        initializeUI();

    }

    private void initializeUI() {
        JPanel inputPanel = new JPanel(new GridLayout(1, 2));


        inputPanel.add(new JLabel("Ville de départ:"));
        textVilleDepart = new JTextField(20);
        inputPanel.add(textVilleDepart);

        inputPanel.add(new JLabel("Ville d'arrivée:"));
        textVilleArrivee = new JTextField(20);
        inputPanel.add(textVilleArrivee);

        inputPanel.add(new JLabel("Date de départ:"));
        textDateDepart = new JTextField(String.valueOf(LocalDate.now()));

        textDateDepart.addMouseListener(new MouseAdapter() {
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
                textDateDepart.setText(format.format(selectedDate.getTime()));
                calendarDialog.setVisible(false);
            }
        });
        calendarDialog.add(okButton, BorderLayout.SOUTH);

        // Pack après avoir ajouté tous les composants
        calendarDialog.pack();
        calendarDialog.setModal(true);
        inputPanel.add(textDateDepart);

        //heure de depart
        inputPanel.add(new JLabel("Heure de départ:"));
        textHeureDepart = new JTextField(String.valueOf(LocalTime.now()));
        textHeureDepart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                timeDialog.setVisible(true);
            }
        });

        // Configuration du JSpinner pour l'heure
        SpinnerDateModel model = new SpinnerDateModel();
        model.setCalendarField(Calendar.HOUR_OF_DAY);
        timeSpinner = new JSpinner(model);

        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor); // Afficher l'heure avec le format HH:mm

        // Création du JDialog pour le JSpinner
        timeDialog = new JDialog();
        timeDialog.setLayout(new BorderLayout());
        timeDialog.add(timeSpinner, BorderLayout.CENTER);

        JButton okButtonne = new JButton("OK");
        okButtonne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                textHeureDepart.setText(format.format(((Date) timeSpinner.getValue())));
                timeDialog.setVisible(false);
            }
        });
        timeDialog.add(okButtonne, BorderLayout.SOUTH);
        timeDialog.pack();
        timeDialog.setModal(true);
        inputPanel.add(textHeureDepart);


        addButton = new JButton("Ajouter");
        addButton.setBackground(Color.green);
        buttonPanel.add(addButton);
        deleteButton = new JButton("Supprimer");
        deleteButton.setBackground(Color.red);
        buttonPanel.add(deleteButton);
        updateButton = new JButton("Modifier");
        updateButton.setBackground(Color.orange);
        buttonPanel.add(updateButton);


        // Configurer le modèle de la table
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Ville de Départ", "Ville d'Arrivée", "Date de Départ", "Heure de Départ"});
        table = new JTable(tableModel);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(table), BorderLayout.SOUTH);
        //add(new JScrollPane(table), BorderLayout.SOUTH);


        // Ajouter des gestionnaires d'événements pour les boutons

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String villeDepart = textVilleDepart.getText();
                String villeArrivee = textVilleArrivee.getText();
                java.sql.Date sqlDateDepart;
                java.sql.Time sqlHeureDepart;
                Date dateDepart;
                try {
                    dateDepart = format.parse(textDateDepart.getText());
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                sqlDateDepart = new java.sql.Date(dateDepart.getTime());
                System.out.println(sqlDateDepart);


                Time heureDepart = Time.valueOf(LocalTime.parse(textHeureDepart.getText()));
                sqlHeureDepart = new java.sql.Time(heureDepart.getTime());
                System.out.println(sqlHeureDepart);
                Trajet trajet = new Trajet(0, villeDepart, villeArrivee, sqlDateDepart, sqlHeureDepart);

                try {
                    TrajetDAO.insertTrajet(trajet);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                refreshTableData();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    try {
                        String value = String.valueOf(Integer.parseInt(String.valueOf(tableModel.getValueAt(selectedRow, 0))));
                        int idTrajet = Integer.parseInt(value);
                       Trajet trajet = TrajetDAO.selectTrajet(idTrajet);
                        if (trajet != null) {
                           TrajetDAO.deleteTrajet(idTrajet);
                            clearTextFields();
                        }
                    } catch (NumberFormatException | SQLException o) {
                        o.printStackTrace();
                    }

                    refreshTableData();
                } else {
                    JOptionPane.showMessageDialog(TrajetPanel.this, "Veuillez sélectionner un trajet à supprimer.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        refreshTableData();

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Supposons que l'ID est dans la première colonne du modèle
                    int modelRow = table.convertRowIndexToModel(selectedRow);
                    int idTrajet = (int) table.getModel().getValueAt(modelRow, 0);

                    String villeDeDepart = textVilleDepart.getText();
                    String villeDeArrivee = textVilleArrivee.getText();
                    Date dateDeDepart;
                    java.sql.Date sqlDateDepart;
                    java.sql.Time sqlHeureDepart;


                    try {
                        dateDeDepart = format.parse(textDateDepart.getText());
                        sqlDateDepart = new java.sql.Date(dateDeDepart.getTime());
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    String heureDeDepart = textHeureDepart.getText();
                    Time heureDepart = Time.valueOf(LocalTime.parse( textHeureDepart.getText()));
                    sqlHeureDepart = new java.sql.Time(heureDepart.getTime());
                    Trajet trajet = new Trajet(idTrajet,villeDeDepart,villeDeArrivee,sqlDateDepart,sqlHeureDepart);
                    try {
                        TrajetDAO.updateTrajet(trajet);
                        clearTextFields();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    refreshTableData();
                    // Mettez à jour la table après la mise à jour
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un client à mettre à jour.");
                }
            }
        });


        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        Object valeur2 = table.getModel().getValueAt(selectedRow, 1);
                        Object valeur3 = table.getModel().getValueAt(selectedRow,2);
                        Object valeur4 = table.getModel().getValueAt(selectedRow,3);
                        Object valeur5 = table.getModel().getValueAt(selectedRow, 4);
                        // Autres colonnes si nécessaire...

                      textVilleDepart.setText(valeur2.toString());
                        textVilleArrivee.setText(valeur3.toString());
                        textDateDepart.setText(valeur4.toString());
                       textHeureDepart.setText(valeur5.toString());
                        // Mettre à jour d'autres composants...
                    }
                }
            }
        });
    }




    // Méthodes pour interagir avec les données du tableau
    private void refreshTableData() {
        // Effacer les données actuelles de la table
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        loadBusData();
    }
    private void loadBusData() {
        tableModel.setRowCount(0);
        // Récupérer les données des bus et les ajouter à la table
        List<Trajet> trajets = TrajetDAO.selectAllTrajets();
        for (Trajet trajet : trajets) {
            tableModel.addRow(new Object[]{trajet.getIdTrajet(),trajet.getVilleDepart(),trajet.getVilleArrivee(),trajet.getDateDepart(),trajet.getHeureDepart()});
        }
    }



    private void clearTextFields() {
        textVilleDepart.setText("");
        textVilleArrivee.setText("");
        textDateDepart.setText("");
        textHeureDepart.setText("");
    }

}
