package contacts_manager;

import contacts_manager.dao.ContactsDAO;
import contacts_manager.dao.FileContactsDAO;
import contacts_manager.dao.MySQLContactsDAO;
import contacts_manager.models.Contact;
import contacts_manager.utils.Input;


import java.sql.SQLException;
import java.util.List;

public class App {
    public static void main(String[] args) throws SQLException {
        ContactsDAO contactsDAO = new MySQLContactsDAO();

        try {
//            contactsDAO.open();
            Input input = new Input();

            while (true) {
                System.out.println("1. View contacts.\n" +
                        "2. Add a new contact.\n" +
                        "3. Search a contact by name.\n" +
                        "4. Delete an existing contact by name.\n" +
                        "5. Exit.\n" +
                        "Enter an option (1, 2, 3, 4 or 5):");
                int option = input.getInt(1, 5);
                switch (option) {
                    case 1:
                        List<Contact> contacts = contactsDAO.fetchContacts();
                        System.out.println(contacts.toString());
                        break;
                    case 2:
                        String fn = input.getString("Give me the full name");
                        String phone = input.getString("Give me the phone number");
                        Contact contact = new Contact(fn, phone);
                        contactsDAO.insertContact(contact);
                        break;
                    case 3:
                        String term = input.getString("Give me the name to search");
                        contact = contactsDAO.searchContacts(term);
                        System.out.println(contact.toString());
                        break;
                    case 4:
                        String aNumber = input.getString("Give me the name to delete");
                        contactsDAO.deleteByName(aNumber);
                        break;
                    case 5:
                        System.exit(0);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error connection to db..." + e.getMessage());
        }
    }

}
