package com.kompbasierte.client.view

import com.kompbasierte.client.model.Command
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import tornadofx.*

class NewOrEditCommandView(val master: MainView) : Fragment("New/Edit Command") {
    lateinit var nameText: TextField
    lateinit var firstComboBox: ComboBox<String>
    lateinit var secondComboBox: ComboBox<String>
    lateinit var strgLabel: Label
    var strgDown = false
    var altDown = false
    var shiftDown = false
    var buttonName = KeyCode.A
    //    lateinit var thirdComboBox: ComboBox<String>
    override val root = vbox {
        hbox {
            label {
                text = "Name: "
            }
            nameText = textfield {}
        }
        spacing = 10.0
        tabpane {
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

            tab("Tastendruck") {
                form {
                    fieldset {
                        field("Shortcut") {
                            /*firstComboBox = combobox<String> {
                                items.add("Strg")
                                items.add("Alt")
                                items.add("Shift")
                            }
                            label("+")
                            secondComboBox = combobox<String> {
                                items.add("+")
                                items.add("-")
                            }
                        }
                    }
                }*/
                            val listenerButton = button("Lauschen") {
                                var listen = false
                                action {
                                    listen = !listen
                                    if (listen) {
                                        text = "Stop"
                                        onKeyPressedProperty().set(EventHandler<KeyEvent>(fun(e: KeyEvent?) {
                                            println(e.toString())
                                            if (e != null) {
                                                strgDown = e.isShortcutDown
                                                altDown = e.isAltDown
                                                shiftDown = e.isShiftDown
                                                buttonName = e.code
                                                strgLabel.requestFocus()
                                            }
                                        }))
                                    } else {
                                        text = "Lauschen"
                                        onKeyPressedProperty().set(null)
                                    }
                                }
                            }
                            strgLabel = label("STRG + ") {
                                visibleWhen { strgDown.toProperty() }
                            }
                            val altLabel = textfield("ALT + ") {
                                visibleWhen ( altDown.toProperty() )
                            }
                            val shiftLabel = label("Shift + ") {
                                visibleWhen ( shiftDown.toProperty() )
                            }
                            val buttonLabel = label(buttonName.getName())
                        }
                    }
                }
                tab("Skript") {
                    isDisable = true
                    tooltip = Tooltip("Später implementiert")
                }
            }

        }
        buttonbar {
            button("Save") {
                action {
                    master.saveCommandForApplication(Command(0, nameText.text, nameText.text,
                            "" + firstComboBox.selectedItem + "+" + secondComboBox.selectedItem, true
                            /*TODO implement later + "+" + thirdComboBox.selectedItem*/))
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