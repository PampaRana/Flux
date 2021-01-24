package com.velectico.rbm.beats.adapters

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.R
import com.velectico.rbm.beats.model.BeatAssignments
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.beats.model.TaskDetails
import com.velectico.rbm.beats.views.BeatListFragmentDirections
import com.velectico.rbm.databinding.RowAssignmentListBinding
import com.velectico.rbm.databinding.RowBeatListBinding
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.expense.adapter.ExpenseListAdapter
import com.velectico.rbm.utils.DateUtility
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.GloblalDataRepository
import java.text.SimpleDateFormat
import java.util.*

class BeatListAdapter(var setCallback: BeatListAdapter.IBeatListActionCallBack,val visit:String) : RecyclerView.Adapter<BeatListAdapter.ViewHolder>() {

    var callBack : BeatListAdapter.IBeatListActionCallBack?=null
    var beatList = listOf<TaskDetails>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    inner class ViewHolder(_binding: RowBeatListBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {

            callBack = setCallback;

      }

       fun bind(beats: TaskDetails?) {
            binding.beats = beats
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowBeatListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return beatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.bind(beatList[position])
            val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
            val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
            val stdate =  DateUtils.parseDate(beatList[position].schedule_startDate,inpFormat,outputformat)
            val endate =  DateUtils.parseDate(beatList[position].schedule_endDate,inpFormat,outputformat)
            holder.binding.datetxt.text = stdate + " To " + endate
           holder.binding.card.setOnClickListener {
                GloblalDataRepository.getInstance().scheduleId = beatList[position].schedule_id
                callBack?.moveToBeatTaskDetails(position, "1",holder.binding )
            }
            if (beatList[position].BM_Resp_Level=="R") {
                holder.binding.tvBeatLevel.text ="Region"
            }else  if (beatList[position].BM_Resp_Level=="D") {
                holder.binding.tvBeatLevel.text ="District"
            }else  if (beatList[position].BM_Resp_Level=="A") {
                holder.binding.tvBeatLevel.text ="Area"
            }else{
                holder.binding.tvBeatLevel.text ="Zone"

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    interface IBeatListActionCallBack{
        fun moveToBeatTaskDetails(position:Int,beatTaskId:String?,binding: RowBeatListBinding)

    }





}
