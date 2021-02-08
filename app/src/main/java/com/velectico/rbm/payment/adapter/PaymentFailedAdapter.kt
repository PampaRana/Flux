package com.velectico.rbm.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.PaymentConfirmViewBinding
import com.velectico.rbm.databinding.PaymentFailedViewBinding
import com.velectico.rbm.payment.models.PaymentConfirmDetails
import com.velectico.rbm.payment.models.PaymentFailedDetails
import com.velectico.rbm.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class PaymentFailedAdapter (var setCallback: PaymentFailedAdapter.IBeatDateListActionCallBack,
                            var context: Context
) : RecyclerView.Adapter<PaymentFailedAdapter.ViewHolder>() {

    var callBack : PaymentFailedAdapter.IBeatDateListActionCallBack?=null
    var paymentFailedList =  listOf<PaymentFailedDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: PaymentFailedViewBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
            callBack = setCallback;



        }

        @SuppressLint("SetTextI18n")
        fun bind(paymentFailedInfo:  PaymentFailedDetails?) {
            binding.paymentInfo = paymentFailedInfo

            /*val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
            val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
            val stdate =  DateUtils.parseDate(paymentFailedInfo!!.collectedDate,inpFormat,outputformat)
            binding.tvCollectDate.text = stdate*/

            binding.tvArea.text = paymentFailedInfo!!.Area_Name
            binding.tvDistrict.text = paymentFailedInfo.District_Name
            binding.tvZone.text = paymentFailedInfo.Zone_Name
            binding.tvRegion.text = paymentFailedInfo.Region_Name
            binding.tvCreditDays.text = paymentFailedInfo.Credit_Days
            binding.tvCreditLimit.text = paymentFailedInfo.Credit_Limit
            binding.tvOutstandingAmount.text = "₹ "+paymentFailedInfo.OutStandingAmount


            if (paymentFailedInfo.OH_Dealer_Name!=null){
                binding.layDealer.visibility= View.VISIBLE
                binding.tvDealerName.text = paymentFailedInfo.OH_Dealer_Name

            }else{
                binding.layDealer.visibility= View.GONE

            }

            if (paymentFailedInfo.OH_Distrib_Name!=null){
                binding.layDist.visibility= View.VISIBLE
                binding.tvDistName.text = paymentFailedInfo.OH_Distrib_Name

            }else{
                binding.layDist.visibility= View.GONE

            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PaymentFailedViewBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return paymentFailedList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(paymentFailedList[position])

    }
    interface IBeatDateListActionCallBack{


    }
}