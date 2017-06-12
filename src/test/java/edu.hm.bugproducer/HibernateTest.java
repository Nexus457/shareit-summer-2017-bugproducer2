package edu.hm.bugproducer;


import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.persistenceLayer.HibernateUtil;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;

public class HibernateTest {

    private static final String NAME = "TestName1";
    private static final String NAME_ALT = "TestName2";
    private static final String TITLE = "TestTitle1";
    private static final String TITLE_ALT = "TestTitle2";
    private static final String ISBN = "3-446-193138";


    @Test
    public void testDb() {
        Book book = new Book(NAME, ISBN, TITLE);

        //Get Session
        Session session = HibernateUtil.getSessionFactory().openSession();
        //start transaction
        session.beginTransaction();
        //Save the Model object
        session.save(book);

        //Commit transaction
        session.getTransaction().commit();
        session.close();

        //Get Session
        Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
        //start transaction
        session2.beginTransaction();
        //get book by isbn
        Book book2 = session2.get(Book.class, "3-446-193138");
        //Commit transaction
        session2.getTransaction().commit();

        System.err.println("" + book2.toString());
        Assert.assertEquals(book.toString(), book2.toString());

        //terminate session factory, otherwise program won't end
        HibernateUtil.getSessionFactory().close();
    }


}
