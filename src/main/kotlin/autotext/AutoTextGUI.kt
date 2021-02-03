package autotext

import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.DefaultListModel
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.SwingUtilities
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.text.BadLocationException

class AutoTextGUI : JFrame(), ActionListener {
    private var textField = JTextField(50)
    private var textFieldSuggestions = JTextField(50)
    private var textFieldCorrections = JTextField(50)
    private var textFieldTolerance = JTextField(50)
    private var suggestionsLabel = JLabel().also { it.text = "Enter the maximum suggestions you want to have:" }
    private var correctionsLabel = JLabel().also { it.text = "Enter the maximum corrections you want to have:" }
    private var toleranceLabel = JLabel().also { it.text = "Enter the tolerance level:" }

    private var suggestionsList = JList<String>()
    private var scrollPane = JScrollPane(suggestionsList)
    private var submitLabel = JLabel()
    private var autocorrectionLabel = JLabel()
    private val autoText: AutoText
    private var updateFromSelectedValue = false

    init {
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT)
        this.layout = FlowLayout()
        this.contentPane.background = Color.WHITE
        defaultCloseOperation = EXIT_ON_CLOSE
        autoText = AutoText("src/main/resources/21k.txt")
        initializeTextField()
        initializeAutocompleteSuggestionsList()
        addComponents()
        this.isVisible = true
    }

    override fun actionPerformed(e: ActionEvent) {
        val text = textField.text
        val words = text.split(" ".toRegex()).toTypedArray()
        submitLabel.text = "You entered: $text"
        val autocorrectionLabelText = StringBuilder("<html><body>")
        for (word in words) {
            val corrections = autoText.autocorrect(word) ?: continue
            autocorrectionLabelText.append(
                "Found corrections for $word: " + java.lang.String.join(
                    ", ",
                    corrections
                ) + "<br>"
            )
        }
        autocorrectionLabelText.append("</body></html>")
        autocorrectionLabel.text = autocorrectionLabelText.toString()
    }

    private fun addComponents() {
        val autocompletePanel = JPanel()
        autocompletePanel.layout = BoxLayout(autocompletePanel, BoxLayout.Y_AXIS)
        autocompletePanel.preferredSize = Dimension(WINDOW_WIDTH, 200)
        val resultPanel = JPanel()
        resultPanel.layout = BoxLayout(resultPanel, BoxLayout.Y_AXIS)
        autocompletePanel.add(textField)
        autocompletePanel.add(scrollPane)

        resultPanel.add(suggestionsLabel)
        resultPanel.add(textFieldSuggestions)
        resultPanel.add(correctionsLabel)
        resultPanel.add(textFieldCorrections)
        resultPanel.add(toleranceLabel)
        resultPanel.add(textFieldTolerance)

        resultPanel.add(submitLabel)
        resultPanel.add(autocorrectionLabel)
        add(autocompletePanel)
        add(resultPanel)
    }

    private fun initializeAutocompleteSuggestionsList() {
        val listListener = ListSelectionListener { e: ListSelectionEvent ->
            if (!e.valueIsAdjusting && suggestionsList.selectedValue != null) {
                val words: Array<String?> = textField.text.split(" ".toRegex()).toTypedArray()
                words[words.size - 1] = suggestionsList.selectedValue
                SwingUtilities.invokeLater {
                    updateFromSelectedValue = true
                    textField.text = java.lang.String.join(" ", *words)
                    updateFromSelectedValue = false
                }
            }
        }
        suggestionsList = JList()
        suggestionsList.addListSelectionListener(listListener)
        scrollPane = JScrollPane(suggestionsList)
        scrollPane.border = BorderFactory.createEmptyBorder()
        suggestionsList.isFocusable = false
    }

    private fun initializeTextField() {
        val documentListener: DocumentListener = object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) {
                if (!updateFromSelectedValue) updateAutocomplete(e)
            }

            override fun removeUpdate(e: DocumentEvent) {
                if (!updateFromSelectedValue) updateAutocomplete(e)
            }

            override fun changedUpdate(e: DocumentEvent) {
                if (!updateFromSelectedValue) updateAutocomplete(e)
            }

            private fun updateAutocomplete(e: DocumentEvent) {
                val doc = e.document
                try {
                    if (!textFieldSuggestions.text.isNullOrBlank()) {
                        autoText.setMaxSuggestions(textFieldSuggestions.text.toInt())
                    }
                    if (!textFieldCorrections.text.isNullOrBlank()) {
                        autoText.setMaxCorrections(textFieldCorrections.text.toInt())
                    }
                    if (!textFieldTolerance.text.isNullOrBlank()) {
                        autoText.setTolerance(textFieldTolerance.text.toInt())
                    }

                    val text = doc.getText(0, doc.length)
                    val words = text.split(" ".toRegex()).toTypedArray()
                    val suggestions = autoText.autocomplete(words[words.size - 1].trim { it <= ' ' })
                    val model = DefaultListModel<String?>()
                    for (suggestion in suggestions) {
                        model.addElement(suggestion)
                    }
                    suggestionsList.setModel(model)
                } catch (e1: BadLocationException) {
                    e1.printStackTrace()
                }
            }
        }
        textField.addActionListener(this)
        textField.document.addDocumentListener(documentListener)
    }

    companion object {
        private const val WINDOW_HEIGHT = 300
        private const val WINDOW_WIDTH = 500

        @JvmStatic
        fun main(args: Array<String>) {
            AutoTextGUI()
        }
    }
}