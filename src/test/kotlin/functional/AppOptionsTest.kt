package functional

import AppParameter
import models.AppResponses
import models.AppResponses.welcomeText
import CLIApplication
import YamlWriter
import io.kotest.matchers.shouldBe
import models.ExitCodes
import models.Prefixes
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.api.Test

@Tags(Tag("regression"), Tag("launch"))
@DisplayName("App launching tests")
class AppOptionsTest {
    private lateinit var app: CLIApplication

    @AfterEach
    fun tearDown() {
        app.destroy()
    }

    @Test
    @DisplayName("Verify that app launching with -h parameters shows help")
    fun verifyHelp() {
        app = CLIApplication(params = AppParameter.withHelp)
        app.readOutput() shouldBe AppResponses.helpText
        app.isAlive() shouldBe false
        app.exitValue() shouldBe ExitCodes.SUCCESSFUL
    }

    @Test
    @DisplayName("Verify that app launching with -f parameters but without file name shows help with warning")
    fun verifyEmptyConfigName() {
        app = CLIApplication(params = AppParameter.withCustomConfig(name = ""))
        app.readOutput() shouldBe AppResponses.missedConfigPath
        app.isAlive() shouldBe false
        app.exitValue() shouldBe ExitCodes.FAILURE
    }

    @Test
    @DisplayName("Verify that app launching with empty config file")
    fun verifyEmptyConfigFile() {
        val config = YamlWriter().createFile(prefixes = Prefixes(emptyList()))
        app = CLIApplication(params = AppParameter.withCustomConfig(name = config))
        app.readOutput() shouldBe AppResponses.emptyConfig
        app.isAlive() shouldBe false
        app.exitValue() shouldBe ExitCodes.FAILURE
    }

    @Test
    @DisplayName("Verify that app will be closed after 'quit' as input")
    fun verifyQuit() {
        app = CLIApplication()
        app.readOutput() shouldBe welcomeText
        app.verifyOID("quit") shouldBe AppResponses.quiting
        app.isAlive() shouldBe false
        app.exitValue() shouldBe ExitCodes.SUCCESSFUL
    }

    @Test
    @DisplayName("Verify that app will be closed after 'quit' as input")
    fun verifyWrongFile() {
        val config = "nonexisted"
        app = CLIApplication(params = AppParameter.withCustomConfig(name = config))
        app.readOutput() shouldBe AppResponses.nonExistedFile(file = config)
        app.isAlive() shouldBe false
        app.exitValue() shouldBe ExitCodes.FAILURE
    }
}