package com.megabank.test.parser;

import com.megabank.test.model.Client;
import com.megabank.test.model.Currency;
import com.megabank.test.model.Transaction;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.megabank.test.parser.soap.SoapParams.ClientFields.*;
import static com.megabank.test.parser.soap.SoapParams.TransactionFields.*;

public class TransactionsParser {
    private static Logger logger = Logger.getLogger(TransactionsParser.class);
    private static final String PATH = "src/main/resources/Java_test.xml";
    private static final String TRANSACTIONS_NODE_NAME = "transactions";

    public static List<Transaction> parseTransactions() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(PATH));
            document.getDocumentElement().normalize();
            Element envelop = document.getDocumentElement();
            Node transactions = getTransactionsNode(envelop);
            List<Transaction> result = new ArrayList<>();
            if(transactions != null) {
                NodeList transactionsList = transactions.getChildNodes();
                for(int i = 0; i < transactionsList.getLength(); i++){
                    Node transactionNode = transactionsList.item(i);
                    if(transactionNode.getNodeType() == Node.ELEMENT_NODE) {
                        Transaction transaction = parseTransaction(transactionNode);
                        result.add(transaction);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            logger.debug("Exception in parser {}", e);
        }
        return Collections.emptyList();
    }

    private static Transaction parseTransaction(Node transactionNode) {
        NodeList fields = transactionNode.getChildNodes();
        Transaction transaction = new Transaction();
        for(int i = 0; i < fields.getLength(); i++) {
            Node field = fields.item(i);
            if(field.getNodeType() != Node.ELEMENT_NODE) continue;
            String nodeName = field.getNodeName();
            String content = field.getTextContent();
            if(place.toString().equals(nodeName)) { transaction.setPlace(content); }
            if(amount.toString().equals(nodeName)) { transaction.setAmount(Float.parseFloat(content)); }
            if(currency.toString().equals(nodeName)) { transaction.setCurrency(Currency.valueOf(content)); }
            if(card.toString().equals(nodeName)) { transaction.setCard(content); }
            if(client.toString().equals(nodeName)) { transaction.setClient(parseClient(field)); }
        }
        return transaction;
    }

    private static Client parseClient(Node clientNode){
        NodeList fields = clientNode.getChildNodes();
        Client client = new Client();
        for(int i = 0; i < fields.getLength(); i++) {
            Node field = fields.item(i);
            if(field.getNodeType() != Node.ELEMENT_NODE) continue;
            String nodeName = field.getNodeName();
            String content = field.getTextContent();
            if(firstName.toString().equals(nodeName)) { client.setFirstName(content); }
            if(lastName.toString().equals(nodeName)) { client.setLastName(content); }
            if(middleName.toString().equals(nodeName)) { client.setMiddleName(content); }
            if(inn.toString().equals(nodeName)) { client.setInn(Long.parseLong(content)); }
        }
        return client;
    }

    private static Node getTransactionsNode(Node node) {
        NodeList children = node.getChildNodes();
        for(int i = 0; i < children.getLength(); i++){
            Node child = children.item(i);
            if (TRANSACTIONS_NODE_NAME.equals(child.getNodeName())){
                   return child;
            } else {
                if(child.getChildNodes().getLength() > 0)
                return getTransactionsNode(child);
            }
        }
        return null;
    }

}
