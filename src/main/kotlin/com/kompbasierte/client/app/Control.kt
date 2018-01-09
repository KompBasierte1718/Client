package com.kompbasierte.client.app

import com.kompbasierte.client.model.Application
import com.kompbasierte.client.model.Category
import com.kompbasierte.client.model.Command
import com.kompbasierte.client.view.MainView
import org.json.JSONObject
import java.sql.*
import java.sql.SQLException
import java.sql.DriverManager
import java.util.logging.Logger
import kotlin.collections.ArrayList

class Control constructor(private val mainView: MainView) {
    private val jsonLink = JSONLink(this, 51337)

    companion object {
        private val LOG = Logger.getLogger(Control::class.java.name)
    }

    private lateinit var db: Connection

    init {

        try {
            db = openDatabase()
        } catch (e: SQLException) {
            mainView.showWarning("Fehler beim Verbinden mit der Datenbank. Bitte neustarten. Sollte der Fehler " +
                    "bestehen bleiben wenden Sie sich bitte an das Team auf https://github.com/KompBasierte1718/Client",
                    true)
        }
        if (!isDatabase()) {
            LOG.info("Database not complete, recreating")
            createDatabase()
            insertData()
        }
        connectToService()
    }

    private fun openDatabase(): Connection {
        val url = "jdbc:sqlite:client.db"

        return try {
            LOG.info("Connecting to DB")
            DriverManager.getConnection(url)
        } catch (e: SQLException) {
            LOG.warning("Connection to DB failed!")
            throw e
        }
    }

    private fun connectToService() {
//        TODO("not implemented")
        //etablish connection to webserver here
    }

    private fun registerToService() {
        TODO("not implemented")
        //register Client here
        LOG.info("Registrieren")
    }

    private fun isDatabase(): Boolean {
        if (checkTable("Befehl") && checkTable("Kategorie") && checkTable("Kategorie_Befehl")
                && checkTable("Programm") && checkTable("Programm_Befehl")) {
            return true
        }
        return false
    }

    private fun checkTable(table: String): Boolean {
        val sql = ("SELECT name FROM sqlite_master WHERE type='table' AND name='$table'")
        val stmt = db.createStatement()
        try {
            val result = stmt.executeQuery(sql)
            result.next()
            if (result.getString("name") == null) {
                return false
            }
            result.close()

        } catch (e: SQLException) {
            return false
        } finally {
            stmt.close()
        }
        return true
    }

    private fun createDatabase() {
        val sqlList = ArrayList<String>()
        sqlList.add("CREATE TABLE IF NOT EXISTS 'Befehl' (\n" +
                " 'ID'        INTEGER NOT NULL,\n" +
                " 'Name'      TEXT NOT NULL,\n" +
                " 'VACallout' TEXT NOT NULL,\n" +
                " 'shortcut'  TEXT NOT NULL,\n" +
                " 'Aktiv'     INTEGER NOT NULL,\n" +
                " PRIMARY KEY('ID')\n" +
                ");")

        sqlList.add("CREATE TABLE IF NOT EXISTS 'Kategorie' (\n" +
                " 'ID'   INTEGER NOT NULL,\n" +
                " 'Name' TEXT NOT NULL UNIQUE,\n" +
                " PRIMARY KEY('ID')\n" +
                ");")

        sqlList.add("CREATE TABLE IF NOT EXISTS 'Kategorie_Befehl' (\n" +
                " 'Kategorie_ID' INTEGER NOT NULL,\n" +
                " 'Befehl_ID'    INTEGER NOT NULL,\n" +
                " PRIMARY KEY('Kategorie_ID','Befehl_ID'),\n" +
                " FOREIGN KEY('Kategorie_ID') REFERENCES 'Kategorie'('ID'),\n" +
                " FOREIGN KEY('Befehl_ID') REFERENCES 'Befehl'('ID')\n" +
                ");")

        sqlList.add("CREATE TABLE IF NOT EXISTS 'Programm' (\n" +
                " 'ID'           INTEGER NOT NULL,\n" +
                " 'Kategorie_ID' INTEGER NOT NULL,\n" +
                " 'Name'         TEXT NOT NULL,\n" +
                " 'Pfad_32'      TEXT NOT NULL,\n" +
                " 'Pfad_64'      TEXT,\n" +
                " 'Aktiv'        INTEGER NOT NULL,\n" +
                " PRIMARY KEY('ID'),\n" +
                " FOREIGN KEY('Kategorie_ID') REFERENCES 'Kategorie'('ID')\n" +
                ");")

        sqlList.add("CREATE TABLE IF NOT EXISTS 'Programm_Befehl' (\n" +
                " 'Programm_ID' INTEGER NOT NULL,\n" +
                " 'Befehl_ID'   INTEGER NOT NULL,\n" +
                " PRIMARY KEY('Programm_ID','Befehl_ID'),\n" +
                " FOREIGN KEY('Befehl_ID') REFERENCES 'Befehl'('ID'),\n" +
                " FOREIGN KEY('Programm_ID') REFERENCES 'Programm'('ID')\n" +
                ");")
        for (sql: String in sqlList) {
            execute(sql)
        }
    }

