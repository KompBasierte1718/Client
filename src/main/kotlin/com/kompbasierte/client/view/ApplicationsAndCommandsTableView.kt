package com.kompbasierte.client.view

import com.kompbasierte.client.model.Application
import com.kompbasierte.client.model.Command
import javafx.geometry.Pos
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableView
import javafx.scene.control.Tooltip
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
                    //TODO Funktionalität einfügen
                    isDisable = true
                    tooltip = Tooltip("Später implementiert")
                    action {
                        //                        master.openApplicationEdit()
                    }
                }
                button("Applikation bearbeiten") {
                    //TODO Funktionalität einfügen
                    isDisable = true
                    tooltip = Tooltip("Später implementiert")
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
                column("VACallout", Command::vACalloutProperty).useTextField()
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
                        val listItem = listView.selectedItem
                        val tableItem = table.selectedItem
                        if (listItem != null && tableItem != null) {
                            master.deleteCommandForApplication(listItem, tableItem)

                        }else{
                            master.showWarning("Bitte zuerst ein Programm und einen Befehl auswählen!")
                        }
                    }
                }
                button("Neue Befehlskette") {
                    //TODO Funktionalität einfügen
                    isDisable = true
                    tooltip = Tooltip("Später implementiert")
                }
                button("Befehlskette bearbeiten") {
                    //TODO Funktionalität einfügen
                    isDisable = true
                    tooltip = Tooltip("Später implementiert")
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

