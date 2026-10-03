package org.linguafranca.pwdb;

import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.jackson.JacksonDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Issue 78: check the XML of a new database, and of the same database after saving and reloading it
 */
public class Issue78 {

    public static void main(String[] args) throws IOException {
        // Create new (empty) KeePass database
        Database database = new JacksonDatabase();
        database.setName("Test KeePass");
        database.setShouldProtect(Entry.STANDARD_PROPERTY_NAME_PASSWORD, true);
        Group rootGroup = database.getRootGroup();

        // Create and add a new group
        Group newGroup = database.newGroup("Test Group");
        rootGroup.addGroup(newGroup);

        // Create and add an entry to the new group
        Entry newEntry = database.newEntry();
        newEntry.setTitle("Test Entry");
        newEntry.setUsername("alice");
        newEntry.setUrl("http://localhost:8080/test");
        newEntry.setProperty(Entry.STANDARD_PROPERTY_NAME_PASSWORD, "password123");
        newGroup.addEntry(newEntry);

        // check the format of the XML
        System.out.println(Xml.toXml(database));

        // Set passphrase as master password
        KdbxCreds credentials = new KdbxCreds("123456".getBytes());

        // Save KeePass database to a .kdbx file
        Path path = Paths.get("target", "Issue78.kdbx");
        Files.createDirectories(path.getParent());
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            database.save(credentials, outputStream);
        }

        System.out.println("=== Reload ===");
        try (InputStream inputStream = Files.newInputStream(path)) {
            Database database2 = JacksonDatabase.load(credentials, inputStream);
            // check the XML picks up default values
            System.out.println(Xml.toXml(database2));
        }
    }
}
