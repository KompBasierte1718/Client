package com.kompbasierte.client.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Application(active:Boolean, name:String, path:String) {

    val activeProperty = SimpleBooleanProperty(active)
    var active by activeProperty

    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    val pathProperty = SimpleStringProperty(path)
    var path by pathProperty
}