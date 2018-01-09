package com.kompbasierte.client.app

import javafx.application.Platform
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.net.Socket
import kotlin.concurrent.thread

class JSONLink(private val control: Control, private val port: Int) {

    private lateinit var host: String
    private lateinit var taskSocket: Socket
    private lateinit var taskInput: BufferedReader
    private lateinit var taskOutputStream: OutputStream

    private var userRegisterConfirmation = 0


    init {
            initialiseConnection()
            taskHandler()
    }

    fun setUserRegisterConfirmation(status :Int){
        userRegisterConfirmation = status
    }

    fun initialiseConnection(){
        try {
            host = "ec2-54-93-34-8.eu-central-1.compute.amazonaws.com"
            taskSocket = Socket(host, port)
            taskInput = BufferedReader(InputStreamReader(taskSocket.getInputStream()))
            taskOutputStream = taskSocket.getOutputStream()
        } catch(e :Exception) {
            return
            Platform.exit()
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

            while (input.read() == -1) {
                //TODO("Read incoming JSON File")
                println("test")
            }

            control.showUserConfirmation()

            while(userRegisterConfirmation == 0){
                Thread.sleep(500)
            }

            var confirmationJson = JSONObject()
            confirmationJson.put("confirmation", true)

            objectoutputstream.writeObject(confirmationJson)
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

                    //TODO("Add success/failure to JSON")

                    objectoutputstream.writeObject(json)
                    taskOutputStream.flush()
                }
            }catch(e :Exception){
                Platform.exit()
            }
        }
    }

    fun onClose() {
        taskSocket.close()
    }
}