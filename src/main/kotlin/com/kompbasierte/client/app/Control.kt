package com.kompbasierte.client.app

import com.kompbasierte.client.view.MainView
import java.sql.JDBCType

class Control constructor(mainview: MainView) {


    val mainView: MainView?
//    val db :

    init {
        mainView = mainview
        println(mainView.heading)

        val db = loadDatabase()

        if (db == null) {
            createDatabase()
            registerToService()
        }

        connectToService()
    }


    fun connectToService() {
        println("Connect")
    }

    fun loadDatabase() {
        println("Lade Datenbank")
    }

    fun registerToService() {
        println("Registrieren")
    }

    fun createDatabase() {
        println("Datenbank erstellen")
    }
}

