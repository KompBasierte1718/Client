package com.kompbasierte.client.view

import tornadofx.*

class KeyConfirmationDialog(private val master: MainView) : Fragment("Geräteregistrierung abschließen")  {

    private var device = ""

    fun setDeviceType(deviceType :String){
        device = deviceType
    }

    override val root =
            vbox {
                label("Es möchte sich ein Gerät vom Typ " + device + " mit Ihnen Verbinden. Verbindung zulassen?")

                buttonbar {
                    button("Ja") {
                        action {
                            master.controller.userRegisterConfirmation(1)
                            close()
                        }
                    }
                    button("Nein") {
                        action {
                            master.controller.userRegisterConfirmation(-1)
                            close()
                        }
                    }
                }
            }
}