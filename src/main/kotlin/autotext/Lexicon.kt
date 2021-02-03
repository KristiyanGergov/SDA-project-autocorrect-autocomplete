package autotext

import java.util.HashSet
import java.io.IOException
import java.io.BufferedReader
import java.io.FileReader

class Lexicon(fileName: String) {
    val lexicon: HashSet<String>
    fun containsWord(word: String): Boolean {
        return lexicon.contains(word)
    }

    private fun readFile(rd: BufferedReader) {
        try {
            while (true) {
                val word = rd.readLine() ?: break
                lexicon.add(word.toLowerCase())
            }
            rd.close()
        } catch (ex: IOException) {
            throw ex
        }
    }

    private fun openFile(fileName: String): BufferedReader {
        return try {
            BufferedReader(FileReader(fileName))
        } catch (ex: IOException) {
            println("File not found: $fileName")
            throw ex
        }
    }

    init {
        val rd = openFile(fileName)
        lexicon = HashSet()
        readFile(rd)
    }
}