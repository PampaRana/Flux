package com.velectico.rbm.beats.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.TypedArrayUtils
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.R
import com.velectico.rbm.beats.model.BeatDate
import com.velectico.rbm.beats.model.BeatDateDetails
import com.velectico.rbm.beats.model.ScheduleDates
import com.velectico.rbm.databinding.FragmentDateWiseBeatListBinding
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.utils.DateUtils
import kotlinx.android.synthetic.main.row_beat_list_dates.view.*
import java.text.SimpleDateFormat
import java.util.*

class BeatDateListAdapter(var setCallback: BeatDateListAdapter.IBeatDateListActionCallBack,var data:List<BeatDateDetails>
) : RecyclerView.Adapter<BeatDateListAdapter.ViewHolder>() {
    var callBack : BeatDateListAdapter.IBeatDateListActionCallBack?=null
    var beatList = listOf<BeatDateDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(_binding: RowBeatListDatesBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding
        init {
            callBack = setCallback;

            if(adapterPosition ==2){
               // binding.beatDateRow.setBackgroundColor(Color.RED)
            }


        }

        fun bind(beatDate: BeatDateDetails?) {
            binding.beatListDateInfo = beatDate
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowBeatListDatesBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if(position == 3 || position == 4 || position == 5 ){
            holder.binding.beatDateRow.setBackgroundColor(Color.parseColor("#ccffdd"))
        }else if(position == 0 || position == 1 || position == 2){
            holder.binding.beatDateRow.setBackgroundColor(Color.parseColor("#ffffe6"))
        }else {
            holder.binding.beatDateRow.setBackgroundColor(Color.parseColor("#ccd9ff"))
            holder.binding.navigateToDetails.tag = position
        }
        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        val btDate =  DateUtils.parseDate(data[position].date,inpFormat,outputformat)
        holder.binding.secondLine.text=btDate

            holder.bind(data[position])
        if(position == 3 ){
            holder.binding.secondLine.text = DateUtils.parseDate(data[3].date,inpFormat,outputformat)+",Yesterday"
        }
        if(position == 4 ){
            holder.binding.secondLine.text = DateUtils.parseDate(data[4].date,inpFormat,outputformat)+",Today"
        }
        if(position == 5 ){
            holder.binding.secondLine.text = DateUtils.parseDate(data[5].date,inpFormat,outputformat)+",Tomorrow"

        }

        if (data[position].status=="incomplete"){

            holder.binding.ivStatus.visibility=View.VISIBLE
            holder.binding.ivStatus.setImageResource(R.drawable.ic_incomplete)

        }else if (data[position].status=="complete"){

            holder.binding.ivStatus.visibility=View.VISIBLE
            holder.binding.ivStatus.setImageResource(R.drawable.ic_complete)

        }else{
            holder.binding.ivStatus.visibility=View.GONE
        }

        holder.binding.beatDateRow.setOnClickListener {
            callBack?.moveToBeatDetails(position, data[position].date,holder.binding )

        }
    }

    interface IBeatDateListActionCallBack{
        fun moveToBeatDetails(position:Int,beatTaskId:String?,binding: RowBeatListDatesBinding)

    }






}