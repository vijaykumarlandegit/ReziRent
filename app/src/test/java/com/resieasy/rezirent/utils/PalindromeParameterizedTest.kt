package com.resieasy.rezirent.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized


@RunWith(Parameterized::class)
class PalindromeParameterizedTest(
    private val input: String,
    private val expected: Boolean
) {

    @Test
    fun testIsPalindrome() {
        assertEquals(expected, UnitTest().isPalindrome(input))
    }

    companion object {
        @JvmStatic  //@JvmStatic is needed because JUnit (Java) expects static method
        @Parameterized.Parameters(name = "Test palindrome with input \"{0}\" should return {1}")//tells JUnit this is your data provider
        fun data(): List<Array<Any>> {
            return listOf(
                arrayOf("madam", true),
                arrayOf("hello", false),
                arrayOf("racecar", true),
                arrayOf("noon", true),
                arrayOf("world", false)
            )
        }
    }


}