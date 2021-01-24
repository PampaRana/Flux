package com.velectico.rbm.teamlist.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.beats.model.BeatDate
import com.velectico.rbm.beats.views.DateWiseBeatListFragmentDirections
import com.velectico.rbm.databinding.FragmentDateWiseBeatListBinding
import com.velectico.rbm.databinding.FragmentTeamListPerformanceListBinding
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.teamlist.adapter.TeamPerformanceListAdapter
import com.velectico.rbm.teamlist.model.TeamPerformanceModel

/**
 * A simple [Fragment] subclass.
 */
class TeamListPerformanceList : BaseFragment()  {

    private lateinit var binding: FragmentTeamListPerformanceListBinding
    private lateinit var teamPerformanceList : List<TeamPerformanceModel>
    private lateinit var adapter: TeamPerformanceListAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_team_list_performance_list
    }


    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentTeamListPerformanceListBinding
        teamPerformanceList = TeamPerformanceModel().getDummyTeamPerformaceList()
        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        adapter = TeamPerformanceListAdapter();
        binding.rvTeamPerformanceList.adapter = adapter
        adapter.teamList = teamPerformanceList
    }

}