    private fun insertData() {
        val sqlList = ArrayList<String>()
        sqlList.add("INSERT INTO Kategorie (Name) VALUES ('Sonstige');")
        sqlList.add("INSERT INTO Kategorie (Name) VALUES ('Mediaplayer');")
        sqlList.add("INSERT INTO Kategorie (Name) VALUES ('Browser');")
        sqlList.add("INSERT INTO Programm (Kategorie_ID, Name, Pfad_32, Pfad_64, Aktiv) VALUES (2, 'VLC', '123pfad32', 0, 1);")
        sqlList.add("INSERT INTO Programm (Kategorie_ID, Name, Pfad_32, Pfad_64, Aktiv) VALUES (2, 'Windows Media Player', '123pfad32', 0, 0);")
        sqlList.add("INSERT INTO Programm (Kategorie_ID, Name, Pfad_32, Pfad_64, Aktiv) VALUES (2, 'Spotify', '123pfad32', '123pfad64', 0);")
        sqlList.add("INSERT INTO Programm (Kategorie_ID, Name, Pfad_32, Pfad_64, Aktiv) VALUES (1, 'Paint', '123pfad32', '123pfad64', 1);")
        sqlList.add("INSERT INTO Programm (Kategorie_ID, Name, Pfad_32, Pfad_64, Aktiv) VALUES (1, 'PDF Architekt', '123pfad32', '123pfad64', 1);")
        sqlList.add("INSERT INTO Programm (Kategorie_ID, Name, Pfad_32, Pfad_64, Aktiv) VALUES (3, 'Google Chrome', 'pfad32', 'pfad64', 1);")
        sqlList.add("INSERT INTO Programm (Kategorie_ID, Name, Pfad_32, Pfad_64, Aktiv) VALUES (3, 'Mozilla Firefox', 'pfad32', 'pfad64', 1);")
        sqlList.add("INSERT INTO Programm (Kategorie_ID, Name, Pfad_32, Pfad_64, Aktiv) VALUES (3, 'Microsoft Edge', 'pfad32', 'hodor', 0);")
        sqlList.add("INSERT INTO Programm (Kategorie_ID, Name, Pfad_32, Pfad_64, Aktiv) VALUES (1, 'Rechner', 'pfad32', 0, 0);")
        sqlList.add("INSERT INTO Befehl (Name, VACallout, shortcut, Aktiv) VALUES ('NeuerTab', 'Neuer Tab öffnen', 'Strg+T', 1);")
        sqlList.add("INSERT INTO Befehl (Name, VACallout, shortcut, Aktiv) VALUES ('Verlauf', 'Verlauf öffnen', 'Strg+H', 0);")
        sqlList.add("INSERT INTO Befehl (Name, VACallout, shortcut, Aktiv) VALUES ('Drucken', 'Drucken', 'Strg+P', 1);")
        sqlList.add("INSERT INTO Befehl (Name, VACallout, shortcut, Aktiv) VALUES ('Suchen', 'Suchen', 'Strg+F', 1);")
        sqlList.add("INSERT INTO Befehl (Name, VACallout, shortcut, Aktiv) VALUES ('Lauter', 'Lauter', 'Strg+Up', 1);")
        sqlList.add("INSERT INTO Befehl (Name, VACallout, shortcut, Aktiv) VALUES ('Leiser', 'Leiser', 'Strg+Down', 1);")
        sqlList.add("INSERT INTO Befehl (Name, VACallout, shortcut, Aktiv) VALUES ('Stream', 'Stream', 'Strg+S', 0);")
        sqlList.add("INSERT INTO Befehl (Name, VACallout, shortcut, Aktiv) VALUES ('test', 'test', 'Alt+1', 1);")
        sqlList.add("INSERT INTO Befehl (Name, VACallout, shortcut, Aktiv) VALUES ('Test', 'Test', 'Strg+-', 0);")
        sqlList.add("INSERT INTO Kategorie_Befehl (Kategorie_ID, Befehl_ID) VALUES (1, 3);")
        sqlList.add("INSERT INTO Kategorie_Befehl (Kategorie_ID, Befehl_ID) VALUES (1, 4);")
        sqlList.add("INSERT INTO Kategorie_Befehl (Kategorie_ID, Befehl_ID) VALUES (1, 8);")
        sqlList.add("INSERT INTO Kategorie_Befehl (Kategorie_ID, Befehl_ID) VALUES (1, 9);")
        sqlList.add("INSERT INTO Kategorie_Befehl (Kategorie_ID, Befehl_ID) VALUES (2, 5);")
        sqlList.add("INSERT INTO Kategorie_Befehl (Kategorie_ID, Befehl_ID) VALUES (2, 6);")
        sqlList.add("INSERT INTO Kategorie_Befehl (Kategorie_ID, Befehl_ID) VALUES (3, 1);")
        sqlList.add("INSERT INTO Kategorie_Befehl (Kategorie_ID, Befehl_ID) VALUES (3, 2);")
        sqlList.add("INSERT INTO Kategorie_Befehl (Kategorie_ID, Befehl_ID) VALUES (3, 3);")
        sqlList.add("INSERT INTO Kategorie_Befehl (Kategorie_ID, Befehl_ID) VALUES (3, 4);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (1, 5);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (1, 6);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (1, 7);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (2, 5);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (2, 6);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (3, 5);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (3, 6);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (4, 3);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (5, 3);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (5, 4);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (6, 1);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (6, 2);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (6, 3);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (6, 4);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (7, 1);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (7, 2);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (7, 3);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (7, 4);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (8, 1);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (8, 2);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (8, 3);")
        sqlList.add("INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (8, 4);")
        for (sql: String in sqlList) {
            execute(sql)
        }
    }

