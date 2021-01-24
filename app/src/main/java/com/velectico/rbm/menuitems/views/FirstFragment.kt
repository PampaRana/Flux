package com.velectico.rbm.menuitems.views

import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FirstFragmentBinding

/**
 * Created by mymacbookpro on 2020-04-26
 * TODO: Add a class header comment!
 */
class FirstFragment : BaseFragment(){
    private lateinit var binding: FirstFragmentBinding

    override fun getLayout(): Int = R.layout.first_fragment

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FirstFragmentBinding

        printLogMessage(TAG, "init")
    }

    companion object{
        const val TAG = "FirstFragment"
    }
}