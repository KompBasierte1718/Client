package com.kompbasierte.client.model

import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*

//For Database purposes
class CategoryCommand(categoryID: Int, commandID: Int) {
    private val categoryIDProperty = SimpleIntegerProperty(categoryID)
    var categoryID by categoryIDProperty

    private val commandIDProperty = SimpleIntegerProperty(commandID)
    var commandID by commandIDProperty
}