package com.velectico.rbm.beats.adapters

import android.app.Activity
import android.app.DatePickerDialog.OnDateSetListener
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.R
import com.velectico.rbm.beats.model.BeatAssignments
import com.velectico.rbm.databinding.RowAssignmentListBinding
import com.velectico.rbm.utils.DateUtility
import com.velectico.rbm.utils.DateUtility.showDatePickerDialog
import java.util.*

class BeatAssignmentTaskListAdapter(activity: Activity,minDate: Long?,maxDate: Long?) :
    RecyclerView.Adapter<BeatAssignmentTaskListAdapter.ViewHolder>() {
    val activity : Activity = activity
    val minDate: Long? = minDate
    val maxDate: Long? = maxDate
    var dealerList: ArrayList<String>?=null;
    var distributorList: ArrayList<String>?=null;
    private var cuurentDatePicketParentView : com.google.android.material.textfield.TextInputEditText? = null;

    public var beatAssignmentList = mutableListOf<BeatAssignments>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    constructor(dealerList:ArrayList<String>?,distributorList: ArrayList<String>?,activity: Activity,minDate: Long?,maxDate: Long?) : this(activity,minDate,maxDate) {
        this.dealerList= dealerList
        this.distributorList = distributorList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowAssignmentListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return beatAssignmentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beatAssignmentList[position])
    }

    //https://stackoverflow.com/questions/45418194/i-cant-reach-any-class-member-from-a-nested-class-in-kotlin
    inner class ViewHolder(_binding: RowAssignmentListBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
            binding.etDate.setOnClickListener {
                cuurentDatePicketParentView = binding.etDate
                showDatePickerDialog(
                    activity,minDate,maxDate,
                    listeners = OnDateSetListener { view, year, month, dayOfMonth ->
                        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, month, dayOfMonth)
                        val subDateString: String =
                            DateUtility.getStringDateFromTimestamp((tempDate.time), DateUtility.dd_MM_yy)
                        cuurentDatePicketParentView?.setText(subDateString)
                        cuurentDatePicketParentView = null;
                    }
                );
            }
            binding.etDealerName.setOnClickListener {
                this.binding.spDealerName.performClick()
            }
            binding.etDistributorName.setOnClickListener {
                this.binding.spDistributorName.performClick()
            }
            binding.ivDelete.setOnClickListener{
                beatAssignmentList.removeAt(adapterPosition)
                notifyDataSetChanged()
            }
            binding.spDealerName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                    binding.etDealerName.setText(dealerList?.get(position))
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }
            binding.spDistributorName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                    binding.etDistributorName.setText(distributorList?.get(position))
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }
            binding.etTask.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {

                    //field2.setText("");
                }

                override fun afterTextChanged(s: Editable) {
                    val position = adapterPosition
                    if (s.toString() != null && s.toString().trim { it <= ' ' } != "") {
                        this@BeatAssignmentTaskListAdapter.beatAssignmentList?.get(position).assTask = s.toString()
                    }
                }
            })

            binding.rgUserType.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
                val rb = activity.findViewById<View>(checkedId) as RadioButton
                //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
                val position = adapterPosition
                if (checkedId == R.id.radio_dealer) {
                    binding.tilDealerName.visibility = View.VISIBLE
                    binding.tilDistributorName.visibility = View.GONE
                    this@BeatAssignmentTaskListAdapter.beatAssignmentList?.get(position).isDealer = true
                } else {
                    binding.tilDealerName.visibility = View.GONE
                    binding.tilDistributorName.visibility = View.VISIBLE
                    this@BeatAssignmentTaskListAdapter.beatAssignmentList?.get(position).isDealer = false
                }
            })
        }

        fun bind(beatAssignments: BeatAssignments?) {
            binding.beatAssignments = beatAssignments
            dealerList?.let { binding.spDealerName.setItem(it) }
            distributorList?.let { binding.spDistributorName.setItem(it) }
            binding.executePendingBindings()
        }
    }
}