package com.kompbasierte.client.model

import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*

//For Database purposes
class ApplicationCommand(applicationID: Int, commandID: Int) {
    private val applicationIDProperty = SimpleIntegerProperty(applicationID)
    var applicationID by applicationIDProperty

    private val commandIDProperty = SimpleIntegerProperty(commandID)
    var commandID by commandIDProperty
}