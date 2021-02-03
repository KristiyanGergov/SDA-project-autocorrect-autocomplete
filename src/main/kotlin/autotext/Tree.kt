package autotext

import autotext.Lexicon

interface Tree {
    fun insertWord(word: String)
    var maxMatches: Int

    fun createTree(lexicon: Lexicon) {
        val words = lexicon.lexicon
        for (word in words) {
            insertWord(word)
        }
    }
}