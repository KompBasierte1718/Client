package com.example.demo.db

import ninja.sakib.pultusorm.annotations.*

class Kategorie(  @NotNull
                  @PrimaryKey
                  @AutoIncrement
                  @Unique
                  var ID: Int = 0,
                  @NotNull
                  var Name: String? = null
) {}