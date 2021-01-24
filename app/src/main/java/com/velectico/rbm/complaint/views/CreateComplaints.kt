package com.velectico.rbm.complaint.views

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.squareup.picasso.Picasso

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.complaint.model.ComplainListDetails
import com.velectico.rbm.databinding.FragmentCreateComplaintsBinding
import com.velectico.rbm.expense.model.ComplaintCreateRequest
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.ImageUtils
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.io.File
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
abstract class CreateComplaints : BaseFragment() {
    private lateinit var binding: FragmentCreateComplaintsBinding
    var taskDetail = BeatTaskDetails()
    var dlrDtl = DealerDetails()
    var complainDetail = ComplainListDetails()
    val loading = MutableLiveData<Boolean>()
    //var complainCreateResponse = MutableLiveData<ComplaintCreateResponse>()

    private var imageUtils : ImageUtils?=null
    private var imageUrl : String? = null
    private var cameraImgUri : Uri?= null
    var prodName= ""
    var complnType= ""
   // lateinit var networkManager: INetworkManager


    override fun getLayout(): Int {
        return R.layout.fragment_create_complaints
    }
    override fun init(binding: ViewDataBinding) {

        this.binding = binding as FragmentCreateComplaintsBinding
        initHud()
        imageUtils = ImageUtils(context as Context,baseActivity,this)
        complainDetail = arguments!!.get("complainDetail")  as ComplainListDetails
        //showToastMessage(complainDetail.toString())
        if (complainDetail.CR_ID != null){

            binding.inputBatchno.setText(complainDetail.CR_Batch_no.toString())
            binding.inputQuantity.setText(complainDetail.CR_Qty.toString())
            binding.inputRemarks.setText(complainDetail.CR_Remarks.toString())
            binding.btnSaveComplain.visibility = View.GONE
            binding.btnCaptureImg.visibility= View.GONE
            binding.btnSelectImg.visibility= View.GONE
            Picasso.with(context).load(complainDetail.imagePath)
                .skipMemoryCache()
                .placeholder(R.drawable.faded_logo_bg)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivExpBill)
            //Picasso.get().load(complainDetail.imagePath).fit().into(binding.ivExpBill)
        }
//        else {
//            binding.inputBatchno.setText("")
//            binding.inputQuantity.setText("")
//            binding.inputRemarks.setText("")
//        }
        taskDetail = arguments!!.get("taskDetails") as BeatTaskDetails
        dlrDtl = arguments!!.get("dealerDetail") as DealerDetails


