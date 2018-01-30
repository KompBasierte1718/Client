package com.kompbasierte.client.view

import com.kompbasierte.client.model.Application
import com.kompbasierte.client.model.Command
import com.kompbasierte.client.app.Control
import com.kompbasierte.client.model.Category
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import org.json.JSONObject
import tornadofx.*
import java.util.logging.Logger
import java.awt.Robot
import java.awt.event.KeyEvent
import java.awt.event.InputEvent

class MainView : View("Hello Tornado") {

    companion object {
        private val LOG = Logger.getLogger(MainView::class.java.name)
    }

    init {
        LOG.info("MainView erstellt")
    }

    //instanciate warning view to display fatal errors
    private val genericWarningView = GenericWarningView()

    //Create a Control
    private val controller = Control(this)

    //instanciate all Views
    private val applicationsAndCommandsView = ApplicationsAndCommandsTableView(this)
    private val newOrEditApplicationView = NewOrEditApplicationView(this)
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
                item("Test Taskexec").onAction = EventHandler<ActionEvent> {
                    val json = JSONObject()
                    json.put("program", "Google Chrome")
                    json.put("task", "starte")
                    controller.executeTask(json)
                }
                item("Test Robot").onAction = EventHandler<ActionEvent> {
                    val json = JSONObject()
                    json.put("program", "Google Chrome")
                    json.put("task", "NeuerTab")
                    controller.executeTask(json)
                }
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
        LOG.info("Refresh Table after init")
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
    fun saveCommandForApplication(commandToSave: Command, updateCommand: Boolean) {
        controller.saveCommandForApplication(commandToSave ,
                applicationsAndCommandsView.getSelectedApplication(), updateCommand)
        LOG.info("Refresh Table after saveCommand")
        refreshTableView()
    }

    /**
     * Opens a Warning-Dialog
     *
     * @param text The text to show in the Warning
     */
    fun showWarning(text: String) {
        showWarning(text, false)
    }

    fun showWarning(text: String, close: Boolean) {
        LOG.info("Show Warning: "+text+" // Close: "+close)
        genericWarningView.setWarningText(text)
        genericWarningView.setCloseBehaviour(close)
        runAsync{}ui {genericWarningView.openModal()}
    }

    /**
     * Opens a Command-View
     */
    fun openCommandNew() {
        newOrEditCommandView.clear()
        openInternalWindow(newOrEditCommandView)
    }
    fun openCommandEdit(commandToEdit:Command?){
        if (commandToEdit!=null){
            newOrEditCommandView.clear()
            newOrEditCommandView.openEditObject(commandToEdit)
            openInternalWindow(newOrEditCommandView)
        }else{
            showWarning("Bitte einen Befehl auswählen!")
        }
    }

    /**
     * Updates the Table-Data of the AppAndCommandView
     */
    private fun refreshTableView() {
        LOG.info("Refresh Appdata")
        applicationsAndCommandsView.refreshApplicationData()
        LOG.info("Refresh Commanddata")
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
        LOG.info("Refresh Table after deleteCommand")
        refreshTableView()
    }

    fun onClose() {
        super.close()
        applicationsAndCommandsView.close()
        genericWarningView.close()
        newOrEditCommandView.close()
        controller.onClose()
    }

    fun openApplicationNew() {
        newOrEditApplicationView.clear()
        openInternalWindow(newOrEditApplicationView)
    }
    fun openApplicationEdit(applicationToEdit: Application?) {
        if (applicationToEdit != null) {
            newOrEditApplicationView.clear()
            newOrEditApplicationView.openEditObject(applicationToEdit)
            openInternalWindow(newOrEditApplicationView)
        }else{
            showWarning("Bitte eine Applikation auswählen")
        }
    }

    fun saveApplication(application: Application){
        controller.saveApplication(application)
        LOG.info("Refresh Table after saveApp")
        refreshTableView()
    }

    fun getCategories(): ArrayList<Category> {
        return controller.getCategories()
    }

    fun deleteApplication(selectedItem: Application) {
        controller.deleteApplication(selectedItem)
        LOG.info("Refresh Table after deleteApp")
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


