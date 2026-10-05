package org.linguafranca.pwdb;

import org.linguafranca.pwdb.format.KdbxCredentials;
import org.linguafranca.pwdb.kdbx.jackson.KdbxDatabase;

import java.io.IOException;
import java.io.InputStream;

/**
 * Read the sample database and list its groups and entries.
 * <p>
 * From 3.1.0 {@code read} leaves the stream open, and try-with-resources closes it.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        KdbxCredentials credentials = new KdbxCredentials("123".getBytes());
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("Database-4.1-123.kdbx")) {
            Database database = KdbxDatabase.read(credentials, inputStream);
            database.visit(new Visitor.Print());
        }
    }
}
