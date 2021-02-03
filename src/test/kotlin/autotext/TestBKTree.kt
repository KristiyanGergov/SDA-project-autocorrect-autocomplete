package autotext

import autotext.autocorrect.BKTree
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.HashSet

class TestBKTree {
    private var bktree = BKTree(Lexicon("src/test/resources/test_words_bk.txt")).also { it.tolerance = 2 }

    @Test
    fun testClosestWords() {
        testCloseWordsHelper("recieve", arrayOf("receive", "recipe", "believe"))
        testCloseWordsHelper("whre", arrayOf("are", "wheel", "care"))
        testCloseWordsHelper("amazin", arrayOf("amazon"))
        testCloseWordsHelper("gogle", arrayOf("hole", "role", "gone"))
        testCloseWordsHelper("asessment", arrayOf("assessment"))
        testCloseWordsHelper("yhere", arrayOf("here"))
        testCloseWordsHelper("utillities", arrayOf("utilities"))
        testCloseWordsHelper("janurary", arrayOf("january"))
    }

    @Test
    fun testClosestWordsPunctuation() {
        bktree.insertWord("etc.")
        assertTrue(bktree.getClosestWords("etc.").contains("el"))
        bktree.insertWord("make up")
        assertTrue(bktree.getClosestWords("make up").contains("makeup"))
    }

    private fun testCloseWordsHelper(word: String, expected: Array<String>) {
        assertEquals(
            HashSet(listOf(*expected)), HashSet(
                bktree.getClosestWords(word)
            )
        )
    }
}