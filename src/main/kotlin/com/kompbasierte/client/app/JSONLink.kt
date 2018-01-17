package com.kompbasierte.client.app

import com.kompbasierte.client.view.MainView
import org.json.JSONObject
import java.io.*
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.logging.Logger
import kotlin.concurrent.thread



class JSONLink(private val control: Control, private val port: Int) {

    private var host = "ec2-54-93-34-8.eu-central-1.compute.amazonaws.com"
    private lateinit var taskSocket: Socket
    private lateinit var taskInput: BufferedReader
    private lateinit var taskOutputStream: OutputStream

    private var userRegisterConfirmation = 0


    companion object {
        private val LOG = Logger.getLogger(MainView::class.java.name)
    }

    fun setUserRegisterConfirmation(status: Int) {
        userRegisterConfirmation = status
    }

    fun initialiseConnection() {
        try {
            taskSocket = Socket(host, port)
            taskInput = BufferedReader(InputStreamReader(taskSocket.getInputStream()))
            taskOutputStream = taskSocket.getOutputStream()
            taskHandler()
        } catch (e: Exception) {
            control.fatalClose("Konnte keine Verbindung zum Server herstellen!")
            return
        }
    }

    fun registerDevice(json: JSONObject, port: Int) {
        thread(start = true) {
            try {
                var registerSocket = Socket(host, port)
                try{
                    registerSocket.soTimeout = 10000
                    //val outputstream = registerSocket.getOutputStream()
                    //Wirft auf dem Server warum auch immer eine Fehlermeldung, senden des JSON läuft allerdings Problemlos
                    val objectoutputstream = ObjectOutputStream(registerSocket.getOutputStream())


                    objectoutputstream.writeObject(json.toString())
                    objectoutputstream.flush()

                    val buffer: CharArray = charArrayOf(' ')
                    var string = ""

                    registerSocket.close()

                    while(string == "") {
                        LOG.info("Opening Socket")
                        try {
                            registerSocket = Socket(host, port)
                            registerSocket.soTimeout = 10000

                            val objectoutputstream = ObjectOutputStream(registerSocket.getOutputStream())


                            var json = JSONObject()
                            json.put("device", "pcclient")
                            json.put("instructions", "true")

                            objectoutputstream.writeObject(json.toString())
                            objectoutputstream.flush()

                            val input = BufferedReader(InputStreamReader(registerSocket.getInputStream()))

                            var count = 0

                            while (input.read(buffer) == -1 && count <= 5) {
                                Thread.sleep(500)
                                count++
                            }

                            if(count <= 5) {
                                LOG.info("Data available")

                                string += buffer[0]

                                LOG.info(string)
                                while (buffer[0] != '}') {
                                    input.read(buffer)
                                    string += buffer[0]
                                }

                                LOG.info("message: " + string)

                                val readyResponse = JSONObject(string)

                                if(readyResponse.get("answer").toString() == "NO COMMANDS")
                                {
                                    string = ""
                                } else {
                                    control.executeTask(readyResponse)
                                }
                            }
                        }catch(e :Exception) {
                        } finally {
                            if(!registerSocket.isClosed){
                                registerSocket.close()
                                LOG.info("Closing Socket")
                            }
                        }

                        Thread.sleep(5000)
                    }



                    /*val input = BufferedReader(InputStreamReader(registerSocket.getInputStream()))

                    while (input.read(buffer) == -1) {
                        Thread.sleep(500)
                    }

                    string += buffer[0]

                    while (buffer[0] != '\u0000') {
                        input.read(buffer)
                        string += buffer[0]
                    }

                    val readyResponse = JSONObject(string)

                    LOG.info(readyResponse.get("answer").toString())

                    if(!registerSocket.isClosed){
                        registerSocket.close()
                    }

                    /*//Waiting for Server Response
                    while (input.read(buffer) == -1) {
                        Thread.sleep(500)
                    }

                    string += buffer[0]

                    while (buffer[0] != '\u0000') {
                        input.read(buffer)
                        string += buffer[0]
                    }

                    val readyResponse = JSONObject(string)

                    LOG.info(readyResponse.get("answer").toString())

                    string = ""

                    //Waiting for Server Response
                    while (input.read(buffer) == -1) {
                        Thread.sleep(500)
                    }

                    string += buffer[0]

                    while (buffer[0] != '\u0000') {
                        input.read(buffer)
                        string += buffer[0]
                    }

                    val serverResponse = JSONObject(string)

                    control.showUserConfirmation(serverResponse.get("answer").toString())

                    while (userRegisterConfirmation == 0) {
                        Thread.sleep(500)
                    }

                    val confirmationJson = JSONObject()

                    confirmationJson.put("device", "pcclient")

                    if (userRegisterConfirmation == -1) {
                        confirmationJson.put("confirmation", false)
                    } else {
                        confirmationJson.put("confirmation", true)
                    }

                    objectoutputstream.flush()
                    objectoutputstream.writeObject(confirmationJson.toString())*/
*/
                } catch(e :Exception) {
                    control.showWarning("Registrierung fehlgeschlagen! Bitte versuchen Sie es erneut." + e.message)
                }finally {
                    if(!registerSocket.isClosed) {
                        registerSocket.close()
                    }
                }
            } catch(e :Exception) {
                control.showWarning("Verbindung zum Server konnte nicht aufgebaut werden. Bitte überprüfen Sie Ihre Internetverbindung und versuchen Sie es erneut!")
            }
        }
    }

    private fun taskHandler() {
        thread(start = true) {
            try {
                //Wirft auf dem Server warum auch immer eine Fehlermeldung, senden des JSON läuft allerdings Problemlos
                val objectoutputstream = ObjectOutputStream(taskOutputStream)
                while (!taskSocket.isClosed) {
                    val buffer :CharArray = charArrayOf(' ')
                    var string = ""

                    //Waiting for Server Response
                    while (taskInput.read(buffer) == -1){
                        Thread.sleep(500)
                    }

                    string += buffer[0]

                    while(buffer[0] != '\u0000'){
                        taskInput.read(buffer)
                        string += buffer[0]
                    }


                    val serverResponse = JSONObject(string)

                    control.executeTask(serverResponse)

                    val confirmationJson = JSONObject()

                    confirmationJson.put("device", "pcclient")
                    //TODO("return success/failure")
                    confirmationJson.put("taskExecuted",true)

                    objectoutputstream.writeObject(confirmationJson)
                    taskOutputStream.flush()
                }
            } catch (e: Exception) {
                control.fatalClose("Ein Netzwerkfehler ist aufgetreten! Das Programm wird beendet")
            } finally {
                if(!taskSocket.isClosed) {
                    taskSocket.close()
                }
            }
        }
    }

    fun onClose() {
        if(!taskSocket.isClosed) {
            taskSocket.close()
        }
    }
}