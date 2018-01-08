package com.kompbasierte.client.view

import com.kompbasierte.client.model.Command
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyCombination.*
import javafx.scene.input.KeyEvent
import tornadofx.*

class NewOrEditCommandView(val master: MainView) : Fragment("New/Edit Command") {
    lateinit var nameText: TextField
    lateinit var firstComboBox: ComboBox<String>
    lateinit var secondComboBox: ComboBox<String>
    lateinit var strgLabel: Label
    lateinit var altLabel: Label
    lateinit var shiftLabel: Label
    lateinit var keyLabel: Label

    var strgDown = false
    var altDown = false
    var shiftDown = false
    var keyName = KeyCode.A
    var pressedKeyWithMod = KeyCodeCombination(keyName, KeyCombination.ModifierValue.UP,
            KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP,
            KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP)

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
                                            if (e != null) {
                                                println(e.code.toString())

                                                strgDown = e.isShortcutDown
                                                val controlModifier: KeyCombination.ModifierValue
                                                if (strgDown) {
                                                    controlModifier = ModifierValue.DOWN
                                                } else {
                                                    controlModifier = ModifierValue.UP
                                                }

                                                altDown = e.isAltDown
                                                val altModifier: KeyCombination.ModifierValue
                                                if (strgDown) {
                                                    altModifier = ModifierValue.DOWN
                                                } else {
                                                    altModifier = ModifierValue.UP
                                                }

                                                shiftDown = e.isShiftDown
                                                val shiftModifier: KeyCombination.ModifierValue
                                                if (strgDown) {
                                                    shiftModifier = ModifierValue.DOWN
                                                } else {
                                                    shiftModifier = ModifierValue.UP
                                                }

                                                keyName = e.code
                                                if (!e.code.isModifierKey) {
                                                    //SHIFT, CTRL, ALT, META, SHORTCUT
                                                    pressedKeyWithMod = KeyCodeCombination(e.code, shiftModifier,
                                                            controlModifier, altModifier, controlModifier,
                                                            controlModifier)
                                                }
                                                e.consume()
                                                refreshPane()
                                            }
                                        }))
                                        /*onKeyReleasedProperty().set(EventHandler<KeyEvent>(fun(e: KeyEvent?) {
                                            if (e != null) {
                                                println(e.code.toString())
                                                strgDown = e.isShortcutDown
                                                altDown = e.isAltDown
                                                shiftDown = e.isShiftDown
                                                keyName = e.code
                                                refreshPane()
                                            }
                                        }))*/
                                    } else {
                                        text = "Lauschen"
                                        onKeyPressedProperty().set(null)
                                    }
                                }
                            }
                            strgLabel = label("STRG + ") {
                                isVisible = false
                            }
                            altLabel = label("ALT + ") {
                                isVisible = false
                            }
                            shiftLabel = label("Shift + ") {
                                isVisible = false
                            }
                            keyLabel = label(keyName.getName())
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
                            ""+pressedKeyWithMod, true))
                    //TODO Change CommandKlasse für KeyValues
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

    private fun refreshPane() {
        strgLabel.isVisible = strgDown
        altLabel.isVisible = altDown
        shiftLabel.isVisible = shiftDown
        if (!keyName.isModifierKey) {
            keyLabel.text = keyName.getName()
        }
        onRefresh()
    }

}