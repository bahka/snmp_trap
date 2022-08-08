package models

object AppResponses {
    fun invalid(oid: String) = "Error: OID '$oid' is invalid."
    fun succeed(oid: String) = "$oid: true"
    fun failed(oid: String) = "$oid: false"
    val welcomeText = "Enter an OID to process.\nEnter 'quit' to exit at any time."
    fun nonExistedFile(file: String) = "Error: Loader: cannot process file '$file'."
    val quiting = "Exiting."
    val helpText = """ 
        A program to determine whether an OID is a descendant of an OID in a YAML document.

        Usage: oid.sh [-f <file name>] [-h]

        where:
          -f  Path to the YAML document.
          -h  Shows this help.
    """.trimIndent()
    val missedConfigPath = "Error: path to OIDs must be specified."
    val emptyConfig = "Error: Loader: no oids defined under key 'trap-type-oid-prefix'"
}