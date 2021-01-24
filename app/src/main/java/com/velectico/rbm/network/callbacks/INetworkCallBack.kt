package com.velectico.rbm.network.callbacks

import com.velectico.rbm.network.response.NetworkResponse
import retrofit2.Callback

/**
 * Created by mymacbookpro on 2020-04-26
 * TODO: Add a class header comment!
 */
interface INetworkCallBack<T> : Callback<T> {
    fun onSuccessNetwork(data:Any?, response: NetworkResponse<T>)
    fun onFailureNetwork(data:Any?, error: NetworkError)
}