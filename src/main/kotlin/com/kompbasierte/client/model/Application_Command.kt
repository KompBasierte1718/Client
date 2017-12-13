package com.kompbasierte.client.model

import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*

class Application_Command(application_ID: Int, command_ID: Int) {
    val applicationIDProperty = SimpleIntegerProperty(application_ID)
    var application_ID by applicationIDProperty

    val commandIDProperty = SimpleIntegerProperty(command_ID)
    var command_ID by commandIDProperty
}