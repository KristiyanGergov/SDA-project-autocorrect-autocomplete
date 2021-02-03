package autotext

import java.util.HashMap

interface Node<E, T, Z> {
    val children: HashMap<Z, T>
    fun getChild(key: Z): T?
    fun addChild(node: T)
}