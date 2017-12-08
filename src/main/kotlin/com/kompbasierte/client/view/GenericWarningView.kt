package com.kompbasierte.client.view

import javafx.geometry.Pos
import tornadofx.*


class GenericWarningView : Fragment("Warnung!") {
    private var warningText = label()
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

    fun setWarningText(text: String) {
        warningText.text = text
    }
}