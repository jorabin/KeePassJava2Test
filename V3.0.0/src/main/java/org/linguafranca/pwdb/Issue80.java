package org.linguafranca.pwdb;

import org.linguafranca.pwdb.format.KdbxCredentials;
import org.linguafranca.pwdb.kdbx.jackson.KdbxDatabase;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.linguafranca.pwdb.Entry.STANDARD_PROPERTY_NAME.PASSWORD;

public class Issue80 {

    public static void main(String[] args) {
        KdbxDatabase database = openDatabase(args[0], args[1]);
        assert database != null;
    }

    // KdbxDatabase become the standard database type in V3.0.0
    private static KdbxDatabase openDatabase(String databasePath, String password) {
        try (InputStream inputStream = Files.newInputStream(Paths.get(databasePath))) {

            // use KdbxCredentials  in V3.0.0
            KdbxCredentials credentials = new KdbxCredentials(password.getBytes());
            KdbxDatabase database = KdbxDatabase.load(credentials, inputStream);
            System.out.println("Database opened successfully.");

            // find all entries with title, URL, username or notes containing "something" as follows
            // List<Entry> entries = database.findEntries("something");

            // to find entries whose title (just the title) contains "something" do something like:
            // note use of List<Entry> - generics are simplified in V3.0.0
            List<Entry> entries = database.findEntries(e -> e.getTitle().toLowerCase().contains("something"));
            for (Entry entry : entries) {
                System.out.println(entry.getTitle());
            }

            if (!entries.isEmpty()) {
                System.out.println("Found " + entries.size() + " entries.");
                Entry entry = entries.get(0);
                // static import for PASSWORD in V3.0.0
                System.out.println("Password was: " + entry.getPropertyValue(PASSWORD).getValueAsString());

                // set a new password on the first entry
                // add property short-cut uses PropertyValueStrategy to determine storage type in V3.0.0
                entry.addProperty(PASSWORD, "new password");
                System.out.println("Password changed: " + entry.getPropertyValue(PASSWORD).getValueAsString());
            } else {
                System.out.println("No entries found.");
            }
            return database;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to open the database.");
            return null;
        }
    }
}
