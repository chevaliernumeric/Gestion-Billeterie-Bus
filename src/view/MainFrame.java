package view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Système de Réservation de Billets");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeUI();
    }

    private void initializeUI() {
        // Créer la barre de menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem exitItem = new JMenuItem("Quitter");
        view.ReservationPanel reservationPanel = new ReservationPanel();

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // On Crée un onglet pour chaque entité
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Accueil", new HomePanel());
        tabbedPane.addTab("Gestion des Bus", new BusPanel());
        tabbedPane.addTab("Gestion des  Trajets", new TrajetPanel());
        tabbedPane.addTab("Gestion des Clients",new ClientPanel());
        tabbedPane.addTab("Gestion des Réservations", new ReservationPanel());
        //Permet de séléctionner chaque onglet en fonction de son index
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                System.out.println("Onglet sélectionné: " + selectedIndex);
                // Ici, vous pouvez ajouter d'autres actions à effectuer
                // lors du changement d'onglet, comme rafraîchir les données
            }
        });


        // Ajouter le panneau d'onglets au cadre
        add(tabbedPane);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });

    }
}

