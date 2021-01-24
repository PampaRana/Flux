package com.velectico.rbm.dealer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.DealerListLayoutBinding
import com.velectico.rbm.databinding.ExistDealerLayoutBinding
import com.velectico.rbm.dealer.model.DealerListDetails
import com.velectico.rbm.dealer.model.ExistDealerDetails

class ExistingDealerListAdapter(
    var setCallback: ExistingDealerListAdapter.IDealerListActionCallBack,
    var context: Context
) : RecyclerView.Adapter<ExistingDealerListAdapter.ViewHolder>() {

    var callBack: ExistingDealerListAdapter.IDealerListActionCallBack? = null


    var dealerList = listOf<ExistDealerDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: ExistDealerLayoutBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
            callBack = setCallback;
            binding.card.setOnClickListener {
                callBack?.moveToDealerDetails(adapterPosition, "1",binding )
            }

            binding.navigateToEdit.setOnClickListener {
                callBack?.moveToDealerEdit(adapterPosition, "1",binding )
            }
            binding.tvMobile.setOnClickListener {
                callBack?.moveToCall(adapterPosition, "1",binding )
            }

        }

        fun bind(cm: ExistDealerDetails?) {
            binding.existDealerInfo = cm
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return dealerList.size
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExistingDealerListAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding=ExistDealerLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dealerList[position])
        if (dealerList[position].UM_Login_Id==""){
            holder.binding.tvMobile.visibility= View.GONE

        }else{
            holder.binding.tvMobile.visibility= View.VISIBLE

        }

    }
    interface IDealerListActionCallBack{
        fun moveToDealerDetails(position:Int,ddId:String?,binding: ExistDealerLayoutBinding)
        fun moveToCall(adapterPosition: Int, s: String, binding: ExistDealerLayoutBinding)
        fun moveToDealerEdit(adapterPosition: Int, s: String, binding: ExistDealerLayoutBinding)

    }
}