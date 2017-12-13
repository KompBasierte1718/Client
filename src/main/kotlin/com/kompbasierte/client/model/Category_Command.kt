package com.kompbasierte.client.model

import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*

class Category_Command(category_ID: Int, command_ID: Int) {
    val categoryIDProperty = SimpleIntegerProperty(category_ID)
    var category_ID by categoryIDProperty

    val commandIDProperty = SimpleIntegerProperty(command_ID)
    var command_ID by commandIDProperty
}