package com.velectico.rbm.payment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.databinding.RowPaymentListBinding
import com.velectico.rbm.payment.models.PaymentInfo
import com.velectico.rbm.payment.adapter.PaymentListAdapter

class PaymentListAdapter (var setCallback: PaymentListAdapter.IBeatListActionCallBack) : RecyclerView.Adapter<PaymentListAdapter.ViewHolder>() {

    var callBack: PaymentListAdapter.IBeatListActionCallBack? = null
    var beatList = listOf<PaymentInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowPaymentListBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {

            callBack = setCallback;
            binding.navigateToTaskDetails.setOnClickListener {
                callBack?.moveToPaymentDetails(adapterPosition, "1", binding)
            }
        }

        fun bind(beats: PaymentInfo?) {
            binding.paymentInfo = beats
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowPaymentListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return beatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beatList[position])
    }


    interface IBeatListActionCallBack {
        fun moveToPaymentDetails(
            position: Int,
            beatTaskId: String?,
            binding: RowPaymentListBinding
        )

    }



}