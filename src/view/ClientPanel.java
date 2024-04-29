package view;

import controller.ClientDAO;
import model.Client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ClientPanel extends JPanel {
    private JTextField textPrenom;
    private JTextField textNom;
    private JTextField textNumeroIdentite;
    private JTextField textTelephone;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JTable table;
    private DefaultTableModel tableModel;

    public ClientPanel() {
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {

        // Panel pour les champs de saisie
        JPanel inputPanel = new JPanel(new GridLayout(1, 2));

        inputPanel.add(new JLabel("Prénom:"));
        textPrenom = new JTextField(20);
        inputPanel.add(textPrenom);

        inputPanel.add(new JLabel("Nom:"));
        textNom = new JTextField(20);
        inputPanel.add(textNom);

        inputPanel.add(new JLabel("Numéro d'Identité:"));
        textNumeroIdentite = new JTextField(20);
        inputPanel.add(textNumeroIdentite);

        inputPanel.add(new JLabel("Téléphone:"));
        textTelephone = new JTextField(20);
        inputPanel.add(textTelephone);


        // Panel pour les boutons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Ajouter");
        addButton.setBackground(Color.green);
        buttonPanel.add(addButton);
        deleteButton = new JButton("Supprimer");
        deleteButton.setBackground(Color.red);
        buttonPanel.add(deleteButton);
        updateButton = new JButton("Modifier");
        updateButton.setBackground(Color.orange);
        buttonPanel.add(updateButton);

        // Table pour afficher les données
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Prénom", "Nom", "Numéro d'Identité", "Téléphone"});
        table = new JTable(tableModel);

        // Ajouter les composants au panneau
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(table), BorderLayout.SOUTH);
        // Ajouter des gestionnaires d'événements pour les boutons
        // et la logique pour interagir avec la base de données ou le modèle de données
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String prenom = textPrenom.getText();
                String nom = textNom.getText();
                String numeroIdentite =  textNumeroIdentite.getText();
                String numeroTelephone = textTelephone.getText();

                Client client = new Client(0,prenom,nom,numeroIdentite,numeroTelephone);
                ClientDAO.enregistrerClientSiNecessaire(client);
                clearTextFields();
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
                        int idClient = Integer.parseInt(value);
                        Client client = ClientDAO.selectClient(idClient);
                        if (client != null) {
                            ClientDAO.deleteClient(idClient);
                            clearTextFields();
                        }
                    } catch (NumberFormatException | SQLException o) {
                        o.printStackTrace();
                    }

                    refreshTableData();
                } else {
                    JOptionPane.showMessageDialog(ClientPanel.this, "Veuillez sélectionner un client à supprimer.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        refreshTableData();

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Supposons que l'ID est dans la première colonne du modèle
                    int modelRow = table.convertRowIndexToModel(selectedRow);
                    int idClient = (int) table.getModel().getValueAt(modelRow, 0);

                    String prenom = textPrenom.getText();
                    String nom = textNom.getText();
                    String numeroIdentite = textNumeroIdentite.getText();
                    String numeroTelephone = textTelephone.getText();

                   Client client = new Client(idClient,prenom,nom,numeroIdentite,numeroTelephone);
                    try {
                        ClientDAO.updateClient(client);
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

                        textPrenom.setText(valeur2.toString());
                        textNom.setText(valeur3.toString());
                        textNumeroIdentite.setText(valeur4.toString());
                        textTelephone.setText(valeur5.toString());

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
        List<Client> clients = ClientDAO.selectAllClients();
        for (Client client : clients) {
            tableModel.addRow(new Object[]{client.getIdClient(),client.getPrenom(),client.getNom(),client.getNumeroIdentite(),client.getTelephone()});
        }
    }

    private void clearTextFields() {
        textPrenom.setText("");
        textNom.setText("");
        textNumeroIdentite.setText("");
        textTelephone.setText("");
    }

}

