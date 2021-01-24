package com.velectico.rbm.payment.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentPaymentListFragmentBinding
import com.velectico.rbm.databinding.PaymentHistoryListFragmentBinding
import com.velectico.rbm.payment.adapter.BeatPaymentListAdapter
import com.velectico.rbm.payment.models.PaymentInfo
import com.velectico.rbm.utils.DEALER_ROLE


class PaymentHistoryList :  BaseFragment() {
    private lateinit var binding: PaymentHistoryListFragmentBinding;
    private lateinit var beatList: List<PaymentInfo>
    private lateinit var adapter: BeatPaymentListAdapter
    private var currentDatePicketParentView: TextInputEditText? = null
    override fun getLayout(): Int {
        return R.layout.payment_history_list_fragment
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as PaymentHistoryListFragmentBinding

        beatList = PaymentInfo().getDummyBeatList()
        setUpRecyclerView()


    }


    private fun setUpRecyclerView() {

        adapter = BeatPaymentListAdapter();
        binding.rvPaymentList.adapter = adapter
        adapter.beatList = beatList
    }

}
