package com.kompbasierte.client.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Command(active: Boolean, name: String, vACallout: String, shortcut: String, val id: Int) {

    val activeProperty = SimpleBooleanProperty(active)
    var active by activeProperty

    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    val vACalloutProperty = SimpleStringProperty(vACallout)
    var vACallout by vACalloutProperty

    val shortcutProperty = SimpleStringProperty(shortcut)
    var shortcut by shortcutProperty
}