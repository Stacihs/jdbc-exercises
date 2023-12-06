package contacts_manager.dao;

import com.mysql.cj.jdbc.Driver;
import config.Config;
import dao.MySQLAlbumsException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLContactsDAO {

    private Connection connection = null;

    public MySQLContactsDAO() {
        createConnection();
    }


    public void createConnection() throws MySQLAlbumsException {
        System.out.print("Trying to connect... ");
        try {
            DriverManager.registerDriver(new Driver());

            connection = DriverManager.getConnection(
                    Config.getUrl(),
                    Config.getUser(),
                    Config.getPassword()
            );

            System.out.println("connection created.");
        } catch (SQLException e) {
            throw new MySQLAlbumsException("connection failed!!!");
        }
    }



    public void closeConnection() {
        if (connection == null) {
            System.out.println("Connection aborted.");
            return;
        }
        try {
            connection.close();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            // ignore this
        }
    }
}
