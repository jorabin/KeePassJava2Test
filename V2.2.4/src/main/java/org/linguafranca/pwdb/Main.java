package org.linguafranca.pwdb;

import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.jackson.JacksonDatabase;

import java.io.IOException;
import java.io.InputStream;

/**
 * Load the sample database and list its groups and entries.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        KdbxCreds credentials = new KdbxCreds("123".getBytes());
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("Database-4.1-123.kdbx")) {
            Database database = JacksonDatabase.load(credentials, inputStream);
            database.visit(new Visitor.Print());
        }
    }
}
