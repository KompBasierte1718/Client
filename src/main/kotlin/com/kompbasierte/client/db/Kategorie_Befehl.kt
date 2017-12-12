package com.example.demo.db

import ninja.sakib.pultusorm.annotations.NotNull
import ninja.sakib.pultusorm.annotations.PrimaryKey

class Kategorie_Befehl(@NotNull
                       @PrimaryKey
                       var Kategorie_ID: Int = 0,
                       @NotNull
                       @PrimaryKey
                       var Befehl_ID: Int = 0
) {}