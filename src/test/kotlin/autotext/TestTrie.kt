package autotext

import autotext.autocomplete.Trie
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.HashSet

class TestTrie {
    private var trie: Trie = Trie(Lexicon("src/test/resources/test_words_trie.txt"))

    @Test
    fun testPrefixSuggestionsNoSpaces() {
        testPrefixSuggestions(
            "wri",
            arrayOf("wrist", "write", "write back", "write down", "writer", "writing", "written")
        )
        testPrefixSuggestions("far", arrayOf("far", "farm", "farmer", "farming", "farther", "farthest"))
        testPrefixSuggestions("z", arrayOf("zero", "zone"))
    }

    @Test
    fun testPrefixSuggestionsWithSpaces() {
        testPrefixSuggestions("give ", arrayOf("give away", "give back", "give in", "give off", "give out", "give up"))
        testPrefixSuggestions("hang a", arrayOf("hang about", "hang about with", "hang around", "hang around with"))
    }

    @Test
    fun testMaxResults() {
        var maxMatches = 6
        trie.maxMatches = maxMatches
        assertEquals(maxMatches.toLong(), trie.getWordsWithPrefix("b").size.toLong())
        maxMatches = 20
        trie.maxMatches = maxMatches
        assertEquals(maxMatches.toLong(), trie.getWordsWithPrefix("a").size.toLong())
    }

    private fun testPrefixSuggestions(prefix: String, expected: Array<String>) {
        assertEquals(
            HashSet(listOf(*expected)), HashSet(
                trie.getWordsWithPrefix(prefix)
            )
        )
    }
}