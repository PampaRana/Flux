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
import com.velectico.rbm.payment.models.PaymentConfirmDetails
import java.text.ParseException
import java.text.SimpleDateFormat


class PaymentConfirmationAdapter (context: Context?, paymentConfirmList: List<PaymentConfirmDetails>) :
    RecyclerView.Adapter<PaymentConfirmationAdapter.pdtViewHolder>() {
    var context: Context
    var paymentConfirmList: List<PaymentConfirmDetails>
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): pdtViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.payment_confirm_view, parent, false)
        return pdtViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(
        holder: pdtViewHolder,
        position: Int
    ) {


        @SuppressLint("SimpleDateFormat") val input =
            SimpleDateFormat("yyyy-MM-dd")
        @SuppressLint("SimpleDateFormat") val output =
            SimpleDateFormat("dd-MM-yyyy")
        try {
            val collectDate =
                input.parse(paymentConfirmList[position].collectedDate)!! // parse input
            holder.tv_collect_date.setText(output.format(collectDate)) // format output

            //holder.tv_date.setText(output.format(teerDate));// format output
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        holder.tv_collect_amount.setText("₹ "+paymentConfirmList[position].collectedAmt)


    }

    override fun getItemCount(): Int {
        return paymentConfirmList.size
    }

    inner class pdtViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_collect_date: TextView
        var tv_collect_amount: TextView



        init {
            tv_collect_date = itemView.findViewById(R.id.tv_collect_date)
            tv_collect_amount = itemView.findViewById(R.id.tv_collect_amount)

        }
    }

    init {
        this.context = context!!
        this.paymentConfirmList = paymentConfirmList
    }




}