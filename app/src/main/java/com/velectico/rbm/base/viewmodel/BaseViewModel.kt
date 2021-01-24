package com.velectico.rbm.base.viewmodel

import androidx.lifecycle.ViewModel
import com.velectico.rbm.network.apiconstants.*

/**
 * Created by mymacbookpro on 2020-04-26
 * Base view model of all the view models used in the project
 */

open class BaseViewModel : ViewModel(){

    fun getLoginRequestHeaderParams():MutableMap<String, String>{
        return mutableMapOf(
            REQ_HEADER_TYPE_KEY to REQ_HEADER_TYPE_VAL ,
            REQ_HEADER_DEVICE_ID_KEY to REQ_HEADER_DEVICE_ID_VAL,
            REQ_HEADER_DEVICE_TYPE_KEY to REQ_HEADER_DEVICE_TYPE_VAL
        )
    }

}