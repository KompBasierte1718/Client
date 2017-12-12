package com.example.demo.db

import ninja.sakib.pultusorm.annotations.*

class Befehl(@NotNull
             @PrimaryKey
             @AutoIncrement
             @Unique
             var ID: Int = 0,
             @NotNull
             var Name: String? = null,
             @NotNull
             var VACallout: String? = null,
             @NotNull
             var shortcut: String? = null,
             @NotNull
             var Aktiv: Boolean = false
) {}