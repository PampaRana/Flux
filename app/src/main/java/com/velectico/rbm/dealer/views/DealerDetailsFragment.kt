package com.velectico.rbm.dealer.views

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.BeatTaskDetails
import com.velectico.rbm.databinding.FragmentDealerDetailsBinding
import com.velectico.rbm.databinding.FragmentOrderDetailsBinding
import com.velectico.rbm.dealer.model.DealerListDetails
import com.velectico.rbm.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class DealerDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentDealerDetailsBinding
    var delearInfo = DealerListDetails()

    override fun getLayout(): Int {
        return R.layout.fragment_dealer_details
    }

    @SuppressLint("SetTextI18n", "UseRequireInsteadOfGet")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentDealerDetailsBinding
        delearInfo = arguments!!.get("dealerInfo") as DealerListDetails
        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        if (delearInfo.DD_Reminder==""){
            val stdate =  DateUtils.parseDate(delearInfo.Create_Date,inpFormat,outputformat)
            binding.tvDate.text = stdate

        }else{
            val stdate =  DateUtils.parseDate(delearInfo.DD_Reminder,inpFormat,outputformat)

            binding.tvDate.text = stdate

        }
        binding.tvDealerName.text = delearInfo.DD_Name
        if (delearInfo.DD_Contact_Name==""){
            binding.llContactName.visibility=View.GONE

        }else{
            binding.llContactName.visibility=View.VISIBLE

            binding.tvContactName.text = delearInfo.DD_Contact_Name

        }
        if (delearInfo.DD_Address==""){
            binding.llAddress.visibility=View.GONE

        }else{
            binding.llAddress.visibility=View.VISIBLE

            binding.tvAddress.text = delearInfo.DD_Address

        }

        if (delearInfo.DD_Sale_Per_Day==""){
            binding.llSaleMonthly.visibility=View.GONE

        }else{
            binding.llSaleMonthly.visibility=View.VISIBLE

            binding.tvSaleMonthly.text = delearInfo.DD_Sale_Per_Day

        }


        if (delearInfo.Segment_Name==""){
            binding.llSegment.visibility=View.GONE

        }else{
            binding.llSegment.visibility=View.VISIBLE
            binding.tvSegment.text = delearInfo.Segment_Name

        }
        if (delearInfo.DD_Grading_Name!=null){
            binding.llGrading.visibility=View.VISIBLE
            binding.tvGrading.text = delearInfo.DD_Grading_Name

        }else{
            binding.llGrading.visibility=View.GONE


        }
        if (delearInfo.DD_FeedBack==""){
            binding.llFeedback.visibility=View.GONE

        }else{
            binding.llFeedback.visibility=View.VISIBLE
            binding.tvFeedback.text = delearInfo.DD_FeedBack

        }
        if (delearInfo.DD_Present_Sup_Names==""){
            binding.llCreditDays.visibility=View.GONE

        }else{
            binding.llCreditDays.visibility=View.VISIBLE
            binding.tvCreditDays.text = delearInfo.DD_Present_Sup_Names+" Days"

        }
        binding.tvArea.text = delearInfo.AreaName

        /*if (delearInfo.DD_Pref_Company_Val!=null) {
            binding.llPrefCompany1.visibility=View.VISIBLE
            binding.tvPrefCompany1.text =
                delearInfo.DD_Pref_Company_Val + " / " + delearInfo.Grade + " / " + delearInfo.Packaging + " / ₹ "+ delearInfo.DD_Price
        }else{
            binding.llPrefCompany1.visibility=View.GONE
        }*/
        /*binding.tvPrice1.text = "₹ "+delearInfo.DD_Price
        binding.tvGrade1.text = delearInfo.Grade
        binding.tvPackage1.text = delearInfo.Packaging*/

        Picasso.with(context).load(
            delearInfo.imagePath+delearInfo.DD_Image
        )
            .skipMemoryCache() //.placeholder(R.drawable.place_holder)
            .error(R.drawable.faded_logo_bg)
            .into(binding.ivDealerImg, object : Callback {
                override fun onSuccess() {
                   binding.contentProgressBar.visibility= View.GONE
                }

                override fun onError() {
                    binding.contentProgressBar.visibility= View.GONE

                }
            })


        if (delearInfo.DD_Mobile_Optional==""){
            //binding.llMobileOption.visibility=View.GONE
            binding.tvMobile.text = delearInfo.DD_Mobile

        }else{
            /*binding.llMobileOption.visibility=View.VISIBLE
            binding.tvMobileOption.text = delearInfo.DD_Mobile_Optional*/
            binding.tvMobile.text = delearInfo.DD_Mobile+" / "+delearInfo.DD_Mobile_Optional


        }


    }

}