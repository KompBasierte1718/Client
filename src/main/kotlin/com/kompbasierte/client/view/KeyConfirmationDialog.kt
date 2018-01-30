package com.kompbasierte.client.view

import tornadofx.*
import javafx.scene.control.Label

class KeyConfirmationDialog(private val master: MainView) : Fragment("Geräteregistrierung abschließen")  {

    private var device = ""
    private lateinit var lblDevice :Label


    fun setDeviceType(deviceType :String){
        device = deviceType
        lblDevice.text = "Es möchte sich ein Gerät vom Typ $device mit Ihnen Verbinden. Verbindung zulassen?"
    }

    override val root =
            vbox {
                lblDevice = label("Es möchte sich ein Gerät vom Typ " + device +
                        " mit Ihnen Verbinden. Verbindung zulassen?")

                buttonbar {
                    button("Ja") {
                        action {
                            master.userRegisterConfirmation(1)
                            close()
                        }
                    }
                    button("Nein") {
                        action {
                            master.userRegisterConfirmation(-1)
                            close()
                        }
                    }
                }
            }
}