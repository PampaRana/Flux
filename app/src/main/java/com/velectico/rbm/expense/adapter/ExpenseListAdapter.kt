package com.velectico.rbm.expense.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.R
import com.velectico.rbm.databinding.RowExpenseListBinding
import com.velectico.rbm.expense.model.EetailsA
import com.velectico.rbm.expense.model.ExpenseDetails
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ExpenseListAdapter(var setCallback: IExpenseActionCallBack) : RecyclerView.Adapter<ExpenseListAdapter.ViewHolder>() {
     var callBack : IExpenseActionCallBack?=null
     var expenseList = listOf<ExpenseDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
   /* init {
        callBack = setCallback;
    }*/


    inner class ViewHolder(_binding: RowExpenseListBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
            callBack = setCallback;
            binding.card.setOnClickListener {
                callBack?.moveToExpenseDetails(adapterPosition, expenseList[position],binding )

            }
            binding.navigateToEdit.setOnClickListener {
                callBack?.moveToEditExpense(adapterPosition, expenseList[position],binding )

            }
        }

        fun bind(expense: ExpenseDetails?) {
            binding.expenseInfo = expense
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowExpenseListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(expenseList[position])
        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        val stdate =  DateUtils.parseDate(expenseList[position].expDate,inpFormat,outputformat)
        holder.binding.tvProdSchemeName.text = stdate

        val separated =
            expenseList[position].expDate!!.split(" ".toRegex()).toTypedArray()
        val code=separated[1]
        //Toast.makeText(context, code, Toast.LENGTH_LONG).show()
        val sdf = SimpleDateFormat("hh:mm:ss")
        val sdfs = SimpleDateFormat("hh:mm a")
        val dt: Date
        try {
            dt = sdf.parse(code)
            println("Time Display: " + sdfs.format(dt)) // <-- I got result here
            // Toast.makeText(context, sdfs.format(dt), Toast.LENGTH_LONG).show()
            holder. binding.tvTime.text = sdfs.format(dt)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (expenseList[position].expenseStatus == "O"){
            holder.binding.tvPackaging.text = "Open"
            holder.binding.tvPackaging.setTextColor(Color.parseColor("#FED615"))
        }else if (expenseList[position].expenseStatus == "A"){
            holder.binding.tvPackaging.text = "Approved"
            holder.binding.tvPackaging.setTextColor(Color.parseColor("#006600"))

        }
        else{
            holder.binding.tvPackaging.text = "Rejected"
            holder.binding.tvPackaging.setTextColor(Color.parseColor("#FF0000"))

        }
        if (expenseList[position].beatName!=null){
            holder.binding.tvBeatName.text=expenseList[position].beatName
        }
        /*if (expenseList[position].totalexpAmt!=null){
            holder.binding.tvTotalAmount.text="₹ "+expenseList[position].totalexpAmt
           // DataConstant.expenseTotalAmt=expenseList[position].totalexpAmt.toString()

        }else{
            holder.binding.tvTotalAmount.text="₹ 0"
            //DataConstant.expenseTotalAmt="0"

        }*/
        var amount=0.0
        for (i in expenseList[position].expenseDtls!!){
            amount+=i.expAmt!!.toDouble()
        }
        holder.binding.tvTotalAmount.text="₹ "+amount.toString()

    }

    interface IExpenseActionCallBack{
        fun moveToExpenseDetails(adapterPosition: Int, expenseDetails: ExpenseDetails, binding: RowExpenseListBinding)
        fun moveToEditExpense(adapterPosition: Int, expenseDetails: ExpenseDetails, binding: RowExpenseListBinding)
        /*fun onDelete(position:Int,expenseId:String?)
        fun onEdit(position:Int,expenseId:String?)*/
    }
}