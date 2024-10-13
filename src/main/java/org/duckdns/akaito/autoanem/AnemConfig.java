package org.duckdns.akaito.autoanem;

public class AnemConfig {
    public String numeroDemandeur;
    public String numeroIdentification;
    public long refreshIntervalInMinutes;

    @Override
    public String toString() {
        return String.format(
                "%s(%s, %s, %d)", AnemConfig.class.getSimpleName(),
                this.numeroDemandeur, numeroIdentification, refreshIntervalInMinutes
        );
    }
}
