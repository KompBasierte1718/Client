package com.kompbasierte.client.app

import java.io.IOException
import com.kompbasierte.client.view.MainView


class TaskExecutioner constructor(private val mainView: MainView){

    fun executeTask(pfad: String) {
        try {
            Runtime.getRuntime().exec(pfad)
        } catch (e: IOException) {
            mainView.showWarning("Bitte gültigen Pfad angeben.")
        }
    }
}