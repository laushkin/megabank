package com.megabank.test.parser.soap;

public class SoapParams {
    public enum TransactionFields {
        place,
        amount,
        currency,
        card,
        client;
    }

    public enum ClientFields {
        firstName,
        lastName,
        middleName,
        inn
    }
}
