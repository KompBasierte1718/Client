package com.kompbasierte.client.app

import com.kompbasierte.client.app.Constants.Companion.LOG
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.ObjectOutputStream
import java.net.*
import java.security.MessageDigest
import kotlin.concurrent.thread


class JSONLink(private val control: Control, private val port: Int) {

    private var host = Constants.HOST
    private lateinit var taskSocket: Socket

    private var userRegisterConfirmation = 0

    fun setUserRegisterConfirmation(status: Int) {
        userRegisterConfirmation = status
    }

    fun registerDevice(json: JSONObject, port: Int) {
        thread(start = true) {
            if (sendPassword(json, port)) {

                var awaitingResponse = true
                var connectionCounter = 0

                Thread.sleep(5000)

                while (awaitingResponse && connectionCounter <= Constants.MAX_CONNECTION_ATTEMPTS) {
                    awaitingResponse = getDeviceType(port)
                    Thread.sleep(10000)
                    connectionCounter++
                }

                if (!awaitingResponse) {
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
                } else {
                    control.showWarning("Keine Antwort vom Server erhalten. Bitte versuchen Sie später erneut " +
                            "ein Gerät zu verbinden!")
                }
            } else {
                control.showWarning("Keine Antwort vom Server erhalten. Bitte versuchen Sie später erneut ein" +
                        " Gerät zu verbinden!")
            }
        }
    }

