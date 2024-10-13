package org.duckdns.akaito.autoanem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var anemConfig = AnemConfigReader.getConfig();
        if (anemConfig == null) {
            System.err.println("Failed to read config. Verify that it exists.");
            System.exit(1);
        } else {
            System.out.println("Config contents: " + anemConfig);
        }

        var minhaApi = new MinhaApi();
        String preinscriptionId = minhaApi.getPreInscriptionId(anemConfig.numeroDemandeur, anemConfig.numeroIdentification).get();
        String structureId = minhaApi.getStructureId(preinscriptionId).get();
        if (preinscriptionId == null || structureId == null) {
            System.err.println("Failed to get initial ids. Verify the contents of config.");
            System.exit(1);
        }

        System.out.println("Starting the loop...");
        List<String> dates;
        while (true) {
            try {
                dates = minhaApi.getDates(preinscriptionId, structureId).get();
                System.out.println("Last checked: " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                var hasFreeSpots = dates.size() > 0;
                if (hasFreeSpots) {
                    System.out.println("Dates " + dates);
                    System.out.println("Done. Go reserve!");
                    System.exit(0);
                } else {
                    System.out.println("Got nothing.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Something went wrong.");
            }
            System.out.println("Retrying shortly...");
            TimeUnit.MINUTES.sleep(anemConfig.refreshIntervalInMinutes);
        }
    }
}
