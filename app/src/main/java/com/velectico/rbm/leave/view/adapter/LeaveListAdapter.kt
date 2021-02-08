package com.velectico.rbm.leave.view.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.R
import com.velectico.rbm.databinding.ItemLeaveListBinding
import com.velectico.rbm.leave.model.LeaveListModel
import com.velectico.rbm.utils.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by mymacbookpro on 2020-05-08
 * TODO: Add a class header comment!
 */
class LeaveListAdapter(private var list:List<LeaveListModel>, private val onClickListener: (View, Int, Int) -> Unit)
    : RecyclerView.Adapter<LeaveListAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemLeaveListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_leave_list,
            parent, false
        )
        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            bind(list.get(position))
            val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
            val outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
            if (list.get(position).leaveFrom!=null) {
                if (list.get(position).leaveFrom=="0000-00-00 00:00:00"){
                    holder.binding.leaveFromTv.text = ""

                }else {
                    val stdate =  DateUtils.parseDate(list.get(position).leaveFrom,inpFormat,outputformat)
                    holder.binding.leaveFromTv.text = stdate

                }
            }

            if (list.get(position).leaveTo!=null) {
                if (list.get(position).leaveTo=="0000-00-00 00:00:00"){
                    holder.binding.leaveToTv.text = ""

                }else {
                    val endate =  DateUtils.parseDate(list.get(position).leaveTo,inpFormat,outputformat)
                    holder.binding.leaveToTv.text = endate

                }
            }

            val output = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            val myFormat = SimpleDateFormat("yyyy-MM-dd")
            val inputString1 = list.get(position).leaveFrom
            val inputString2 = list.get(position).leaveTo

            try {
                val date1: Date = myFormat.parse(inputString1)
                val date2: Date = myFormat.parse(inputString2)
                val diff: Long = (date2.getTime() - date1.getTime())
                val days= TimeUnit.DAYS.convert(
                    diff,
                    TimeUnit.MILLISECONDS
                ).toInt()+1


                holder.binding.leaveDaysTv.text=days.toString()
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            /*val separated =
                list[position].leaveAppliedOn!!.split(" ".toRegex()).toTypedArray()
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
            }*/

            holder.binding.ivDelete.setOnClickListener(View.OnClickListener {
                onClickListener.invoke(holder.itemView, position,1) //delete
            })
            holder.itemView.setOnClickListener {
                onClickListener.invoke(holder.itemView, position,2) //edit
            }

            if (list.get(position).LD_Status=="A"){
                holder.binding.leaveStatusTv.text = "Approved"
                holder.binding.leaveStatusTv.setTextColor(Color.parseColor("#006600"))

            }else if (list.get(position).LD_Status=="R"){
                holder.binding.leaveStatusTv.setTextColor(Color.parseColor("#FF0000"))
                holder.binding.leaveStatusTv.text = "Rejected"

            }else {
                holder.binding.leaveStatusTv.text = "Open"
                holder.binding.leaveStatusTv.setTextColor(Color.parseColor("#FED615"))

            }
        }
    }

    class ViewHolder(val binding: ItemLeaveListBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(model: LeaveListModel){
            binding.leaveModel = model
            binding.executePendingBindings()
        }
    }
}