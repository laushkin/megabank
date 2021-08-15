package com.megabank.test;

import com.megabank.test.model.Client;
import com.megabank.test.model.Currency;
import com.megabank.test.model.Transaction;
import com.megabank.test.parser.TransactionsParser;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TransactionsTest {
    private SessionFactory sessionFactory;
    private Session session;

    @Before
    public void setUp() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
        this.session = sessionFactory.openSession();
    }

    @After
    public void close() {
        this.session.close();
        this.sessionFactory.close();
    }

    @Test
    public void testNumberOfParsedTransactions() {
        List<Transaction> transactions = TransactionsParser.parseTransactions();
        assertEquals(transactions.size(), 12);
    }

    @Test
    public void testTransactionCurrencies() {
        Transaction transaction = new Transaction();
        transaction.setCurrency(Currency.EUR);
        Long id = (Long) session.save(transaction);
        Transaction transactionFromDb = session.get(Transaction.class, id);
        assertNotNull(transactionFromDb.getCurrency());
        assertEquals(transaction.getCurrency(), transactionFromDb.getCurrency());
    }

    @Test(expected = NonUniqueObjectException.class)
    public void testIfIdsCouldNotBeSame() {
        Client client1 = new Client();
        Client client2 = new Client();
        client1.setInn(1L);
        client2.setInn(1L);
        session.save(client1);
        session.save(client2);
    }

    @Test(timeout = 1000)
    public void testParsingTime() {
        TransactionsParser.parseTransactions();
    }

}
