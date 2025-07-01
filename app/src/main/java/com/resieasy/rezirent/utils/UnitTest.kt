package com.resieasy.rezirent.utils

class UnitTest {
    fun isPalindrome(input: String): Boolean {
        val cleaned = input.lowercase()
        return cleaned == cleaned.reversed()
    }
}
