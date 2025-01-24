package org.linguafranca.pwdb;

import org.linguafranca.pwdb.kdbx.jackson.JacksonDatabase;

public class Xml {
    public static void main(String[] args) throws Exception {
        Database database = new JacksonDatabase();
        database.save(new StreamFormat.None(), null, System.out);
    }
}