    /**
     * Gets all known applications from the Database
     *
     * @return ArrayList of known Applications
     * @see Application
     */
    fun getApplications(): ArrayList<Application> {
        val appList = ArrayList<Application>()
        val stmt = db.createStatement()
        val sql = "SELECT * FROM Programm;"
        try {
            val result = stmt!!.executeQuery(sql)
            while (result.isBeforeFirst)
                result.next()
            while (!result.isAfterLast) {
                val active: Boolean = result.getInt("Aktiv") == 1
                appList.add(Application(result.getInt("ID"), result.getInt("Kategorie_ID"),
                        result.getString("Name"), result.getString("Pfad_32"), result.getString("Pfad_64"), active))
                result.next()
            }
            result.close()
            return appList
        } catch (e: SQLException) {
            return appList
        } finally {
            stmt.close()
        }
    }

    /**
     * Gets all known categories from the Database
     *
     * @return ArrayList of known Categories
     * @see Category
     */
    fun getCategories(): ArrayList<Category> {
        val categoryList = ArrayList<Category>()
        val stmt = db.createStatement()
        val sql = "SELECT * FROM Kategorie;"
        try {
            val result = stmt!!.executeQuery(sql)
            while (result.isBeforeFirst)
                result.next()
            while (!result.isAfterLast) {
                categoryList.add(Category(result.getInt("ID"), result.getString("Name")))
                result.next()
            }
            result.close()

        } catch (e: SQLException) {
        } finally {
            stmt.close()
            return categoryList
        }
    }

