package com.example.demo.app

import com.example.demo.view.MainView
import javafx.application.Application
import tornadofx.App

class MyApp: App(MainView::class, Styles::class)

fun main(args : Array<String>){
    Application.launch(MyApp::class.java, *args)
}