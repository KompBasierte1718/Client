package com.kompbasierte.client.view

import javafx.scene.control.TextField
import tornadofx.*

class KeyDialog(private val master: MainView) : Fragment("Key Dialog") {

    lateinit var keyText: TextField

    override val root =
            vbox {
                label("Bitte geben Sie einen persönlichen Schlüssel ein. Merken Sie sich diesen gut!")
                form {
                    fieldset {
                        field("Schlüssel") {
                            keyText = textfield()
                        }
                    }
                }
                buttonbar {
                    button("Save") {
                        action {
                            master.transmitKeys(keyText.text)
                            close()
                        }
                    }
                    button("Cancel") {
                        action {
                            close()
                        }
                    }
                }
            }
}