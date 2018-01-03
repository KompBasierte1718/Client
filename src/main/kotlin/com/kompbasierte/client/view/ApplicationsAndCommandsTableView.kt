package com.kompbasierte.client.view

import com.kompbasierte.client.model.Application
import com.kompbasierte.client.model.Command
import javafx.geometry.Pos
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableView
import javafx.scene.control.Tooltip
import tornadofx.*
import java.util.logging.Logger


class ApplicationsAndCommandsTableView(val master: MainView) : View() {
    companion object {
        private val LOG = Logger.getLogger(ApplicationsAndCommandsTableView::class.java.name)
    }

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
                selectionModel.selectedIndexProperty().addListener { _ -> refreshCommandData() }

                selectionModel.selectionMode = SelectionMode.SINGLE

            }
            hbox {

                button("Applikation hinzufügen") {
                    action {
                        master.openApplicationEdit()
                    }
                }
                button("Applikation bearbeiten") {
                    //TODO Funktionalität einfügen
                    isDisable = true
                    tooltip = Tooltip("Später implementiert")
                }
                button("Applikation löschen") {
                    action {
                        master.deleteApplication(listView.selectionModel.selectedItem)
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

                        } else {
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
        LOG.info("Selecting first App")
        listView.selectionModel.selectFirst()

    }

    fun refreshCommandData() {
        if (!listView.items.isEmpty()) {
            LOG.info("Existing Apps found")
            if (listView.selectionModel.selectedIndex < 0) {
                LOG.info("Appindex is negative / No active Selection")
                listView.selectionModel.selectFirst()
            }
            val app = getSelectedApplication()
            commands = master.getCommandsForApplication(app)
            table.items = commands.observable()
            table.refresh()
        }
    }

    fun getSelectedApplication(): Application {
        LOG.info("Getting Applications")
        return listView.selectionModel.selectedItem
    }
}

