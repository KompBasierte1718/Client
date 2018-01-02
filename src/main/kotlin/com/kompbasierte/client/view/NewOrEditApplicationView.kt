package com.kompbasierte.client.view

import com.kompbasierte.client.model.Application
import com.kompbasierte.client.model.Category
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import tornadofx.*

class NewOrEditApplicationView(val master: MainView) : Fragment("New/Edit Applikation") {
    var categoryList = ArrayList<Category>()
    lateinit var nameText: TextField
    lateinit var pfad32Text: TextField
    lateinit var pfad64Text: TextField
    lateinit var kategorieComboBox: ComboBox<String>

    override val root = vbox {

        form {
            fieldset {
                field("Name"){
                    nameText = textfield()
                }
                field("Pfad 32"){
                    pfad32Text = textfield()
                }
                field("Pfad 64") {
                    pfad64Text = textfield()
                }
                field("Kategorie"){
                    kategorieComboBox = combobox<String>{
                        categoryList = master.getCategories()
                        for (c: Category in categoryList) {
                            items.add(c.name)
                        }
                        items.observable()
                        selectionModel.selectFirst()
                    }
                }
            }
        }

        buttonbar {
            button("Speichern"){
                action {
                    var categoryID = 0
                    for (c: Category in categoryList) {
                        if (c.name.equals(kategorieComboBox.selectedItem)) {
                            categoryID = categoryList.indexOf(c) + 1
                        }
                    }
                    master.saveApplication(Application(0, categoryID, nameText.text, pfad32Text.text, pfad64Text.text, true))
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
