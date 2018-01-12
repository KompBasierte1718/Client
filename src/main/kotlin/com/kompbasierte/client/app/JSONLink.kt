package com.kompbasierte.client.app

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.net.Socket
import kotlin.concurrent.thread



class JSONLink(private val control: Control, private val port: Int) {

    private var host = "ec2-54-93-34-8.eu-central-1.compute.amazonaws.com"
    private lateinit var taskSocket: Socket
    private lateinit var taskInput: BufferedReader
    private lateinit var taskOutputStream: OutputStream

    private var userRegisterConfirmation = 0


    init {
        //initialiseConnection()
        //taskHandler()
    }

    fun setUserRegisterConfirmation(status: Int) {
        userRegisterConfirmation = status
    }

    fun initialiseConnection() {
        try {
            taskSocket = Socket(host, port)
            taskInput = BufferedReader(InputStreamReader(taskSocket.getInputStream()))
            taskOutputStream = taskSocket.getOutputStream()
        } catch (e: Exception) {
            control.fatalClose("Konnte keine Verbindung zum Server herstellen!")
            return
        }
    }

    fun registerDevice(json: JSONObject, port: Int) {
        thread(start = true) {
            val registerSocket = Socket(host, port)
            val outputstream = registerSocket.getOutputStream()
            //Wirft auf dem Server warum auch immer eine Fehlermeldung, senden des JSON läuft allerdings Problemlos
            val objectoutputstream = ObjectOutputStream(outputstream)
            val input = BufferedReader(InputStreamReader(registerSocket.getInputStream()))

            objectoutputstream.writeObject(json.toString())
            outputstream.flush()

            var buffer :CharArray = charArrayOf(' ')
            var string = ""

            //Waiting for Server Response
            while (input.read(buffer) == -1){
                Thread.sleep(500)
            }

            string += buffer[0]

            while(buffer[0] != '\u0000'){
                input.read(buffer)
                string += buffer[0]
            }

            var serverResponse = JSONObject(string)
            control.showUserConfirmation(serverResponse.get("answer").toString())

            while (userRegisterConfirmation == 0) {
                Thread.sleep(500)
            }

            val confirmationJson = JSONObject()

            if(userRegisterConfirmation == -1) {
                confirmationJson.put("confirmation", false)
            } else {
                confirmationJson.put("confirmation", true)
            }

            objectoutputstream.writeObject(confirmationJson.toString())
            outputstream.flush()

            registerSocket.close()
        }
    }

    private fun taskHandler() {
        thread(start = true) {
            try {
                //Wirft auf dem Server warum auch immer eine Fehlermeldung, senden des JSON läuft allerdings Problemlos
                val objectoutputstream = ObjectOutputStream(taskOutputStream)
                while (!taskSocket.isClosed) {
                    while (taskInput.read() == -1) {
                        //TODO("Read incoming JSON File")
                    }

                    //TODO("Execute task and return success/failure")

                    val json = JSONObject()

                    json.put("program", "VLC")
                    json.put("task","starten")

                    control.executeTask(json)

                    //TODO("Add success/failure to JSON")

                    objectoutputstream.writeObject(json)
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
        taskSocket.close()
    }
}