package com.velectico.rbm.network.request

import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by mymacbookpro on 2020-05-06
 * TODO: Add a class header comment!
 */
abstract class BaseRequest (
    val apiName:String,
    val endPoint:String,
    var headers:Map<String, String>? = null,
    var paths:Map<String, String>? = null,
    var queryParameters:Map<String, String>? = null,
    var fields:Map<String, String>? = null,
    var requestParam:String = "",
    val requestBody : RequestBody? = null
    )