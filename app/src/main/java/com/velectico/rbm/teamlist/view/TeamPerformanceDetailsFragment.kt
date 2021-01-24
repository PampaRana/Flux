package com.velectico.rbm.teamlist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentTeamListBinding
import com.velectico.rbm.databinding.FragmentTeamPerformanceDetailsBinding
import com.velectico.rbm.databinding.RowTeamListBinding
import com.velectico.rbm.teamlist.adapter.TeamListAdapter
import com.velectico.rbm.teamlist.adapter.TeamPerformanceDetailsAdapter
import com.velectico.rbm.teamlist.adapter.TeamPerformanceListAdapter
import com.velectico.rbm.teamlist.model.TeamListModel
import com.velectico.rbm.teamlist.model.TeamPerformanceModel

/**
 * A simple [Fragment] subclass.
 */
class TeamPerformanceDetailsFragment : BaseFragment()  {

    private lateinit var binding: FragmentTeamPerformanceDetailsBinding
    private lateinit var teamList : List<TeamPerformanceModel>
    private lateinit var adapter: TeamPerformanceDetailsAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_team_performance_details
    }


    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentTeamPerformanceDetailsBinding
        teamList = TeamPerformanceModel().getDummyTeamPerformaceList()
        setUpRecyclerView()

    }


    private fun setUpRecyclerView() {
        adapter = TeamPerformanceDetailsAdapter();
        binding.rvTeamPerformanceList.adapter = adapter
        adapter.teamList = teamList
    }
}
