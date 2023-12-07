package contacts_manager.dao;

import contacts_manager.models.Contact;

import java.sql.SQLException;
import java.util.List;

public interface ContactsDAO {
    List<Contact> fetchContacts() throws SQLException;
    long insertContact(Contact contact) throws SQLException;
    void deleteByName(String name) throws SQLException;
    Contact searchContacts(String searchTerm) throws SQLException;

    void open();
    void close();
//    List<Contact> printContacts() throws SQLException;
}