package com.velectico.rbm.payment.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import androidx.databinding.ViewDataBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentPaymentPayBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.payment.models.InvoicePaidResponse
import com.velectico.rbm.payment.models.InvoiceRequestParams
import retrofit2.Callback


class PaymentPayFragment : BaseFragment() {
    private lateinit var binding: FragmentPaymentPayBinding
    var userId=""
    var dealerId=""
    var distribId=""
    var paymentType=""
    var dialog: Dialog? = null
    override fun getLayout(): Int {
       return R.layout.fragment_payment_pay
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentPaymentPayBinding
        userId = arguments?.getString(  "userId").toString()
        dealerId = arguments?.getString(  "dealerId").toString()
        distribId = arguments?.getString(  "distribId").toString()
        //showToastMessage("userId"+userId)
        //showToastMessage("dealerId"+dealerId)
        //showToastMessage("distribId"+distribId)
        showToastMessage("dealId"+dealerId)
        showToastMessage("distId"+distribId)
        binding.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (binding.spinner.selectedItem.toString().equals("--Select Payment Mode--")){
                        //showToastMessage("Select Payment Mode")
                    }else if (binding.spinner.selectedItem.toString().equals("Cash")){
                        paymentType="CASH"

                    }else if (binding.spinner.selectedItem.toString().equals("Cheque")){
                        paymentType="CHEQUE"

                    }


                    }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }

        binding.btnConfirm.setOnClickListener{
          val  amountString=binding.inputBlncAmt!!.text.toString()
            if (binding.spinner.selectedItem.toString().equals("--Select Payment Mode--")){
                showToastMessage("Select Payment Mode")

            }else if (amountString.equals("")){
                showToastMessage("Enter Amount")

            }else if (amountString.toInt()<=0){
                showToastMessage("Enter Amount")

            }else{
                callOutStandApiList()

            }
        }

    }

    private fun callOutStandApiList() {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getInvoiceList(
            InvoiceRequestParams(userId, dealerId, distribId, paymentType,binding.inputBlncAmt.text.toString().trim() )
        )
        responseCall.enqueue(invoicePaidResponse as Callback<InvoicePaidResponse>)
    }

    private val invoicePaidResponse = object : NetworkCallBack<InvoicePaidResponse>(){
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<InvoicePaidResponse>) {
            hide()
            response.data?.status?.let { status ->

                if (response.data.status ==1){
                   // binding.tranId.text=response.data.Tran_ID
                    val builder = AlertDialog.Builder(context)
                    //set title for alert dialog
                    builder.setTitle("Payment Successfull")
                    //set message for alert dialog
                    builder.setMessage("Transaction Id : "+response.data.Tran_ID)
                    //builder.setIcon(android.R.drawable.ic_dialog_alert)

                    //performing positive action
                    builder.setPositiveButton("Ok"){dialogInterface, which ->
                        binding.inputBlncAmt.text?.clear()
                        binding.spinner.setSelection(0)
                    }

                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()

                }else{
                    showToastMessage("Please try again")
                }


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


    /*   override fun onCreateView(
           inflater: LayoutInflater, container: ViewGroup?,
           savedInstanceState: Bundle?
       ): View? {
           // Inflate the layout for this fragment
           return inflater.inflate(R.layout.fragment_payment_pay, container, false)
       }
   */

}