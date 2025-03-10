package org.linguafranca.pwdb;

import org.linguafranca.pwdb.kdbx.jackson.JacksonDatabase;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.jackson.JacksonEntry;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.linguafranca.pwdb.Entry.STANDARD_PROPERTY_NAME_PASSWORD;

public class Issue80 {

    public static void main(String[] args) {
        JacksonDatabase database = openDatabase(args[0], args[1]);
        assert database != null;
    }

    private static JacksonDatabase openDatabase(String databasePath, String password) {
        try (InputStream inputStream = Files.newInputStream(Paths.get(databasePath))) {

            // use KdbxCreds for credentials - KdbxCredentials is re-introduced in V3.0.0
            KdbxCreds credentials = new KdbxCreds(password.getBytes());
            JacksonDatabase database = JacksonDatabase.load(credentials, inputStream);
            System.out.println("Database opened successfully.");

            // find all entries with title, URL, username or notes containing "something" as follows
            // List<? extends JacksonEntry> entries = database.findEntries("something");

            // to find entries whose title (just the title) contains "something" do something like:
            // generics are not necessary in V3.0.0
            List<? extends JacksonEntry> entries = database.findEntries(e -> e.getTitle().toLowerCase().contains("something"));
            for (JacksonEntry entry : entries) {
                System.out.println(entry.getTitle());
            }

            if (!entries.isEmpty()) {
                System.out.println("Found " + entries.size() + " entries.");
                JacksonEntry entry = entries.get(0);
                System.out.println(entry.getPropertyValue(STANDARD_PROPERTY_NAME_PASSWORD).getValueAsString());

                // set a new password on the first entry
                // use PropertyValueStrategy.newProtected() to create a protected property value
                // this all becomes a little less verbose in V3.0.0
                PropertyValue propertyValue = database.getPropertyValueStrategy().newProtected().of("new password");
                entry.setPropertyValue(STANDARD_PROPERTY_NAME_PASSWORD, propertyValue);
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
