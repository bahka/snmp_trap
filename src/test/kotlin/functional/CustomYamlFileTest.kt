package functional

import AppParameter
import models.AppResponses.succeed
import CLIApplication
import YamlWriter
import io.kotest.matchers.shouldBe
import models.Prefixes
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@Tags(Tag("regression"), Tag("launch"))
@DisplayName("Tests with custom config (./oid.sh -f file.yaml)")
class CustomYamlFileTest {
    private val shortPrefix = ".9"
    private val longPrefix = ".1.3.6.1.4.1.2681.1.4.5.1.1.99.1.1.6.12.8.4.3.2.10.12.15.75.32.12.32.5.9"
    private val customCompany = ".1.3.6.1.4.99999.10000.333333.4444444.55555555"
    private val prefixes = Prefixes(
        listOf(
            shortPrefix,
            customCompany,
            longPrefix,
            customCompany
        )
    )
    private val configName = YamlWriter().createFile(prefixes = prefixes)
    private val app: CLIApplication = CLIApplication(params = AppParameter.withCustomConfig(name = configName))

    @BeforeAll
    fun skipWelcomeMessage() {
        app.readOutput()
    }

    @AfterAll
    fun tearDown() {
        app.destroy()
    }

    private fun provideAllPrefixesFromCustomConfig(): Stream<String> {
        return prefixes.`trap-type-oid-prefix`.stream()
    }

    @ParameterizedTest
    @MethodSource("provideAllPrefixesFromCustomConfig")
    @DisplayName("Verify that OID from a custom yaml config has `true` for full match")
    fun verifyOIDWithFullMatch(oid: String) {
        app.verifyOID(oid) shouldBe succeed(oid)
    }

    @Test
    @DisplayName("Verify that OID with the same first octet will be marked as 'true'")
    fun verifyOIDWithGenericPrefix() {
        val oid = "$shortPrefix.1.2.3.4.6.7.8.9.0.111.21"
        app.verifyOID(oid) shouldBe succeed(oid)
    }

    @Test
    @DisplayName("Verify that OID with about 30 sections will be marked as 'true'")
    fun verifyLongOID() {
        app.verifyOID(longPrefix) shouldBe succeed(longPrefix)
    }

    @Test
    @DisplayName("Verify that OID with big numbers in sections will be marked as 'true'")
    fun verifyOIDWithLongSections() {
        app.verifyOID(customCompany) shouldBe succeed(customCompany)
    }
}