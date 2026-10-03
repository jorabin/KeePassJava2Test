package org.linguafranca.pwdb;

import org.linguafranca.pwdb.format.KdbxCredentials;
import org.linguafranca.pwdb.kdbx.jackson.KdbxDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Load the sample database and print it as XML.
 * <p>
 * {@code save} closes the stream it is given (KeePassJava2 issue #109), so saving straight to
 * {@code System.out} would close standard output, and anything printed afterwards would be lost.
 * Instead, save to a byte array and print that.
 */
public class Xml {
    public static void main(String[] args) throws IOException {
        KdbxCredentials credentials = new KdbxCredentials("123".getBytes());
        try (InputStream inputStream = Xml.class.getClassLoader().getResourceAsStream("Database-4.1-123.kdbx")) {
            Database database = KdbxDatabase.load(credentials, inputStream);
            System.out.println(toXml(database));
        }
    }

    /**
     * Save a database as unencrypted XML and return it as a string
     */
    static String toXml(Database database) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            database.save(new StreamFormat.None(), new Credentials.None(), outputStream);
            return outputStream.toString(StandardCharsets.UTF_8);
        }
    }
}
