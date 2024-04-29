package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {


    public static Connection connect() {

        try {
            String url = "jdbc:mysql://localhost:3306/billetery";
            String user = "root";
            String password = "H@kkufr@mwork12*";
            return DriverManager.getConnection(url, user, password);



        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connexion echoué à la base de données !");
            return null;
        }
    }
}