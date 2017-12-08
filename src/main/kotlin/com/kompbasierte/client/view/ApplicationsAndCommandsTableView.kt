package com.kompbasierte.client.view

import com.kompbasierte.client.model.Application
import com.kompbasierte.client.model.Command
import javafx.geometry.Pos
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableView
import tornadofx.*


class ApplicationsAndCommandsTableView : View() {

    lateinit var master: MainView
    private lateinit var listView: ListView<Application>
    private lateinit var table: TableView<Command>
    private var commands = ArrayList<Command>()
    private var appList = ArrayList<Application>()

    override val root = hbox {
        vbox {

            listView = listview(appList.observable()) {
                cellFormat {
                    useMaxWidth = true
                    graphic = hbox {
                        useMaxWidth = true
                        label(it.name)
                        spacer {
                            spacing = 10.0
                        }
                        checkbox("", it.activeProperty) {

                            alignment = Pos.CENTER_RIGHT
                        }
                    }

                }
                onUserSelect {
                    refreshCommandData()
                }
                selectionModel.selectionMode = SelectionMode.SINGLE

            }
            hbox {

                button("Applikation hinzufügen") {
                    action {
                        master.openCommandEdit()
                    }
                }
                button("Applikation bearbeiten") {

                }
                button("Applikation löschen") {
                    action {
                        appList.remove(listView.selectedItem)
                        listView.refresh()
                    }
                }
            }
        }

        vbox {

            table = tableview(commands.observable()) {
                isEditable = true
                column("active", Command::activeProperty).useCheckbox()
                column("Name", Command::nameProperty).useTextField()
                column("vACallout", Command::vACalloutProperty).useTextField()
                column("shortcut", Command::shortcutProperty).useTextField()
            }
            hbox {
                button("Neuer Befehl") {
                    action {
                        master.openCommandEdit()
                    }
                }
                button("Befehl bearbeiten") {}
                button("Löschen") {
                    action {
                        master.deleteCommandForApplication(listView.selectedItem!!, table.selectedItem!!)
                    }
                }
                button("Neue Befehlskette")
                button("Befehlskette bearbeiten") {
                    useMaxSize = true
                }
            }
        }
    }

    fun refreshApplicationData() {
        appList = master.getApplications()
        listView.items = appList.observable()
        listView.refresh()
        listView.selectionModel.selectFirst()
    }

    fun refreshCommandData() {
        if (!listView.items.isEmpty()) {
            commands = master.getCommandsForPrgramm(listView.selectedItem!!)!!
            table.items = commands.observable()
            table.refresh()
        }
    }

}

