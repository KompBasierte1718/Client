package com.kompbasierte.client.app

import java.io.IOException
import java.awt.Robot
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.*
import java.lang.reflect.Field

class TaskExecutioner constructor(private val control: Control){

    private val r = Robot()

    fun executeTask(pfad: String) {
        try {
            Runtime.getRuntime().exec(pfad)
        } catch (e: IOException) {
            control.showWarning("Bitte gültigen Pfad angeben.")
        }
    }

    fun executeCommand(command: String) {
        val sc = command.split("+")
        var string1: String

        if (sc[0] == "Strg") {
            string1 = "VK_CONTROL"
        } else if (sc[0] == "Alt" || sc[0] == "Shift") {
            string1 = "VK_" + sc[0].toUpperCase()
        } else {
            string1 = ""
        }

        val string2: String = "VK_" + sc[1]
        val field1: Field = KeyEvent::class.java.getField(string1)
        val field2: Field = KeyEvent::class.java.getField(string2)
        val key1: Int = field1.getInt(null)
        val key2: Int = field2.getInt(null)

        switchFocus()
        r.delay(500)
        r.keyPress(key1)
        r.keyPress(key2)
        r.delay(10)
        r.keyRelease(key1)
        r.keyRelease(key2)
        r.delay(500)
        switchFocus()
    }

    private fun switchFocus() {
        r.keyPress( VK_ALT )
        r.keyPress( VK_TAB )
        r.delay(10)
        r.keyRelease( VK_ALT )
        r.keyRelease( VK_TAB )
    }
}