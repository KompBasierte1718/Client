package com.kompbasierte.client.app

import javafx.application.Application
import tornadofx.*
import com.kompbasierte.client.view.MainView

class Launcher: App(MainView::class, Styles::class){
    override fun stop() {
        super.stop()
        find(MainView::class).controller.onClose()
    }
}

fun main(args: Array<String>) {
    Application.launch(Launcher::class.java, *args)
}