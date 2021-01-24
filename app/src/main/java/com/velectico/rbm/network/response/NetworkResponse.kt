package com.velectico.rbm.network.response

import okhttp3.Headers

/**
 * Created by mymacbookpro on 2020-04-26
 * Base Network response
 */
class NetworkResponse<T>(val code:Int?, val data:T?, headers: Headers)