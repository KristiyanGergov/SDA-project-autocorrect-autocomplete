package autotext

interface Tree {
    fun insertWord(word: String)

    /* The maximum number of matches to get while traversing the tree. */
    var maxMatches: Int

    fun createTree(lexicon: Lexicon) {
        val words = lexicon.lexicon
        for (word in words) {
            insertWord(word)
        }
    }
}