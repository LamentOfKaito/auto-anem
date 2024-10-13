package org.duckdns.akaito.autoanem;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.File;

public class AnemConfigReader {

    static final String CONFIG_FILENAME = "auto-anem.properties";

    static final long DEFAULT_REFRESH_INTERVAL_IN_MINUTES = 15;

    static final String NUMERO_DEMANDEUR_KEY = "numeroDemandeur";
    static final String NUMERO_IDENTIFICATION_KEY = "numeroIdentification";
    static final String REFRESH_INTERVAL_IN_MINUTES_KEY = "refreshIntervalInMinutes";

    public static AnemConfig getConfig() {
        AnemConfig result = new AnemConfig();

        try {
            String homeDir = System.getProperty("user.home");

            // Load the properties file
            File propertiesFile = new File(homeDir, CONFIG_FILENAME);
            Configurations configs = new Configurations();
            PropertiesConfiguration config = configs.properties(propertiesFile);

            result.numeroDemandeur = config.getString(NUMERO_DEMANDEUR_KEY);
            result.numeroIdentification = config.getString(NUMERO_IDENTIFICATION_KEY);
            result.refreshIntervalInMinutes = config.getLong(REFRESH_INTERVAL_IN_MINUTES_KEY, DEFAULT_REFRESH_INTERVAL_IN_MINUTES);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
