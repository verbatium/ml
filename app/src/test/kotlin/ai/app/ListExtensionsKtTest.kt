package ai.app

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class ListExtensionsKtTest {

    @Test
    fun permutations() {
        assertEquals(listOf(listOf(1)), listOf(1).permutations(1), "1 of 1")
        assertEquals(listOf(listOf(1), listOf(2)), listOf(1, 2).permutations(1), "1 of 2")
        assertEquals(listOf(listOf(1,2)), listOf(1, 2).permutations(2), "2 of 2")
        assertEquals(listOf(listOf(1), listOf(2), listOf(3)), listOf(1, 2, 3).permutations(1), "1 of 3")
        assertEquals(listOf(listOf(1,2), listOf(1,3), listOf(2,3)), listOf(1, 2, 3).permutations(2), "2 of 3")
        assertEquals(listOf(listOf(1,2,3)), listOf(1, 2, 3).permutations(3), "3 of 3")

        assertEquals(listOf(
            listOf("a", "b", "c"),
            listOf("a", "b", "d"),
            listOf("a", "b", "e"),
            listOf("a", "c", "d"),
            listOf("a", "c", "e"),
            listOf("a", "d", "e"),
            listOf("b", "c", "d"),
            listOf("b", "c", "e"),
            listOf("b", "d", "e"),
            listOf("c", "d", "e"),
        ) , listOf("a", "b", "c", "d", "e").permutations(3))
    }
}