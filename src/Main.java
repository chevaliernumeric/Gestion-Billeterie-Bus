import controller.DatabaseConnection;
import view.MainFrame;

import javax.swing.*;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
       final var db = new DatabaseConnection();
        System.out.println(db);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);

            }
        });
    }
}