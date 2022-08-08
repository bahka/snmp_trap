package com.bouff.oids.app;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

/**
 * A test class for '{@link OidAppImpl}'.
 */
public class OidAppTest {

    /**
     * The OID application.
     */
    private OidApp oidApp;

    /**
     * Constructor.
     */
    public OidAppTest() {
        oidApp = new OidAppImpl(new HashSet<>(Arrays.asList(
                ".1.3.6.1.6.3.1.1.5",
                ".1.3.6.1.2.1.10.166.3",
                ".1.3.6.1.4.1.9.9.117.2",
                ".1.3.6.1.2.1.10.32.0.1",
                ".1.3.6.1.2.1.14.16.2.2",
                ".1.3.6.1.4.1.9.10.137.0.1")));
    }

    /**
     * Test whether various OIDs are descendants or not.
     */
    @Test
    public void test() {
        // Case 1: OID is a descendant of a prefix.
        Assert.assertTrue(oidApp.isOidDescendant(".1.3.6.1.4.1.9.9.117.2.0.1"));
        Assert.assertTrue(oidApp.isOidDescendant(".1.3.6.1.4.1.9.10.137.0.1.111.222.333.444.555"));

        // Case 2: OID is exactly a prefix.
        Assert.assertTrue(oidApp.isOidDescendant(".1.3.6.1.4.1.9.9.117.2"));
        Assert.assertTrue(oidApp.isOidDescendant(".1.3.6.1.4.1.9.10.137.0.1"));

        // Case 3: OID doesn't match a prefix.
        Assert.assertFalse(oidApp.isOidDescendant(".1.3.6.1.4.1.9.9.118.2.0.1"));
        Assert.assertFalse(oidApp.isOidDescendant(".1.5"));
        Assert.assertFalse(oidApp.isOidDescendant(".99"));

        // Case 4: OID "partially matches" a prefix.  This must return false since these OIDs are ancestors.
        Assert.assertFalse(oidApp.isOidDescendant(".1.3.6"));
        Assert.assertFalse(oidApp.isOidDescendant(".1.3.6.1.2"));
        Assert.assertFalse(oidApp.isOidDescendant(".1.3.6.1.4"));
        Assert.assertFalse(oidApp.isOidDescendant(".1.3.6.1.6.3.1.1"));
    }
}
