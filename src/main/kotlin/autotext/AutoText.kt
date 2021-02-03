package autotext

import autotext.autocomplete.Trie
import autotext.autocorrect.BKTree
import java.util.ArrayList

class AutoText(lexiconFile: String) {
    private val lexicon = Lexicon(lexiconFile)
    private val trie = Trie(lexicon)
    private val bktree = BKTree(lexicon)

    /**
     * If the input word is an invalid word (not found in lexicon), gets closest
     * words (shortest edit distance) to the input word. If word is valid or no words
     * are found (ex. word is not in the BKTree lexicon or the if the input word had
     * an edit distance greater than the specified tolerance), returns null.
     * @param word                    String
     * @return ArrayList<String>
     */
    fun autocorrect(word: String): ArrayList<String>? {
        if (isValidWord(word)) return null
        val words = bktree.getClosestWords(word)
        return if (words.size == 0) null else words
    }

    /**
     * Gets a list of words that have the given prefix and sorts them based on String length.
     * If no words were found in the Trie, an empty set will be returned.
     * @param prefix			String
     * @return ArrayList<String>
     */
    fun autocomplete(prefix: String): ArrayList<String> {
        val result = trie.getWordsWithPrefix(prefix)
        result.sortWith { o1, o2 -> o1.length - o2.length }
        return result
    }

    /**
     * Checks if the given word is in the lexicon.
     * @param word		String
     * @return boolean
     */
    private fun isValidWord(word: String): Boolean {
        return lexicon.containsWord(word)
    }

    /**
     * Sets the maximum number of auto-suggestions to return.
     * @param numSuggestions		int
     */
    fun setMaxSuggestions(numSuggestions: Int) {
        trie.maxMatches = numSuggestions
    }

    /**
     * Sets the maximum number of autocorrected words to return.
     * @param numCorrections		int
     */
    fun setMaxCorrections(numCorrections: Int) {
        bktree.maxMatches = numCorrections
    }

    fun setTolerance(tolerance: Int) {
        bktree.tolerance = tolerance
    }
}