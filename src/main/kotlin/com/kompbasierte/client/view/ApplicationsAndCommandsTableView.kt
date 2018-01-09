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


class ApplicationsAndCommandsTableView(private val master: MainView) : View() {
    companion object {
        private val LOG = Logger.getLogger(ApplicationsAndCommandsTableView::class.java.name)
    }

    private lateinit var listView: ListView<Application>
    private lateinit var table: TableView<Command>
    private var commands = ArrayList<Command>()
    private var appList = ArrayList<Application>()

    //Define Layout
    override val root = hbox {
        vbox {
            //ApplicationList on the left
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

            //Buttonbar below ApplicationList
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
            //CommandTable on the right
            table = tableview(commands.observable()) {
                isEditable = true
                column("active", Command::activeProperty).useCheckbox()
                column("Name", Command::nameProperty).useTextField()
                column("VACallout", Command::vACalloutProperty).useTextField()
                column("shortcut", Command::shortcutProperty).useTextField()
            }
            //Buttonbar below the CommandTable
            hbox {
                button("Neuer Befehl") {
                    action {
                        master.openCommandEdit()
                    }
                }
                button("Befehl bearbeiten") {
                    TODO("Not implemented")
                }
                button("Löschen") {
                    action {
                        val listItem = listView.selectedItem
                        val tableItem = table.selectedItem
                        if (listItem != null && tableItem != null) {
                            master.deleteCommandForApplication(listItem, tableItem)
                        } else {
                            //Either no App or no Command is selected
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

    /**
     * Refresh the shown AppList
     *
     * This function causes the AppList to get all Applications from the Database and show the (new) Content.
     * At the end this function selects the first element in the AppList to prevent NULLPointer
     */
    fun refreshApplicationData() {
        appList = master.getApplications()
        listView.items = appList.observable()
        listView.refresh()
        LOG.info("Selecting first App")
        listView.selectionModel.selectFirst()
    }

    /**
     * Refresh the shown CommandList
     *
     * This function causes the CommandList to get all Commands for the selected Application in the AppList from the
     * Database and show the (new) Content.
     */
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

    /**
     * Return selected Application
     *
     * This function returns the Application-Object that is selected in the AppList
     * @return Application that is selected
     * @see Application
     */
    fun getSelectedApplication(): Application {
        LOG.info("Getting Applications")
        return listView.selectionModel.selectedItem
    }
}

