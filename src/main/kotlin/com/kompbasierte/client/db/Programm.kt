package com.example.demo.db

import ninja.sakib.pultusorm.annotations.*

class Programm(@NotNull
               @PrimaryKey
               @AutoIncrement
               @Unique
               var ID: Int = 0,
               @NotNull
               var Kategorie_ID: Int = 0,
               @NotNull
               var Name: String? = null,
               @NotNull
               var Pfad_32: String? = null,
               var Pfad_64: String? = null,
               @NotNull
               var Aktiv: Boolean = false
) {}