package com.velectico.rbm.network.request

import okhttp3.RequestBody

/**
 * Created by mymacbookpro on 2020-04-30
 * Base Network request
 */
class NetworkRequest<T> (
    apiName:String,
    endPoint:String,
    headers:Map<String, String>? = null,
    paths:Map<String, String>? = null,
    queryParameters:Map<String, String>? = null,
    fields:Map<String, String>? = null,
    var request:T? = null,
    requestParam:String = "",
    requestBody: RequestBody? = null
    ):BaseRequest(apiName, endPoint, headers, paths, queryParameters, fields, requestParam,requestBody)