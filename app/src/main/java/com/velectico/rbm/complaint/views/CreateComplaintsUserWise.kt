package com.velectico.rbm.complaint.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.complaint.model.ComplainListDetails
import com.velectico.rbm.databinding.FragmentCreateComplaintsUserWiseBinding
import com.velectico.rbm.dealer.model.AreaDetails
import com.velectico.rbm.dealer.model.AreaResponse
import com.velectico.rbm.dealer.model.DealerAreaParams
import com.velectico.rbm.expense.model.SuccessResponse
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.manager.AppApiClient
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CreateComplaintsUserWise : BaseFragment() {
    private lateinit var binding: FragmentCreateComplaintsUserWiseBinding
    var complainDetail = ComplainListDetails()
    var prodName = ""
    var complnType = ""
    var dealerId = ""
    var distribId = ""
    var mechId = ""
    var userId=""
    var role=""
    var umId=""
    var taskId="0"
    lateinit var imageUri: Uri
    var imageFile: File? = null
    var areaValue=""


    override fun getLayout(): Int {
        return R.layout.fragment_create_complaints_user_wise
    }


    @SuppressLint("SetTextI18n", "UseRequireInsteadOfGet")
    override fun init(binding: ViewDataBinding) {

        this.binding = binding as FragmentCreateComplaintsUserWiseBinding
        initHud()

        role= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Role").toString()
        umId= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_ID").toString()
        if (role == DISTRIBUTER_ROLE ||
            role == MECHANIC_ROLE ||
            role == DEALER_ROLE
        ) {

            binding.llSpinner11.visibility = View.GONE
            binding.llSpinnerDealDist.visibility = View.GONE
        }
        if (role == DISTRIBUTER_ROLE) {
            distribId = umId
            dealerId=""
            mechId=""
        }
        if (role == DEALER_ROLE) {
            dealerId = umId
            distribId=""
            mechId=""

        }
        if (role == MECHANIC_ROLE) {
            mechId = umId
            dealerId=""
            distribId=""
        }
       // showToastMessage(complainDetail.toString())
        complainDetail = arguments!!.get("complainDetail") as ComplainListDetails
        if (complainDetail.CR_ID != null) {

            binding.inputBatchno.setText(complainDetail.CR_Batch_no.toString())
            binding.inputQuantity.setText(complainDetail.CR_Qty.toString())
            binding.inputRemarks.setText(complainDetail.CR_Remarks.toString())
            binding.inputRemarks.isEnabled=false
            binding.inputQuantity.isEnabled=false
            binding.inputBatchno.isEnabled=false
            binding.btnSubmit.visibility = View.GONE
            binding.llSelect.visibility=View.GONE
            binding.btnCaptureImg.visibility = View.GONE
            binding.btnSelectImg.visibility = View.GONE
            //Picasso.get().load(complainDetail.imagePath).fit().into(binding.ivExpBill)
            Picasso.with(context).load(complainDetail.imagePath)
                .skipMemoryCache()
                .placeholder(R.drawable.faded_logo_bg)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivExpBill)
            binding.llSpinner33.visibility = View.VISIBLE
            binding.llSpinner22.visibility = View.VISIBLE
            binding.llSpinner11.visibility = View.GONE
            binding.llSpinnerDealDist.visibility = View.GONE
            binding.spinner33.visibility = View.GONE
            binding.spinner22.visibility = View.GONE
            binding.tvComplainType.visibility = View.VISIBLE
            binding.tvPdtName.visibility = View.VISIBLE
            binding.tvComplainType.text="Complain Type : "+complainDetail.ComplaintType.toString()
            binding.tvPdtName.text="Product Name : "+complainDetail.prodName.toString()


        }else{
            checkAndRequestPermissions()

        }
        if (RBMLubricantsApplication.globalRole == "Team" ){
            userId = GloblalDataRepository.getInstance().teamUserId
        }
        else{
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }



        callApi("Complain Types")
        callApi("Brand Name")
        //callApiArea(userId)


        binding.spinnerTp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (binding.spinnerTp.selectedItem.toString().equals("Dealer")) {
                        binding.llArea.visibility=View.VISIBLE
                        binding.llSpinnerDealDist.visibility=View.GONE

                        callApiArea(userId)
                        showToastMessage("Select Area")

                    }else if (binding.spinnerTp.selectedItem.toString().equals("Distributor")) {
                        binding.llArea.visibility=View.GONE
                        binding.llSpinnerDealDist.visibility = View.GONE


                        callDistApi()



                    } else {
                        binding.llArea.visibility=View.GONE


                    }
                    /*if (binding.spinnerTp.selectedItem.toString().equals("Dealer")){
                        //showToastMessage("Select Payment Mode")
                        callDealApi()

                    }else {
                        callDistApi()


                    }*/


                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }

        binding.spinnerDealDist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {

                if (binding.spinnerTp.selectedItem == "Dealer"){
                    if (binding.spinnerDealDist.selectedItem=="Select Dealer Name"){

                    }else{
                        val x = dealNameList[position-1]
                        dealerId = x.UM_ID!!
                        distribId=""
                        /*showToastMessage("dealId"+dealerId)
                        showToastMessage("distId"+distribId)*/
                    }


                }
                else if (binding.spinnerTp.selectedItem == "Distributor"){
                    if (binding.spinnerDealDist.selectedItem=="Select Distributor Name"){

                    }else{
                        val x = distNameList[position-1]
                        distribId = x.UM_ID!!
                        dealerId=""
                        /*showToastMessage("dealId"+dealerId)
                        showToastMessage("distId"+distribId)*/
                    }

                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        binding.btnCaptureImg.setOnClickListener{
            openCamera()

        }

        binding.btnSelectImg.setOnClickListener{
            openGallery()

        }
        binding.btnSubmit.setOnClickListener {
            saveComplaint()
        }
    }

    private fun callApiArea(userId: String) {
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getArea(
            DealerAreaParams(userId)
        )
        responseCall.enqueue(areaResponse as Callback<AreaResponse>)

    }

    var areaList: List<AreaDetails> = emptyList<AreaDetails>()

    val areaResponse = object : NetworkCallBack<AreaResponse>() {
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<AreaResponse>) {
            response.data?.status?.let { status ->

                hide()
                areaList = response.data.AreaList
                var statList1: MutableList<String> = java.util.ArrayList()
                statList1.add("Select Area")
                var statList: MutableList<String> = java.util.ArrayList()
                Collections.sort(areaList,
                    Comparator { o1, o2 -> o1.AM_Area_Name!!.compareTo(o2.AM_Area_Name!!) })
                for (i in areaList){
                    //showToastMessage(i.toString())
                    statList.add(i.AM_Area_Name!!)
                }
                statList = (statList1 + statList).toMutableList()
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerArea.adapter = adapter2

                binding.spinnerArea.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (binding.spinnerArea.selectedItem == "Select Area") {

                            } else {
                                val x = areaList[position - 1]
                                areaValue = x.AM_ID!!
                                callDealApi(userId, areaValue)
                            }

                        }

                        override fun onNothingSelected(adapterView: AdapterView<*>) {}
                    }
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }

    private fun openGallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, 1)
    }

    private fun openCamera() {

        checkPermission(Manifest.permission.CAMERA, 0);
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> if (resultCode === Activity.RESULT_OK) {
                val extras: Bundle = data?.extras!!
                val imageBitmap = extras["data"] as Bitmap?
                binding.ivExpBill.setImageBitmap(imageBitmap)
                imageUri = getImageUri(context, imageBitmap)
                imageFile = File(getRealPathFromURI(imageUri))


                // Get the User Selected Image as Bitmap from the static method of ImagePicker class
                //Bitmap bitmap = ImagePicker.getImageFromResult(this.getActivity(), resultCode, data);

                // Upload the Bitmap to ImageView
                Log.e("Image File", "onActivityResult: $imageFile")
                // imageUri = imageBitmap!!
                /*imageFile=File(imageBitmap.toString())
                showToastMessage(imageFile.toString())*/

            }
            1 -> if (resultCode === Activity.RESULT_OK) {
                val selectedImage: Uri? = data?.data
                binding.ivExpBill.setImageURI(selectedImage)
                if (selectedImage != null) {
                    imageUri = selectedImage
                    imageFile = File(getRealPathFromURI(imageUri))
                    Log.e("Image File", "onActivityResult: $imageFile")

                }
            }

        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun getImageUri(context: Context?, imageBitmap: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            context!!.getContentResolver(),
            imageBitmap,
            "Title",
            null
        )
        return Uri.parse(path)

    }
    @SuppressLint("UseRequireInsteadOfGet")
    fun getRealPathFromURI(uri: Uri?): String? {
        val cursor: Cursor =
            context!!.getContentResolver().query(uri!!, null, null, null, null)!!
        cursor.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(activity as BaseActivity, permission)
            == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                activity as BaseActivity, arrayOf(permission),
                requestCode
            )
        } else {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, 0) //zero can be replaced with any action code

        }
    }


     fun saveComplaint() {
        if (binding.spinner33.selectedItem == "Select Complain Type") {
            showToastMessage("Select Complain Type")

        }else if (binding.spinner22.selectedItem == "Select Product Name") {
            showToastMessage("Select Product Name")

        }
        else {
           //
            if (imageFile!=null) {
               // imageFile = File(imageUri.getPath())
                showHud()
                addComplaint(userId, "0", complnType, prodName,dealerId, distribId,mechId,
                    binding.inputQuantity.text.toString().trim(),
                    binding.inputBatchno.text.toString().trim(),
                    binding.inputRemarks.text.toString().trim(), imageFile)
            }else{
                showHud()
                addComplaint(userId, "0", complnType, prodName,dealerId, distribId,mechId,
                    binding.inputQuantity.text.toString().trim(),
                    binding.inputBatchno.text.toString().trim(),
                    binding.inputRemarks.text.toString().trim(), null)
            }

        }

    }
    fun addComplaint(
        userId: String,
        taskId: String,
        complnType: String,
        prodName: String,
        dealerId: String,
        distributorId: String,
        mechanicId: String,
        qty: String,
        batchNo: String,
        remark: String,
        imageFile: File?
    ) {
        val user_Id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            userId
        )
        val task_Id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            taskId
        )

        val compln_Type: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            complnType
        )
        val prod_Name: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            prodName
        )
        val dealer_Id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            dealerId
        )
        val distributor_Id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            distributorId
        )
        val mechanic_Id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            mechanicId
        )
        val quantity: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            qty
        )

        val batch_No: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            batchNo
        )
        val reMark: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            remark
        )
        var filePart: MultipartBody.Part? = null
        if (imageFile != null) {
            filePart = MultipartBody.Part.createFormData(
                "recPhoto",
                imageFile.getName(),
                RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
            )
        }else{
            filePart=null
        }
        val responseCall: Call<SuccessResponse> = AppApiClient.getInstance().addComplaint(
            user_Id, task_Id, compln_Type, prod_Name,dealer_Id, distributor_Id,mechanic_Id,
            quantity,
            batch_No,
            reMark, filePart
        )
        responseCall.enqueue(addComplainResponse as Callback<SuccessResponse>)

    }


    private val addComplainResponse = object : NetworkCallBack<SuccessResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<SuccessResponse>
        ) {
            hide()
            if (response.data!!.status==1){
                showToastMessage(response.data.respMessage.toString())
                activity!!.onBackPressed()

            }else{
                showToastMessage(response.data.respMessage.toString())

            }
        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
           // showToastMessage(error.toString())
           // showToastMessage("network Error")

        }

    }

    private fun checkAndRequestPermissions(): Boolean {
        val cameraPermission =
            ContextCompat.checkSelfPermission(context as Context, Manifest.permission.CAMERA)
        val externalStorageWrite = ContextCompat.checkSelfPermission(
            context as Context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val externalStorageRead = ContextCompat.checkSelfPermission(
            context as Context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val listPermissionsNeeded: MutableList<String> =
            ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (externalStorageWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (externalStorageRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                activity as BaseActivity,
                listPermissionsNeeded.toTypedArray(),
                2
            )
            return false
        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        Log.d("TAG", "Permission callback called-------")
        when (requestCode) {
            2 -> {
                val perms: MutableMap<String, Int> =
                    HashMap()
                // Initialize the map with both permissions
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED

                // Fill with actual results from user
                if (grantResults.size > 0) {
                    var i = 0
                    while (i < permissions.size) {
                        perms[permissions[i]] = grantResults[i]
                        i++
                    }
                    // Check for both permissions
                    if (perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                    ) {
                        Log.d("TAG", "sms & location services permission granted")
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("TAG", "Some permissions are not granted ask again ")
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                activity as BaseActivity,
                                Manifest.permission.SEND_SMS
                            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                                activity as BaseActivity,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        ) {
                            showDialogOK("SMS and Location Services Permission required for this app",
                                DialogInterface.OnClickListener { dialog, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                        DialogInterface.BUTTON_NEGATIVE -> {
                                        }
                                    }
                                })
                        } else {
                            showToastMessage("Go to settings and enable permissions")

                        }
                    }
                }
            }
        }
    }

    private fun showDialogOK(
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(context as Context)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }
    fun callApi(type: String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        when (type) {
            "Complain Types" -> {
                responseCall.enqueue(complainTypeResponse as Callback<OrderVSQualityResponse>)
            }
            "Brand Name" -> {
                responseCall.enqueue(prodNameResponse as Callback<OrderVSQualityResponse>)
            }


        }
    }

    private var complaintTypeList: List<DropdownDetails> = emptyList<DropdownDetails>()
    private val complainTypeResponse = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            if (response.data!!.status==1){
                hide()
                complaintTypeList = response.data.BeatReportList
                if (complaintTypeList.size > 0) {
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Select Complain Type")
                    for (i in complaintTypeList) {
                        statList.add(i.Exp_Head_Name!!)
                    }
                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding.spinner33.adapter = adapter2
                    binding.spinner33.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding.spinner33.selectedItem == "Select Complain Type") {

                                } else {
                                    complnType = complaintTypeList[position - 1].Exp_Head_Id!!
                                    //showToastMessage("ComId"+complnType)

                                }


                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }

                }
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }
    private var prodNameList: List<DropdownDetails> = emptyList<DropdownDetails>()
    private val prodNameResponse = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                prodNameList = response.data.BeatReportList
                if (prodNameList.size>0) {
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Select Product Name")

                    for (i in prodNameList) {
                        statList.add(i.Exp_Head_Name!!)
                    }
                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding.spinner22.adapter = adapter2

                    binding.spinner22.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding.spinner22.selectedItem == "Select Product Name") {

                                } else {
                                    prodName = prodNameList[position - 1].Exp_Head_Id!!
                                    //showToastMessage("PdtId" + prodName)

                                }


                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }
                }
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    private  var distNameList : List<DistDropdownDetails> = emptyList<DistDropdownDetails>()
    private val distNameResponse = object : NetworkCallBack<DistListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DistListResponse>) {
            response.data?.status?.let { status ->
                //showToastMessage(response.data.DistList.toString())
                hide()
                distNameList  = response.data.DistList
                var statList1: MutableList<String> = ArrayList()
                statList1.add("Select Distributor Name")
                var statList: MutableList<String> = ArrayList()
                Collections.sort(distNameList,
                    Comparator { o1, o2 -> o1.UM_Name!!.compareTo(o2.UM_Name!!) })
                for (i in distNameList){
                    //showToastMessage(i.toString())
                    statList.add(i.UM_Name!!)
                }
               // Collections.sort(statList, String.CASE_INSENSITIVE_ORDER);
                statList = (statList1 + statList).toMutableList()
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }
                binding.spinnerDealDist.adapter = adapter2

                binding.llSpinnerDealDist.visibility=View.VISIBLE

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }


    @SuppressLint("UseRequireInsteadOfGet")
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
            val navDirection = CreateComplaintsUserWiseDirections.actionCreateComplaintsUserWiseToComplaintList()
            Navigation.findNavController(binding.btnSubmit).navigate(navDirection)

        }

    }



    fun callDistApi() {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.distDropDownList(
            DistListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context))
        )

        responseCall.enqueue(distNameResponse as Callback<DistListResponse>)

    }

    private  var dealNameList : List<DealDropdownDetails> = emptyList<DealDropdownDetails>()
    private val dealNameResponse = object : NetworkCallBack<DealListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DealListResponse>) {
            response.data?.status?.let { status ->
                //showToastMessage(response.data.DealList.toString())
                hide()

                dealNameList = response.data.DealList
                var statList1: MutableList<String> = ArrayList()
                statList1.add("Select Dealer Name")
                var statList: MutableList<String> = ArrayList()

                Collections.sort(dealNameList,
                    Comparator { o1, o2 -> o1.UM_Name!!.compareTo(o2.UM_Name!!) })
                for (i in dealNameList){
                    //showToastMessage(i.toString())
                    statList.add(i.UM_Name!!)
                }
                statList = (statList1 + statList).toMutableList()
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerDealDist.adapter = adapter2
                binding.llSpinnerDealDist.visibility=View.VISIBLE

            }



        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callDealApi(userId: String, areaId: String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealDropDownList(
            DealListRequestParams(userId,areaId)
        )

        responseCall.enqueue(dealNameResponse as Callback<DealListResponse>)

    }


    var hud: KProgressHUD? = null
    fun showHud() {
        if (hud != null) {

            hud!!.show()
        }
    }

    fun hide() {
        hud?.dismiss()

    }

}