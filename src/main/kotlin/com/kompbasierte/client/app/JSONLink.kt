package com.kompbasierte.client.app

import com.kompbasierte.client.view.MainView
import org.json.JSONObject
import java.io.*
import java.net.ConnectException
import java.net.Socket
import java.net.UnknownHostException
import java.util.logging.Logger
import kotlin.concurrent.thread


class JSONLink(private val control: Control, private val port: Int) {

    private var host = "ec2-54-93-34-8.eu-central-1.compute.amazonaws.com"
    private lateinit var taskSocket: Socket

    private var userRegisterConfirmation = 0


    companion object {
        private val LOG = Logger.getLogger(MainView::class.java.name)
    }

    fun setUserRegisterConfirmation(status: Int) {
        userRegisterConfirmation = status
    }

    fun registerDevice(json: JSONObject, port: Int) {
        thread(start = true) {
            sendPassword(json)

            var awaitingResponse = true

            Thread.sleep(5000)

            while (awaitingResponse) {
                awaitingResponse = getDeviceType(port)
                Thread.sleep(4000)
            }

            while (userRegisterConfirmation == 0) {
                Thread.sleep(1000)
            }

            val confirmationJson = JSONObject()
            confirmationJson.put("device", "pcclient")
            if (userRegisterConfirmation == -1) {
                confirmationJson.put("koppeln", false)
            } else {
                confirmationJson.put("koppeln", true)
            }

            sendConfirmation(port, confirmationJson)
        }
    }

    private fun sendConfirmation(port: Int, confirmationJson: JSONObject) {
        LOG.info("Opening Socket")
        try {
            val socket = Socket(host, port)
            try {
                socket.soTimeout = 10000

                val objectoutputstream = ObjectOutputStream(socket.getOutputStream())

                objectoutputstream.writeObject(confirmationJson.toString())
                objectoutputstream.flush()
            } catch (e: ConnectException) {
                control.showWarning("Der Applikationsserver hat die Verbindung abgelehnt. Bitte versuchen Sie es später noch einmal")
            } catch (e: UnknownHostException) {
                control.showWarning("Fehler bei der Verbindung zum Applikationsserver! Bitte überprüfen Sie ihre Internetverbindung.")
            } catch (e: IOException) {
                control.showWarning("Fehler beim Öffnen der Verbindung zum Applikationsserver! Bitte versuchen Sie es später noch einmal.")
            } finally {
                if (!socket.isClosed) {
                    socket.close()
                }
            }
        } catch (e: ConnectException) {
            control.showWarning("Der Applikationsserver hat die Verbindung abgelehnt. Bitte versuchen Sie es später noch einmal")
        } catch (e: UnknownHostException) {
            control.showWarning("Verbindung zum Applikationsserver konnte nicht aufgebaut werden. Bitte überprüfen Sie Ihre Internetverbindung und versuchen Sie es erneut!")
        }
    }

    private fun getDeviceType(port: Int): Boolean {
        var waiting = true
        val buffer: CharArray = charArrayOf(' ')
        var string = ""
        LOG.info("Opening Socket")
        try {
            val socket = Socket(host, port)
            try {
                socket.soTimeout = 10000

                val objectoutputstream = ObjectOutputStream(socket.getOutputStream())

                val request = JSONObject()
                request.put("device", "pcclient")
                request.put("getDevice", "true")

                LOG.info(request.toString())
                objectoutputstream.writeObject(request.toString())
                objectoutputstream.flush()

                val input = BufferedReader(InputStreamReader(socket.getInputStream()))

                //Waiting for Server Response
                while (input.read(buffer) == -1) {
                    Thread.sleep(500)
                }

                string += buffer[0]

                while (buffer[0] != '}') {
                    input.read(buffer)
                    string += buffer[0]
                }

                val serverResponse = JSONObject(string)
                if (serverResponse.has("answer")) {
                    if (serverResponse.get("answer").toString() != "NO COMMANDS") {
                        waiting = false
                        control.showUserConfirmation(serverResponse.get("answer").toString())
                    }
                } else {
                    LOG.info(string)
                }
            } catch (e: ConnectException) {
                control.showWarning("Der Applikationsserver hat die Verbindung abgelehnt. Bitte versuchen Sie es später noch einmal")
            } catch (e: UnknownHostException) {
                control.showWarning("Fehler bei der Verbindung zum Applikationsserver! Bitte überprüfen Sie ihre Internetverbindung.")
            } catch (e: IOException) {
                control.showWarning("Fehler beim Öffnen der Verbindung zum Applikationsserver! Bitte versuchen Sie es später noch einmal.")
            } finally {
                if (!socket.isClosed) {
                    socket.close()
                }
            }
        } catch (e: ConnectException) {
            control.showWarning("Der Applikationsserver hat die Verbindung abgelehnt. Bitte versuchen Sie es später noch einmal")
        } catch (e: UnknownHostException) {
            control.showWarning("Verbindung zum Applikationsserver konnte nicht aufgebaut werden. Bitte überprüfen Sie Ihre Internetverbindung und versuchen Sie es erneut!")
        }
        return waiting
    }

