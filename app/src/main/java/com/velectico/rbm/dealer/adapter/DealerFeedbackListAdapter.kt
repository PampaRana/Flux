package com.velectico.rbm.dealer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.DealerFeedbackLayoutBinding
import com.velectico.rbm.databinding.DealerListLayoutBinding
import com.velectico.rbm.dealer.model.DealerListDetails
import com.velectico.rbm.dealer.model.FeedbackDetails
import com.velectico.rbm.utils.DateUtils
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

    override fun onBindViewHolder(holder: DealerFeedbackListAdapter.ViewHolder, position: Int) {
        holder.bind(dealerFeedList[position])
        if (dealerFeedList[position].Reminder_Date!=null) {
            val inpFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US);
            val outputformat = SimpleDateFormat("dd-MMM-yy", Locale.US);
            val stdate =
                DateUtils.parseDate(dealerFeedList[position].Reminder_Date, inpFormat, outputformat)
            holder.binding.tvRemind.text = stdate
        }
    }
}