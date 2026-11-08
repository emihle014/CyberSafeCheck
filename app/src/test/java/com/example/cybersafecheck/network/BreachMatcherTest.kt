package com.example.cybersafecheck.network

import org.junit.Assert.assertEquals
import org.junit.Test

class BreachMatcherTest {

    private val suffix = "C6008F9CAB4083784CBD1874F76618D2A97"

    @Test
    fun findsTheMatchingSuffix() {
        val body = "0018A45C4D1DEF81644B54AB7F969B88D65:1\r\n" +
                "C6008F9CAB4083784CBD1874F76618D2A97:2254650\r\n" +
                "00D4F6E8FA6EECAD2A3AA415EEC418D38EC:2"
        assertEquals(2254650L, BreachMatcher.countFor(body, suffix))
    }

    @Test
    fun returnsZeroWhenNotListed() {
        val body = "0018A45C4D1DEF81644B54AB7F969B88D65:1\r\n" +
                "00D4F6E8FA6EECAD2A3AA415EEC418D38EC:2"
        assertEquals(0L, BreachMatcher.countFor(body, suffix))
    }
}

//Author: Mangesana E
//Student number: 2030630053
