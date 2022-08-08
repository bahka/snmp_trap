package functional

import models.AppResponses.failed
import models.AppResponses.invalid
import models.AppResponses.succeed
import CLIApplication
import io.kotest.matchers.shouldBe
import models.Prefixes
import org.apache.commons.lang3.RandomStringUtils
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
@DisplayName("Tests with application with default config (./oid.sh)")
class DefaultYamlFileTest {
    // the same prefixes as in src/test/resources/oids-master/snmp.yaml
    private val prefixes = Prefixes(
        listOf(
            ".1.3.6.1.6.3.1.1.5",
            ".1.3.6.1.2.1.10.166.3",
            ".1.3.6.1.4.1.9.9.117.2",
            ".1.3.6.1.2.1.10.32.0.1",
            ".1.3.6.1.2.1.14.16.2.2",
            ".1.3.6.1.4.1.9.10.137.0.1"
        )
    )
    private val firstPrefix: String = prefixes.`trap-type-oid-prefix`.first()
    private val app = CLIApplication()

    @BeforeAll
    fun skipWelcomeMessage() {
        app.readOutput()
    }

    @AfterAll
    fun tearDown() {
        app.destroy()
    }

    private fun provideAllPrefixesFromExample(): Stream<String> {
        return prefixes.`trap-type-oid-prefix`.stream()
    }

    @ParameterizedTest
    @MethodSource("provideAllPrefixesFromExample")
    @DisplayName("Verify that OID from the yaml config has `true` for full match")
    fun verifyOIDWithFullMatch(oid: String) {
        app.verifyOID(oid) shouldBe succeed(oid)
    }

    @Test
    @DisplayName("Verify that OID with extended last section will be marked as 'false' (ie: .1 doesn't match with .10")
    fun verifyOIDWithExtendedLastSection() {
        val oid = "$firstPrefix${RandomStringUtils.randomNumeric(1)}"
        app.verifyOID(oid) shouldBe failed(oid)
    }

    @Test
    @DisplayName("Verify that OID with additional sections at the end should be marked as 'true'")
    fun verifyOIDEndsWithAdditionalSections() {
        val oid = "$firstPrefix.1.2.3.4.6.7.8.0.1"
        app.verifyOID(oid) shouldBe succeed(oid)
    }

    @Test
    @DisplayName("Verify that OID with additional sections at the beginning should be marked as 'true'")
    fun verifyOIDBeginsWithAdditionalSections() {
        val oid = ".1$firstPrefix"
        app.verifyOID(oid) shouldBe failed(oid)
    }

    private fun provideOIDWithWrongFormat(): Stream<String> {
        return listOf("1.1.1.1", "100", "a.b.c.d.e", ".1.2.3.o.4", ".1..1").stream()
    }

    @ParameterizedTest
    @MethodSource("provideOIDWithWrongFormat")
    @DisplayName("Verify that OID with wrong format will be denied")
    fun verifyOIDWrongFormat(oid: String) {
        app.verifyOID(oid) shouldBe invalid(oid)
    }
}