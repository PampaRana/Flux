package com.velectico.rbm.dealer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.DealerFeedbackLayoutBinding
import com.velectico.rbm.databinding.DealerListLayoutBinding
import com.velectico.rbm.dealer.model.DealerListDetails
import com.velectico.rbm.dealer.model.FeedbackDetails
import com.velectico.rbm.utils.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DealerFeedbackListAdapter(
    context: Context,
    dealerFeedList: MutableList<FeedbackDetails>
) : RecyclerView.Adapter<DealerFeedbackListAdapter.ViewHolder>(){
    var dealerFeedList = listOf<FeedbackDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(_binding: DealerFeedbackLayoutBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {

        }

        fun bind(cm: FeedbackDetails?) {
            binding.feedbackInfo = cm
            binding.executePendingBindings()
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DealerFeedbackListAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding=DealerFeedbackLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
       return dealerFeedList.size
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: DealerFeedbackListAdapter.ViewHolder, position: Int) {
        holder.bind(dealerFeedList[position])
        if (dealerFeedList[position].Reminder_Date!=null) {
            if (dealerFeedList[position].Reminder_Date=="0000-00-00 00:00:00"){
                holder.binding.tvRemind.text = ""

            }else {
                val inpFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US);
                val outputformat = SimpleDateFormat("dd-MMM-yy", Locale.US);
                val stdate =
                    DateUtils.parseDate(
                        dealerFeedList[position].Reminder_Date,
                        inpFormat,
                        outputformat
                    )
                holder.binding.tvRemind.text = stdate
            }
        }
        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        val separated =
            dealerFeedList[position].Create_Date!!.split(" ".toRegex()).toTypedArray()
        val code=separated[1]
        //Toast.makeText(context, code, Toast.LENGTH_LONG).show()
        val sdf = SimpleDateFormat("hh:mm:ss")
        val sdfs = SimpleDateFormat("hh:mm a")
        val dt: Date
        try {
            dt = sdf.parse(code)
            // println("Time Display: " + sdfs.format(dt)) // <-- I got result here
            // Toast.makeText(context, sdfs.format(dt), Toast.LENGTH_LONG).show()
            holder. binding.tvTime.text = sdfs.format(dt)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val stdate =  DateUtils.parseDate(dealerFeedList[position].Create_Date,inpFormat,outputformat)
        holder. binding.tvDate.text = stdate
        if (dealerFeedList[position].Created_By_User!=null){
            holder.binding.tvCreatedBy.text=dealerFeedList[position].Created_By_User
        }else{
            holder.binding.tvCreatedBy.text=""

        }
    }
}