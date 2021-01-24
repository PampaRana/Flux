package com.velectico.rbm.network.manager

import com.velectico.rbm.network.WebService

/**
 * Created by mymacbookpro on 2020-04-30
 * TODO: Add a class header comment!
 */

fun getNetworkManager(type: ManagerFactory): INetworkManager {

    return when(type){
        ManagerFactory.GOOGLE -> NetworkManager.getInstanceGoogle(
            WebService.getService(),
            WebService.getHttpClient()
        )
        ManagerFactory.DEV -> NetworkManager.getInstance(
            WebService.getService(),
            WebService.getHttpClient()
        )
        ManagerFactory.TEST -> NetworkManager.getInstanceTest(
            WebService.getTestService(),
            WebService.getHttpClient()
        )
    }
}

enum class ManagerFactory {
    GOOGLE,
    DEV,
    TEST
}