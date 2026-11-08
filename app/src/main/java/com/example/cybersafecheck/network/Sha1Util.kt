package com.example.cybersafecheck.network

import java.security.MessageDigest

// Hashes a String with SHA-1 and returns the result as UPPERCASE hex text.
// This happens on the phone, so the real password is never sent anywhere.
object Sha1Util {

    fun sha1Hex(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-1").digest(input.toByteArray())
        return bytes.joinToString("") { "%02X".format(it) }
    }
}

//Author: Mangesana E
//Student number: 2030630053
