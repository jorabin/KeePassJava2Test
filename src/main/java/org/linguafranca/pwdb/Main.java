package org.linguafranca.pwdb;

import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.jackson.JacksonDatabase;

import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        KdbxCreds creds = new KdbxCreds("123".getBytes());
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Database-4.1-123.kdbx");
        try {
            Database database = JacksonDatabase.load(creds, inputStream);
            database.visit(new Visitor.Print());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}