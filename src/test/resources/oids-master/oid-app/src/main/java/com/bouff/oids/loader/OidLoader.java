package com.bouff.oids.loader;

import java.util.Set;

/**
 * Defines a facility to load OIDs from a source.
 */
public interface OidLoader {

    /**
     * Load a set of OIDs from the provided path.
     *
     * @param path       the path to the resource containing the OIDs.
     * @return           a set of OIDs.
     * @throws Exception if the OIDs cannot be loaded from the source.
     */
    Set<String> load(String path) throws Exception;
}
