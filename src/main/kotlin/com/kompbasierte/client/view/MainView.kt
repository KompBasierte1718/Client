package com.kompbasierte.client.view

import com.kompbasierte.client.model.Application
import com.kompbasierte.client.model.Command
import com.kompbasierte.client.app.Control
import tornadofx.*

class MainView : View("Hello Tornado") {

    val controller = Control(this)
    private val applicationsAndCommandsView: ApplicationsAndCommandsTableView by inject()
    private val genericWarningView = find(GenericWarningView::class)
    private val newOrEditCommandView = find(NewOrEditCommandView::class)

    override val root = borderpane {
        top = menubar {

            menu("Datei") {
                item("Beispiel")
                item("Beispiel 2")
            }
            menu("Einstellungen") {
                item("Beispiel")
                item("Beispiel 2")
            }
            menu("Hilfe") {
                item("Beispiel")
                item("Beispiel 2")
            }
        }

        applicationsAndCommandsView.master = this@MainView
        center = vbox { add(applicationsAndCommandsView) }
        applicationsAndCommandsView.refreshApplicationData()
        applicationsAndCommandsView.refreshCommandData()

        bottom = button("Trigger Warning") {
            action {
                showWarning("Hier könnte ihre Warnung stehen!")
            }
        }

    }

    init {
    }

    fun getCommandsForPrgramm(application: Application): ArrayList<Command>? {
        return controller.getCommandsForApplications(application)
    }

    fun saveCommandForApplication(commandToSave: Command /*application: Application*/){
        controller.saveCommandForApplication(commandToSave)
        refreshTableView()
    }

    fun showWarning(text: String) {
        genericWarningView.setWarningText(text)
        openInternalWindow(genericWarningView)
    }

    fun openCommandEdit() {
        newOrEditCommandView.master = this
        openInternalWindow(newOrEditCommandView)
    }

    private fun refreshTableView(){
        applicationsAndCommandsView.refreshCommandData()
    }

    fun getApplications(): ArrayList<Application> {
        return controller.getApplications()
    }

    fun deleteCommandForApplication(selectedApp: Application, commandToDelete: Command) {
        controller.deleteCommandForApplication(commandToDelete)
        refreshTableView()
    }

}


