package com.velectico.rbm.order.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.RowOrderHeadListBinding
import com.velectico.rbm.databinding.RowOrderdetailListBinding
import com.velectico.rbm.databinding.RowPaymentListBinding
import com.velectico.rbm.order.model.OrderDetailInfo
import com.velectico.rbm.order.model.OrderHead
import com.velectico.rbm.payment.adapter.PaymentListAdapter
import com.velectico.rbm.payment.models.PaymentInfo

class OrderDetailsListAdapter : RecyclerView.Adapter<OrderDetailsListAdapter.ViewHolder>() {

    var orderList = listOf<OrderDetailInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowOrderdetailListBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
        }

        fun bind(orderHead: OrderDetailInfo?) {
            binding.orderDetailInfo = orderHead
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderDetailsListAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowOrderdetailListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderDetailsListAdapter.ViewHolder, position: Int) {
        holder.bind(orderList[position])
    }


}