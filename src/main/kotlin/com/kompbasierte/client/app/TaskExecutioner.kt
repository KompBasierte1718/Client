package com.kompbasierte.client.app

import java.awt.Robot
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_ALT
import java.awt.event.KeyEvent.VK_TAB
import java.io.IOException
import java.lang.reflect.Field

class TaskExecutioner constructor(private val control: Control){

    private val r = Robot()

    fun executeTask(pfad: String) {
        try {
            Runtime.getRuntime().exec(pfad)
            switchFocus()
        } catch (e: IOException) {
            control.showWarning("Bitte gültigen Pfad angeben.")
        }
    }

    fun executeCommand(command: String) {
        val sc = command.split(Constants.COMMAND_DELIMITER)
        val string1: String

        string1 = if (sc[0] == "Strg"||sc[0] == "Ctrl") {
            Constants.CONTROL_KEY_STRING
        } else if (sc[0] == "Alt" || sc[0] == "Shift") {
            "VK_" + sc[0].toUpperCase()
        } else {
            ""
        }

        val string2: String = "VK_" + sc[1].toUpperCase()
        val field1: Field = KeyEvent::class.java.getField(string1)
        val field2: Field = KeyEvent::class.java.getField(string2)
        val key1: Int = field1.getInt(null)
        val key2: Int = field2.getInt(null)

        switchFocus()
        r.delay(Constants.DELAY_BEFORE_OR_AFTER_COMMANDS)
        r.keyPress(key1)
        r.keyPress(key2)
        r.delay(Constants.DELAY_COMMAND_PRESSED)
        r.keyRelease(key1)
        r.keyRelease(key2)
        r.delay(Constants.DELAY_BEFORE_OR_AFTER_COMMANDS)
        switchFocus()
    }

    private fun switchFocus() {
        r.keyPress( VK_ALT )
        r.keyPress( VK_TAB )
        r.delay(Constants.DELAY_COMMAND_PRESSED)
        r.keyRelease( VK_ALT )
        r.keyRelease( VK_TAB )
    }
}