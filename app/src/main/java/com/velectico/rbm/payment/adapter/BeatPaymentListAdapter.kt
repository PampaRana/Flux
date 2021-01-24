package com.velectico.rbm.payment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.RowBeatPaymentListBinding
import com.velectico.rbm.databinding.RowPaymentListBinding
import com.velectico.rbm.payment.models.PaymentInfo

class BeatPaymentListAdapter : RecyclerView.Adapter<BeatPaymentListAdapter.ViewHolder>() {

    var beatList = listOf<PaymentInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowBeatPaymentListBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {


        }

        fun bind(beats: PaymentInfo?) {
            binding.paymentInfo = beats
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowBeatPaymentListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return beatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beatList[position])
    }



}