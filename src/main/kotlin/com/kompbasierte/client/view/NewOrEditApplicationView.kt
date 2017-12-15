package com.kompbasierte.client.view

import com.kompbasierte.client.model.Category
import javafx.scene.control.ComboBox
import tornadofx.*

class NewOrEditApplicationView : Fragment("My View") {
    lateinit var master : MainView
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
                    ComboBox<Category>(master.getCategories())
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
