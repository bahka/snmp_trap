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
@DisplayName("Performance tests: a config with 10K prefixes")
class ConfigWith10KPrefixesTest {
    private var prefixes = Prefixes.generateListOf(size = 10_000)
    private val configName = YamlWriter().createFile(prefixes = prefixes)
    private val app: CLIApplication = CLIApplication(params = AppParameter.withCustomConfig(name = configName))

    private var correctOIDs = prefixes.`trap-type-oid-prefix`.shuffled()
    private var combinedWithMostOfRightOIDs = correctOIDs.map { it to succeed(it) }
        .plus(Prefixes.generateListOf(size = 1_000, suffix = ".8").`trap-type-oid-prefix`.map { it to failed(it) })
        .plus(Prefixes.generateInvalidPrefixes(size = 1_000).`trap-type-oid-prefix`.map { it to invalid(it) })
        .shuffled()
    private var combinedWithMostOfWrongOIDs = correctOIDs.take(2_000).map { it to succeed(it) }
        .plus(Prefixes.generateListOf(size = 4_000, suffix = ".8").`trap-type-oid-prefix`.map { it to failed(it) })
        .plus(Prefixes.generateInvalidPrefixes(size = 4_000).`trap-type-oid-prefix`.map { it to invalid(it) })
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
    @DisplayName("Verify combined input for the application with correct oid (10k) only")
    fun verifyOIDWithFullMatch() {
        correctOIDs.forEach { oid ->
            app.sendCommand(oid)
            app.readOutput() shouldBe succeed(oid)
        }
    }

    @Test
    @DisplayName("Verify combined input for the application with correct oid (10k), invalid oids (1k), oids not from the config (1k)")
    fun verifyCombinedOIDsMostlyRight() {
        combinedWithMostOfRightOIDs.forEach { (oid, expectedResponse) ->
            app.sendCommand(oid)
            app.readOutput() shouldBe expectedResponse
        }
    }

    @Test
    @DisplayName("Verify combined input for the application with correct oid (2k), invalid oids (4k), oids not from the config (4k)")
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