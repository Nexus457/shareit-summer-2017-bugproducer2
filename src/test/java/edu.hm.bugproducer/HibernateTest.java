package edu.hm.bugproducer;


import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.persistenceLayer.HibernateUtil;
import org.hibernate.Session;
import org.junit.Test;

public class HibernateTest {

    private static final String NAME = "TestName1";
    private static final String NAME_ALT = "TestName2";
    private static final String TITLE = "TestTitle1";
    private static final String TITLE_ALT = "TestTitle2";
    private static final String ISBN = "3-446-193138";


    @Test
    public void testDb() {
        Book book = new Book(NAME, TITLE, ISBN);

        //Get Session
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //start transaction
        session.beginTransaction();
        //Save the Model object
        session.save(book);
        //Commit transaction
        session.getTransaction().commit();
        System.out.println("Book ISBN=" + book.getIsbn());

        //terminate session factory, otherwise program won't end
        HibernateUtil.getSessionFactory().close();
    }
}
