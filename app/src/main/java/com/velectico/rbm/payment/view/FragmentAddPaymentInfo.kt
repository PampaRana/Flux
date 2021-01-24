package com.velectico.rbm.payment.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentAddPaymentInfoBinding
import com.velectico.rbm.databinding.FragmentPaymentListFragmentBinding
import com.velectico.rbm.databinding.FragmentTeamPerformanceBinding
import com.velectico.rbm.payment.models.PaymentInfo

/**
 * A simple [Fragment] subclass.
 */
class FragmentAddPaymentInfo :  BaseFragment() {


    private lateinit var binding: FragmentAddPaymentInfoBinding
    override fun getLayout(): Int {
        return R.layout.fragment_add_payment_info
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentAddPaymentInfoBinding

    }

}
