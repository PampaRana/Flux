package com.velectico.rbm.beats.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.tbruyelle.rxpermissions3.RxPermissions
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.BeatTaskDetails
import com.velectico.rbm.beats.model.DealerDetails
import com.velectico.rbm.beats.model.DealerDetailsRequestParams
import com.velectico.rbm.beats.model.DealerDetailsResponse
import com.velectico.rbm.complaint.model.ComplainListDetails
import com.velectico.rbm.databinding.FragmentBeatTaskDealerDetailsBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import com.velectico.rbm.utils.SharedPreferencesClass
import com.velectico.rbm.utils.TrackerGPS_V3.REQUEST_CHECK_SETTINGS
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import retrofit2.Callback
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 */
class BeatTaskDealerDetailsFragment : BaseFragment()/*, TrackerGPS_V3.LocationCallBack*/ {

    private lateinit var binding: FragmentBeatTaskDealerDetailsBinding
    var taskDetails = BeatTaskDetails()
    var dealerDetails = DealerDetails()
    var userId = ""
    var dealerName=""
    var latitude=0.0
    var longitude=0.0
    private val REQUEST_LOCATION = 1


    private var complaintList = ComplainListDetails()
    override fun getLayout(): Int {
        return R.layout.fragment_beat_task_dealer_details
    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatTaskDealerDetailsBinding

        //checkPermission()

        taskDetails = arguments!!.get("beatTaskDetails") as BeatTaskDetails
        //showToastMessage(arguments.)
        /*if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == MECHANIC_ROLE){

            binding.btnNewOrder.visibility = View.INVISIBLE
            binding.btnOrderHistory.visibility = View.INVISIBLE
            binding.btnPerformanceHistory.visibility = View.INVISIBLE
            binding.viewAllTransBtn.visibility = View.INVISIBLE
        }*/
        if (RBMLubricantsApplication.globalRole == "Team" ){
           /* binding.llBeatReport.visibility = View.INVISIBLE
            binding.sampleLinesix.visibility = View.INVISIBLE
            binding.sampleLineSeven.visibility = View.INVISIBLE*/
            //binding.btnComplaints.visibility = View.INVISIBLE
            //binding.btnBeatReport.visibility = View.INVISIBLE
            userId = GloblalDataRepository.getInstance().teamUserId
        }
        else{
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        binding.btnPerformanceHistory.setOnClickListener {
            moveToPerformanceHistory()
        }

        /*binding.btnComplaints.setOnClickListener {
            moveToCreateComplaint()
        }*/
        binding.btnBeatReport.isEnabled=false
        if (SharedPreferencesClass.retriveData(context as Context,"SUM_Location_Lock").toString()=="N"){
            binding.btnBeatReport.isEnabled=true

        }
        binding.btnBeatReport.setOnClickListener {
            if (SharedPreferencesClass.retriveData(context as Context,"SUM_Location_Lock").toString()=="Y"){

                if (String.format(
                        "%.2f", distance(
                            latitude, DataConstant.lat,
                            longitude, DataConstant.lng
                            /*latitude, DataConstant.lat,
                            longitude, DataConstant.lng*/
                        )
                    ) + " KM">"2.00 KM"){
                    showToastMessage("Can not create Beat Report")

                }/*else if ( binding.tvKm.text.toString().trim()>="100.00 KM"){
                    showToastMessage("Can not create Beat Report")

                }else if ( binding.tvKm.text.toString().trim()>="10.00 KM"){
                    showToastMessage("Can not create Beat Report")

                }*//*else if (binding.tvKm.text.toString().trim()==""){
                    showToastMessage("Create Beat Report")

                }*/
                else{
                    moveToAllBeatReport()
                    //showToastMessage("Create Beat Report")

                }

            }else {
               // binding.btnBeatReport.isEnabled=true
                moveToAllBeatReport()

                /*if (String.format(
                        "%.2f", distance(
                            latitude, DataConstant.lat,
                            longitude, DataConstant.lng
                            *//*latitude, DataConstant.lat,
                            longitude, DataConstant.lng*//*
                        )
                    ) + " KM">"2.00 KM"){
                    showToastMessage("Can not create Beat Report")

                }else{
                    showToastMessage("Create Beat Report")
                    moveToAllBeatReport()

                }*/

                /*else if ( binding.tvKm.text.toString().trim()>="100.00 KM"){
                    showToastMessage("Can not create Beat Report")

                }else if ( binding.tvKm.text.toString().trim()>="10.00 KM"){
                    showToastMessage("Can not create Beat Report")

                }*//*else if (binding.tvKm.text.toString().trim()==""){
                    showToastMessage("Create Beat Report")

                }*/

                // moveToBeatReport()
            }
        }

        /*binding.btnNewOrder.setOnClickListener {
            moveToCreateOrder()
        }*/
       // showToastMessage("Location"+SharedPreferencesClass.retriveData(context as Context,"SUM_Location_Lock").toString())
        binding.btnOrderHistory.setOnClickListener {
            //showToastMessage(binding.tvKm.text.toString())
            moveToOrderHistory()


        }
        binding.btnComplaints.setOnClickListener {
            moveToViewAllComplaints()
        }

        binding.viewAllTransBtn.setOnClickListener {
            moveToBeatPayAndTrans()
        }

       /* binding.allBeatReport.setOnClickListener {
            moveToAllBeatReport()
        }*/
        binding.tvDelearCallNo.setOnClickListener {
            callUser()
        }

        //gps_v3 = TrackerGPS_V3(context as Context)
       // checkRuntimePermission()


        //val arg  = arguments!!.get("beatTaskDetails") as BeatTaskDetails
        if (taskDetails.taskId!=null){

            DataConstant.dealerId = taskDetails.dealerId!!
            DataConstant.distributorId = taskDetails.distribId!!
            DataConstant.taskId=taskDetails.taskId!!
            if (taskDetails.distribName !=""){
                DataConstant.name=taskDetails.distribName!!
                DataConstant.nameValue="Distributor"
            }
            else{
                DataConstant.name=taskDetails.dealerName!!
                DataConstant.nameValue="Dealer"
            }
            DataConstant.orderAmt=taskDetails.BSD_Order_Target.toString()
            DataConstant.collectionAmt=taskDetails.BSD_Collection_Target.toString()
            DataConstant.beatVisit=taskDetails.visit.toString()
            callDealerDetails(taskDetails)
        }else{
            DataConstant.taskId=DataConstant.taskId
            DataConstant.name= DataConstant.name
            DataConstant.nameValue=DataConstant.nameValue
            DataConstant.dealerId = DataConstant.dealerId
            DataConstant.distributorId = DataConstant.distributorId

            callDealerDetailsApi(DataConstant.taskId,DataConstant.dealerId, DataConstant.distributorId)
        }




    }



    fun checkRuntimePermission() {
        val rxPermissions = RxPermissions(this)
        rxPermissions.request(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
            .subscribe(object : Observer<Boolean> {
                override fun onSubscribe(d: Disposable) {}
                @SuppressLint("SetTextI18n")
                override fun onNext(aBoolean: Boolean) {
                    if (aBoolean) {
                        (activity as BaseActivity). gps_v3!!.startLocationUpdates()
                        //showHud()

                        val handler = Handler()
                        handler.postDelayed({

                            binding.tvKm.text=String.format(
                                "%.2f", distance(
                                    /*53.32055555555556,53.31861111111111,
                                    -1.7297222222222221,-1.6997222222222223*/
                                    latitude, DataConstant.lat,
                                    longitude, DataConstant.lng
                                )
                            ) + " KM"
                            binding.btnBeatReport.isEnabled=true

                            /*showToastMessage( """
                                    Your Current Position is:
                                    ${DataConstant.lat}
                                    ${DataConstant.lng}
                                    """.trimIndent())*/
                        }, 4000)
                       // hide()// Do something after 5s = 5000ms

                    } else {
                        showToastMessage("Permission request denied")

                    }
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        //For Grab location
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CHECK_SETTINGS) {
            (activity as BaseActivity). gps_v3!!.startLocationUpdates()
            /*val handler = Handler()
            handler.postDelayed({ // Do something after 5s = 5000ms
                showToastMessage( """
                                    Your Current Position is:
                                    ${DataConstant.lat.toString()}
                                    ${DataConstant.lng}
                                    """.trimIndent())

                val lat1 = 53.32055555555556
                val lon1 = -1.7297222222222221

                showToastMessage(String.format(
                    "%.2f", distance(
                        lat1, DataConstant.lat,
                        lon1, DataConstant.lng
                    )
                ) + " K.M")

            }, 4000)*/
        }
    }



    fun distance(
        lat1: Double,
        lat2: Double, lon1: Double,
        lon2: Double
    ): Double {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        var lat1 = lat1
        var lat2 = lat2
        var lon1 = lon1
        var lon2 = lon2
        lon1 = Math.toRadians(lon1)
        lon2 = Math.toRadians(lon2)
        lat1 = Math.toRadians(lat1)
        lat2 = Math.toRadians(lat2)

        // Haversine formula
        val dlon = lon2 - lon1
        val dlat = lat2 - lat1
        val a = (Math.pow(Math.sin(dlat / 2), 2.0)
                + (Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2.0)))
        val c = 2 * Math.asin(Math.sqrt(a))

        // Radius of earth in kilometers. Use 3956
        // for miles
        val r = 6371.0

        // calculate the result
        return c * r
    }





    private fun moveToPerformanceHistory(){
       // showToastMessage("Coming Soon")
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatPerformanceHistory(binding.tvDelearCallNo.text.toString())
        Navigation.findNavController(binding.btnPerformanceHistory).navigate(navDirection)
    }

   /* private fun moveToCreateComplaint(){

        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToAddComplaintFragment(taskDetails,dealerDetails*//*,complaintList*//*)
        Navigation.findNavController(binding.btnComplaints).navigate(navDirection)
    }*/

    /*private fun moveToBeatReport(){

        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatReportFragment(taskDetails,dealerDetails)
        Navigation.findNavController(binding.btnBeatReport).navigate(navDirection)
    }*/

   /* private fun moveToCreateOrder(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToProductFilterFragment()
        Navigation.findNavController(binding.btnNewOrder).navigate(navDirection)
    }*/

    private fun moveToOrderHistory(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToOrderListFragment(taskDetails)
        Navigation.findNavController(binding.btnOrderHistory).navigate(navDirection)
    }
    private fun moveToViewAllComplaints(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatSpecificComplaintList(taskDetails,dealerDetails)
        Navigation.findNavController(binding.btnComplaints).navigate(navDirection)
    }

    private fun moveToBeatPayAndTrans(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatPaymentListFragment(taskDetails,dealerDetails)
        Navigation.findNavController(binding.viewAllTransBtn).navigate(navDirection)


    }

    private fun moveToAllBeatReport(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatReportListFragment(taskDetails,dealerDetails)
        Navigation.findNavController(binding.btnBeatReport).navigate(navDirection)
    }
    /*private fun checkPermission(){
        val checkSelfPermission = ContextCompat.checkSelfPermission(baseActivity, android.Manifest.permission.CALL_PHONE)
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(baseActivity, arrayOf(android.Manifest.permission.CAMERA), 1)
        }
    }*/

    /*override fun onResume() {
        super.onResume()
        val arg  = arguments!!.get("beatTaskDetails") as BeatTaskDetails
        callDealerDetails(arg)
    }*/

    private fun callUser(){
        val u = Uri.parse("tel:" + binding.tvDelearCallNo.text.toString())

        val i = Intent(Intent.ACTION_DIAL, u)

        try {
            // Launch the Phone app's dialer with a phone
            // number to dial a call.
            startActivity(i)
        } catch (s: SecurityException) {
            // show() method display the toast with
            // exception message.
            Log.e("Error::","error while opening call");
        }

    }


    var hud: KProgressHUD? = null
    fun  showHud(){
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show();
    }

    fun hide(){
        hud?.dismiss()

    }

    private fun callDealerDetailsApi(taskId: String, dealerId: String, distribId: String) {
        showHud()
        showToastMessage("Dtls "+userId+"\n"+taskId+"\n"+dealerId+"\n"+distribId)
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getDealerDetailsByBeat(
            DealerDetailsRequestParams(userId,taskId,dealerId,distribId)
        )
        responseCall.enqueue(beatTaskDetailsListResponse as Callback<DealerDetailsResponse>)

    }
    fun callDealerDetails(arg:BeatTaskDetails){
        showHud()
        //showToastMessage("Dtls "+userId+"\n"+arg.taskId+"\n"+arg.dealerId+"\n"+arg.distribId)

        //  DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getDealerDetailsByBeat(
            DealerDetailsRequestParams(userId,arg.taskId,arg.dealerId,arg.distribId!!)
        )
        responseCall.enqueue(beatTaskDetailsListResponse as Callback<DealerDetailsResponse>)
    }
    private val beatTaskDetailsListResponse = object : NetworkCallBack<DealerDetailsResponse>(){
        @SuppressLint("SetTextI18n")
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DealerDetailsResponse>) {
           if (response.data!!.status==1){
               if (response.data.count > 0){

                   // activity!!.runOnUiThread(java.lang.Runnable {
                   //showToastMessage("Data has been loaded successfully!!")
                   binding.scrollView.visibility=View.VISIBLE
                   binding.tvNoData.visibility=View.GONE
                   binding.dealerDetails = response.data.scheduleDates[0]
                   dealerDetails = response.data.scheduleDates[0]
                   binding.actAmtVal.text = "₹ "+response.data.actualCollectionAmt
                   binding.actQtyVal.text = "₹ "+response.data.actualOrderAmt
                   if (DataConstant.orderAmt!="") {
                       binding.tarQtyVal.text = "₹ " + DataConstant.orderAmt
                       binding.tvOrdrAmt.text="₹ "+DataConstant.orderAmt
                   }else{
                       binding.tarQtyVal.text = "₹ 0"
                       binding.tvOrdrAmt.text="₹ 0"

                   }
                   if (DataConstant.collectionAmt!="") {
                       binding.collectionAmt.text = "₹ " + DataConstant.collectionAmt
                       binding.tarAmtVal.text = "₹ "+DataConstant.collectionAmt

                   }else{
                       binding.collectionAmt.text = "₹ 0"
                       binding.tarAmtVal.text = "₹ 0 "

                   }
                   binding.dealerName.text = DataConstant.name
                   binding.type.text = DataConstant.nameValue
                   /* binding.tvOrdrAmt.text="₹ "+DataConstant.orderAmt
                    binding.collectionAmt.text="₹ "+DataConstant.collectionAmt*/

                   if (response.data.scheduleDates[0].Grade!=null) {
                       binding.gradeval.text = response.data.scheduleDates[0].Grade
                       DataConstant.dealerGrade = response.data.scheduleDates[0].Grade!!

                   }else{
                       binding.gradeval.text = ""
                       DataConstant.dealerGrade = ""

                   }
                   if (response.data.scheduleDates[0].lati !="" && response.data.scheduleDates[0].longi !=""){
                       latitude = response.data.scheduleDates[0].lati!!.toDouble()
                       longitude=response.data.scheduleDates[0].longi!!.toDouble()
                       checkRuntimePermission()
                   }else{
                       binding.btnBeatReport.isEnabled=true
                   }
                   //checkRuntimePermission()

                   //binding.etTaskAssigned.setText("  "+taskDetails.BSD_Work_Assg_Comment.toString())

                   hide()
               }
               else{
                   showToastMessage("no data found!!")
                   binding.scrollView.visibility=View.GONE
                   binding.tvNoData.visibility=View.VISIBLE
               }
           }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

}


