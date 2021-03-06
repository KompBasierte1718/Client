package com.kompbasierte.client.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Command(val id: Int, name: String, vACallout: String, shortcut: String, active: Boolean) {

    val nameProperty = SimpleStringProperty(name)
    var name: String by nameProperty

    val vACalloutProperty = SimpleStringProperty(vACallout)
    var vACallout: String by vACalloutProperty

    val shortcutProperty = SimpleStringProperty(shortcut)
    var shortcut: String by shortcutProperty

    val activeProperty = SimpleBooleanProperty(active)
    var active by activeProperty
}