package contacts_manager.dao;

import com.mysql.cj.jdbc.Driver;
import config.Config;
import contacts_manager.models.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLContactsDAO implements ContactsDAO {

    private Connection connection = null;


    public MySQLContactsDAO() {
        open();
    }



    @Override
    public List<Contact> fetchContacts() throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Contact contact = new Contact();
                contact.setId(resultSet.getLong("id"));
                contact.setFullName(resultSet.getString("name"));
                contact.setPhoneNumber(resultSet.getString("phone"));
                contacts.add(contact);
            }
            return contacts;

        } catch (SQLException e) {
            throw new SQLException("Something went wrong");
        }
    }


    @Override
    public long insertContact(Contact contact) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO contacts" +
                    "(name, phone)" +
                    "VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, contact.getFullName());
            statement.setString(2, contact.getPhoneNumber());

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();

            return keys.getLong(1);
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    @Override
    public void deleteByName(String name) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM contacts WHERE name = ?");
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    @Override
    public Contact searchContacts(String name) throws SQLException {
        Contact contact;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE name = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            contact = new Contact();
            contact.setId(resultSet.getLong("id"));
            contact.setFullName(resultSet.getString("name"));
            contact.setPhoneNumber(resultSet.getString("phone"));

        } catch (SQLException e) {
            throw new SQLException("This contact does not exist");
        }

        return contact;

    }


    public void open() {
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
            throw new RuntimeException("connection failed!!!");
        }
    }

    @Override
    public void close() {
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
