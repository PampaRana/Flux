package com.velectico.rbm.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.R
import com.velectico.rbm.payment.models.OutStandingInfoDetails
import com.velectico.rbm.payment.models.PaymentHistoryDetails
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PaymentHistoryAdapter (context: Context?, paymentOutStandList: List<PaymentHistoryDetails>) :
    RecyclerView.Adapter<PaymentHistoryAdapter.pdtViewHolder>() {
    var context: Context
    var paymentHistoryList: List<PaymentHistoryDetails>
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): pdtViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.payment_outstand_item_view, parent, false)
        return pdtViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(
        holder: pdtViewHolder,
        position: Int
    ) {

        holder.tv_invoice_no_value.text =paymentHistoryList.get(position).PH_Invoice_No
        try {
            val f: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val d: Date = f.parse(paymentHistoryList.get(position).payDate)
            val date: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val inVoiceDate = SimpleDateFormat("dd-MM-yyyy")
            val time: DateFormat = SimpleDateFormat("hh:mm:ss a")
            holder.tv_invoice_date_value.text = inVoiceDate.format(d)

            System.out.println("Date: " + date.format(d))
            System.out.println("Time: " + time.format(d))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        holder.tv_invoice_amount_value.text = "₹"+paymentHistoryList.get(position).tran_amount


    }

    override fun getItemCount(): Int {
        return paymentHistoryList.size
    }

    inner class pdtViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_invoice_no_value: TextView
        var tv_invoice_date_value: TextView
        var tv_invoice_amount_value: TextView



        init {
            tv_invoice_no_value = itemView.findViewById(R.id.tv_invoice_no_value)
            tv_invoice_date_value = itemView.findViewById(R.id.tv_invoice_date_value)
            tv_invoice_amount_value = itemView.findViewById(R.id.tv_invoice_amount_value)

        }
    }

    init {
        this.context = context!!
        this.paymentHistoryList = paymentOutStandList
    }
}