package org.linguafranca.pwdb;

import org.linguafranca.pwdb.format.KdbxCredentials;
import org.linguafranca.pwdb.kdbx.jackson.KdbxDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Times from 3.1.0 (KeePassJava2 issue #111).
 * <p>
 * {@code Entry} and {@code Group} times are {@code java.time.Instant} rather than {@code java.util.Date}.
 * Code written for 3.0.0 that needs a {@code Date} converts at the call, as shown below.
 */
public class Times {
    public static void main(String[] args) throws IOException {
        KdbxCredentials credentials = new KdbxCredentials("123".getBytes());
        try (InputStream inputStream = Times.class.getClassLoader().getResourceAsStream("Database-4.1-123.kdbx")) {
            Database database = KdbxDatabase.read(credentials, inputStream);
            Entry entry = database.findEntries(e -> true).get(0);

            // 3.1.0: times are Instants, in UTC
            Instant created = entry.getCreationTime();
            System.out.println(entry.getTitle() + " created " + created);
            // for display in local time
            System.out.println("which is " + created.atZone(ZoneId.systemDefault()) + " here");

            // upgrading 3.0.0 code that uses Date: convert at the call
            Date createdDate = Date.from(entry.getCreationTime());
            System.out.println("as a Date " + createdDate);

            Date expiryDate = Date.from(Instant.now().plus(30, ChronoUnit.DAYS));
            entry.setExpiryTime(expiryDate.toInstant());
            entry.setExpires(true);
            System.out.println("expires " + entry.getExpiryTime());
        }
    }
}
