package com.bouff.oids.loader;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import com.bouff.oids.validator.OidValidator;

/**
 * A Snake YAML backed implementation of a {@link OidLoader}.
 */
public class YamlOidLoader implements OidLoader {

    /**
     * The key in the YAML document which is expected to contain a list of OID prefixes.
     */
    private static final String YAML_KEY = "trap-type-oid-prefix";

    /**
     * The Snake YAML object; used to parse the YAML file.
     */
    private Yaml yaml;

    /**
     * The validator for OIDs.
     */
    private OidValidator validator;

    /**
     * Constructor.
     *
     * @param oidValidator a validator for OIDs.
     */
    public YamlOidLoader(OidValidator oidValidator) {

        // With the following option, if YAML key duplicate(s) are found, then an exception will be thrown (instead of
        // the last duplicate being used).
        LoaderOptions loadOptions = new LoaderOptions();
        loadOptions.setAllowDuplicateKeys(false);

        yaml = new Yaml(loadOptions);
        validator = oidValidator;
    }

    @Override
    public Set<String> load(String pathYamlFile) throws Exception {

        Map<String, List<String>> yamlMap = yaml.load(new FileInputStream(new File(pathYamlFile)));
        if (yamlMap == null || yamlMap.isEmpty()) {
            throw new Exception("empty document");
        }

        List<String> oidList = yamlMap.get(YAML_KEY);
        if (oidList == null || oidList.isEmpty()) {
            throw new Exception("no oids defined under key '" + YAML_KEY + "'");
        }

        // For all elements: remove leading/trailing whitespace, if the entry is a valid OID,
        // collect into a set (which means deduplication as well).
        Set<String> oidSet = oidList.stream().map(String::trim).filter(entry -> validator.isValid(entry)).collect(
                Collectors.toSet());

        if (oidSet.isEmpty()) {
            throw new Exception("no valid oids found under key '" + YAML_KEY + "'");
        }

        return oidSet;
    }
}
