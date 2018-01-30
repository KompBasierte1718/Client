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
import java.util.logging.Logger

class NewOrEditCommandView(private val master: MainView) : Fragment("New/Edit Command") {

    companion object {
        private val LOG = Logger.getLogger(ApplicationsAndCommandsTableView::class.java.name)
    }

    private var commandID = 0
    private lateinit var nameText: TextField
    private lateinit var activeCheckbox: CheckBox
    private lateinit var strgLabel: Label
    private lateinit var altLabel: Label
    private lateinit var shiftLabel: Label
    private lateinit var keyLabel: Label
    private lateinit var listenerButton:Button
    private var listen=false

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
            form {
                fieldset {
                    field("Name") {
                        nameText = textfield {}
                    }

                    field("Aktiv") {
                        activeCheckbox = checkbox { }
                    }
                }
            }
        }
        spacing = 10.0
        tabpane {
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

            //Button um nach Keybordeingaben zu lauschen
            tab("Tastendruck") {
                form {
                    fieldset {
                        field("Shortcut") {
                            listenerButton = button("Lauschen") {
                                action {
                                    listen = !listen
                                    if (listen) {
                                        text = "Stop"

                                        //KeyListener
                                        onKeyPressedProperty().set(EventHandler<KeyEvent>(fun(e: KeyEvent?) {
                                            if (e != null) {
                                                //Check CTRL-Status
                                                strgDown = e.isShortcutDown

                                                //Check Alt-Status
                                                altDown = e.isAltDown

                                                //Check Shift-Status
                                                shiftDown = e.isShiftDown

                                                //Save for unknown keys on special keyboads
                                                if (e.code == KeyCode.UNDEFINED) {
                                                    master.showWarning("Taste nicht erkannt, bitte versuchen Sie " +
                                                            "eine andere")
                                                } else {
                                                    keyName = e.code
                                                }
                                                //Save for modifier key and unknown keys
                                                if (!e.code.isModifierKey && e.code != KeyCode.UNDEFINED) {
                                                    pressedKeyWithMod = createKeyCombinationWithModifier()
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
                    LOG.info("Saving with Shortcut: " + pressedKeyWithMod.toString())
                    master.saveCommandForApplication(Command(commandID, nameText.text, nameText.text,
                            "" + pressedKeyWithMod.toString(), activeCheckbox.isSelected),commandID!=0)
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

    fun openEditObject(command: Command) {
        commandID = command.id
        nameText.text = command.name
        activeCheckbox.isSelected = command.active

        LOG.info("Erstelle Shortcut Key zu " + command.shortcut)
        val sc = command.shortcut.split("+")
        if (sc.contains("Strg") || sc.contains("Ctrl")) {
            strgDown = true
            LOG.info("Strg gefunden")
        }
        if (sc.contains("ALT")) {
            altDown = true
            LOG.info("Alt gefunden")
        }
        if (sc.contains("Shift")) {
            shiftDown = true
            LOG.info("Shift gefunden")
        }
        keyName = KeyCode.getKeyCode(sc.last())
        LOG.info("Key " + keyName.toString() + " gefunden")

        pressedKeyWithMod = createKeyCombinationWithModifier()

        refreshPane()
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

    private fun createKeyCombinationWithModifier(): KeyCodeCombination {
        val controlModifier: KeyCombination.ModifierValue = if (strgDown) {
            ModifierValue.DOWN
        } else {
            ModifierValue.UP
        }

        val altModifier: KeyCombination.ModifierValue = if (altDown) {
            ModifierValue.DOWN
        } else {
            ModifierValue.UP
        }

        val shiftModifier: KeyCombination.ModifierValue = if (shiftDown) {
            ModifierValue.DOWN
        } else {
            ModifierValue.UP
        }
        //Order: SHIFT, CTRL, ALT, META(MAC), SHORTCUT(Strg or Meta)
        return KeyCodeCombination(keyName, shiftModifier, controlModifier, altModifier, ModifierValue.UP, ModifierValue.UP)
    }

    fun clear() {
        listen=false
        commandID = 0
        nameText.text = ""
        keyName = KeyCode.A
        strgDown = false
        altDown = false
        shiftDown = false
        pressedKeyWithMod = createKeyCombinationWithModifier()
        refreshPane()


        //Workaround für schließen mit aktiviertem Button
        listenerButton.text="Lausche"
        listenerButton.onKeyPressedProperty().set(null)
    }

}