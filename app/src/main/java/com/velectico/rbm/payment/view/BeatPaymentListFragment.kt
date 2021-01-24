package com.velectico.rbm.payment.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.BeatTaskDetails
import com.velectico.rbm.beats.model.DealerDetails
import com.velectico.rbm.beats.views.BeatListFragmentDirections
import com.velectico.rbm.databinding.FragmentBeatPaymentListBinding
import com.velectico.rbm.databinding.FragmentPaymentListFragmentBinding
import com.velectico.rbm.databinding.RowPaymentListBinding
import com.velectico.rbm.menuitems.views.DefaultFragmentDirections
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.payment.adapter.BeatPaymentListAdapter
import com.velectico.rbm.payment.adapter.PaymentListAdapter
import com.velectico.rbm.payment.adapter.PaymentOutStandAdapter
import com.velectico.rbm.payment.models.OutStandingInfoDetails
import com.velectico.rbm.payment.models.OutStandingResponse
import com.velectico.rbm.payment.models.PaymentHistoryRequestParams
import com.velectico.rbm.payment.models.PaymentInfo
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BeatPaymentListFragment :  BaseFragment() {

    private lateinit var binding: FragmentBeatPaymentListBinding;
    //private lateinit var beatList : List<PaymentInfo>
    var taskDetails = BeatTaskDetails()
    var dlrDtl = DealerDetails()
    var userId = ""
    //var dealerId = ""
    //var distribId = ""
    var paymentOutStandAdapter: PaymentOutStandAdapter? = null
    private var paymentOutStandList : List<OutStandingInfoDetails> = emptyList()
    override fun getLayout(): Int {
        return R.layout.fragment_beat_payment_list
    }

    @SuppressLint("SetTextI18n")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatPaymentListBinding
        /*if (RBMLubricantsApplication.globalRole == "Team" ){
            binding.paybtn1.visibility = View.GONE
        }*/
        binding.tvPersonName.text = DataConstant.name
        //taskDetails = arguments!!.get("taskDetail") as BeatTaskDetails
        dlrDtl = arguments!!.get("dealerDetails") as DealerDetails
        binding.tvType.text = DataConstant.nameValue
        binding.gradeval.text = DataConstant.dealerGrade


        binding.tvDelearCallNo.text=dlrDtl.dealerPhone.toString()
        binding.tvContactPerson.text=dlrDtl.DM_Contact_Person.toString()



        if (DataConstant.orderAmt!="") {
            binding.tvOrdrAmt.text="₹ "+DataConstant.orderAmt
        }else{
            binding.tvOrdrAmt.text="₹ 0"

        }
        if (DataConstant.collectionAmt!="") {
            binding.collectionAmt.text = "₹ " + DataConstant.collectionAmt

        }else{
            binding.collectionAmt.text = "₹ 0"

        }
        if (RBMLubricantsApplication.globalRole == "Team" ){

            userId = GloblalDataRepository.getInstance().teamUserId
        }
        else{
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        /*showToastMessage("DealerId"+DataConstant.dealerId)
        showToastMessage("DistId"+DataConstant.distributorId)*/

       callOutStandApiList()


        binding.paymenthistory.setOnClickListener{
            val navDirection =  BeatPaymentListFragmentDirections.actionFragmentPaymentListToPaymentHistoryFragment()
            Navigation.findNavController(binding.paymenthistory).navigate(navDirection)
        }
        binding.beatPay.setOnClickListener{
            val navDirection =  BeatPaymentListFragmentDirections.actionFragmentPaymentListToPaymentPayFragment(
                userId, DataConstant.dealerId, DataConstant.distributorId
            )
            Navigation.findNavController(binding.beatPay).navigate(navDirection)

        }


    }

    private fun callOutStandApiList() {

        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getPaymentOutStandingList(
            PaymentHistoryRequestParams(userId, DataConstant.dealerId, DataConstant.distributorId)
        )
        responseCall.enqueue(paymentOutStandingResponse as Callback<OutStandingResponse>)


    }
    private val paymentOutStandingResponse = object : NetworkCallBack<OutStandingResponse>() {
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OutStandingResponse>) {
            hide()

            if (response.data!!.status==1){
                binding.card.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.GONE
                binding.card.visibility = View.VISIBLE
                binding.beatTotalamt.text = "Total Outstanding : ₹" + response.data.totalOutStanding

                binding.mTableLayout.removeAllViews()
                binding.mTableLayout.addView(View(context))
                val tableRowHeader = layoutInflater.inflate(
                    R.layout.payment_table_header,
                    null
                ) as TableRow
                binding.mTableLayout.addView(tableRowHeader)
                binding.mTableLayout.setVisibility(View.VISIBLE)
                for (i in response.data.paymentOutStandingList) {

                    val tableRow = layoutInflater.inflate(
                        R.layout.payment_table_item,
                        null
                    ) as TableRow
                    val tv_date_value = tableRow.findViewById(R.id.tv_date_value) as TextView
                    val tv_invoice_no_value =
                        tableRow.findViewById(R.id.tv_invoice_no_value) as TextView
                    val tv_amount_value =
                        tableRow.findViewById(R.id.tv_amount_value) as TextView

                    tv_amount_value.text = "₹ " + i.SIH_Invoice_Amt
                    val inpFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    val outputformat = SimpleDateFormat("dd-MMM-yy", Locale.US);
                    val stdate = DateUtils.parseDate(i.invoiceDate, inpFormat, outputformat)
                    tv_date_value.text = stdate
                    tv_invoice_no_value.text = i.SIH_Invoice_No
                    binding.mTableLayout.addView(tableRow);

                }


            }else{
                binding.card.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
            }


        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }

    }


    var hud: KProgressHUD? = null
    fun showHud() {
        hud = KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show();
    }

    fun hide() {
        hud?.dismiss()
    }

    /*private fun setUpRecyclerView() {


        adapter = BeatPaymentListAdapter();

        binding.rvPaymentList.adapter = adapter
        adapter.beatList = beatList
    }*/

    /*private fun moveToPaymentDetails(){
      val navDirection =  BeatListFragmentDirections.actionMoveToCreateBeat()
         Navigation.findNavController(binding.fab).navigate(navDirection)
     }*/


}
