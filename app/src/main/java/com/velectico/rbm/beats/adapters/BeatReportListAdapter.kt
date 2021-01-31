package com.velectico.rbm.beats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.beats.model.BeatReport
import com.velectico.rbm.beats.model.BeatReportListDetails
import com.velectico.rbm.databinding.RowBeatReportListBinding
import com.velectico.rbm.databinding.RowTeamPerformanceListBinding
import com.velectico.rbm.teamlist.adapter.TeamPerformanceDetailsAdapter
import com.velectico.rbm.teamlist.model.TeamPerformanceModel
import com.velectico.rbm.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class BeatReportListAdapter : RecyclerView.Adapter<BeatReportListAdapter.ViewHolder>() {


    var teamList = listOf<BeatReportListDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowBeatReportListBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {

        }

        fun bind(beatReport: BeatReportListDetails?) {
            binding.beatReportListInfo = beatReport
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowBeatReportListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return teamList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.bind(teamList[position])
        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        if (teamList[position].SR_Follow_Up_Date!=null) {
            if (teamList[position].SR_Follow_Up_Date=="0000-00-00 00:00:00"){
                holder.binding.tvFollowDate.text = ""

            }else {
                val followDate =
                    DateUtils.parseDate(
                        teamList[position].SR_Follow_Up_Date,
                        inpFormat,
                        outputformat
                    )
                holder.binding.tvFollowDate.text = followDate
            }
        }
        val reportDate =  DateUtils.parseDate(teamList[position].reportDate,inpFormat,outputformat)
        holder.binding.tvReportDate.text = reportDate

        if (teamList[position].BSD_Collection_Target != null) {
            holder.binding.tvCollectionAmount.text = "₹ " + teamList[position].BSD_Collection_Target

        } else if (teamList[position].BSD_Collection_Target == "") {
            holder.binding.tvCollectionAmount.text = "₹ 0"


        } else {
            holder.binding.tvCollectionAmount.text = "₹ 0"

        }
        if (teamList[position].collectionSortFall != null) {
            holder.binding.tvCollectionSrtfall.text = "₹ " + teamList[position].collectionSortFall


        } else if (teamList[position].collectionSortFall == "") {
            holder.binding.tvCollectionSrtfall.text = "₹ 0"


        } else {
            holder.binding.tvCollectionSrtfall.text = "₹ 0"
        }
        if (teamList[position].BSD_Order_Target != null) {
            holder.binding.tvOrderAmt.text = "₹ " + teamList[position].BSD_Order_Target

        } else if (teamList[position].BSD_Order_Target == "") {
            holder.binding.tvOrderAmt.text = "₹ 0"


        } else {
            holder.binding.tvOrderAmt.text = "₹ 0"

        }

        if (teamList[position].orderSortFall != null) {
            holder.binding.tvOrderSrtfall.text = "₹ " + teamList[position].orderSortFall

        } else if (teamList[position].orderSortFall == "") {
            holder.binding.tvOrderSrtfall.text = "₹ 0"


        } else {
            holder.binding.tvOrderSrtfall.text = "₹ 0"
        }
    }
}
