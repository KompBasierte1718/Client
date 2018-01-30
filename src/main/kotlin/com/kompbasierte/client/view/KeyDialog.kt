package com.kompbasierte.client.view

import javafx.scene.control.TextField
import tornadofx.*

class KeyDialog(private val master: MainView) : Fragment("Gerät registrieren") {

    lateinit var keyText: TextField

    override val root =
            vbox {
                label("Bitte geben Sie einen persönlichen Schlüssel ein. Der Schlüssel muss aus zwei Wörtern, " +
                        "getrennt durch ein Leerzeichen bestehen. Merken Sie sich diesen gut!")
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
                            val regex = Regex(pattern = "^([A-Za-z])+ ([A-Za-z])+$")
                            val matched = regex.find(keyText.text)
                            if(matched != null) {
                                master.transmitKeys(keyText.text)
                                close()
                            } else {
                                master.showWarning("Schlüssel nicht korrekt. Bitte erneut versuchen.")
                                keyText.clear()
                            }

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