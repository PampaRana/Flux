package com.velectico.rbm.beats.adapters

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.beats.model.DealDistMechList
import com.velectico.rbm.beats.model.OnEditTextChanged
import com.velectico.rbm.beats.views.AssignTargetToBeat.Companion.orderDetailsBeatTaskList
import com.velectico.rbm.beats.views.AssignTargetToBeat.Companion.seletedItems
import com.velectico.rbm.databinding.RowAssignTargetToBeatBinding
import java.util.*


class BeatTargetAssignmentAdapter(
    val context: Context?,
    val beatList: List<DealDistMechList>
) : RecyclerView.Adapter<BeatTargetAssignmentAdapter.ViewHolder>() {

    var amount=""
    var pmtClctd=""
    var task=""
    //private val mCallback: UpdateClinicListener? = null
    private var onEditTextChanged: OnEditTextChanged? = null


    inner class ViewHolder(_binding: RowAssignTargetToBeatBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
        }

        fun bind(beats: DealDistMechList?) {
            binding.assignments = beats
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeatTargetAssignmentAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowAssignTargetToBeatBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return beatList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun getTaskData() : List<DealDistMechList>{
        return beatList
    }





    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: BeatTargetAssignmentAdapter.ViewHolder, position: Int) {
        holder.bind(beatList[position])
        if (beatList[position].UM_Role == "R"){
            holder.binding.tvType.text = "Dealer"
        }
        else if (beatList[position].UM_Role == "D"){
            holder.binding.tvType.text = "Distributor"
        }
        else if (beatList[position].UM_Role == "M"){
            holder.binding.tvType.text = "Mechanic"
        }

        holder.binding.etTask.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                beatList[position].task = s.toString();
            }
        })

        holder.binding.inputOrderAmt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                beatList[position].amount = s.toString();
            }
        })

        holder.binding.inputPmtClctd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                beatList[position].collectAmount = s.toString();
            }
        })
        holder.binding.checkBox2.setOnClickListener(){view->
            beatList[position].assignmentSelected = holder.binding.checkBox2.isChecked
            seletedItems.add(beatList[position])


            notifyDataSetChanged()
        }




    }








}

data class orderDetailsBeatTask (
    val orderAmount:String?,val paymentAmount:String?,val comments:String?

)