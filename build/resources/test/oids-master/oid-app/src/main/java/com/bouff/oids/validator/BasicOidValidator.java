package com.bouff.oids.validator;

import java.util.regex.Pattern;

/**
 * A basic implementation of {@link OidValidator}.
 */
public class BasicOidValidator implements OidValidator {

    /**
     * A pattern for OIDs.
     */
    private static final Pattern OID_PATTERN = Pattern.compile("((\\.)(\\d+))+");

    /**
     * Constructor.
     */
    public BasicOidValidator() {
    }

    @Override
    public boolean isValid(String candidateOid) {
        return candidateOid != null && (OID_PATTERN.matcher(candidateOid.trim())).matches();
    }
}
