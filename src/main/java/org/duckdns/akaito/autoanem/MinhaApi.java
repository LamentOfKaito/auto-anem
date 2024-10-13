package org.duckdns.akaito.autoanem;

import org.apache.commons.io.IOUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import com.google.gson.Gson;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.net.URI;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class MinhaApi implements Minha.MinhaQueryable {

    private static final String API_BASE_URL = "https://ac-controle.anem.dz/AllocationChomage/api";

    @Override
    public Future<String> getPreInscriptionId(String numeroDemandeur, String numeroIdentification) {
        String endpoint = "/validateCandidate/query";
        URI uri = buildUri(() -> new URIBuilder(API_BASE_URL + endpoint)
                .addParameter("wassitNumber", numeroDemandeur)
                .addParameter("identityDocNumber", numeroIdentification)
        );

        var data = getInsecure(uri, Minha.CandidateValidationData.class);

        var result = data.preInscriptionId;
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public Future<String> getStructureId(String preInscriptionId) {
        String endpoint = "/PreInscription/GetPreInscription";
        URI uri = buildUri(() -> new URIBuilder(API_BASE_URL + endpoint)
                .addParameter("Id", preInscriptionId)
        );

        var data = getInsecure(uri, Minha.GetPreInscriptionData.class);

        var result = data.structureId;
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public Future<List<String>> getDates(String preInscriptionId, String structureId) {
        String endpoint = "/RendezVous/GetAvailableDates";
        URI uri = buildUri(() -> new URIBuilder(API_BASE_URL + endpoint)
                .addParameter("PreInscriptionId", preInscriptionId)
                .addParameter("StructureId", structureId)
        );

        var data = getInsecure(uri, Minha.GetAvailableDatesData.class);

        var result = data.dates;
        return CompletableFuture.completedFuture(result);
    }

    // ---

    private static URI buildUri(Callable<URIBuilder> uriSupplier) {
        try {
            URIBuilder uriBuilder =         uriSupplier.call();
            return uriBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * - Using Apache WebComponents' HttpClient 5, get the URL with SSL disabled ("insecure").
     * - Using Gson, parse the text/JSON body as a Java object.
     * - If there is any error, console log it and then return null.
     */
    private static <T> T getInsecure(URI url, Class<T> clazz) {
        try {
            // Create a trust strategy that trusts all certificates
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
            // Create an SSL connection socket factory with the SSL context and no hostname verifier
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            var connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                    .setSSLSocketFactory(sslSocketFactory)
                    .build();
            try (CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .build()) {

                HttpGet request = new HttpGet(url);
                return httpClient.execute(request, response -> {
                    if (response.getCode() != 200) {
                        System.err.println(response);
                        System.err.println("Response was not successful.");
                        return null;
                    }
                    String responseBody = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
                    return new Gson().fromJson(responseBody, clazz);
                });
            }
        } catch (Exception e) {
            System.err.println("Error in getInsecure: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
