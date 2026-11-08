package com.example.cybersafecheck.network

import org.junit.Assert.assertEquals
import org.junit.Test

class Sha1UtilTest {

    @Test
    fun hashOfPasswordIsCorrect() {
        assertEquals("5BAA61E4C9B93F3F0682250B6CF8331B7EE68FD8", Sha1Util.sha1Hex("password"))
    }

    @Test
    fun hashOfPassword123IsCorrect() {
        assertEquals("CBFDAC6008F9CAB4083784CBD1874F76618D2A97", Sha1Util.sha1Hex("password123"))
    }

    @Test
    fun prefixIsFiveCharactersAndSuffixIsThirtyFive() {
        val hash = Sha1Util.sha1Hex("password123")
        assertEquals("CBFDA", hash.substring(0, 5))
        assertEquals(35, hash.substring(5).length)
    }
}

//Author: Mangesana E
//Student number: 2030630053
