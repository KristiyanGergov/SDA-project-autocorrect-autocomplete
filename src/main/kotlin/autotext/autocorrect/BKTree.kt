package autotext.autocorrect

import autotext.Lexicon
import autotext.Tree
import java.util.HashSet
import java.util.ArrayList
import kotlin.math.min

class BKTree(lexicon: Lexicon) : Tree {
    override var maxMatches = 3
    var tolerance = 3

    private var root: TreeNode? = null

    init {
        createTree(lexicon)
    }

    override fun insertWord(word: String) {
        var word = word
        require(word.isNotEmpty()) { "String is not valid." }

        word = word.toLowerCase().replace("[^a-z]".toRegex(), "")
        if (root == null) {
            root = TreeNode(word)
            return
        }
        var current: TreeNode = root!!
        var distance = getDistance(word, current.value)
        while (current.getChild(distance) != null && distance != 0) {
            current = current.getChild(distance)!!
            distance = getDistance(word, current.value)
        }
        current.addChild(TreeNode(word, distance))
    }

    fun getClosestWords(word: String): ArrayList<String> {
        //Get closest words (edit distance within TOLERANCE)
        val closeWords = getCloseWords(word)

        //The shortest distance found within `closeWords`
        var shortestDistance = Int.MAX_VALUE

        //All the words with the `shortestDistance`
        val closestWords = ArrayList<String>()

        //find the shortest edit distance within `closeWords`
        for (closeWord in closeWords) {
            if (closeWord.distance < shortestDistance) shortestDistance = closeWord.distance
        }

        //Get all the words with the `shortestDistance`
        for (closeWord in closeWords) {
            if (closeWord.distance == shortestDistance) closestWords.add(closeWord.word)
            if (closestWords.size == maxMatches) break
        }
        return closestWords
    }

    private inner class CloseWord(var word: String, var distance: Int)

    private fun getCloseWords(word: String): HashSet<CloseWord> {
        val closeWords = HashSet<CloseWord>()
        if (word == "") return closeWords
        getCloseWords(closeWords, root, word.toLowerCase().replace("[^a-z]".toRegex(), ""))
        return closeWords
    }

    private fun getCloseWords(closeWords: HashSet<CloseWord>, root: TreeNode?, word: String) {
        if (root == null) return
        if (closeWords.size == maxMatches) return
        val distance = getDistance(word, root.value)
        val minDist = distance - tolerance
        val maxDist = distance + tolerance
        if (distance <= tolerance) closeWords.add(CloseWord(root.value, distance))
        for (i in minDist..maxDist) {
            val node = root.getChild(i)
            node?.let { getCloseWords(closeWords, it, word) }
        }
    }

    private fun min(a: Int, b: Int, c: Int): Int {
        return min(a, min(b, c))
    }

    private fun getDistance(wordA: String, wordB: String): Int {
        if (wordA.isEmpty() || wordB.isEmpty()) return wordA.length.coerceAtLeast(wordB.length)
        val m = wordA.length
        val n = wordB.length
        val dp = Array(m + 1) { IntArray(n + 1) }

        for (i in 0..m) dp[i][0] = i
        for (j in 0..n) dp[0][j] = j

        for (i in 1..m) {
            for (j in 1..n) {
                if (wordA[i - 1] != wordB[j - 1]) {
                    dp[i][j] = min(
                        1 + dp[i - 1][j],  // deletion
                        1 + dp[i][j - 1],  // insertion
                        1 + dp[i - 1][j - 1] // replacement
                    )
                } else dp[i][j] = dp[i - 1][j - 1]
            }
        }

        return dp[m][n]
    }
}