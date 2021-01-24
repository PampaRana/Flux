package com.velectico.rbm.dealer.views

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentAddComplaintBinding
import com.velectico.rbm.databinding.FragmentDealerDashboardBinding
import com.velectico.rbm.menuitems.views.DefaultFragmentDirections
import com.velectico.rbm.teamlist.view.TeamDashboardDirections
import com.velectico.rbm.utils.SharedPreferenceUtils
import com.velectico.rbm.utils.SharedPreferencesClass

class DealerDashboardFragment : BaseFragment() {
    private lateinit var binding: FragmentDealerDashboardBinding
    var userId=""

    override fun getLayout(): Int {
        return R.layout.fragment_dealer_dashboard
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentDealerDashboardBinding
        userId= SharedPreferenceUtils.getLoggedInUserId(context as Context)
        binding.viewPerformanceButton.setOnClickListener {
            val navDirection =  DealerDashboardFragmentDirections.actionDealerDashboardFragmentToDealerPerformanceFragment()
            Navigation.findNavController(binding.viewPerformanceButton).navigate(navDirection)
        }
        binding.addDealerButton.setOnClickListener {
            val navDirection =  DealerDashboardFragmentDirections.actionDealerDashboardFragmentToAddDealerFragment()
            Navigation.findNavController(binding.addDealerButton).navigate(navDirection)
        }

        binding.dealerListButton.setOnClickListener {
            val navDirection=DealerDashboardFragmentDirections.actionDealerDashboardFragmentToDealerListFragment()
            Navigation.findNavController(binding.dealerListButton).navigate(navDirection)

        }
    }


}