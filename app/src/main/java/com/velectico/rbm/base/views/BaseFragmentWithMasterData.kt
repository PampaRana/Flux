package com.velectico.rbm.base.views


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.velectico.rbm.masterdata.model.MasterDataResponse
import com.velectico.rbm.masterdata.viewmodel.MasterDataViewModel

/**
 * Created by mymacbookpro on 2020-04-26
 * Base class of all the fragments within this project
 */

abstract class BaseFragmentWithMasterData : BaseFragment(){

    private var masterDataViewModel: MasterDataViewModel? = null
    private var masterDataType : String? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        observeMasterDataViewModelData()
    }

    fun getMasterDataListFromServer(dataType: String , loader : View) {
        masterDataType = dataType;
        loader?.visibility = View.VISIBLE
        masterDataViewModel?.expenseListAPICall(dataType)
    }

    private fun observeMasterDataViewModelData() {
        //Master data observer
        masterDataViewModel = MasterDataViewModel.getInstance(baseActivity)
        masterDataViewModel?.masterDataListResponse?.observe(this, Observer { listResponse ->
            listResponse?.let {
                masterDataReceived(masterDataType,listResponse)
                // adapter?.expenseList = expenseViewModel.expenseListResponse.value?.expenseDetails as List<Expense>
            }
        })
        masterDataViewModel?.loading?.observe(this, Observer { progress ->
            //binding?.progressLayout?.visibility = if(progress) View.VISIBLE else View.GONE
            hideMasterDataLoader(masterDataType)
        })
    }

    abstract fun hideMasterDataLoader(masterDataType : String?)
    abstract fun  masterDataReceived(masterDataType : String? , masterData : MasterDataResponse? )

    companion object{
        const val BASE_FRAGMENT = "BaseFragment"
    }
}