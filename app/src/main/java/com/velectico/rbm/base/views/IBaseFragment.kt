package com.velectico.rbm.base.views

import androidx.databinding.ViewDataBinding

/**
 * Created by mymacbookpro on 2020-04-26
 * interface for the layout and initialise methods
 */
interface IBaseFragment {
    fun getLayout():Int
    fun init(binding: ViewDataBinding)
}