package com.velectico.rbm.complaint.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.complaint.model.ComplainListDetails
import com.velectico.rbm.databinding.RowComplaintListBinding
import com.velectico.rbm.utils.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ComplaintListAdapter (var setCallback: ComplaintListAdapter.IComplaintListActionCallBack,
                            var context: Context
) : RecyclerView.Adapter<ComplaintListAdapter.ViewHolder>() {

    var callBack : ComplaintListAdapter.IComplaintListActionCallBack?=null


    var complaintList = listOf<ComplainListDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(_binding: RowComplaintListBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
            callBack = setCallback;
            binding.card.setOnClickListener {
                callBack?.moveToComplainDetails(adapterPosition, "1",binding )
            }

        }

        fun bind(cm: ComplainListDetails?) {
            binding.complaintInfo = cm
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowComplaintListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return complaintList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(complaintList[position])
        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        val stdate =  DateUtils.parseDate(complaintList[position].CR_Date,inpFormat,outputformat)
        holder.binding.tvDate.text = stdate

        /*val separated =
            complaintList[position].CR_Date!!.split(" ".toRegex()).toTypedArray()
        val code=separated[1]
        //Toast.makeText(context, code, Toast.LENGTH_LONG).show()
        val sdf = SimpleDateFormat("hh:mm:ss")
        val sdfs = SimpleDateFormat("hh:mm a")
        val dt: Date
        try {
            dt = sdf.parse(code)
            println("Time Display: " + sdfs.format(dt)) // <-- I got result here
            // Toast.makeText(context, sdfs.format(dt), Toast.LENGTH_LONG).show()
            holder. binding.tvTime.text = sdfs.format(dt)

        } catch (e: ParseException) {
            e.printStackTrace()
        }*/
        Picasso.with(context).load(
            complaintList[position].imagePath
        )
            .skipMemoryCache() //.placeholder(R.drawable.place_holder)
             .error(R.drawable.faded_logo_bg)
            .into(holder.binding.ivComplaintImageUrl, object : Callback {
                override fun onSuccess() {
                    holder.binding.contentProgressBar.visibility=View.GONE
                }

                override fun onError() {
                    holder.binding.contentProgressBar.visibility=View.GONE

                }
            })
        /*Picasso.with(context).load(complaintList[position].imagePath)
            .skipMemoryCache()
            .placeholder(R.drawable.faded_logo_bg)
            .error(R.drawable.faded_logo_bg)
            .into(holder.binding.ivComplaintImageUrl)*/
        //Picasso.get().load(complaintList[position].imagePath).fit().into(holder.binding.ivComplaintImageUrl)
    }
    interface IComplaintListActionCallBack{
        fun moveToComplainDetails(position:Int,beatTaskId:String?,binding: RowComplaintListBinding)

    }






}