package com.velectico.rbm.beats.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.beats.model.BeatTaskDetails
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.databinding.RowBeatTaskDetailsViewBinding
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.GloblalDataRepository


class BeatTaskDetailsViewAdapter (var setCallback: BeatTaskDetailsViewAdapter.IBeatTaskDetailsViewActionCallBack) : RecyclerView.Adapter<BeatTaskDetailsViewAdapter.ViewHolder>() {

    var callBack : BeatTaskDetailsViewAdapter.IBeatTaskDetailsViewActionCallBack?=null
    var beatList = listOf<BeatTaskDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()

        }
    inner class ViewHolder(_binding: RowBeatTaskDetailsViewBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {

            callBack = setCallback;

        }

        fun bind(beats: BeatTaskDetails?) {
            binding.beats = beats
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowBeatTaskDetailsViewBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return beatList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beatList[position])
        GloblalDataRepository.getInstance().taskId = beatList[position].taskId
        GloblalDataRepository.getInstance().delalerId = beatList[position].dealerId
        GloblalDataRepository.getInstance().distribId = beatList[position].distribId
        holder.binding.cardDetailsForTask.setOnClickListener {
            callBack?.moveToBeatTaskDetails(position, "1",holder.binding )
        }
        if (beatList[position].dealerName!="" ){
            holder.binding.tvProdNetPrice.text = "Dealer"
            holder.binding.cutGrade.text = beatList[position].dealerGrade
            holder.binding.tvPersonName.text=beatList[position].dealerName
            holder.binding.tvAddress.text=beatList[position].dealerAddress

        }
        else{
            holder.binding.tvProdNetPrice.text = "Distributor"
            holder.binding.cutGrade.text = beatList[position].distribGrade
            holder.binding.tvPersonName.text=beatList[position].distribName
            holder.binding.tvAddress.text=beatList[position].distribAddress

        }
        if (beatList[position].BSD_Order_Target=="") {
            holder.binding.tvOrdrAmt.text ="₹ 0"
        }else{
            holder.binding.tvOrdrAmt.text ="₹ "+beatList[position].BSD_Order_Target
        }
        if (beatList[position].BSD_Collection_Target=="") {
            holder.binding.collectionAmt.text ="₹ 0"
        }else{
            holder.binding.collectionAmt.text ="₹ "+beatList[position].BSD_Collection_Target
        }
       // holder.binding.tvVisit.text=beatList[position].visit


    }


    interface IBeatTaskDetailsViewActionCallBack{
        fun moveToBeatTaskDetails(position:Int,beatTaskId:String?,binding: RowBeatTaskDetailsViewBinding)

    }





}