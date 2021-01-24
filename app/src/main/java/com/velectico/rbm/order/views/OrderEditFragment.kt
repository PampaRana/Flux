package com.velectico.rbm.order.views

import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.database.CartProduct
import com.velectico.rbm.database.DB_Manager
import com.velectico.rbm.database.Helper_Cart_DB
import com.velectico.rbm.databinding.FragmentOrderEditBinding
import com.velectico.rbm.order.adapters.OrderEditListAdapter

class OrderEditFragment : BaseFragment() {
    private lateinit var binding: FragmentOrderEditBinding
    var orderCartList : ArrayList<CartProduct>?=ArrayList<CartProduct>()
    private lateinit var adapter: OrderEditListAdapter
    var db: Helper_Cart_DB? = null
    override fun getLayout(): Int {
        return R.layout.fragment_order_edit
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentOrderEditBinding

        db = Helper_Cart_DB(context)
        DB_Manager.initializeInstance(db)
        orderCartList!!.clear()
        orderCartList!!.addAll(db!!.getCartItems())
        //showToastMessage(orderCartList!!.size.toString())
        setUpRecyclerView()
        binding.btnConfirmPlace.setOnClickListener {
            if (db!!.getCartItems().size>0){
                val navDirection= OrderEditFragmentDirections.actionOrderEditFragmentToOrderCheckOutFragment()
                Navigation.findNavController(binding.btnConfirmPlace).navigate(navDirection)
            }

        }

    }

    private fun setUpRecyclerView() {
        adapter = OrderEditListAdapter(
            context,
            orderCartList,
            binding.tvProdId,
            binding.btnConfirmPlace
        )
        binding.rvCartList.adapter = adapter

    }


}