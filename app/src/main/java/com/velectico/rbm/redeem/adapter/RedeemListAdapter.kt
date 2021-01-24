package com.velectico.rbm.redeem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.RowRedeemListBinding
import com.velectico.rbm.redeem.model.RedeemInfo
import com.velectico.rbm.redeem.model.RedeemListDetails
import com.velectico.rbm.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class RedeemListAdapter (var setCallback: RedeemListAdapter.IBeatListActionCallBack) : RecyclerView.Adapter<RedeemListAdapter.ViewHolder>() {

    var callBack: RedeemListAdapter.IBeatListActionCallBack? = null
    var beatList = listOf<RedeemListDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowRedeemListBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {

            callBack = setCallback;
//            binding.navigateToTaskDetails.setOnClickListener {
//                callBack?.moveToBeatTaskDetails(adapterPosition, "1", binding)
//            }
        }

        fun bind(beats: RedeemListDetails?) {
            binding.redeemlist = beats
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowRedeemListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return beatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beatList[position])
        if (beatList[position].QRD_Status == "P"){
            holder.binding.tvPaymentAmount.text = "Pending"
        }
        else{
            holder.binding.tvPaymentAmount.text = "Approved"
        }
        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        if (beatList[position].QRD_Payment_Date != null){
            val pmtDt =  DateUtils.parseDate(beatList[position].QRD_Payment_Date,inpFormat,outputformat)
            holder.binding.tvDueAmount.text = pmtDt
        }
        else{
            holder.binding.tvDueAmount.text = "--"
        }

        val redmDt =  DateUtils.parseDate(beatList[position].QRD_Req_Date,inpFormat,outputformat)

        holder.binding.tvInvoice.text = redmDt
    }


    interface IBeatListActionCallBack {
        fun moveToBeatTaskDetails(
            position: Int,
            beatTaskId: String?,
            binding: RowRedeemListBinding
        )

    }
}
