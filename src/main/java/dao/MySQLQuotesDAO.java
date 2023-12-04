package dao;

import com.mysql.cj.jdbc.Driver;
import config.Config;
import models.Quote;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLQuotesDAO {
    private Connection connection = null;

    public void createConnection() {
        System.out.println("Connecting to db...");
        try {
            //register driver
            DriverManager.registerDriver(new Driver());

            //establish connection
            connection = DriverManager.getConnection(
                    Config.getUrl(),
                    Config.getUser(),
                    Config.getPassword()
            );
        } catch (SQLException sqlx) {
            System.out.println("Error connection to db..." + sqlx.getMessage());
        }

    }

    public List<Quote> getQuotes() {
        List<Quote> quotes = new ArrayList<>();
        try {

            //Create statement object
            Statement statement = connection.createStatement();

            //Execute statement
            ResultSet resultSet = statement.executeQuery("SELECT * FROM quotes");
            while (resultSet.next()) {
                quotes.add(new Quote(
                        resultSet.getString("author"),
                        resultSet.getString("content")
                ));
            }

        } catch (SQLException sqlx) {
            System.out.println(sqlx.getMessage());
        }

        return quotes;
    }

    public void closeConnection() {
        System.out.println("Closing db connection...");
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException sqlx) {
                System.out.println(sqlx.getMessage());
            }
        }
    }

    public long insertQuotes() {
        //create connection

        //execute statement

        //close connection

        return 0;
    }
}
