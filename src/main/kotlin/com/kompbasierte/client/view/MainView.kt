package com.kompbasierte.client.view

import com.kompbasierte.client.app.Control
import tornadofx.*

class MainView : View("Hello Tornado") {

    val controller = Control(this)
    val applicationsAndCommandsView : ApplicationsAndCommandsTableView by inject()
    val genericWarningView = find(GenericWarningView::class)

    override val root = borderpane {
       top=  menubar {

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


        bottom = button("Trigger Warning"){
            action {
                showWarning("Hier könnte ihre Warnung stehen!")
            }
        }
    }

    init {
    }


    fun showWarning(text:String){
        genericWarningView.setWarningText(text)
       openInternalWindow(genericWarningView)

    }

}


