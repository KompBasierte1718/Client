package com.kompbasierte.client.view

import com.kompbasierte.client.model.Command
import javafx.scene.control.*
import tornadofx.*

class NewOrEditCommandView : Fragment("New/Edit Command") {
    lateinit var master: MainView
    lateinit var nameText: TextField
    lateinit var firstComboBox: ComboBox<String>
    lateinit var secondComboBox: ComboBox<String>
//    lateinit var thirdComboBox: ComboBox<String>
    override val root = vbox {
        hbox {
            label {
                text = "Name: "
            }
            nameText = textfield{}
        }
        spacing = 10.0
        tabpane {
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

            tab("Tastendruck") {
                form {
                    fieldset {
                        field("Shortcut") {
                            firstComboBox = combobox<String> {
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
                }
            }
            tab("Skript") {
                isDisable = true
                tooltip = Tooltip("Später implementiert")
            }
        }
        buttonbar {
            button("Save") {
                action {
                    master.saveCommandForApplication(Command(0, nameText.text, nameText.text,
                            "" + firstComboBox.selectedItem + "+" + secondComboBox.selectedItem,true
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
