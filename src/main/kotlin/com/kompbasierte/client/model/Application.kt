package com.kompbasierte.client.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Application(val id:Int, categoryID:Int, name:String, path32:String, path64:String?, active:Boolean) {

    private val categoryIDProperty = SimpleIntegerProperty(categoryID)
    var categoryID by categoryIDProperty

    private val nameProperty = SimpleStringProperty(name)
    var name: String by nameProperty

    private val path32Property = SimpleStringProperty(path32)
    var path32: String by path32Property

    private val path64Property = SimpleStringProperty(path64)
    var path64: String by path64Property

    val activeProperty = SimpleBooleanProperty(active)
    var active by activeProperty
}