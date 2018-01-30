package com.kompbasierte.client.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

//For Database purposes
class Category(val id: Int, name: String) {

    private val nameProperty = SimpleStringProperty(name)
    var name: String by nameProperty
}