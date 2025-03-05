package org.linguafranca.pwdb;

import org.linguafranca.pwdb.kdbx.jackson.JacksonDatabase;

import java.io.IOException;

public class Issue78 {
    public static void main(String[] args) throws Exception {
        test();
    }

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

        database.save(new StreamFormat.None(), null, System.out);

        /*// Set passphrase as master password
        KdbxCreds creds = new KdbxCreds("123456".getBytes());

        // Save KeePass database to .kdbx file
        File outputFile = new File("test.kdbx");
        try (
                FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            database.save(creds, outputStream);
        }*/
    }
}
