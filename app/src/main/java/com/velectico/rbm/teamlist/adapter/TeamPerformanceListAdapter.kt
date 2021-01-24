package com.velectico.rbm.teamlist.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.RowTeamListBinding
import com.velectico.rbm.databinding.RowTeamPerformanceListBinding
import com.velectico.rbm.teamlist.model.TeamListModel
import com.velectico.rbm.teamlist.model.TeamPerformanceModel

class TeamPerformanceListAdapter () : RecyclerView.Adapter<TeamPerformanceListAdapter.ViewHolder>() {


    var teamList = listOf<TeamPerformanceModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(_binding: RowTeamPerformanceListBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding
        init {

        }

        fun bind(teamPerformanceModel: TeamPerformanceModel) {
            binding.performanceHistoryInfo = teamPerformanceModel
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowTeamPerformanceListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return teamList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {





        holder.bind(teamList[position])
    }





}