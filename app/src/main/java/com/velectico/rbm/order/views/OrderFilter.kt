package com.velectico.rbm.order.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentOrderFilterBinding
import com.velectico.rbm.databinding.FragmentOrderListBinding
import com.velectico.rbm.order.model.OrderHead

/**
 * A simple [Fragment] subclass.
 */
class OrderFilter : BaseFragment()  {
    private lateinit var binding: FragmentOrderFilterBinding

    override fun getLayout(): Int {
        return R.layout.fragment_order_filter
    }


    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentOrderFilterBinding

        binding.btnOrderHistory.setOnClickListener {

            val navDirection =  OrderFilterDirections.actionOrderFilterToCreateOrderFragment("","")
            Navigation.findNavController(binding.btnOrderHistory).navigate(navDirection)
        }




    }

}
