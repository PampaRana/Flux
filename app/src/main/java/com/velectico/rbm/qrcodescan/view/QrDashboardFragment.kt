package com.velectico.rbm.qrcodescan.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentDealerDashboardBinding
import com.velectico.rbm.databinding.FragmentQrDashboardBinding
import com.velectico.rbm.dealer.views.DealerDashboardFragmentDirections


class QrDashboardFragment : BaseFragment() {
    private lateinit var binding: FragmentQrDashboardBinding

    override fun getLayout(): Int {
        return R.layout.fragment_qr_dashboard
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentQrDashboardBinding
        binding.instantScanButton.setOnClickListener {
            val navDirection =  QrDashboardFragmentDirections.actionQrDashboardFragmentToQrcodeScanner()
            Navigation.findNavController(binding.instantScanButton).navigate(navDirection)
        }
        binding.manualScanButton.setOnClickListener {
            val navDirection =  QrDashboardFragmentDirections.actionQrDashboardFragmentToManualScanFragment()
            Navigation.findNavController(binding.manualScanButton).navigate(navDirection)
        }


    }


}