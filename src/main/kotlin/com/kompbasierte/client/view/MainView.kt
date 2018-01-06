package com.kompbasierte.client.view

import com.kompbasierte.client.model.Application
import com.kompbasierte.client.model.Command
import com.kompbasierte.client.app.Control
import com.kompbasierte.client.model.Category
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import tornadofx.*
import java.util.logging.Logger

class MainView : View("Hello Tornado") {

    companion object {
        private val LOG = Logger.getLogger(MainView::class.java.name)
    }

    val controller = Control(this)
    private val applicationsAndCommandsView = ApplicationsAndCommandsTableView(this)
    private val genericWarningView = GenericWarningView()
    private val newOrEditCommandView = NewOrEditCommandView(this)


    override val root = borderpane {
        top = menubar {

            menu("Datei") {
                item("Gerät registrieren"){controller.registerDevice()}
                item("Speichern") { isDisable = true }
                item("Schließen").onAction = EventHandler<ActionEvent> {
                    LOG.info("Close Client")
                    Platform.exit()
                }
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

        center = vbox { add(applicationsAndCommandsView) }
        applicationsAndCommandsView.refreshApplicationData()
        applicationsAndCommandsView.refreshCommandData()

//        Vorerst nicht mehr benötigt

//        bottom = button("Trigger Warning")
//        {
//            action {
//                showWarning("Hier könnte ihre Warnung stehen!")
//            }
//        }

    }


    fun getCommandsForApplication(application: Application): ArrayList<Command> {
        return controller.getCommandsForApplications(application)
    }

    fun saveCommandForApplication(commandToSave: Command) {
        controller.saveCommandForApplication(commandToSave , applicationsAndCommandsView.getSelectedApplication())
        refreshTableView()
    }

    fun showWarning(text: String) {
        genericWarningView.setWarningText(text)
        openInternalWindow(genericWarningView)
    }

    fun openCommandEdit() {
        openInternalWindow(newOrEditCommandView)
    }

    private fun refreshTableView() {
        applicationsAndCommandsView.refreshApplicationData()
        applicationsAndCommandsView.refreshCommandData()
    }

    fun getApplications(): ArrayList<Application> {
        return controller.getApplications()
    }

    fun deleteCommandForApplication(selectedApp: Application, commandToDelete: Command) {
        controller.deleteCommandForApplication(commandToDelete, selectedApp)
        refreshTableView()
    }

    fun onClose() {
        super.close()
        applicationsAndCommandsView.close()
        genericWarningView.close()
        newOrEditCommandView.close()
        controller.onClose()
    }

    fun openApplicationEdit() {
        val newOrEditApplicationView = NewOrEditApplicationView(this)
        openInternalWindow(newOrEditApplicationView)
    }

    fun saveApplication(application: Application){
        controller.saveApplication(application)
        refreshTableView()
    }

    fun getCategories(): ArrayList<Category> {
        return controller.getCategories()
    }

    fun deleteApplication(selectedItem: Application) {
        controller.deleteApplication(selectedItem)
        refreshTableView()
    }

}


