package com.kompbasierte.client.view

import com.kompbasierte.client.app.Control
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.SelectionMode
import tornadofx.*

class MainView : View("Hello Tornado") {
    var index = 0
    val controller = Control(this)


    override val root = hbox {

        vbox {
            listview<String> {

                items.add("Alexa")
                items.add("Google Home")
                selectionModel.selectionMode = SelectionMode.SINGLE

            }
        }

        vbox {

            val commands = HashMap<Int, Command>()

            val table = tableview(commands.values.toList().observable()) {
                isEditable = true
                column("ID", Command::id)
                column("Name", Command::name).useTextField()
                column("VACallout", Command::VACallout).useTextField()
                column("shortcut", Command::shortcut).useTextField()
            }
            button("Neuer Eintrag") {
                action {
                    commands.put(index, Command(index, "Eintrag " + index, "Eintrag " + index, "" + index))
                    index++
                    table.items = commands.values.toList().observable()
                    table.refresh()
                }

            }
        }
    }

}

class Command(id: Int, name: String, VACallout: String, shortcut: String) {

    val idProperty = SimpleIntegerProperty(id)
    var id by idProperty

    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    val VACalloutProperty = SimpleStringProperty(VACallout)
    var VACallout by VACalloutProperty

    val shortcutProperty = SimpleStringProperty(shortcut)
    var shortcut by shortcutProperty
}