import io.qameta.allure.Step
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.concurrent.TimeUnit.MILLISECONDS

class AppParameter(val parameters: String?) {
    companion object {
        val withHelp = "-h"
        fun withCustomConfig(name: String) = "-f $name"
        val withoutParams = null
    }
}

class CLIApplication(params: String? = AppParameter.withoutParams) {
    val process: Process = when (params) {
        null -> ProcessBuilder("src/test/resources/oids-master/bin/oid.sh")
        else -> ProcessBuilder("src/test/resources/oids-master/bin/oid.sh", params)
    }.redirectErrorStream(true).start()

    private val appOutput: BufferedReader = BufferedReader(InputStreamReader(process.inputStream))
    private val appInput: BufferedWriter = BufferedWriter(OutputStreamWriter(process.outputStream))

    fun exitValue() = process.exitValue()

    @Step("Verify state of the CLI app")
    fun isAlive(): Boolean {
        process.waitFor()
        return process.isAlive
    }

    @Step("Send '{prefix}' to the CLI app's input")
    fun sendCommand(prefix: String) {
        appInput.write("$prefix\n")
        appInput.flush()
    }

    @Step("Read everything from CLI app's output")
    fun readOutput(): String {
        val response = StringBuffer()
        process.waitFor(1, MILLISECONDS) // I didn't get avoid `empty line` response without it. especially for performance suit
        var line: String? = appOutput.readLine()
        response.append(line).append("\n")
        while (appOutput.ready()) {
            line = appOutput.readLine()
            response.append(line).append("\n")
        }
        return response.toString().trim()
    }

    @Step("Send OID to CLI app and verify the response")
    fun verifyOID(prefix: String): String {
        sendCommand(prefix)
        return readOutput()
    }

    fun destroy() {
        appInput.close()
        appOutput.close()
        process.destroy()
    }
}