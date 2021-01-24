package com.velectico.rbm.beats.adapters



import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.R
import com.velectico.rbm.databinding.FragmentViewAssignedBeatScheduleTaskBinding
import com.velectico.rbm.databinding.RowViewAssignedBeatTaskBinding


class ViewAssignedBeatTaskAdapter(activity: Activity) : RecyclerView.Adapter<ViewAssignedBeatTaskAdapter.ViewHolder>() {
    val activity : Activity = activity

    inner class ViewHolder(_binding: RowViewAssignedBeatTaskBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
            binding.ivMoreOption.setOnClickListener {

                 Log.e("IS_CLICKED::","YES");
                   //creating a popup menu
                   val popupMenu = PopupMenu(activity,binding.ivMoreOption)
                   popupMenu.inflate(R.menu.beat_option_menu)
                   popupMenu.show()
                   popupMenu.setOnMenuItemClickListener {
                       when(it.itemId) {
                           R.id.createOrder -> {
                               Log.e("ADD_TASK::","YES");


                           }
                           R.id.payReceipt -> {
                               Log.e("EDIT_TASK::","YES");
                           }
                           R.id.createReceipt -> {
                               Log.e("VIEW_TASK::","YES");
                           }
                           R.id.complaint -> {
                               Log.e("DEl_TASK::","YES");
                           }

                       }
                       true
                   }

            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAssignedBeatTaskAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowViewAssignedBeatTaskBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 10
    }



    override fun onBindViewHolder(holder: ViewAssignedBeatTaskAdapter.ViewHolder, position: Int) {
        //holder.bind(orderCart[position])
    }





}