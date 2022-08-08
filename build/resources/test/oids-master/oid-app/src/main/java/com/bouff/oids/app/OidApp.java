package com.bouff.oids.app;

/**
 * A definition for the OID application.
 */
public interface OidApp {

    /**
     * Determine if the provided OID is a descendant (based on the collection OID
     * prefixes maintained by the implementation).
     *
     * @param candiateOid the OID.
     * @return            whether or not the OID is a descendant.
     */
    boolean isOidDescendant(String candiateOid);
}
