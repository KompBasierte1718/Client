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

class NewOrEditCommandView(private val master: MainView) : Fragment("New/Edit Command") {
    private lateinit var nameText: TextField
    private lateinit var strgLabel: Label
    private lateinit var altLabel: Label
    private lateinit var shiftLabel: Label
    private lateinit var keyLabel: Label

    //Helper Vars for Key-Detection
    private var strgDown = false
    private var altDown = false
    private var shiftDown = false
    private var keyName = KeyCode.A
    private var pressedKeyWithMod = KeyCodeCombination(keyName, KeyCombination.ModifierValue.UP,
            KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP,
            KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP)

    //Define Layout
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

            //Button um nach Keybordeingaben zu lauschen
            tab("Tastendruck") {
                form {
                    fieldset {
                        field("Shortcut") {
                            val listenerButton = button("Lauschen") {
                                var listen = false
                                action {
                                    listen = !listen
                                    if (listen) {
                                        text = "Stop"

                                        //KeyListener
                                        onKeyPressedProperty().set(EventHandler<KeyEvent>(fun(e: KeyEvent?) {
                                            if (e != null) {
                                                //Check CTRL-Status
                                                strgDown = e.isShortcutDown
                                                val controlModifier: KeyCombination.ModifierValue
                                                controlModifier = if (strgDown) {
                                                    ModifierValue.DOWN
                                                } else {
                                                    ModifierValue.UP
                                                }

                                                //Check Alt-Status
                                                altDown = e.isAltDown
                                                val altModifier: KeyCombination.ModifierValue
                                                altModifier = if (altDown) {
                                                    ModifierValue.DOWN
                                                } else {
                                                    ModifierValue.UP
                                                }

                                                //Check Shift-Status
                                                shiftDown = e.isShiftDown
                                                val shiftModifier: KeyCombination.ModifierValue
                                                shiftModifier = if (shiftDown) {
                                                    ModifierValue.DOWN
                                                } else {
                                                    ModifierValue.UP
                                                }

                                                //Save for unknown keys on special keyboads
                                                if (e.code == KeyCode.UNDEFINED) {
                                                    master.showWarning("Taste nicht erkannt, bitte versuchen Sie " +
                                                            "eine andere")
                                                }else{
                                                    keyName=e.code
                                                }
                                                //Save for modifier key and unknown keys
                                                if (!e.code.isModifierKey && e.code != KeyCode.UNDEFINED) {
                                                    //SHIFT, CTRL, ALT, META, SHORTCUT
                                                    pressedKeyWithMod = KeyCodeCombination(e.code, shiftModifier,
                                                            controlModifier, altModifier, ModifierValue.UP,
                                                            ModifierValue.UP)
                                                }
                                                //Consume KeyEvent to prevent further processing of it
                                                e.consume()
                                                //refresh the View to show new Values
                                                refreshPane()
                                            }
                                        }))
                                        /* TODO Unused right now, for later usage
                                        onKeyReleasedProperty().set(EventHandler<KeyEvent>(fun(e: KeyEvent?) {
                                            if (e != null) {
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
                            ""+pressedKeyWithMod.toString(), true))
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
            //Don't show modifier keynames at the end since they are already displayed in front
            keyLabel.text = keyName.getName()
        }
        onRefresh()
    }

}