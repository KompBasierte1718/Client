package com.kompbasierte.client.app

import java.util.logging.Logger

class Constants {

    companion object {
        val LOG: Logger = Logger.getLogger(Control::class.java.name)

        //JSON-LINK
        const val HEX_CHARS = "0123456789ABCDEF"
        const val ENCRYPT_ALGORITHM = "SHA-512"
        const val SOCKET_TIMEOUT = 10000
        const val HOST = "ec2-54-93-34-8.eu-central-1.compute.amazonaws.com"
        const val MAX_CONNECTION_ATTEMPTS = 30

        //Control
        const val COMMANDPORT = 41337
        const val DATABSEURL = "jdbc:sqlite:client.db"
        const val REGISTERPORT = 51337

        //TaskExec
        const val CONTROL_KEY_STRING = "VK_CONTROL"
        const val DELAY_BEFORE_OR_AFTER_COMMANDS= 500
        const val DELAY_COMMAND_PRESSED = 10
        const val COMMAND_DELIMITER  = "+"
    }
}