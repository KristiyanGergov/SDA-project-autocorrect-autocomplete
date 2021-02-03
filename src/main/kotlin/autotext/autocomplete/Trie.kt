package autotext.autocomplete

import autotext.Lexicon
import autotext.Tree
import java.lang.StringBuilder
import java.util.ArrayList

class Trie(lexicon: Lexicon) : Tree {
    override var maxMatches = 10

    private var root: TrieNode? = TrieNode()

    init {
        createTree(lexicon)
    }

    override fun insertWord(word: String) {
        var word = word

        word = word.toLowerCase()
        var current = root

        for (element in word) {
            if (current?.getChild(element) == null) {
                current?.addChild(TrieNode(element))
            }
            current = current?.getChild(element)
        }
        current?.isLeaf = true
    }


    fun getWordsWithPrefix(prefix: String): ArrayList<String> {
        val prefix = prefix.toLowerCase()

        val wordsWithPrefix = ArrayList<String>()
        if (prefix.isEmpty()) {
            return wordsWithPrefix
        }

        var current = root

        for (c in prefix) {
            val node = current?.getChild(c) ?: return wordsWithPrefix
            current = node
        }

        getWordsWithPrefix(current, StringBuilder(prefix), wordsWithPrefix)
        return wordsWithPrefix
    }

    private fun getWordsWithPrefix(root: TrieNode?, word: StringBuilder, result: ArrayList<String>) {
        if (root == null || result.size == maxMatches) {
            return
        }
        if (root.isLeaf) {
            result.add(word.toString())
        }
        for (child in root.children.values) {
            updateWord(child, word, result)
        }
    }

    private fun updateWord(node: TrieNode, prefix: StringBuilder, result: ArrayList<String>) {
        val newPrefix = StringBuilder(prefix)
        newPrefix.append(node.value)
        getWordsWithPrefix(node, newPrefix, result)
    }
}