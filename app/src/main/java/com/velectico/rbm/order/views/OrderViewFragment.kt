package com.velectico.rbm.order.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.CreateOrderListDetails
import com.velectico.rbm.database.CartProduct
import com.velectico.rbm.databinding.FragmentCreateOrderBinding
import com.velectico.rbm.databinding.FragmentOrderPreviewBinding
import com.velectico.rbm.databinding.FragmentOrderViewBinding
import com.velectico.rbm.order.adapters.OrderPreviewListAdapter
import com.velectico.rbm.order.adapters.OrderViewListAdapter


class OrderViewFragment :  BaseFragment() {

    private lateinit var binding: FragmentOrderViewBinding
    private var orderCartList : List<CreateOrderListDetails> = emptyList()
    private lateinit var adapter: OrderViewListAdapter
    override fun getLayout(): Int {
        return R.layout.fragment_order_view
    }

    override fun init(binding: ViewDataBinding) {

        this.binding = binding as FragmentOrderViewBinding
        /*adapter = OrderViewListAdapter(
            context,
            orderCartList
        )
        binding.rvCartList.adapter = adapter*/

    }


}