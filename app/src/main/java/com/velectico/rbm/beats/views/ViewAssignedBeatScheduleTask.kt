package com.velectico.rbm.beats.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatTargetAssignmentAdapter
import com.velectico.rbm.beats.adapters.ViewAssignedBeatTaskAdapter
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.databinding.FragmentAssignTargetToBeatBinding
import com.velectico.rbm.databinding.FragmentViewAssignedBeatScheduleTaskBinding
import kotlinx.android.synthetic.main.fragment_view_assigned_beat_schedule_task.*

/**
 * A simple [Fragment] subclass.
 */
class ViewAssignedBeatScheduleTask : BaseFragment() {

    private lateinit var binding: FragmentViewAssignedBeatScheduleTaskBinding;
    private lateinit var beatList : List<Beats>
    private lateinit var adapter: ViewAssignedBeatTaskAdapter
    override fun getLayout(): Int {
        return R.layout.fragment_view_assigned_beat_schedule_task
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentViewAssignedBeatScheduleTaskBinding
        beatList = Beats().getDummyBeatList()
        setUpRecyclerView()
    }


    private fun setUpRecyclerView() {
        adapter = ViewAssignedBeatTaskAdapter(requireActivity())
        binding.rvTaskList.adapter = adapter
        // adapter.beatList = beatList
    }

}
