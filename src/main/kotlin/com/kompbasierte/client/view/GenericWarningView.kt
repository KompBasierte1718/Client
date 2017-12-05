package com.kompbasierte.client.view

import tornadofx.*
import java.awt.Label


class GenericWarningView : Fragment("Warnung!") {
    private var warningText = label()
    override val root = vbox {

        warningText = label {
            useMaxSize
        }

        button {
            text = "Ok"
            action { close() }
        }

    }

    fun setWarningText(text: String) {
        warningText.text = text
    }
}