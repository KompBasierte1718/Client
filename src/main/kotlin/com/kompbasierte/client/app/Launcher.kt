package com.kompbasierte.client.app

import javafx.application.Application
import tornadofx.*
import com.kompbasierte.client.view.MainView
import javafx.stage.Stage
import java.util.logging.Logger

class Launcher : App(MainView::class, Styles::class) {

    lateinit var mainView:MainView

    companion object {
        private val LOG = Logger.getLogger(Control::class.java.name)
    }

    override fun start(stage: Stage) {
        super.start(stage)
        mainView = stage.uiComponent()!!
        LOG.info("Mainview beim Start gesetzt: "+ mainView.toString())
    }

    override fun stop() {
        mainView.onClose()
        super.stop()
    }

}

fun main(args: Array<String>) {
    Application.launch(Launcher::class.java, *args)
}
