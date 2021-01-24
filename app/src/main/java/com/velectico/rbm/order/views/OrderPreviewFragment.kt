package com.velectico.rbm.order.views

import android.util.Log
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment

import com.velectico.rbm.database.CartProduct
import com.velectico.rbm.database.DB_Manager
import com.velectico.rbm.database.Helper_Cart_DB
import com.velectico.rbm.databinding.FragmentOrderPreviewBinding

import com.velectico.rbm.order.adapters.OrderPreviewListAdapter

import com.velectico.rbm.utils.DataConstant


class OrderPreviewFragment : BaseFragment() {

    private lateinit var binding: FragmentOrderPreviewBinding
    var orderCartList : ArrayList<CartProduct>?=ArrayList<CartProduct>()
    private lateinit var adapter: OrderPreviewListAdapter
    var db: Helper_Cart_DB? = null
    override fun getLayout(): Int {
        return R.layout.fragment_order_preview
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentOrderPreviewBinding

        db = Helper_Cart_DB(context)
        DB_Manager.initializeInstance(db)
        //showToastMessage(db!!.getCartItems().toString())
        orderCartList!!.clear()
        orderCartList!!.addAll(db!!.getCartItems())
        //Toast.makeText(context,"Size"+orderCartList!!.size,Toast.LENGTH_LONG).show()
        binding.tvGrandPrice.text="₹ "+DataConstant.grandTotalPrice

        binding.btnConfirm.setOnClickListener {
            val navDirection= OrderPreviewFragmentDirections.actionOrderPreviewFragmentToOrderCheckOutFragment()
            Navigation.findNavController(binding.btnConfirm).navigate(navDirection)
        }
        binding.btnEditOrder.setOnClickListener {
            val navDirection= OrderPreviewFragmentDirections.actionOrderPreviewFragmentToOrderEditFragment()
            Navigation.findNavController(binding.btnEditOrder).navigate(navDirection)
        }

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        adapter = OrderPreviewListAdapter(
            context,
            orderCartList
        )
        binding.rvCartList.adapter = adapter

    }



    var hud: KProgressHUD? = null
    fun  showHud(){
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show();
    }

    fun hide(){
        hud?.dismiss()
    }

}