package view;
import controller.BusDAO;
import model.Bus;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;


public class BusPanel extends JPanel {
    private JTextField textDescription;
    private JComboBox<String> comboEtat;
    private JTextField textCapacite;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JTable table;
    private DefaultTableModel tableModel;

    public BusPanel() {
        setLayout(new BorderLayout());

        // Panel pour les champs de saisie
        JPanel inputPanel = new JPanel(new GridLayout(1, 2));

        inputPanel.add(new JLabel("Description:"));
        textDescription = new JTextField(20);
        inputPanel.add(textDescription);

        inputPanel.add(new JLabel("État:"));
        String[] etats = {"panne", "fonctionnel"};
        comboEtat = new JComboBox<>(etats);
        inputPanel.add(comboEtat);

        inputPanel.add(new JLabel("Capacité:"));
        textCapacite = new JTextField(20);
        inputPanel.add(textCapacite);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Ajouter");
        addButton.setBackground(Color.GREEN);
        buttonPanel.add(addButton);
        deleteButton = new JButton("Supprimer");
        deleteButton.setBackground(Color.red);
        buttonPanel.add(deleteButton);
        updateButton = new JButton("Modifier");
        updateButton.setBackground(Color.ORANGE);
        buttonPanel.add(updateButton);

        // Tableau pour afficher les bus
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Description", "État", "Capacité"});
        table = new JTable(tableModel);

        // Ajouter les composants au panneau
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(table), BorderLayout.SOUTH);

        // les gestionnaire d'événements sur les buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description = textDescription.getText();
                String etat =  comboEtat.getSelectedItem().toString();
                int capacite = Integer.parseInt(textCapacite.getText());
                Bus bus = new Bus(0, description, etat, capacite);
                try {
                    BusDAO.insertBus(bus);
                    clearTextFields();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                refreshTableData();
            }
        });

        refreshTableData();
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    try {
                        String value = String.valueOf(Integer.parseInt(String.valueOf(tableModel.getValueAt(selectedRow, 0))));
                        int idBus = Integer.parseInt(value);
                        Bus bus = BusDAO.selectBusById(idBus);
                        if (bus != null) {
                            BusDAO.deleteBus(idBus);
                            clearTextFields();
                        }
                    } catch (NumberFormatException | SQLException o) {
                        o.printStackTrace();
                    }

                    refreshTableData();
                } else {
                    JOptionPane.showMessageDialog(BusPanel.this, "Veuillez sélectionner un bus à supprimer.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Supposons que l'ID est dans la première colonne du modèle
                    int modelRow = table.convertRowIndexToModel(selectedRow);
                    int idBus = (int) table.getModel().getValueAt(modelRow, 0);

                    String description = textDescription.getText();
                    String etat = comboEtat.getSelectedItem().toString();
                    int capacite = Integer.parseInt(textCapacite.getText());

                    Bus bus = new Bus(idBus, description, etat, capacite);
                    try {
                        BusDAO.updateBus(bus);
                        clearTextFields();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    refreshTableData();
                    // Mettez à jour la table après la mise à jour
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un bus à mettre à jour.");
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        //Object valeur1 = table.getModel().getValueAt(selectedRow, 0); // Index de la colonne 0
                        Object valeur2 = table.getModel().getValueAt(selectedRow, 1);
                        //Object valeur3 = table.getModel().getValueAt(selectedRow,2);
                        Object valeur4 = table.getModel().getValueAt(selectedRow,3);// Index de la colonne 1
                        // Autres colonnes si nécessaire...

                        textDescription.setText(valeur2.toString());
                        textCapacite.setText(valeur4.toString());
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
        List<Bus> buses = BusDAO.selectAllBuses();
        for (Bus bus : buses) {
            tableModel.addRow(new Object[]{bus.getIdBus(),bus.getDescription(), bus.getEtat(), bus.getCapacite()});
        }
    }
    private void clearTextFields() {
        textDescription.setText("");

        textCapacite.setText("");
    }



}

