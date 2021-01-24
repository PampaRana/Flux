package com.velectico.rbm.expense.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.LayoutExpenseDtlsBinding
import com.velectico.rbm.databinding.RowExpenseDetailsBinding
import com.velectico.rbm.expense.model.EetailsA
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class ExpenseDetailsAdapter : RecyclerView.Adapter<ExpenseDetailsAdapter.ViewHolder>()  {


    var expenDetailsList =  listOf<EetailsA>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: LayoutExpenseDtlsBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding


        fun bind(orderCart: EetailsA) {
            binding.expDtlsInfo = orderCart
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseDetailsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutExpenseDtlsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return expenDetailsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ExpenseDetailsAdapter.ViewHolder, position: Int) {
        holder.bind(expenDetailsList[position])
        if (expenDetailsList[position].expType=="Petrol expense") {
            holder.binding.tvKmValue.visibility=View.VISIBLE
            holder.binding.tvKmValue.text = expenDetailsList[position].km_run + "Km"
        }else{
            holder.binding.tvKmValue.visibility=View.GONE
        }

        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        val stdate =  DateUtils.parseDate(expenDetailsList[position].expDate,inpFormat,outputformat)
        holder.binding.tvDateValue.text = stdate



    }
}