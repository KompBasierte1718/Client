package com.kompbasierte.client.view

import com.kompbasierte.client.app.Control
import tornadofx.*

class MainView : View("Hello Tornado") {

    val controller = Control(this)
    val applicationsAndCommandsView : ApplicationsAndCommandsTableView by inject()
    val genericWarningView = find(GenericWarningView::class)

    override val root = vbox {
        menubar {

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

        add(applicationsAndCommandsView)
        applicationsAndCommandsView.master = this@MainView

        button("delete"){
            action {
                showWarning("Hier könnte ihre Warnung stehen!1!")
            }
        }
    }


    fun showWarning(text:String){
        genericWarningView.setWarningText(text)
       openInternalWindow(genericWarningView)

    }

}


