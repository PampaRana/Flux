package com.velectico.rbm.network.manager

import com.velectico.rbm.network.WebService
import com.velectico.rbm.network.callbacks.INetworkCallBack
import com.velectico.rbm.network.getCall
import com.velectico.rbm.network.request.NetworkRequest
import okhttp3.OkHttpClient
import retrofit2.Call

/**
 * Created by mymacbookpro on 2020-04-30
 * TODO: Add a class header comment!
 */
class NetworkManager private constructor(private val service: WebService, private val httpClient:OkHttpClient): INetworkManager {

    override fun <T> cancelRequest(request: NetworkRequest<T>) {
        for(call in httpClient.dispatcher.queuedCalls()){
            if(call.request().tag() == request.apiName){
                call.cancel()
            }
        }
    }

    override fun <T, K> makeAsyncCall(request: NetworkRequest<T>, callBack: INetworkCallBack<K>) {
        val call = getCall(service, request) as Call<K>
        return call.enqueue(callBack)
    }

    override fun <T, K> makeSyncCall(request: NetworkRequest<T>): K? {
        val call = getCall(service, request) as Call<K>
        return call.execute().body()
    }


    companion object{
        @Volatile private var INSTANCE: NetworkManager? = null
        @Volatile private var INSTANCE_GOOGLE: NetworkManager? = null
        @Volatile private var INSTANCE_TEST: NetworkManager? = null

        fun getInstance(service: WebService, okHttpClient: OkHttpClient) : NetworkManager =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: NetworkManager(service, okHttpClient).also { INSTANCE = it }
            }

        fun getInstanceGoogle(service: WebService, okHttpClient: OkHttpClient) : NetworkManager =
            INSTANCE_GOOGLE ?: synchronized(this){
                INSTANCE_GOOGLE ?: NetworkManager(service, okHttpClient).also { INSTANCE_GOOGLE = it }
            }

        fun getInstanceTest(service: WebService, okHttpClient: OkHttpClient) : NetworkManager =
            INSTANCE_TEST ?: synchronized(this){
                INSTANCE_TEST ?: NetworkManager(service, okHttpClient).also { INSTANCE_TEST = it }
            }

    }
}