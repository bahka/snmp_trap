package performance

import AppParameter
import models.AppResponses.failed
import models.AppResponses.invalid
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

@Tags(Tag("performance"))
@DisplayName("Performance tests: a config with 1K prefixes")
class ConfigWith1KPrefixesTest {
    private var prefixes = Prefixes.generateListOf(size = 1_000)
    private val configName = YamlWriter().createFile(prefixes = prefixes)
    private val app: CLIApplication = CLIApplication(params = AppParameter.withCustomConfig(name = configName))

    private var correctOIDs = prefixes.`trap-type-oid-prefix`.shuffled()
    private var combinedWithMostOfRightOIDs = correctOIDs.map { it to succeed(it) }
        .plus(Prefixes.generateListOf(size = 100, suffix = ".8").`trap-type-oid-prefix`.map { it to failed(it) })
        .plus(Prefixes.generateInvalidPrefixes(size = 100).`trap-type-oid-prefix`.map { it to invalid(it) })
        .shuffled()

    private var combinedWithMostOfWrongOIDs = correctOIDs.take(200).map { it to succeed(it) }
        .plus(Prefixes.generateListOf(size = 400, suffix = ".8").`trap-type-oid-prefix`.map { it to failed(it) })
        .plus(Prefixes.generateInvalidPrefixes(size = 400).`trap-type-oid-prefix`.map { it to invalid(it) })
        .shuffled()

    @BeforeAll
    fun skipWelcomeMessage() {
        app.readOutput()
    }

    @AfterAll
    fun tearDown() {
        app.destroy()
    }

    @Test
    @DisplayName("Verify combined input for the application with correct oid (1k) only")
    fun verifyOIDWithFullMatch() {
        correctOIDs.forEach { oid ->
            app.sendCommand(oid)
            app.readOutput() shouldBe succeed(oid)
        }
    }

    @Test
    @DisplayName("Verify combined input for the application with correct oid (1k), invalid oids (100), oids not from the config (100)")
    fun verifyCombinedOIDsMostlyRight() {
        combinedWithMostOfRightOIDs.forEach { (oid, expectedResponse) ->
            app.sendCommand(oid)
            app.readOutput() shouldBe expectedResponse
        }
    }

    @Test
    @DisplayName("Verify combined input for the application with correct oid (200), invalid oids (400), oids not from the config (400)")
    fun verifyCombinedOIDsMostlyWrong() {
        combinedWithMostOfWrongOIDs.forEach { (oid, expectedResponse) ->
            app.sendCommand(oid)
            app.readOutput() shouldBe expectedResponse
        }
    }

    @Test
    @DisplayName("Verify OID from the first line of the config")
    fun verifyFirstPrefixFromBigFile() {
        val oid = prefixes.`trap-type-oid-prefix`.first()
        app.verifyOID(oid) shouldBe succeed(oid)
    }

    @Test
    @DisplayName("Verify OID from the last line of the config")
    fun verifyLastPrefixFromBigFile() {
        val oid = prefixes.`trap-type-oid-prefix`.last()
        app.verifyOID(oid) shouldBe succeed(oid)
    }
}