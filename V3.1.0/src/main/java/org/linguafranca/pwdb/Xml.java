package org.linguafranca.pwdb;

import org.linguafranca.pwdb.format.KdbxCredentials;
import org.linguafranca.pwdb.kdbx.jackson.KdbxDatabase;

import java.io.IOException;
import java.io.InputStream;

/**
 * Read the sample database and print it as XML.
 * <p>
 * From 3.1.0 {@code write} leaves the stream open (KeePassJava2 issue #109), so the XML can be written
 * straight to {@code System.out}, and standard output can still be used afterwards. In 3.0.0
 * {@code save} closed the stream, so the XML had to go through a byte array.
 */
public class Xml {
    public static void main(String[] args) throws IOException {
        KdbxCredentials credentials = new KdbxCredentials("123".getBytes());
        try (InputStream inputStream = Xml.class.getClassLoader().getResourceAsStream("Database-4.1-123.kdbx")) {
            Database database = KdbxDatabase.read(credentials, inputStream);
            database.write(new StreamFormat.None(), new Credentials.None(), System.out);
        }
        System.out.println();
        System.out.println("System.out is still open");
    }
}
