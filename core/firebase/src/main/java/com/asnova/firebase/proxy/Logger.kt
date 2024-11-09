package com.asnova.firebase.proxy

import android.util.Log
import com.asnova.model.Resource

abstract class Logger (loggerTag: String){
    val tag: String = loggerTag

    abstract fun <T> logResourceResult(methodName: String, resource: Resource<T>)
}