    private fun sendConfirmation(port: Int, confirmationJson: JSONObject) {
        LOG.info("Opening Socket")
        try {
            val socket = Socket(host, port)
            try {
                socket.soTimeout = Constants.SOCKET_TIMEOUT

                val objectoutputstream = ObjectOutputStream(socket.getOutputStream())

                confirmationJson.put("deviceID",getHashedMac())

                objectoutputstream.flush()
                objectoutputstream.writeObject(confirmationJson.toString())
                objectoutputstream.flush()
            } catch (e: ConnectException) {
                control.showWarning("Der Applikationsserver hat die Verbindung abgelehnt. Bitte versuchen Sie es" +
                        " später noch einmal")
            } catch (e: UnknownHostException) {
                control.showWarning("Fehler bei der Verbindung zum Applikationsserver! Bitte überprüfen Sie " +
                        "ihre Internetverbindung.")
            } catch (e: IOException) {
                control.showWarning("Fehler beim Öffnen der Verbindung zum Applikationsserver! Bitte versuchen" +
                        " Sie es später noch einmal.")
            } finally {
                if (!socket.isClosed) {
                    socket.close()
                }
            }
        } catch (e: ConnectException) {
            control.showWarning("Der Applikationsserver hat die Verbindung abgelehnt. Bitte versuchen Sie es " +
                    "später noch einmal")
        } catch (e: UnknownHostException) {
            control.showWarning("Verbindung zum Applikationsserver konnte nicht aufgebaut werden. Bitte " +
                    "überprüfen Sie Ihre Internetverbindung und versuchen Sie es erneut!")
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
                socket.soTimeout = Constants.SOCKET_TIMEOUT

                val objectoutputstream = ObjectOutputStream(socket.getOutputStream())

                val request = JSONObject()
                request.put("device", "pcclient")
                request.put("getDevice", "true")
                request.put("deviceID",getHashedMac())

                LOG.info("Sending request for a device to connect...")
                objectoutputstream.flush()
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
                    if (serverResponse.get("answer").toString().toUpperCase() != "WAITING FOR VA") {
                        waiting = false
                        LOG.info("Device to connect found: " + serverResponse.get("answer").toString())
                        control.showUserConfirmation(serverResponse.get("answer").toString())
                    }else{
                        LOG.info("Still waiting for device to connect...")
                    }
                } else {
                    LOG.info("Unexpected JSON received: " + string)
                }
            } catch (e: ConnectException) {
                control.showWarning("Der Applikationsserver hat die Verbindung abgelehnt. Bitte versuchen Sie es " +
                        "später noch einmal")
            } catch (e: UnknownHostException) {
                control.showWarning("Fehler bei der Verbindung zum Applikationsserver! Bitte überprüfen Sie ihre " +
                        "Internetverbindung.")
            } catch (e: IOException) {
                control.showWarning("Fehler beim Öffnen der Verbindung zum Applikationsserver! Bitte versuchen " +
                        "Sie es später noch einmal.")
            } finally {
                if (!socket.isClosed) {
                    LOG.info("Closing Socket")
                    socket.close()
                }
            }
        } catch (e: ConnectException) {
            control.showWarning("Der Applikationsserver hat die Verbindung abgelehnt. Bitte versuchen Sie es " +
                    "später noch einmal")
        } catch (e: UnknownHostException) {
            control.showWarning("Verbindung zum Applikationsserver konnte nicht aufgebaut werden. " +
                    "Bitte überprüfen Sie Ihre Internetverbindung und versuchen Sie es erneut!")
        }
        return waiting
    }

    /**
     * Retrieves the clients mac address and returns it as a hashed value
     */
    private fun getHashedMac(): String {
        /*val ip = InetAddress.getLocalHost()
        val network = NetworkInterface.getByInetAddress(ip)
        val mac = network.hardwareAddress
        val sb = StringBuilder()
        for (i in 0 until mac.size) {
            sb.append(String.format("%02X%s", mac[i], if (i < mac.size - 1) "-" else ""))
        }

        val bytes = MessageDigest
                .getInstance(Constants.ENCRYPT_ALGORITHM)
                .digest(sb.toString().toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(Constants.HEX_CHARS[i shr 4 and 0x0f])
            result.append(Constants.HEX_CHARS[i and 0x0f])
        }
        return result.toString()*/
        return "LINUXCLIENT"
    }

    private fun sendPassword(json: JSONObject, port: Int): Boolean {
        val buffer: CharArray = charArrayOf(' ')
        var string = ""
        try {
            val socket = Socket(host, port)
            try {
                socket.soTimeout = Constants.SOCKET_TIMEOUT
                val outputstream = socket.getOutputStream()
                val objectoutputstream = ObjectOutputStream(outputstream)

                json.put("deviceID",getHashedMac())

                LOG.info(json.toString())
                objectoutputstream.flush()
                val jsonstring = json.toString()
                objectoutputstream.writeObject(jsonstring)
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
                    if (serverResponse.get("answer").toString().toUpperCase() != "WAITING FOR VA") {
                        LOG.info("answer: " + serverResponse.get("answer").toString())
                        return false
                    } else {
                        LOG.info("Password successfully transmitted")
                    }
                } else {
                    LOG.info(string)
                    return false
                }

            } catch (e: ConnectException) {
                control.showWarning("Der Server hat die Verbindung abgelehnt. Bitte versuchen Sie es später" +
                        " noch einmal")
                return false
            } catch (e: UnknownHostException) {
                control.showWarning("Fehler bei der Verbindung zum Applikationsserver! Bitte überprüfen Sie ihre" +
                        " Internetverbindung.")
                return false
            } catch (e: IOException) {
                control.showWarning("Fehler beim Öffnen der Verbindung zum Applikationsserver! Bitte versuchen " +
                        "Sie es später noch einmal.")
                return false
            } finally {
                if (!socket.isClosed) {
                    LOG.info("Closing Socket")
                    socket.close()
                }
            }
        } catch (e: ConnectException) {
            control.showWarning("Der Applikationsserver hat die Verbindung abgelehnt. Bitte versuchen Sie es " +
                    "später noch einmal")
            return false
        } catch (e: UnknownHostException) {
            control.showWarning("Verbindung zum Applikationsserver konnte nicht aufgebaut werden. Bitte" +
                    " überprüfen Sie Ihre Internetverbindung und versuchen Sie es erneut!")
            return false
        }
        return true
    }

    fun taskHandler() {
        thread(start = true) {
            var connectionRefusedCounter = 0
            var working = true
            val retrys = Constants.MAX_CONNECTION_ATTEMPTS
            while (working) {
                val buffer: CharArray = charArrayOf(' ')
                var string = ""
                LOG.info("Trying to open Tasksocket")
                try {
                    taskSocket = Socket(host, port)
                    try {
                        LOG.info("Tasksocket opened")
                        taskSocket.soTimeout = Constants.SOCKET_TIMEOUT

                        val objectoutputstream = ObjectOutputStream(taskSocket.getOutputStream())

                        val json = JSONObject()
                        json.put("device", "pcclient")
                        json.put("instructions", "true")
                        json.put("deviceID",getHashedMac())

                        objectoutputstream.flush()
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

                        if (serverResponse.has("answer")) {
                            if (serverResponse.get("answer").toString().toUpperCase() != "NO COMMANDS") {
                                LOG.info("Executing Task")
                                control.executeTask(serverResponse)
                            }
                        } else {
                            LOG.info("No Answer Tag found in Server Instructions" + serverResponse.toString())
                        }
                    } catch (e: ConnectException) {
                        control.showWarning("Der Server hat die Verbindung abgelehnt. Bitte versuchen Sie es " +
                                "später noch einmal")
                    } catch (e: UnknownHostException) {
                        control.showWarning("Fehler bei der Verbindung zum Applikationsserver! Bitte überprüfen " +
                                "Sie ihre Internetverbindung.")
                    } catch (e: IOException) {
                        control.showWarning("Fehler beim Öffnen der Verbindung zum Applikationsserver! Bitte" +
                                " versuchen Sie es später noch einmal.")
                    } finally {
                        if (!taskSocket.isClosed) {
                            LOG.info("Closing Tasksocket")
                            taskSocket.close()
                        }
                    }
                    connectionRefusedCounter = 0
                } catch (e: ConnectException) {
                    connectionRefusedCounter++
                    LOG.info("Server refusing connection on Tasksocket. No of retrys: "
                            + (retrys - connectionRefusedCounter))
                    if (connectionRefusedCounter >= retrys) {
                        working = false
                        control.fatalClose("Der Applikationsserver kann momentan nicht erreicht werden." +
                                " Die Anwendung wird beendet. Bitte versuchen Sie es später noch einmal!")
                    }
                } catch (e: UnknownHostException) {
                    control.showWarning("Verbindung zum Applikationsserver konnte nicht aufgebaut werden." +
                            " Bitte überprüfen Sie Ihre Internetverbindung und versuchen Sie es erneut!")
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
