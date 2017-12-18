package com.kompbasierte.client.view

import com.kompbasierte.client.model.Category
import tornadofx.*

class NewOrEditApplicationView(val master: MainView) : Fragment("New/Edit Applikation") {
    override val root = vbox {

        form {
            fieldset {
                field("Name"){
                    textfield()
                }
                field("Pfad"){
                    textfield()
                }
                field("Kategorie"){
                    combobox<Category>{
                        items = master.getCategories().observable()
                        selectionModel.selectFirst()
                    }
                }
            }
        }

        buttonbar {
            button("Speichern"){
                action {
//                    master.saveApplication()
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
