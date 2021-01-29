package com.velectico.rbm.payment.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentPaymentConfirmationBinding
import com.velectico.rbm.databinding.FragmentPaymentFailedBinding

class PaymentFailedFragment : BaseFragment() {
    private lateinit var binding: FragmentPaymentFailedBinding;

    override fun getLayout(): Int {
        return R.layout.fragment_payment_failed
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentPaymentFailedBinding

    }

}