package com.kompbasierte.client.view

import tornadofx.*


class GenericWarningView : Fragment("Warnung!") {
    private var warningText = label()

    //Define Layout
    override val root = vbox {

        warningText = label {
            useMaxSize
        }

        spacer { spacing = 5.0 }

        buttonbar {
            button {
                text = "Ok"
                action { close() }
            }
        }

    }

    /**
     * Sets a specific text to show
     *
     * This method sets the text to show in the warning view. Should always be called before opening the warning view to
     * display correct information
     *
     * @param text The text to show in the warning
     */
    fun setWarningText(text: String) {
        warningText.text = text
    }
}