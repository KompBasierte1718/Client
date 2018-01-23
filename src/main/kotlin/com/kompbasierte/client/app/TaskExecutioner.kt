package com.kompbasierte.client.app

import java.io.IOException
import com.kompbasierte.client.view.MainView
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.*

class TaskExecutioner constructor(private val mainView: MainView){

    fun executeTask(pfad: String) {
        try {
            Runtime.getRuntime().exec(pfad)
        } catch (e: IOException) {
            mainView.showWarning("Bitte gültigen Pfad angeben.")
        }
    }

    fun executeCommand(command: String) {
        val r = Robot()
        val sc = command.split("+")
        val key1: Int
        val key2: Int
        when (sc[0]) {
            "Strg" -> key1 = VK_CONTROL
            "Alt" -> key1 = VK_ALT
            else -> {
                key1 = 0
                mainView.showWarning("Bitte gültigen Shortcut angeben")
            }
        }
        when (sc[1]) {
            "T" -> key2 = VK_T
            "F" -> key2 = VK_F
            else -> {
                key2 = 0
                mainView.showWarning("Bitte gültigen Shortcut angeben")
            }
        }
        r.keyPress( VK_ALT )
        r.keyPress( VK_TAB )
        r.keyRelease( VK_ALT )
        r.keyRelease( VK_TAB )
        r.delay(500)
        r.keyPress(key1)
        r.keyPress(key2)
        r.keyRelease(key1)
        r.keyRelease(key2)
    }
}