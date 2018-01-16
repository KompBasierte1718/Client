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

    //instanciate warning view to display fatal errors
    private val genericWarningView = GenericWarningView()

    //Create a Control
    private val controller = Control(this)

    //instanciate all Views
    private val applicationsAndCommandsView = ApplicationsAndCommandsTableView(this)
    private val newOrEditCommandView = NewOrEditCommandView(this)
    private val keyDialog = KeyDialog(this)
    private val keyConfirmationDialog = KeyConfirmationDialog(this)


    //Define Layout
    override val root = borderpane {
        //Menu on top
        top = menubar {

            menu("Datei") {
                item("Gerät registrieren").onAction = EventHandler<ActionEvent>{openKeyDialog()}
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

        //Open AppAndCommandView in Center and refresh Data
        center = vbox { add(applicationsAndCommandsView) }
        refreshTableView()

//        Vorerst nicht mehr benötigt
//        bottom = button("Trigger Warning")
//        {
//            action {
//                showWarning("Hier könnte ihre Warnung stehen!", true)
//            }
//        }

    }

    /**
     *
     */
    fun getCommandsForApplication(application: Application): ArrayList<Command> {
        return controller.getCommandsForApplications(application)
    }

    /**
     *
     */
    fun saveCommandForApplication(commandToSave: Command) {
        controller.saveCommandForApplication(commandToSave , applicationsAndCommandsView.getSelectedApplication())
        refreshTableView()
    }

    /**
     * Opens a Warning-Dialog
     *
     * @param text The text to show in the Warning
     */
    fun showWarning(text: String) {
        LOG.info("Show Warning: "+text)
        genericWarningView.setWarningText(text)
        runAsync{}ui {genericWarningView.openModal()}
    }

    fun showWarning(text: String, close: Boolean) {
        LOG.info("Show closing Warning: "+text)
        genericWarningView.setWarningText(text)
        genericWarningView.setCloseBehaviour(close)
        runAsync{}ui {genericWarningView.openModal()}
    }

    /**
     * Opens a Command-View
     */
    fun openCommandEdit() {
        openInternalWindow(newOrEditCommandView)
    }

    /**
     * Updates the Table-Data of the AppAndCommandView
     */
    private fun refreshTableView() {
        applicationsAndCommandsView.refreshApplicationData()
        applicationsAndCommandsView.refreshCommandData()
    }

    /**
     * Gets List of all known Applications from Database
     *
     * @return ArrayList of Applications
     * @see Application
     */
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

    fun openKeyDialog() {
        keyDialog.keyText.clear()
        openInternalWindow(keyDialog)
    }

    fun transmitKeys(arg :String){
        controller.registerToService(arg)
    }

    fun openKeyConfirmationDialog(device :String){
        keyConfirmationDialog.setDeviceType(device)
        runAsync {  }ui { openInternalWindow(keyConfirmationDialog)}
    }

    fun userRegisterConfirmation(status :Int){
        controller.userRegisterConfirmation(status)
    }
}


