package com.kompbasierte.client.app

import com.kompbasierte.client.model.Application
import com.kompbasierte.client.model.Command
import com.kompbasierte.client.view.MainView
import java.sql.*
import java.sql.SQLException
import java.sql.DriverManager
import kotlin.collections.ArrayList


class Control constructor(mainview: MainView) {

    private val db = openDatabase()
    private val mainView = mainview

    init {
        println(mainView.heading)

        if (!isDatabase()) {
            println("Database not complete, recreating")
            createDatabase()
        }
//        connectToService()
    }

    private fun isDatabase(): Boolean {
        //TODO better select to check
        val sql = ("SELECT Count(*) FROM Commands")
        val stmt = db!!.createStatement()
        try {
            val result = stmt.executeQuery(sql)
            result.next()
            if (result.getInt(1) == 0) {
                return false
            }
            result.close()
        } catch (e: SQLException) {
            mainView.showWarning(e.toString())
            return false
        } finally {
            stmt.close()
        }
        return true
    }

    private fun createDatabase() {
        //TODO
        val sql = ("CREATE TABLE IF NOT EXISTS Commands (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	active integer NOT NULL,\n"
                + "	name text NOT NULL,\n"
                + "	VAshoutout text NOT NULL,\n"
                + "	shortcut text NOT NULL\n"
                + ");")
        val stmt = db!!.createStatement()
        try {
            stmt.execute(sql)
        } catch (e: SQLException) {
        } finally {
            stmt.close()
        }
    }


    private fun connectToService() {
        TODO("not implemented")
    }

    private fun openDatabase(): Connection? {
        val url = "jdbc:sqlite:client.db"

        return try {
            DriverManager.getConnection(url)
        } catch (e: SQLException) {
            mainView.showWarning(e.toString())
            return null
        }
    }

    private fun registerToService() {
        TODO("not implemented")
        println("Registrieren")
    }


    private fun closeDatabase() {
        println("closing Database")
        if (db != null) {
            db.close()
        }
    }

    fun onClose() {
        println("Closing APP")
        closeDatabase()
    }

    fun getCommandsForApplications(programm: Application): ArrayList<Command>? {
        //TODO insert correct query
        val stmt = db!!.createStatement()
        val sql = "SELECT * FROM Commands"
        try {
            val result = stmt!!.executeQuery(sql)
            val commandList = ArrayList<Command>()

            while (result.isBeforeFirst)
                result.next()
            while (!result.isAfterLast) {
                val active: Boolean = result.getInt("active") == 1
                println("Active set " + active)
                commandList.add(Command(active, result.getString("name"),
                        result.getString("VAshoutout"), result.getString("shortcut"),
                        result.getInt("id")))
                result.next()
            }
            result.close()
            return commandList
        } catch (e: SQLException) {
            mainView.showWarning(e.toString())
            return null
        } finally {
            stmt.close()
        }
    }

    fun getApplications(): ArrayList<Application> {
        val appList = ArrayList<Application>()
        //TODO Databasequery
        appList.add(Application(true, "VLC", "123pfad"))
        appList.add(Application(true, "Spotify", "123pfad"))
        return appList
    }

    //TODO add application reference
    fun saveCommandForApplication(commandToSave: Command) {
        val active = if (commandToSave.active)
            1
        else
            0
        val sql = "INSERT INTO Commands (active, name, vashoutout, shortcut) VALUES (" + active + ",\"" +
                commandToSave.name + "\",\"" + commandToSave.vACallout + "\",\"" + commandToSave.shortcut + "\");"
        println(sql)
        val stmt = db!!.createStatement()
        try {
            stmt.executeUpdate(sql)
        } catch (e: SQLException) {
            mainView.showWarning(e.toString())
        } finally {
            stmt.close()
        }
    }

    //TODO add app reference
    fun deleteCommandForApplication(commandToDelete: Command) {

        val sql = "DELETE FROM Commands WHERE ID =" + commandToDelete.id + ";"
        println(sql)
        val stmt = db!!.createStatement()
        try {
            stmt.executeUpdate(sql)
        } catch (e: SQLException) {
            mainView.showWarning(e.toString())
        } finally {
            stmt.close()
        }
    }
}

