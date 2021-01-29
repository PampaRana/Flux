package com.velectico.rbm.teamlist.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.databinding.RowTeamListBinding
import com.velectico.rbm.teamlist.model.TeamListDetails
import com.velectico.rbm.teamlist.model.TeamListModel

class TeamListAdapter (var setCallback: TeamListAdapter.ITeamListActionCallBack) : RecyclerView.Adapter<TeamListAdapter.ViewHolder>() {
    var callBack : TeamListAdapter.ITeamListActionCallBack?=null
    var teamList = listOf<TeamListDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(_binding: RowTeamListBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding
        init {
            callBack = setCallback;


            binding.beatDateRow.setOnClickListener {
                callBack?.moveToBeatDetails(adapterPosition, "1",binding )

            }
        }

        fun bind(teamListModel: TeamListDetails?) {
            binding.teamListInfo = teamListModel
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowTeamListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return teamList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {





        holder.bind(teamList[position])
    }

    interface ITeamListActionCallBack{
        fun moveToBeatDetails(position:Int,beatTaskId:String?,binding: RowTeamListBinding)

    }






}