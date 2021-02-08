package com.velectico.rbm.reminder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.RowReminderListBinding
import com.velectico.rbm.reminder.model.ReminderList
import com.velectico.rbm.reminder.model.ReminderListDetails
import com.velectico.rbm.utils.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReminderListAdapter (var setCallback: ReminderListAdapter.IBeatListActionCallBack) : RecyclerView.Adapter<ReminderListAdapter.ViewHolder>() {

    var callBack: ReminderListAdapter.IBeatListActionCallBack? = null
    var beatList = listOf<ReminderListDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowReminderListBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {

            callBack = setCallback;
            binding.navigateToTaskDetails.setOnClickListener {
                callBack?.moveToBeatTaskDetails(adapterPosition, "1", binding)
            }
        }

        fun bind(beats: ReminderListDetails?) {
            binding.reminderlist = beats
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowReminderListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return beatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beatList[position])

        /*if (beatList[position].dummyDealerName != null ){
            holder.binding.llDeal.visibility=View.VISIBLE
            holder.binding.llDist.visibility=View.GONE
            holder.binding.tvDealerName.text = beatList[position].dummyDealerName
        }else*/ if( beatList[position].dummyDealerName!=""){
            holder.binding.llDeal.visibility=View.VISIBLE
            holder.binding.llDist.visibility=View.GONE
            holder.binding.tvDealerName.text = beatList[position].dummyDealerName
        } else{
            if (beatList[position].dealName != null){
                holder.binding.llDeal.visibility=View.VISIBLE
                holder.binding.llDist.visibility=View.GONE
                holder.binding.tvDealerName.text = beatList[position].dealName
            }else if (beatList[position].dealName != ""){
                holder.binding.llDeal.visibility=View.VISIBLE
                holder.binding.llDist.visibility=View.GONE
                holder.binding.tvDealerName.text = beatList[position].dealName
            }else  if (beatList[position].distribName != null){
                holder.binding.llDist.visibility=View.VISIBLE
                holder.binding.llDeal.visibility=View.GONE
                holder.binding.tvDistName.text = beatList[position].distribName
            }else if (beatList[position].distribName != ""){
                holder.binding.llDist.visibility=View.VISIBLE
                holder.binding.llDeal.visibility=View.GONE
                holder.binding.tvDistName.text = beatList[position].distribName
            }

        }
        if (beatList[position].beatName != null || beatList[position].beatName!=""){
            holder.binding.tvBeatName.text = beatList[position].beatName

        }
        if (beatList[position].RM_Followup_Date!= null){
            val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
            val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);

                if (beatList.get(position).RM_Followup_Date=="0000-00-00 00:00:00"){
                    holder.binding.tvDate.text = ""

                }else {
                    val stdate =  DateUtils.parseDate(beatList[position].RM_Followup_Date,inpFormat,outputformat)
                    holder.binding.tvDate.text = stdate
                    val separated =
                        beatList[position].RM_Followup_Date!!.split(" ".toRegex()).toTypedArray()
                    val code=separated[1]
                    //Toast.makeText(context, code, Toast.LENGTH_LONG).show()
                    val sdf = SimpleDateFormat("HH:mm:ss")
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

                }



        }


    }


    interface IBeatListActionCallBack {
        fun moveToBeatTaskDetails(
            position: Int,
            beatTaskId: String?,
            binding: RowReminderListBinding
        )

    }
}
