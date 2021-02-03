package autotext

import autotext.autocomplete.Trie
import autotext.autocorrect.BKTree
import java.util.ArrayList

class AutoText(lexiconFile: String) {
    private val lexicon = Lexicon(lexiconFile)
    private val trie = Trie(lexicon)
    private val bktree = BKTree(lexicon)

    fun autocorrect(word: String): ArrayList<String>? {
        if (isValidWord(word)) return null
        val words = bktree.getClosestWords(word)
        return if (words.size == 0) null else words
    }

    fun autocomplete(prefix: String): ArrayList<String> {
        val result = trie.getWordsWithPrefix(prefix)
        result.sortWith { o1, o2 -> o1.length - o2.length }
        return result
    }

    private fun isValidWord(word: String): Boolean {
        return lexicon.containsWord(word)
    }

    fun setMaxSuggestions(numSuggestions: Int) {
        trie.maxMatches = numSuggestions
    }

    fun setMaxCorrections(numCorrections: Int) {
        bktree.maxMatches = numCorrections
    }

    fun setTolerance(tolerance: Int) {
        bktree.tolerance = tolerance
    }

    fun getTolerance() = bktree.tolerance
    fun getMaxCorrections() = bktree.maxMatches
    fun getMaxSuggestions() = trie.maxMatches

}