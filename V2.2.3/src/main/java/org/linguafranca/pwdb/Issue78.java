package org.linguafranca.pwdb;

import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.jackson.JacksonDatabase;

import java.io.*;

public class Issue78 {

    /**
     * WHen we save a database to an output stream it closes it, we want
     * System.out to remain open so we disable closing
     */
    static PrintStream out = new PrintStream(System.out) {public void close() {}};

    public static void main(String[] args) throws Exception {
        test();
    }

    // from the Issue
    static void test() throws IOException {
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

        // save the XML and check the format
        database.save(new StreamFormat.None(), null, out);

        // Set passphrase as master password
        KdbxCreds creds = new KdbxCreds("123456".getBytes());

        // Save KeePass database to .kdbx file
        try (FileOutputStream outputStream = new FileOutputStream("test.kdbx")) {
            database.save(creds, outputStream);
        }

        out.println("=== Reload ===");
        // reload the database
        try (FileInputStream inputStream = new FileInputStream("test.kdbx")) {
            JacksonDatabase database2 = JacksonDatabase.load(creds, inputStream);
            // review XML to verify it picks up default values
            database2.save(new StreamFormat.None(), null, out);
        }
    }
}
