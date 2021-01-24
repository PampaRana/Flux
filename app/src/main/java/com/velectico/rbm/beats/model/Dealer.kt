package com.velectico.rbm.beats.model

import java.util.ArrayList

class Dealer(){
    var dealerId : String? =null;
    var dealerName : String? =null;

    constructor(dealerId : String?,dealerName : String?) : this() {
        this.dealerId = dealerId
        this.dealerName = dealerName
    }

    fun getDummyDealerList() : ArrayList<String> {
        val dealerList = mutableListOf<Dealer>()
        var provinceList: ArrayList<String> = ArrayList()
        for (i in 0..10) {
            provinceList.add(i,"Dealer Name "+i)
            dealerList.add(i, Dealer("Dealer id "+i,"Dealer Name "+i));
        }
        return provinceList
    }

    fun getDummyDistributorList() : ArrayList<String> {
        val dealerList = mutableListOf<Dealer>()
        var provinceList: ArrayList<String> = ArrayList()
        for (i in 0..10) {
            provinceList.add(i,"Distributor Name "+i)
            dealerList.add(i, Dealer("Distributor id "+i,"Distributor Name "+i));
        }
        return provinceList
    }
}