    /**
     * Gets all known Commands for a specific application from the Database
     *
     * @param application The application to get the Commands for
     * @return ArrayList of known Commands
     * @see Command
     * @see Application
     */
    fun getCommandsForApplications(application: Application): ArrayList<Command> {
        val commandList = ArrayList<Command>()
        val stmt = db.createStatement()
        val sql = "SELECT * FROM Befehl JOIN Programm_Befehl ON  Programm_Befehl.Befehl_ID  = Befehl.ID JOIN Programm" +
                " ON Programm_Befehl.Programm_ID = Programm.ID WHERE Programm.ID = ${application.id};"
        try {
            val result = stmt!!.executeQuery(sql)

            while (result.isBeforeFirst)
                result.next()
            while (!result.isAfterLast) {
                val active: Boolean = result.getInt("Aktiv") == 1
                commandList.add(Command(result.getInt("ID"), result.getString("Name"),
                        result.getString("VACallout"), result.getString("shortcut"), active))
                result.next()
            }
            result.close()
            return commandList
        } catch (e: SQLException) {
            mainView.showWarning(e.toString())
            return commandList
        } finally {
            stmt.close()
        }
    }

    /**
     * Saves a Commands for a specific application into the Database
     *
     * @param application The application to save the Command for
     * @param commandToSave The command to save
     * @see Command
     * @see Application
     */
    fun saveCommandForApplication(commandToSave: Command, application: Application) {
        var id = 0
        var sql = "SELECT COUNT(*), ID FROM Befehl WHERE Name = '${commandToSave.name}';"
        val stmt = db.createStatement()
        try {
            val result = stmt!!.executeQuery(sql)
            while (result.isBeforeFirst) {
                result.next()
            }
            id = result.getInt("ID")
            result.close()
        } catch (e: SQLException) {
            mainView.showWarning(e.toString())
        } finally {
            stmt.close()
        }
        if (id == 0) {
            val active = if (commandToSave.active) {
                1
            } else {
                0
            }
            sql = "INSERT INTO Befehl (Name, VACallout, shortcut, Aktiv) " +
                    "VALUES ('${commandToSave.name}', '${commandToSave.vACallout}', '${commandToSave.shortcut}', '$active');"
            println(sql)
            executeUpdate(sql)
            sql = "SELECT ID FROM Befehl WHERE Name = '${commandToSave.name}';"
            try {
                val result = stmt!!.executeQuery(sql)
                while (result.isBeforeFirst) {
                    result.next()
                }
                id = result.getInt("ID")
                result.close()
            } catch (e: SQLException) {
                mainView.showWarning(e.toString())
            } finally {
                stmt.close()
            }
        }
        sql = "INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES (${application.id}, $id);"
        println(sql)
        executeUpdate(sql)
    }
    /**
     * Saves a specific application into the Database
     *
     * @param application The application to save
     * @see Application
     */
    fun saveApplication(application: Application) {
        var id = 0
        var sql = "SELECT COUNT(*), ID FROM Programm WHERE Name = '${application.name}';"
        val stmt = db.createStatement()
        try {
            val result = stmt!!.executeQuery(sql)
            while (result.isBeforeFirst) {
                result.next()
            }
            id = result.getInt("ID")
            result.close()
        } catch (e: SQLException) {
            mainView.showWarning(e.toString())
        } finally {
            stmt.close()
        }
        if (id == 0) {
            val active = if (application.active) {
                1
            } else {
                0
            }
            sql = "INSERT INTO Programm (Kategorie_ID, Name, Pfad_32, Pfad_64, Aktiv) " +
                    "VALUES ('${application.categoryID}', '${application.name}', '${application.path32}', '${application.path64}', '$active');"
            println(sql)
            executeUpdate(sql)
            sql = "SELECT ID FROM Programm WHERE Name = '${application.name}';"
            try {
                val result = stmt!!.executeQuery(sql)
                while (result.isBeforeFirst) {
                    result.next()
                }
                id = result.getInt("ID")
                result.close()
            } catch (e: SQLException) {
                mainView.showWarning(e.toString())
            } finally {
                stmt.close()
            }
            sql = "SELECT * FROM Befehl JOIN Kategorie_Befehl ON Kategorie_Befehl.Befehl_ID = Befehl.ID " +
                    "JOIN Kategorie ON Kategorie_Befehl.Kategorie_ID = Kategorie.ID WHERE Kategorie.ID = ${application.categoryID};"
            try {
                val result = stmt!!.executeQuery(sql)

                while (result.isBeforeFirst)
                    result.next()
                while (!result.isAfterLast) {
                    sql = "INSERT INTO Programm_Befehl (Programm_ID, Befehl_ID) VALUES ('$id', '${result.getInt("ID")}')"
                    println(sql)
                    executeUpdate(sql)
                    result.next()
                }
                result.close()
            } catch (e: SQLException) {
                mainView.showWarning(e.toString())
            } finally {
                stmt.close()
            }
        }
    }

