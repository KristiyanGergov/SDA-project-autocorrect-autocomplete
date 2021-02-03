package autotext.autocorrect

import autotext.Node
import java.util.HashMap

class TreeNode : Node<String, TreeNode, Int> {
    var value: String
        private set

    private var distance: Int? = null

    override var children: HashMap<Int, TreeNode>
        private set

    constructor(s: String) {
        value = s
        children = HashMap()
    }

    constructor(s: String, distance: Int?) {
        value = s
        this.distance = distance
        children = HashMap()
    }

    private val distanceFromParent: Int
        get() = distance!!

    override fun getChild(key: Int): TreeNode? {
        return children[key]
    }

    override fun addChild(node: TreeNode) {
        children[node.distanceFromParent] = node
    }

    override fun toString() = value
}