        binding.dlrNm.text = taskDetail.dealerName.toString()
        binding.tvProdNetPrice.text = dlrDtl.dealerPhone.toString()
        binding.tvProdTotalPrice.text = dlrDtl.DM_Contact_Person.toString()
        binding.tvOrdrAmt.text = "₹" +dlrDtl.orderAmt.toString()
        binding.collectionAmt22.text = "₹" +dlrDtl.collectionAmt.toString()
        if (taskDetail.distribName != null){
            binding.gradeval.text = taskDetail.distribGrade
            binding.type.text = "Distributor"
        }
        else{
            binding.gradeval.text = taskDetail.dealerGrade
            binding.type.text = "Dealer"
        }
        binding.btnCaptureImg.setOnClickListener {
            cameraImgUri = imageUtils?.getImageUri()
            imageUtils?.captureImageIntent(cameraImgUri)
        }
        binding.btnSelectImg.setOnClickListener {
            imageUtils?.openAlbum()
        }
        binding.btnSaveComplain.setOnClickListener{
            saveComplaint()
        }
        callApi("Complain Types")
        callApi("Brand Name")
        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (complaintTypeList.size > 0 ){
                    val x = complaintTypeList[position]
                    complnType = x.Exp_Head_Id!!

                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (prodNameList.size > 0 ){
                    val x = prodNameList[position]
                    prodName = x.Exp_Head_Id!!

                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            imageUtils?.FINAL_TAKE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    imageUrl = imageUtils?.imgFilePath
                    imageUtils?.displayImage(imageUtils?.imgFilePath,binding.ivExpBill)
                }
            imageUtils?.FINAL_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        imageUrl = imageUtils?.handleImageOnKitkat(data)
                        imageUtils?.displayImage(imageUrl,binding.ivExpBill)
                    }
                }
        }
    }

    private fun saveComplaint(){
       if (binding.inputBatchno.text.toString()?.trim() == ""){
           showToastMessage("Please provide a batchno")
       }
        else if (binding.inputQuantity.text.toString()?.trim() == ""){
           showToastMessage("Please provide a quantity")
       }
       else if (binding.inputRemarks.text.toString()?.trim() == ""){
           showToastMessage("Please provide a remarks")
       }
        else {
           showHud()
           val userId = SharedPreferenceUtils.getLoggedInUserId(context as Context);
           someTaskComplain(
               File(imageUrl),
               complnType,
               userId,
               binding.inputBatchno.text.toString(),
               taskDetail.dealerId.toString(),
               taskDetail.distribId.toString(),
               "0",
               binding.inputQuantity.text.toString(),
               binding.inputRemarks.text.toString(),
               prodName,
               taskDetail.taskId.toString(),
               context!!
           ).execute()
       }

    }
    class someTaskComplain(val file: File,
                           var complaintype:String?,
                           var userId:String?,
                           val CR_Batch_no:String?,
                           val CR_Dealer_ID:String?,
                           val CR_Distrib_ID:String?,
                           val CR_Mechanic_ID:String?,
                           val CR_Remarks:String?,
                           val CR_Qty:String?,
                           val prodNam:String?,
                           val tskId:String?,

                           val context: Context) : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String? {
            // ...G
            GloblalDataRepository.getInstance().complainTest(file,complaintype,userId,CR_Batch_no,
                CR_Dealer_ID,
                CR_Distrib_ID,
                CR_Mechanic_ID,
                CR_Remarks,
                CR_Qty,
                prodNam,
                tskId,
                context)


            return ""
        }

        override fun onPreExecute() {
            super.onPreExecute()

            // ...
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            // ...
        }
    }

    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        init {
            execute()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }
    }


    fun callApi(type:String){
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        when(type){
            "Complain Types"->{
                responseCall.enqueue(complainTypeResponse as Callback<OrderVSQualityResponse>)
            }
            "Brand Name"->{
                responseCall.enqueue(prodNameResponse as Callback<OrderVSQualityResponse>)
            }


        }
    }
    private  var complaintTypeList : List<DropdownDetails> = emptyList<DropdownDetails>()
    private val complainTypeResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                complaintTypeList  = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                for (i in complaintTypeList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }
                binding.spinner2.adapter = adapter2


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }
    private  var prodNameList : List<DropdownDetails> = emptyList<DropdownDetails>()
    private val prodNameResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                prodNameList  = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                for (i in prodNameList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }
                binding.spinner.adapter = adapter2


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }



    var hud: KProgressHUD? = null
    fun  showHud(){
        if (hud!=null){

            hud!!.show()
        }
    }

    fun hide(){
        hud?.dismiss()

    }
    fun initHud(){
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)

        LocalBroadcastManager.getInstance(context!!).registerReceiver(mMessageReceiver,
            IntentFilter("custom-event-name")
        );
    }
    private var mMessageReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val message = intent?.getStringExtra("message")
            //Log.d("receiver $colname", "Got message: " + message)
            hide()
            showToastMessage("Image and data ara inserted successfully")
            val navDirection = CreateComplaintsDirections.actionCreateComplaintsToBeatSpecificComplaintList(taskDetail,dlrDtl)
            Navigation.findNavController(binding.btnSaveComplain).navigate(navDirection)

        }

    }
}
