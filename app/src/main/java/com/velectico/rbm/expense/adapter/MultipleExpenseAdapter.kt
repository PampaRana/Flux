package com.velectico.rbm.expense.adapter

import android.app.Activity
import android.app.DatePickerDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.beats.model.BeatAssignments
import com.velectico.rbm.databinding.RowAssignmentListBinding
import com.velectico.rbm.databinding.RowMultipleExpenseBinding
import com.velectico.rbm.utils.DateUtility
import java.util.*


class MultipleExpenseAdapter (activity: Activity) :
    RecyclerView.Adapter<MultipleExpenseAdapter.ViewHolder>() {

    val activity : Activity = activity
    val minDate: Long? = 88888
    val maxDate: Long? = 888888

    private var cuurentDatePicketParentView : com.google.android.material.textfield.TextInputEditText? = null;

    public var beatAssignmentList = mutableListOf<BeatAssignments>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowMultipleExpenseBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return beatAssignmentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beatAssignmentList[position])
    }

    //https://stackoverflow.com/questions/45418194/i-cant-reach-any-class-member-from-a-nested-class-in-kotlin
    inner class ViewHolder(_binding: RowMultipleExpenseBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
            binding.etDate.setOnClickListener {
                cuurentDatePicketParentView = binding.etDate
                DateUtility.showDatePickerDialog(
                    activity,minDate,maxDate,
                    listeners = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val tempDate: Date =
                            DateUtility.getDateFromYearMonthDay(year, month, dayOfMonth)
                        val subDateString: String =
                            DateUtility.getStringDateFromTimestamp(
                                (tempDate.time),
                                DateUtility.dd_MM_yy
                            )
                        cuurentDatePicketParentView?.setText(subDateString)
                        cuurentDatePicketParentView = null;
                    }
                );
            }
            /*  binding.etDealerName.setOnClickListener {
                  this.binding.spDealerName.performClick()
              }*/
            binding.ivDelete.setOnClickListener{
                beatAssignmentList.removeAt(adapterPosition)
                notifyDataSetChanged()
            }
            /*  binding.spDealerName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                  override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                      binding.etDealerName.setText(dealerList?.get(position))
                  }

                  override fun onNothingSelected(adapterView: AdapterView<*>) {}
              }*/
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
                        this@MultipleExpenseAdapter.beatAssignmentList?.get(position).assTask = s.toString()
                    }
                }
            })
        }

        fun bind(beatAssignments: BeatAssignments?) {
            binding.beatAssignments = beatAssignments
            binding.executePendingBindings()
        }
    }





}