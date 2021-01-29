package com.velectico.rbm.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.R
import com.velectico.rbm.beats.model.OrderListDetails
import com.velectico.rbm.databinding.PaymentConfirmViewBinding
import com.velectico.rbm.databinding.RowOrderHeadListBinding
import com.velectico.rbm.order.adapters.OrderHeadListAdapter
import com.velectico.rbm.payment.models.PaymentConfirmDetails
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.SharedPreferencesClass
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class PaymentConfirmationAdapter( var setCallback: PaymentConfirmationAdapter.IBeatDateListActionCallBack,
var context: Context
) : RecyclerView.Adapter<PaymentConfirmationAdapter.ViewHolder>() {

    var callBack : PaymentConfirmationAdapter.IBeatDateListActionCallBack?=null
    var paymentConfirmList =  listOf<PaymentConfirmDetails>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    inner class ViewHolder(_binding: PaymentConfirmViewBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
            callBack = setCallback;



        }

        @SuppressLint("SetTextI18n")
        fun bind(paymentConfirmInfo:  PaymentConfirmDetails?) {
            binding.paymentInfo = paymentConfirmInfo

            val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
            val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
            val stdate =  DateUtils.parseDate(paymentConfirmInfo!!.collectedDate,inpFormat,outputformat)
            binding.tvCollectDate.text = stdate

            binding.tvInvoice.text = paymentConfirmInfo.SIH_Invoice_No

            binding.tvCollectAmount.text = "₹ "+paymentConfirmInfo. collectedAmt


            if (paymentConfirmInfo.dealName!=null){
               binding.layDealer.visibility=View.VISIBLE
               binding.tvDealerName.text = paymentConfirmInfo.dealName

            }else{
                binding.layDealer.visibility=View.GONE

            }

            if (paymentConfirmInfo.distribName!=null){
                binding.layDist.visibility=View.VISIBLE
                binding.tvDistName.text = paymentConfirmInfo.distribName

            }else{
                binding.layDist.visibility=View.GONE

            }

            if (paymentConfirmInfo.OH_Collected_Confirm_Status=="C"){
                binding.ivPending.visibility=View.GONE
                binding.icCheck.visibility=View.VISIBLE


            }else{
                binding.ivPending.visibility=View.VISIBLE
                binding.icCheck.visibility=View.GONE

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PaymentConfirmViewBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return paymentConfirmList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(paymentConfirmList[position])

    }
    interface IBeatDateListActionCallBack{


    }
}