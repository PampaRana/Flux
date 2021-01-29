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
import com.velectico.rbm.databinding.ExistDealerLayoutBinding
import com.velectico.rbm.dealer.model.DealerListDetails
import com.velectico.rbm.dealer.model.ExistDealerDetails
import com.velectico.rbm.dealer.model.FeedbackDetails
import com.velectico.rbm.utils.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
            binding.tvMobileOption.setOnClickListener {
                callBack?.moveToCallOption(adapterPosition, "1",binding )
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dealerList[position])

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

        if (dealerList[position].UM_Phone==""){
            holder.binding.tvMobile.visibility= View.GONE

        }else{
            holder.binding.tvMobile.visibility= View.VISIBLE

        }
        if (dealerList[position].DM_Mobile_Optional==""){
            holder.binding.tvMobileOption.visibility=View.GONE

        }else if (dealerList[position].DM_Mobile_Optional==null){
            holder.binding.tvMobileOption.visibility=View.GONE

        }else{
            holder.binding.tvMobileOption.visibility=View.VISIBLE

        }
        if (dealerList[position].DM_Grading_Name!=null ||dealerList[position].DM_Grading_Name!="" ){
            holder.binding.tvGrading.text=dealerList[position].DM_Grading_Name
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



            }


        }

        Picasso.with(context).load(
            dealerList[position].imagePath+dealerList[position].DM_Image_Path
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
        fun moveToDealerDetails(position:Int,ddId:String?,binding: ExistDealerLayoutBinding)
        fun moveToCall(adapterPosition: Int, s: String, binding: ExistDealerLayoutBinding)
        fun moveToDealerEdit(adapterPosition: Int, s: String, binding: ExistDealerLayoutBinding)
        fun moveToCallOption(adapterPosition: Int, s: String, binding: ExistDealerLayoutBinding)

    }
}