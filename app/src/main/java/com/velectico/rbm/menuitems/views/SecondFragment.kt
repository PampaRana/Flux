package com.velectico.rbm.menuitems.views

import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.SecondFragmentBinding

/**
 * Created by mymacbookpro on 2020-04-26
 * TODO: Add a class header comment!
 */
class SecondFragment : BaseFragment(){
    private lateinit var binding: SecondFragmentBinding

    override fun getLayout(): Int = R.layout.second_fragment

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as SecondFragmentBinding

        printLogMessage(TAG, "init")
    }

    companion object{
        const val TAG = "SecondFragment"
    }
}