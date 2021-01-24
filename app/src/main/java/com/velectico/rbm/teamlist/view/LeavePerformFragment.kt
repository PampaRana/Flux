package com.velectico.rbm.teamlist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentLeavePerformBinding
import com.velectico.rbm.databinding.FragmentSalesOrderBinding

class LeavePerformFragment : BaseFragment() {
    private lateinit var binding: FragmentLeavePerformBinding

    override fun getLayout(): Int {
        return R.layout.fragment_leave_perform
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentLeavePerformBinding
        binding.tvLeaveValue.text=arguments!!.getString("leave_taken")

    }


}