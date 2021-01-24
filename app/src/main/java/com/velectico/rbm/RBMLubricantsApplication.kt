package com.velectico.rbm

import android.app.Application
import android.content.Context

/**
 * Created by mymacbookpro on 2020-05-01
 * TODO: Add a class header comment!
 */
class RBMLubricantsApplication : Application(){

    init{
        instance = this
    }
    companion object {
        var instance:RBMLubricantsApplication? = null
        var globalRole = ""
        var fromBeat = ""
        var filterFrom = ""
        var fromProductList = ""
        @JvmStatic
        fun getAppContext():Context?{
            return instance?.applicationContext
        }
    }
}