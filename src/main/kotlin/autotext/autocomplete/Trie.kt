package autotext.autocomplete

import autotext.Lexicon
import autotext.Tree
import java.lang.StringBuilder
import java.util.ArrayList

class Trie(lexicon: Lexicon) : Tree {
    /* The maximum number of matches to get while traversing the tree. */
    override var maxMatches = 10

    /* Root of trie. */
    private var root: TrieNode? = TrieNode()

    init {
        createTree(lexicon)
    }

    /**
     * Inserts a new word into the Trie.
     * @param word		String, the word to be inserted
     */
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


    /**
     * Gets words with given prefix (including `prefix`, if it is a valid word).
     * @param prefix				String
     * @return ArrayList<String>	A list of all words with prefix
     */
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

    /**
     * Traverses the Trie and adds new words into the result list if the current TrieNode is a leaf node.
     * Stops traversing the Trie once the maximum number of matches have been added to the list or
     * there are no more nodes left to traverse.
     * @param root			TrieNode, current node
     * @param word			StringBuilder
     * @param result		ArrayList<String>, list that will contain all words with given prefix
     */
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

    /**
     * Adds the current node's character onto `prefix` and calls `getWordsWithPrefix` to continue
     * traversing the tree and forming the full word.
     * @param node			TrieNode, current node
     * @param prefix		StringBuilder
     * @param result		ArrayList<String>
     */
    private fun updateWord(node: TrieNode, prefix: StringBuilder, result: ArrayList<String>) {
        val newPrefix = StringBuilder(prefix)
        newPrefix.append(node.value)
        getWordsWithPrefix(node, newPrefix, result)
    }
}