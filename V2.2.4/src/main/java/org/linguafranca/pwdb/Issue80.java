package org.linguafranca.pwdb;

import org.linguafranca.pwdb.kdbx.jackson.JacksonDatabase;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.jackson.JacksonEntry;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.linguafranca.pwdb.Entry.STANDARD_PROPERTY_NAME_PASSWORD;

public class Issue80 {

    public static void main(String[] args) throws IOException {
        JacksonDatabase database = openDatabase(args[0], args[1]);
    }

    private static JacksonDatabase openDatabase(String databasePath, String password) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(databasePath))) {

            // use KdbxCreds for credentials - KdbxCredentials is re-introduced in V3.0.0
            KdbxCreds credentials = new KdbxCreds(password.getBytes());
            JacksonDatabase database = JacksonDatabase.load(credentials, inputStream);
            System.out.println("Database opened successfully.");

            // find all entries with title, URL, username or notes containing "something" as follows
            // List<? extends JacksonEntry> entries = database.findEntries("something");

            // to find entries whose title (just the title) contains "something" do something like:
            // generics will be simplified in V3.0.0
            List<? extends JacksonEntry> entries = database.findEntries(e -> e.getTitle().toLowerCase().contains("something"));
            for (JacksonEntry entry : entries) {
                System.out.println(entry.getTitle());
            }

            if (!entries.isEmpty()) {
                System.out.println("Found " + entries.size() + " entries.");
                JacksonEntry entry = entries.get(0);
                System.out.println("Password was: " + entry.getPropertyValue(STANDARD_PROPERTY_NAME_PASSWORD).getValueAsString());

                // set a new password on the first entry
                // use PropertyValueStrategy.newProtected() to create a protected property value
                // this all becomes a little less verbose in V3.0.0
                PropertyValue propertyValue = database.getPropertyValueStrategy().newProtected().of("new password");
                entry.setPropertyValue(STANDARD_PROPERTY_NAME_PASSWORD, propertyValue);
                System.out.println("Password is now: " + entry.getPropertyValue(STANDARD_PROPERTY_NAME_PASSWORD).getValueAsString());
            } else {
                System.out.println("No entries found.");
            }
            return database;
        }
    }
}
