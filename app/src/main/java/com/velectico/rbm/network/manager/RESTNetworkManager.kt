package com.velectico.rbm.network.manager

import com.velectico.rbm.base.model.BaseModel
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
class RESTNetworkManager private constructor(private val service: WebService, private val httpClient:OkHttpClient,private  val model: BaseModel): INetworkManager {

    override fun <T> cancelRequest(request: NetworkRequest<T>) {
        for(call in httpClient.dispatcher.queuedCalls()){
            if(call.request().tag() == request.apiName){
                call.cancel()
            }
        }
    }
    override fun <T, K> makeAsyncCall(request: NetworkRequest<T>, callBack: INetworkCallBack<K>) {
        val call = getCall(service, request,model) as Call<K>
        return call.enqueue(callBack)
    }

    override fun <T, K> makeSyncCall(request: NetworkRequest<T>): K? {
        val call = getCall(service, request,model) as Call<K>
        return call.execute().body()
    }


    companion object{
        @Volatile private var INSTANCE: RESTNetworkManager? = null
        @Volatile private var INSTANCE_GOOGLE: RESTNetworkManager? = null
        @Volatile private var INSTANCE_TEST: RESTNetworkManager? = null

        fun getInstance(service: WebService, okHttpClient: OkHttpClient,model: BaseModel) : RESTNetworkManager =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: RESTNetworkManager(service, okHttpClient,model =model ).also { INSTANCE = it }
            }



    }
}