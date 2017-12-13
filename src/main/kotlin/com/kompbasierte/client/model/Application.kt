package com.kompbasierte.client.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Application(val id:Int, category_ID:Int, name:String, path_32:String, path_64:String?, active:Boolean) {

    val categoryIDProperty = SimpleIntegerProperty(category_ID)
    var category_ID by categoryIDProperty

    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    val path32Property = SimpleStringProperty(path_32)
    var path_32 by path32Property

    val path64Property = SimpleStringProperty(path_64)
    var path_64 by path64Property

    val activeProperty = SimpleBooleanProperty(active)
    var active by activeProperty
}