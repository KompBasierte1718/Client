package com.kompbasierte.client.app

import org.json.JSONObject
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.net.Socket

class JSONLink(private val control: Control) {

    var host = "ec2-54-93-34-8.eu-central-1.compute.amazonaws.com"

    fun sendJSON(json :JSONObject, port :Int){
        val socket = Socket(host, port)
        val outputstream = socket.getOutputStream()
        //Wirft auf dem Server warum auch immer eine Fehlermeldung, senden des JSON läuft allerdings Problemlos
        val objectoutputstream = ObjectOutputStream(outputstream)
        objectoutputstream.writeObject(json.toString())
        outputstream.flush()
        socket.close()
    }
}