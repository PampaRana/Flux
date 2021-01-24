package com.velectico.rbm.teamlist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentExpensePerformBinding
import com.velectico.rbm.databinding.FragmentSalesOrderBinding


class ExpensePerformFragment : BaseFragment() {
    private lateinit var binding: FragmentExpensePerformBinding

    override fun getLayout(): Int {
        return R.layout.fragment_expense_perform
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentExpensePerformBinding
        binding.tvTotalExpenseValue.text=arguments!!.getString("total_expense")
        binding.tvTaskValue.text=arguments!!.getString("total_task")


    }

    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense_perform, container, false)
    }*/


}