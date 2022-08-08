package com.bouff.oids.validator;

import org.junit.Assert;
import org.junit.Test;

/**
 * A test class for '{@link OidValidator}'.
 */
public class OidValidatorTest {

    /**
     *  The OID validator to test.
     */
    private OidValidator validator;

    /**
     * Constructor.
     */
    public OidValidatorTest() {
        validator = new BasicOidValidator();
    }

    /**
     * Test for valid OIDs.
     */
    @Test
    public void testValid() {
        Assert.assertTrue(validator.isValid(".1"));
        Assert.assertTrue(validator.isValid(".123"));
        Assert.assertTrue(validator.isValid(".1.3.6.1.2.1.14.16.2.2"));
        Assert.assertTrue(validator.isValid("  .1.5.4.3  "));
    }

    /**
     * Test for invalid OIDs.
     */
    @Test
    public void testInvalid() {
        Assert.assertFalse(validator.isValid(null));
        Assert.assertFalse(validator.isValid(""));
        Assert.assertFalse(validator.isValid(" "));
        Assert.assertFalse(validator.isValid("."));
        Assert.assertFalse(validator.isValid(".123."));
        Assert.assertFalse(validator.isValid("ABC"));
        Assert.assertFalse(validator.isValid(".1.2.C"));
    }
}
