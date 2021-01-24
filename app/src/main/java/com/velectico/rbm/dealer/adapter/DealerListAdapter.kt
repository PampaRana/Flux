package com.velectico.rbm.dealer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.databinding.DealerListLayoutBinding
import com.velectico.rbm.dealer.model.DealerListDetails
import com.velectico.rbm.dealer.model.FeedbackDetails
import com.velectico.rbm.utils.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DealerListAdapter (var setCallback: DealerListAdapter.IDealerListActionCallBack,
                         var context: Context
): RecyclerView.Adapter<DealerListAdapter.ViewHolder>(){
    var callBack : DealerListAdapter.IDealerListActionCallBack?=null


    var dealerList = listOf<DealerListDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(_binding: DealerListLayoutBinding) : RecyclerView.ViewHolder(_binding.root) {
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
            binding.tvMobileOption.setOnClickListener {
                callBack?.moveToCallOption(adapterPosition, "1",binding )
            }
        }

        fun bind(cm: DealerListDetails?) {
            binding.dealerInfo = cm
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealerListAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding=DealerListLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dealerList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DealerListAdapter.ViewHolder, position: Int) {
        holder.bind(dealerList[position])
        /*val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        val stdate =  DateUtils.parseDate(dealerList[position].CR_Date,inpFormat,outputformat)
        holder.binding.tvUnit.text = stdate
       */
        /*val dateTime: LocalDateTime =
            LocalDateTime.parse(dateToParse, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val localDate: LocalDate = dateTime.toLocalDate()
        val localTime: LocalTime = dateTime.toLocalTime()*/

        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        val separated =
            dealerList[position].Create_Date!!.split(" ".toRegex()).toTypedArray()
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
        }
        val stdate =  DateUtils.parseDate(dealerList[position].Create_Date,inpFormat,outputformat)
        holder. binding.tvDate.text = stdate
        val rmdate =  DateUtils.parseDate(dealerList[position].DD_Reminder,inpFormat,outputformat)
        holder. binding.tvReminder.text = rmdate
       /* if (dealerList[position].DD_Reminder==""){
            holder.binding.llDate.visibility=View.VISIBLE
            val stdate =  DateUtils.parseDate(dealerList[position].Create_Date,inpFormat,outputformat)
            holder. binding.tvDate.text = stdate


        }else{
            val stdate =  DateUtils.parseDate(dealerList[position].DD_Reminder,inpFormat,outputformat)
            holder.binding.llDate.visibility=View.VISIBLE
            holder. binding.tvDate.text = stdate

        }*/
        if (dealerList[position].DD_Mobile_Optional==""){
            holder.binding.tvMobileOption.visibility=View.GONE

        }else{
            holder.binding.tvMobileOption.visibility=View.VISIBLE

        }
        if (dealerList[position].DD_Mobile==""){
            holder.binding.tvMobile.visibility=View.GONE

        }else{
            holder.binding.tvMobile.visibility=View.VISIBLE

        }
        /*
        if (dealerList[position].DD_Address==""){
            holder.binding.llAddress.visibility=View.GONE

        }else{
            holder.binding.llAddress.visibility=View.VISIBLE

        }
        if (dealerList[position].DD_Contact_Name==""){
            holder.binding.llContactName.visibility=View.GONE

        }else{
            holder.binding.llContactName.visibility=View.VISIBLE

        }*/
        if (dealerList[position].DD_Grading_Name!=null ||dealerList[position].DD_Grading_Name!="" ){
            holder.binding.tvGrading.text=dealerList[position].DD_Grading_Name
        }else{
            holder.binding.tvGrading.text=""

        }
        if (dealerList[position].Segment_Name!=null ||dealerList[position].Segment_Name!="" ){
            holder.binding.tvSegment.text=dealerList[position].Segment_Name
        }else{
            holder.binding.tvSegment.text=""

        }
        if (dealerList[position].details.size>0){
            if (dealerList[position].details.size==1){
                if (dealerList[position].details[0].DD_Pref_Company_Name !=null){
                    if (dealerList[position].details[0].DD_Grade_Name !=null){
                        if (dealerList[position].details[0].DD_Packaging_Name !=null){
                            if (dealerList[position].details[0].DD_Price !=""){
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Grade_Name + " / " +
                                            dealerList[position].details[0].DD_Packaging_Name + " / ₹ "+
                                            dealerList[position].details[0].DD_Price
                            } else{
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Grade_Name + " / " +
                                            dealerList[position].details[0].DD_Packaging_Name
                            }
                        }else{
                            if (dealerList[position].details[0].DD_Price !=""){
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Grade_Name + " / ₹ "+
                                            dealerList[position].details[0].DD_Price
                            } else{
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Grade_Name
                            }
                        }
                    }else{
                        if (dealerList[position].details[0].DD_Packaging_Name !=null){
                            if (dealerList[position].details[0].DD_Price !=""){
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Packaging_Name + " / ₹ "+
                                            dealerList[position].details[0].DD_Price
                            } else{
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name +  " / " +
                                            dealerList[position].details[0].DD_Packaging_Name
                            }
                        }else{
                            if (dealerList[position].details[0].DD_Price !=""){
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / ₹ "+
                                            dealerList[position].details[0].DD_Price
                            } else{
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name
                            }
                        }
                    }

                }



            }else  if (dealerList[position].details.size==2){

                if (dealerList[position].details[0].DD_Pref_Company_Name !=null){
                    if (dealerList[position].details[0].DD_Grade_Name !=null){
                        if (dealerList[position].details[0].DD_Packaging_Name !=null){
                            if (dealerList[position].details[0].DD_Price !=""){
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Grade_Name + " / " +
                                            dealerList[position].details[0].DD_Packaging_Name + " / ₹ "+
                                            dealerList[position].details[0].DD_Price
                            } else{
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Grade_Name + " / " +
                                            dealerList[position].details[0].DD_Packaging_Name
                            }
                        }else{
                            if (dealerList[position].details[0].DD_Price !=""){
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Grade_Name + " / ₹ "+
                                            dealerList[position].details[0].DD_Price
                            } else{
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Grade_Name
                            }
                        }
                    }else{
                        if (dealerList[position].details[0].DD_Packaging_Name !=null){
                            if (dealerList[position].details[0].DD_Price !=""){
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Packaging_Name + " / ₹ "+
                                            dealerList[position].details[0].DD_Price
                            } else{
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name +  " / " +
                                            dealerList[position].details[0].DD_Packaging_Name
                            }
                        }else{
                            if (dealerList[position].details[0].DD_Price !=""){
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / ₹ "+
                                            dealerList[position].details[0].DD_Price
                            } else{
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name
                            }
                        }
                    }

                }


                if (dealerList[position].details[1].DD_Pref_Company_Name !=null){
                    if (dealerList[position].details[1].DD_Grade_Name !=null){
                        if (dealerList[position].details[1].DD_Packaging_Name !=null){
                            if (dealerList[position].details[1].DD_Price !=""){
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[1].DD_Grade_Name + " / " +
                                            dealerList[position].details[1].DD_Packaging_Name + " / ₹ "+
                                            dealerList[position].details[1].DD_Price
                            } else{
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[1].DD_Grade_Name + " / " +
                                            dealerList[position].details[1].DD_Packaging_Name
                            }
                        }else{
                            if (dealerList[position].details[1].DD_Price !=""){
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[1].DD_Grade_Name + " / ₹ "+
                                            dealerList[position].details[1].DD_Price
                            } else{
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[1].DD_Grade_Name
                            }
                        }
                    }else{
                        if (dealerList[position].details[1].DD_Packaging_Name !=null){
                            if (dealerList[position].details[1].DD_Price !=""){
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[1].DD_Packaging_Name + " / ₹ "+
                                            dealerList[position].details[1].DD_Price
                            } else{
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name +  " / " +
                                            dealerList[position].details[1].DD_Packaging_Name
                            }
                        }else{
                            if (dealerList[position].details[1].DD_Price !=""){
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name + " / ₹ "+
                                            dealerList[position].details[1].DD_Price
                            } else{
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name
                            }
                        }
                    }

                }




               /* holder.binding.tvCompany1.text =
                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                            dealerList[position].details[0].DD_Grade_Name + " / " +
                            dealerList[position].details[0].DD_Packaging_Name + " / ₹ "+
                            dealerList[position].details[0].DD_Price

                holder.binding.tvCompany2.text =
                    dealerList[position].details[1].DD_Pref_Company_Name + " / " +
                            dealerList[position].details[1].DD_Grade_Name + " / " +
                            dealerList[position].details[1].DD_Packaging_Name + " / ₹ "+
                            dealerList[position].details[1].DD_Price*/
            }else  if (dealerList[position].details.size==3){

                if (dealerList[position].details[0].DD_Pref_Company_Name !=null){
                    if (dealerList[position].details[0].DD_Grade_Name !=null){
                        if (dealerList[position].details[0].DD_Packaging_Name !=null){
                            if (dealerList[position].details[0].DD_Price !=""){
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Grade_Name + " / " +
                                            dealerList[position].details[0].DD_Packaging_Name + " / ₹ "+
                                            dealerList[position].details[0].DD_Price
                            } else{
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Grade_Name + " / " +
                                            dealerList[position].details[0].DD_Packaging_Name
                            }
                        }else{
                            if (dealerList[position].details[0].DD_Price !=""){
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Grade_Name + " / ₹ "+
                                            dealerList[position].details[0].DD_Price
                            } else{
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Grade_Name
                            }
                        }
                    }else{
                        if (dealerList[position].details[0].DD_Packaging_Name !=null){
                            if (dealerList[position].details[0].DD_Price !=""){
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[0].DD_Packaging_Name + " / ₹ "+
                                            dealerList[position].details[0].DD_Price
                            } else{
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name +  " / " +
                                            dealerList[position].details[0].DD_Packaging_Name
                            }
                        }else{
                            if (dealerList[position].details[0].DD_Price !=""){
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name + " / ₹ "+
                                            dealerList[position].details[0].DD_Price
                            } else{
                                holder.binding.tvCompany1.text =
                                    dealerList[position].details[0].DD_Pref_Company_Name
                            }
                        }
                    }

                }


                if (dealerList[position].details[1].DD_Pref_Company_Name !=null){
                    if (dealerList[position].details[1].DD_Grade_Name !=null){
                        if (dealerList[position].details[1].DD_Packaging_Name !=null){
                            if (dealerList[position].details[1].DD_Price !=""){
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[1].DD_Grade_Name + " / " +
                                            dealerList[position].details[1].DD_Packaging_Name + " / ₹ "+
                                            dealerList[position].details[1].DD_Price
                            } else{
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[1].DD_Grade_Name + " / " +
                                            dealerList[position].details[1].DD_Packaging_Name
                            }
                        }else{
                            if (dealerList[position].details[1].DD_Price !=""){
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[1].DD_Grade_Name + " / ₹ "+
                                            dealerList[position].details[1].DD_Price
                            } else{
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[1].DD_Grade_Name
                            }
                        }
                    }else{
                        if (dealerList[position].details[1].DD_Packaging_Name !=null){
                            if (dealerList[position].details[1].DD_Price !=""){
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[1].DD_Packaging_Name + " / ₹ "+
                                            dealerList[position].details[1].DD_Price
                            } else{
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name +  " / " +
                                            dealerList[position].details[1].DD_Packaging_Name
                            }
                        }else{
                            if (dealerList[position].details[1].DD_Price !=""){
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name + " / ₹ "+
                                            dealerList[position].details[1].DD_Price
                            } else{
                                holder.binding.tvCompany2.text =
                                    dealerList[position].details[1].DD_Pref_Company_Name
                            }
                        }
                    }

                }

                if (dealerList[position].details[2].DD_Pref_Company_Name !=null){
                    if (dealerList[position].details[2].DD_Grade_Name !=null){
                        if (dealerList[position].details[2].DD_Packaging_Name !=null){
                            if (dealerList[position].details[2].DD_Price !=""){
                                holder.binding.tvCompany3.text =
                                    dealerList[position].details[2].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[2].DD_Grade_Name + " / " +
                                            dealerList[position].details[2].DD_Packaging_Name + " / ₹ "+
                                            dealerList[position].details[2].DD_Price
                            } else{
                                holder.binding.tvCompany3.text =
                                    dealerList[position].details[2].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[2].DD_Grade_Name + " / " +
                                            dealerList[position].details[2].DD_Packaging_Name
                            }
                        }else{
                            if (dealerList[position].details[2].DD_Price !=""){
                                holder.binding.tvCompany3.text =
                                    dealerList[position].details[2].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[2].DD_Grade_Name + " / ₹ "+
                                            dealerList[position].details[2].DD_Price
                            } else{
                                holder.binding.tvCompany3.text =
                                    dealerList[position].details[2].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[2].DD_Grade_Name
                            }
                        }
                    }else{
                        if (dealerList[position].details[2].DD_Packaging_Name !=null){
                            if (dealerList[position].details[2].DD_Price !=""){
                                holder.binding.tvCompany3.text =
                                    dealerList[position].details[2].DD_Pref_Company_Name + " / " +
                                            dealerList[position].details[2].DD_Packaging_Name + " / ₹ "+
                                            dealerList[position].details[2].DD_Price
                            } else{
                                holder.binding.tvCompany3.text =
                                    dealerList[position].details[2].DD_Pref_Company_Name +  " / " +
                                            dealerList[position].details[2].DD_Packaging_Name
                            }
                        }else{
                            if (dealerList[position].details[2].DD_Price !=""){
                                holder.binding.tvCompany3.text =
                                    dealerList[position].details[2].DD_Pref_Company_Name + " / ₹ "+
                                            dealerList[position].details[2].DD_Price
                            } else{
                                holder.binding.tvCompany3.text =
                                    dealerList[position].details[2].DD_Pref_Company_Name
                            }
                        }
                    }

                }



                /*holder.binding.tvCompany1.text =
                    dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                            dealerList[position].details[0].DD_Grade_Name + " / " +
                            dealerList[position].details[0].DD_Packaging_Name + " / ₹ "+
                            dealerList[position].details[0].DD_Price

                holder.binding.tvCompany2.text =
                    dealerList[position].details[1].DD_Pref_Company_Name + " / " +
                            dealerList[position].details[1].DD_Grade_Name + " / " +
                            dealerList[position].details[1].DD_Packaging_Name + " / ₹ "+
                            dealerList[position].details[1].DD_Price

                holder.binding.tvCompany3.text =
                    dealerList[position].details[2].DD_Pref_Company_Name + " / " +
                            dealerList[position].details[2].DD_Grade_Name + " / " +
                            dealerList[position].details[2].DD_Packaging_Name + " / ₹ "+
                            dealerList[position].details[2].DD_Price*/
            }


        }
        /*if (dealerList[position].details[0].DD_Pref_Company_Name!=null) {
            holder.binding.tvCompany1.text =
                dealerList[position].details[0].DD_Pref_Company_Name + " / " +
                        dealerList[position].details[0].DD_Grade_Name + " / " +
                        dealerList[position].details[0].DD_Packaging_Name + " / ₹ "+
                        dealerList[position].details[0].DD_Price
        }else{
            //holder.binding.llPrefCompany1.visibility=View.GONE
        }*/
        Picasso.with(context).load(
            dealerList[position].imagePath+dealerList[position].DD_Image
        )
            .skipMemoryCache() //.placeholder(R.drawable.place_holder)
            .error(R.drawable.faded_logo_bg)
            .into(holder.binding.ivComplaintImageUrl, object : Callback {
                override fun onSuccess() {
                    holder.binding.contentProgressBar.visibility= View.GONE
                }

                override fun onError() {
                    holder.binding.contentProgressBar.visibility= View.GONE

                }
            })

        var dealerFeedList: List<FeedbackDetails> = emptyList()
        var adapter: DealerFeedbackListAdapter
        if (dealerList[position].feedback.size>0) {
            dealerFeedList.toMutableList().clear()
            dealerFeedList = dealerList[position].feedback.toMutableList()
            Collections.reverse(dealerFeedList);

            adapter = DealerFeedbackListAdapter(context, dealerFeedList)
            holder.binding.rvFeedList.adapter = adapter
            adapter.dealerFeedList = dealerFeedList;
            holder.binding.rvFeedList.visibility = View.VISIBLE

        }else{
            holder.binding.rvFeedList.visibility = View.GONE
        }


    }

    interface IDealerListActionCallBack{
        fun moveToDealerDetails(position:Int,ddId:String?,binding: DealerListLayoutBinding)
        fun moveToCall(adapterPosition: Int, s: String, binding: DealerListLayoutBinding)
        fun moveToCallOption(adapterPosition: Int, s: String, binding: DealerListLayoutBinding)
        fun moveToDealerEdit(adapterPosition: Int, s: String, binding: DealerListLayoutBinding)

    }
}