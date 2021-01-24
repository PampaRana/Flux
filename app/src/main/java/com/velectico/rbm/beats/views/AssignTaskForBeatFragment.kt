package com.velectico.rbm.beats.views

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatAssignmentTaskListAdapter
import com.velectico.rbm.beats.model.BeatAssignments
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.beats.model.Dealer
import com.velectico.rbm.beats.viewmodel.BeatSharedViewModel
import com.velectico.rbm.databinding.FragmentAssignTaskForBeatBinding


/**
 * A simple [Fragment] subclass.
 */
class AssignTaskForBeatFragment : BaseFragment() {

    private var binding: FragmentAssignTaskForBeatBinding? = null
    private lateinit var mBeatSharedViewModel: BeatSharedViewModel
    private lateinit var beatAssignmentList: MutableList<BeatAssignments>;
    private lateinit var adapter: BeatAssignmentTaskListAdapter;
    private lateinit var mBeat: Beats
    private lateinit var mAssignments : BeatAssignments


    override fun getLayout(): Int {
        return R.layout.fragment_assign_task_for_beat
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentAssignTaskForBeatBinding
        mBeatSharedViewModel =
            ViewModelProviders.of(requireActivity()).get(BeatSharedViewModel::class.java)
       // beatAssignmentList = BeatAssignments().getBlankList() //mBeatSharedViewModel?.assignmentList?.value!!;
        setHasOptionsMenu(true);
        getIntentData()
        setUp();
    }

    override
    fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.beat_assig_menu, menu)
        super.onCreateOptionsMenu(menu , inflater)
    }


    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                beatAssignmentList.add(BeatAssignments())
                binding?.rvAssignmentList?.layoutManager?.scrollToPosition(beatAssignmentList.size-1)
                adapter.beatAssignmentList = beatAssignmentList
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getIntentData() {
      /*  val bundle = arguments
        if (bundle != null) {
            val args: AssignTaskForBeatFragmentArgs = AssignTaskForBeatFragmentArgs.fromBundle(bundle)
            if (args != null) {
                mBeat = args.beatInfo
                beatAssignmentList = BeatAssignments().getBlankList();
            }
        }*/
    }



    private fun setUpRecyclerView() {
        adapter = BeatAssignmentTaskListAdapter(Dealer().getDummyDealerList(),Dealer().getDummyDistributorList(),requireActivity(),mBeat.startDate,mBeat.endDate);
        binding?.rvAssignmentList?.adapter = adapter
        adapter?.beatAssignmentList = beatAssignmentList
    }

    private fun setUp() {
        setUpRecyclerView()
        binding?.btnCancel?.setOnClickListener {
            Navigation.findNavController(binding?.btnCancel as Button).popBackStack()
        }
        binding?.btnSave?.setOnClickListener {
            //Navigation.findNavController(binding?.btnSave as Button).popBackStack()
        }
    }
}
