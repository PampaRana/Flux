package com.velectico.rbm.payment.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentPaymentConfirmationBinding
import com.velectico.rbm.databinding.FragmentPaymentDashboardBinding
import com.velectico.rbm.databinding.FragmentPaymentFailedBinding
import com.velectico.rbm.teamlist.view.TeamDashboardDirections


class PaymentDashboardFragment : BaseFragment() {
    private lateinit var binding: FragmentPaymentDashboardBinding;

    override fun getLayout(): Int {
        return R.layout.fragment_payment_dashboard
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentPaymentDashboardBinding
        binding.viewPaymentConfirmButton.setOnClickListener {
            val navDirection =  PaymentDashboardFragmentDirections.actionPaymentDashboardFragmentToTeamListFragment(binding.viewPaymentConfirmButton.text.toString())
            Navigation.findNavController(binding.failedReportButton).navigate(navDirection)
        }

        binding.failedReportButton.setOnClickListener{

            val navDirection =  PaymentDashboardFragmentDirections.actionPaymentDashboardFragmentToTeamListFragment(binding.failedReportButton.text.toString())
            Navigation.findNavController(binding.failedReportButton).navigate(navDirection)
        }
    }

}