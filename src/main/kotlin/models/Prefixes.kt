package models

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomStringUtils.randomNumeric

data class Prefixes(var `trap-type-oid-prefix`: List<String>) {
    companion object {
        fun generateListOf(size: Int, suffix: String = ".1"): Prefixes =
            Prefixes(`trap-type-oid-prefix` = MutableList(size) {
                ".1.3.6" + suffix +
                    ".${randomNumeric(1)}" +
                    ".${randomNumeric(1)}" +
                    ".${randomNumeric(2)}" +
                    ".${randomNumeric(3)}" +
                    ".${randomNumeric(1)}"
            })

        fun generateInvalidPrefixes(size: Int): Prefixes =
            Prefixes(`trap-type-oid-prefix` = MutableList(size) { index ->
                when {
                    index % 2 == 0 -> randomNumeric(10)
                    else -> ".1.3.6.1.${randomAlphabetic(2)}"
                }
            })
    }
}