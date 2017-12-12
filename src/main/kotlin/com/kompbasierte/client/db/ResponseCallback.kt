package com.example.demo.db

import ninja.sakib.pultusorm.callbacks.Callback
import ninja.sakib.pultusorm.core.PultusORMQuery
import ninja.sakib.pultusorm.core.log
import ninja.sakib.pultusorm.exceptions.PultusORMException

class ResponseCallback : Callback {
    override fun onSuccess(type: PultusORMQuery.Type) {
        log("${type.name}", "Success")
    }

    override fun onFailure(type: PultusORMQuery.Type, exception: PultusORMException) {
        log("${type.name}", "Failure")
        exception.printStackTrace()
    }
}