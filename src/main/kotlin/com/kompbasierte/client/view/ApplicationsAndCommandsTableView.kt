package com.kompbasierte.client.view

import com.kompbasierte.client.app.Constants.Companion.LOG
import com.kompbasierte.client.model.Application
import com.kompbasierte.client.model.Command
import javafx.geometry.Pos
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableView
import javafx.scene.control.Tooltip
import tornadofx.*


class ApplicationsAndCommandsTableView(private val master: MainView) : View() {

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
                            isDisable=true
                            tooltip= Tooltip("Bitte bearbeiten, um zu (de-)aktivieren")
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
                        master.openApplicationNew()
                    }
                }
                button("Applikation bearbeiten") {
                    action { master.openApplicationEdit(listView.selectedItem) }
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
                column("active", Command::activeProperty).useCheckbox(isDisabled)
                column("Name", Command::nameProperty).useTextField()
                column("VACallout", Command::vACalloutProperty).useTextField()
                column("shortcut", Command::shortcutProperty).useTextField()
            }
            //Buttonbar below the CommandTable
            hbox {
                button("Neuer Befehl") {
                    action {
                        master.openCommandNew()
                    }
                }
                button("Befehl bearbeiten") {
                    action{master.openCommandEdit(table.selectedItem)}
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
        LOG.info("Refreshing Appdata")
        appList = master.getApplications()
        listView.items = appList.observable()
        listView.refresh()
        LOG.info("Selecting first App")
        //Dies führt zu einem redundantem Aufruf von refreshCommandData beim Start der App
        //Wird vermutlich inzwischen nicht mehr benötigt
//        listView.selectionModel.selectFirst()
    }

    /**
     * Refresh the shown CommandList
     *
     * This function causes the CommandList to get all Commands for the selected Application in the AppList from the
     * Database and show the (new) Content.
     */
    fun refreshCommandData() {
        LOG.info("Refreshing Commanddata")
        if (!listView.items.isEmpty()) {
            LOG.info("AppList is not empty")
            if (listView.selectionModel.selectedIndex < 0) {
                LOG.info("Appindex is negative / No active Selection")
                listView.selectionModel.selectFirst()
                LOG.info("Selected first Entry")
                //Return, da das select automatisch die Funktion neu aufruft und Sie sonst doppelt ausgeführt wird
                return
            }
            val app = getSelectedApplication()
            commands = master.getCommandsForApplication(app)
            table.items = commands.observable()
            LOG.info("Show available Commands")
            table.refresh()
        }else{
            LOG.info("AppList empty")
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
        LOG.info("Getting selected App in Listview")
        return listView.selectionModel.selectedItem
    }
}

