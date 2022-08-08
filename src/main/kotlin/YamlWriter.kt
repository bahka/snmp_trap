import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.WRITE_DOC_START_MARKER
import io.qameta.allure.Step
import models.Prefixes
import org.apache.commons.lang3.RandomStringUtils
import java.io.File

class YamlWriter {
    @Step("Prepare config '{path}' with prefixes '{prefixes'}")
    fun createFile(prefixes: Prefixes, path: String = ConfigName.getName()): String {
        val name = "src/test/resources/$path"
        val mapper = ObjectMapper(
            YAMLFactory()
//                .enable(MINIMIZE_QUOTES) // doesn't allow me to check really generic oid pattern with just 1 section
                .disable(WRITE_DOC_START_MARKER)
        )

        mapper.writeValue(File(name), prefixes)
        return name
    }
}

object ConfigName {
    fun getName(): String = "configs/${RandomStringUtils.randomAlphabetic(10).lowercase()}.yaml"
    fun getDefault(): String = "oids-master/snmp.yaml"
}