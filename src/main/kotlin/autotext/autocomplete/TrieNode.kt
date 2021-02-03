package autotext.autocomplete

import autotext.Node
import java.util.HashMap

class TrieNode(val value: Char? = null) : Node<Char?, TrieNode, Char> {

    override var children: HashMap<Char, TrieNode> = HashMap()
        private set

    var isLeaf = false

    override fun getChild(key: Char): TrieNode? {
        return children[key]
    }

    override fun addChild(node: TrieNode) {
        children[node.value!!] = node
    }
}