package com.velectico.rbm.order.views

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
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
import com.velectico.rbm.databinding.*
import com.velectico.rbm.order.adapters.OrderDetailsListAdapter
import com.velectico.rbm.order.model.OrderDetailInfo
import com.velectico.rbm.order.model.OrderHead

class OrderDetails : BaseFragment()  {
    private lateinit var binding: OrderDetailsFragmentBinding
    private lateinit var orderHeadList : List<OrderDetailInfo>
    private lateinit var adapter: OrderDetailsListAdapter

    override fun getLayout(): Int {
        return R.layout.order_details_fragment
    }


    override fun init(binding: ViewDataBinding) {
        this.binding = binding as OrderDetailsFragmentBinding
        orderHeadList = OrderDetailInfo().getDummyBeatList()
        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        adapter = OrderDetailsListAdapter();
        binding.rvOrderDetailList.adapter = adapter
        adapter.orderList = orderHeadList
    }



}
