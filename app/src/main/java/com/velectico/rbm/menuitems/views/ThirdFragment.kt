package com.velectico.rbm.menuitems.views

import androidx.databinding.ViewDataBinding
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.R
import com.velectico.rbm.databinding.ThirdFragmentBinding

/**
 * Created by mymacbookpro on 2020-04-26
 * TODO: Add a class header comment!
 */
class ThirdFragment : BaseFragment(){
    private lateinit var binding: ThirdFragmentBinding

    override fun getLayout(): Int = R.layout.third_fragment

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as ThirdFragmentBinding

        printLogMessage(TAG, "init")
    }

    companion object{
        const val TAG = "ThirdFragment"
    }
}