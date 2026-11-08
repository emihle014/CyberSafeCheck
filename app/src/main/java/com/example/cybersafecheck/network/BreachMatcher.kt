package com.example.cybersafecheck.network

// The server replies with one line for every breached hash that starts with our
// 5 characters. Each line looks like this:   HASHSUFFIX:NUMBER_OF_TIMES_SEEN
// We look for our own suffix in that list, on the phone.
object BreachMatcher {

    // Returns how many times the suffix was found in breaches (0 means not found)
    fun countFor(responseBody: String, suffix: String): Long {
        for (line in responseBody.lines()) {
            val parts = line.trim().split(":")
            if (parts.size == 2 && parts[0].equals(suffix, ignoreCase = true)) {
                return parts[1].toLongOrNull() ?: 0L
            }
        }
        return 0L
    }
}

//Author: Mangesana E
//Student number: 2030630053
