package com.example.demo.db

import ninja.sakib.pultusorm.annotations.NotNull
import ninja.sakib.pultusorm.annotations.PrimaryKey

class Programm_Befehl(@NotNull
                      @PrimaryKey
                      var Programm_ID: Int = 0,
                      @NotNull
                      @PrimaryKey
                      var Befehl_ID: Int = 0
) {}