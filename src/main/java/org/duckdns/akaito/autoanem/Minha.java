package org.duckdns.akaito.autoanem;

import java.util.List;
import java.util.concurrent.Future;

public class Minha {

    public interface MinhaQueryable {

        Future<String> getPreInscriptionId(String numeroDemandeur, String numeroIdentification);

        Future<String> getStructureId(String preInscriptionId);

        Future<List<String>> getDates(String preInscriptionId, String structureId);

    }

    // --- DATA CLASSES ---

    public static class CandidateValidationData {
        String preInscriptionId;
    }

    public static class GetPreInscriptionData {
        public String structureId;

        /**
         * E.g. "Alem AZZABA"
         */
        public String structureFr;

        /**
         * E.g. "LIDDELL"
         */
        public String nomDemandeurFr;

        /**
         * E.g. "ALICE"
         */
        public String prenomDemandeurFr;
    }

    public static class GetAvailableDatesData {
        List<String> dates;
    }
}
