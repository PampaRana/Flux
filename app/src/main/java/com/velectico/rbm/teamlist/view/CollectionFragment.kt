package com.velectico.rbm.teamlist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentCollectionBinding
import com.velectico.rbm.databinding.FragmentSalesOrderBinding
import com.velectico.rbm.databinding.FragmentTeamPerformanceBinding


class CollectionFragment : BaseFragment() {
    private lateinit var binding: FragmentSalesOrderBinding

    override fun getLayout(): Int {
        return R.layout.fragment_sales_order
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentSalesOrderBinding
        binding.tvMonthlyTargetValue.text=arguments!!.getString("collection_monthly_order_target")
        binding.tvMonthlyTargetAchieveValue.text=arguments!!.getString("collection_monthly_order_target_achieve")
        binding.tvQuarterlyTargetValue.text=arguments!!.getString("collection_quarterly_order_target")
        binding.tvQuarterlyTargetAchieveValue.text=arguments!!.getString("collection_quarterly_order_target_achieve")
        binding.tvHlyTargetValue.text=arguments!!.getString("collection_hly_order_target")
        binding.tvHlyTargetAchieveValue.text=arguments!!.getString("collection_hly_order_target_achieve")
        binding.tvAnnuallyTargetValue.text=arguments!!.getString("collection_annually_order_target")
        binding.tvAnnuallyTargetAchieveValue.text=arguments!!.getString("collection_annually_order_target_achieve")
    }


}