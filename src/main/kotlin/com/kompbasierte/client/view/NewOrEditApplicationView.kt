package com.kompbasierte.client.view

import com.kompbasierte.client.model.Application
import com.kompbasierte.client.model.Category
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import tornadofx.*

class NewOrEditApplicationView(private val master: MainView) : Fragment("New/Edit Applikation") {
    private var categoryList = ArrayList<Category>()
    private var appId = 0
    private lateinit var nameText: TextField
    private lateinit var activeCheckbox: CheckBox
    private lateinit var pfad32Text: TextField
    private lateinit var pfad64Text: TextField
    private lateinit var kategorieComboBox: ComboBox<String>

    //Define Layout
    override val root = vbox {
        //Input Form
        form {
            fieldset {
                field("Name"){
                    nameText = textfield()
                }
                field("Aktiv"){
                    activeCheckbox = checkbox {  }
                }
                field("Pfad 32"){
                    pfad32Text = textfield()
                }
                field("Pfad 64") {
                    pfad64Text = textfield()
                }
                field("Kategorie"){
                    kategorieComboBox = combobox{
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
                    println("Checkboxstate: "+activeCheckbox.isSelected)
                    var categoryID = 0
                    categoryList
                            .filter { it.name == kategorieComboBox.selectedItem }
                            .forEach { categoryID = categoryList.indexOf(it) + 1 }
                    master.saveApplication(Application(appId, categoryID, nameText.text,
                            pfad32Text.text, pfad64Text.text, activeCheckbox.isSelected))
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

    fun openEditObject(application: Application){
        appId=application.id
        nameText.text=application.name
        activeCheckbox.isSelected=application.active
        pfad32Text.text=application.path32
        pfad64Text.text=application.path64
        kategorieComboBox.selectionModel.select(application.categoryID-1)
    }
    fun clear(){
        appId=0
        nameText.text=""
        activeCheckbox.isSelected=true
        pfad32Text.text=""
        pfad64Text.text=""
        kategorieComboBox.selectionModel.select(0)
    }
}
