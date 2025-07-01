package com.resieasy.rezirent.utils

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class UnitTestTest {

    @Test
    fun isPalindrome() {
        val unitTest = UnitTest()
        val result = unitTest.isPalindrome("POIO")
        //assertTrue(result) // ✅ Correct assertion
        assertEquals(true, result)
    }
}