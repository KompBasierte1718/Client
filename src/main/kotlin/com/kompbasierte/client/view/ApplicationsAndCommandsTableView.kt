package com.kompbasierte.client.view

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.SelectionMode
import tornadofx.*


class ApplicationsAndCommandsTableView : View() {

    lateinit var master: MainView
    //    var control = master.controller
    var index = 0
    override val root = borderpane {
        left = vbox {
            val appList = ArrayList<String>().observable()
            appList.add("VLC")
            appList.add("Spotify")
            val listView = listview(appList) {

                selectionModel.selectionMode = SelectionMode.SINGLE

            }
            flowpane {
                button("Applikation hinzufügen") {

                }
                button("Applikation bearbeiten") {
                    /*if (listView.selectedItem.equals("Spotify")) {
                    }*/
                }
                button("Applikation löschen") {
                    action {
                        appList.remove(listView.selectedItem)
                        listView.refresh()
                    }
                }
            }
        }

        center = vbox {

            val commands = ArrayList<Command>()

            val table = tableview(commands.observable()) {
                isEditable = true
                column("ID", Command::id)
                column("Name", Command::name).useTextField()
                column("VACallout", Command::VACallout).useTextField()
                column("shortcut", Command::shortcut).useTextField()
            }
            flowpane {
                button("Neuer Befehl") {
                    action {
                        commands.add(Command(index, "Eintrag " + index, "Eintrag " + index, "" + index))
                        index++
                        table.refresh()
                    }
                }
                button("Befehl bearbeiten") {}
                button("Löschen") {
                    action {
                        println(commands.remove(table.selectedItem))
                        table.refresh()
                    }
                }
                button("Neue Befehlskette")
                button("Befehlskette bearbeiten")
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