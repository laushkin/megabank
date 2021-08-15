import com.megabank.test.model.Transaction;
import com.megabank.test.parser.TransactionsParser;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        List<Transaction> transactions = TransactionsParser.parseTransactions();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            for(Transaction transaction : transactions){
                session.merge(transaction.getClient());
                session.save(transaction);
            }
            session.getTransaction().commit();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root root = cq.from(Transaction.class);
            cq.select(root);
            Query query = session.createQuery(cq);
            List<Transaction> transactionsFromDb = query.getResultList();

            for(Transaction  transaction : transactionsFromDb) {
                logger.debug(transaction);
            }

        } catch (Exception e) {
            logger.debug("DB exception {}", e);
        } finally {
            session.close();
            sessionFactory.close();
        }
    }
}