    /**
     * Deletes a Commands for a specific application from the Database
     *
     * @param application The application to delete the Command for
     * @param commandToDelete The command to delete
     * @see Command
     * @see Application
     */
    fun deleteCommandForApplication(commandToDelete: Command, application: Application) {
        var sql = "DELETE FROM Programm_Befehl WHERE Befehl_ID = ${commandToDelete.id} AND Programm_ID = ${application.id};"
        println(sql)
        executeUpdate(sql)
        var n = 0
        sql = "SELECT COUNT(*) FROM Programm_Befehl WHERE Befehl_ID = ${commandToDelete.id};"
        val stmt = db.createStatement()
        try {
            val result = stmt!!.executeQuery(sql)
            while (result.isBeforeFirst) {
                result.next()
            }
            n += result.getInt("COUNT(*)")
            result.close()
        } catch (e: SQLException) {
            mainView.showWarning(e.toString())
        } finally {
            stmt.close()
        }
        sql = "SELECT COUNT(*) FROM Kategorie_Befehl WHERE Befehl_ID = ${commandToDelete.id};"
        try {
            val result = stmt!!.executeQuery(sql)
            while (result.isBeforeFirst) {
                result.next()
            }
            n += result.getInt("COUNT(*)")
            result.close()
        } catch (e: SQLException) {
            //mainView.showWarning(e.toString())
        } finally {
            stmt.close()
        }
        if (n == 0) {
            sql = "DELETE FROM Befehl WHERE ID = '${commandToDelete.id}';"
            println(sql)
            executeUpdate(sql)
        }
    }

    /**
     * Deletes a specific application from the Database
     *
     * @param application The application to delete
     * @see Command
     * @see Application
     */
    fun deleteApplication(application: Application) {
        var sql = "DELETE FROM Programm_Befehl WHERE Programm_ID = ${application.id};"
        println(sql)
        executeUpdate(sql)
        sql = "DELETE FROM Programm WHERE ID = '${application.id}';"
        println(sql)
        executeUpdate(sql)
    }

    private fun execute(sql: String) {
        val stmt = db.createStatement()
        try {
            stmt.execute(sql)
        } catch (e: SQLException) {
        } finally {
            stmt.close()
        }
    }

    private fun executeUpdate(sql: String) {
        val stmt = db.createStatement()
        try {
            stmt.executeUpdate(sql)
        } catch (e: SQLException) {
            mainView.showWarning(e.toString())
        } finally {
            stmt.close()
        }
    }

    private fun closeDatabase() {
        LOG.info("closing Database")
        if (!db.isClosed) {
            db.close()
        }
    }

    /**
     * Shuts down services
     *
     * This function is called to close the connection to the Database and shut down any other services that may still
     * run
     */
    fun onClose() {
        LOG.info("Closing APP")
        closeDatabase()
        jsonLink.onClose()
    }

    fun registerDevice(arg :String) {
        val json = JSONObject()
        LOG.info("Schlüssel ist: "+arg)
        json.put("Passwort", arg)
        jsonLink.registerDevice(json,41337)
    }

    fun userRegisterConfirmation(status :Int) {
        jsonLink.setUserRegisterConfirmation(status)
    }

    fun showUserConfirmation(){
        mainView.openKeyConfirmationDialog()
    }
}

