import dao.MySQLQuotesDAO;
import models.Quote;

import java.util.List;
import java.util.jar.JarOutputStream;

public class QuotesTest {

    public static void main(String[] args) {
        MySQLQuotesDAO quotesDAO = new MySQLQuotesDAO();
        quotesDAO.createConnection();
        List<Quote> quotesFromDb = quotesDAO.getQuotes();
        for (Quote quote : quotesFromDb) {
            System.out.println(quote.getAuthor() + " said:");
            System.out.println(quote.getContent());
        }
        quotesDAO.closeConnection();
    }
}
