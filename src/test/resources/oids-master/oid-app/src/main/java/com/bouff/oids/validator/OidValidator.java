package com.bouff.oids.validator;

/**
 * Defines a facility to validate OIDs.
 */
public interface OidValidator {

    /**
     * Determines whether or not an input String is a valid OID.
     *
     * @param candidateOid the input String.
     * @return            whether or not the input String is a valid OID.
     */
    boolean isValid(String candidateOid);
}
