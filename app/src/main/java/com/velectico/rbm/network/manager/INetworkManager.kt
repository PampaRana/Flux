package com.velectico.rbm.network.manager

import com.velectico.rbm.network.callbacks.INetworkCallBack
import com.velectico.rbm.network.request.NetworkRequest

/**
 * Created by mymacbookpro on 2020-04-30
 * TODO: Add a class header comment!
 */
interface INetworkManager {
    fun<T, K> makeAsyncCall(request: NetworkRequest<T>, callBack: INetworkCallBack<K>)

    fun<T, K> makeSyncCall(request: NetworkRequest<T>) : K?

    fun <T>cancelRequest(request: NetworkRequest<T>)
}