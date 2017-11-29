package com.kompbasierte.client.app

import com.kompbasierte.client.view.MainView

class Control constructor(mainview: MainView) {


    val mainView : MainView?

   init {
       mainView = mainview
       println(mainView.heading)
   }


}