    private fun sendPassword(json: JSONObject) {
        val buffer: CharArray = charArrayOf(' ')
        var string = ""
        try {
            val socket = Socket(host, port)
            try {
                socket.soTimeout = 10000
                val objectoutputstream = ObjectOutputStream(socket.getOutputStream())

                LOG.info(json.toString())
                objectoutputstream.writeObject(json.toString())
                objectoutputstream.flush()

                val input = BufferedReader(InputStreamReader(socket.getInputStream()))

                //Waiting for Server Response
                while (input.read(buffer) == -1) {
                    Thread.sleep(500)
                }

                string += buffer[0]

                while (buffer[0] != '}') {
                    input.read(buffer)
                    string += buffer[0]
                }

                val serverResponse = JSONObject(string)
                if (serverResponse.has("answer")) {
                    if (serverResponse.get("answer").toString() != "WAITING FOR VA") {
                        control.showUserConfirmation(serverResponse.get("answer").toString())
                    }
                } else {
                    LOG.info(string)
                }

                socket.close()
            } catch (e: ConnectException) {
                control.showWarning("Der Server hat die Verbindung abgelehnt. Bitte versuchen Sie es später noch einmal")
            } catch (e: UnknownHostException) {
                control.showWarning("Fehler bei der Verbindung zum Applikationsserver! Bitte überprüfen Sie ihre Internetverbindung.")
            } catch (e: IOException) {
                control.showWarning("Fehler beim Öffnen der Verbindung zum Applikationsserver! Bitte versuchen Sie es später noch einmal.")
            } finally {
                if (!socket.isClosed) {
                    socket.close()
                }
            }
        } catch (e: ConnectException) {
            control.showWarning("Der Applikationsserver hat die Verbindung abgelehnt. Bitte versuchen Sie es später noch einmal")
        } catch (e: UnknownHostException) {
            control.showWarning("Verbindung zum Applikationsserver konnte nicht aufgebaut werden. Bitte überprüfen Sie Ihre Internetverbindung und versuchen Sie es erneut!")
        }
    }

    fun taskHandler() {
        thread(start = true) {
            var connectionRefusedCounter = 0
            var working = true
            val retrys = 20
            while (working) {
                val buffer: CharArray = charArrayOf(' ')
                var string = ""
                LOG.info("Trying to open Tasksocket")
                try {
                    taskSocket = Socket(host, port)
                    try {
                        LOG.info("Tasksocket opened")
                        taskSocket.soTimeout = 10000

                        val objectoutputstream = ObjectOutputStream(taskSocket.getOutputStream())

                        val json = JSONObject()
                        json.put("device", "pcclient")
                        json.put("instructions", "true")

                        objectoutputstream.writeObject(json.toString())
                        objectoutputstream.flush()

                        val input = BufferedReader(InputStreamReader(taskSocket.getInputStream()))

                        //Waiting for Server Response
                        while (input.read(buffer) == -1) {
                            Thread.sleep(500)
                        }

                        string += buffer[0]

                        while (buffer[0] != '}') {
                            input.read(buffer)
                            string += buffer[0]
                        }

                        val serverResponse = JSONObject(string)

                        if (serverResponse.get("answer").toString() != "NO COMMANDS") {
                            LOG.info("Executing Task")
                            control.executeTask(serverResponse)
                        }
                    } catch (e: ConnectException) {
                        control.showWarning("Der Server hat die Verbindung abgelehnt. Bitte versuchen Sie es später noch einmal")
                    } catch (e: UnknownHostException) {
                        control.showWarning("Fehler bei der Verbindung zum Applikationsserver! Bitte überprüfen Sie ihre Internetverbindung.")
                    } catch (e: IOException) {
                        control.showWarning("Fehler beim Öffnen der Verbindung zum Applikationsserver! Bitte versuchen Sie es später noch einmal.")
                    } finally {
                        if (!taskSocket.isClosed) {
                            LOG.info("Closing Tasksocket")
                            taskSocket.close()
                        }
                    }
                    connectionRefusedCounter = 0
                } catch (e: ConnectException) {
                    connectionRefusedCounter++
                    LOG.info("Server refusing connection on Tasksocket. No of retrys: " + (retrys - connectionRefusedCounter))
                    if (connectionRefusedCounter >= retrys) {
                        working = false
                        control.fatalClose("Der Applikationsserver kann momentan nicht erreicht werden. Die Anwendung wird beendet. Bitte versuchen Sie es später noch einmal!")
                    }
                } catch (e: UnknownHostException) {
                    control.showWarning("Verbindung zum Applikationsserver konnte nicht aufgebaut werden. Bitte überprüfen Sie Ihre Internetverbindung und versuchen Sie es erneut!")
                }
                Thread.sleep(4000)
            }
        }
    }

    fun onClose() {
        try {
            if (!taskSocket.isClosed) {
                taskSocket.close()
            }
        } catch (e: UninitializedPropertyAccessException) {
            return
        }
    }
}
