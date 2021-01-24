package com.velectico.rbm.complaint.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.kaopiz.kprogresshud.KProgressHUD
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.complaint.model.ComplainListDetails
import com.velectico.rbm.databinding.FragmentAddComplaintBinding
import com.velectico.rbm.expense.model.SuccessResponse
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.manager.AppApiClient
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*


class AddComplaintFragment : BaseFragment() {
    private lateinit var binding: FragmentAddComplaintBinding
    var taskDetail = BeatTaskDetails()
    var dlrDtl = DealerDetails()
    var userId = ""
    var dealerId = ""
    var taskId = ""
    var distributorId = ""
    var complainDetail = ComplainListDetails()
    var prodName = ""
    var complnType = ""
    var imageFile: File? = null
    var mechId=""
    lateinit var imageUri: Uri

    override fun getLayout(): Int {
        return R.layout.fragment_add_complaint
    }


    @SuppressLint("SetTextI18n")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentAddComplaintBinding
        taskDetail = arguments!!.get("taskDetails") as BeatTaskDetails
        dlrDtl = arguments!!.get("dealerDetails") as DealerDetails

        binding.tvProdNetPrice.text = dlrDtl.dealerPhone.toString()
        binding.tvProdTotalPrice.text = dlrDtl.DM_Contact_Person.toString()
        //binding.tvOrdrAmt.text = "₹" +dlrDtl.orderAmt.toString()
        //binding.collectionAmt.text = "₹" +dlrDtl.collectionAmt.toString()
        if (DataConstant.orderAmt != "") {
            binding.tvOrdrAmt.text = "₹ " + DataConstant.orderAmt
        } else {
            binding.tvOrdrAmt.text = "₹ 0"

        }
        if (DataConstant.collectionAmt != "") {
            binding.collectionAmt.text = "₹ " + DataConstant.collectionAmt

        } else {
            binding.collectionAmt.text = "₹ 0"

        }

        binding.gradeval.text = DataConstant.dealerGrade
        binding.type.text = DataConstant.nameValue
        binding.name.text = DataConstant.name

        if (RBMLubricantsApplication.globalRole == "Team") {

            userId = GloblalDataRepository.getInstance().teamUserId
        } else {
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        if (taskDetail.taskId != null) {
            dealerId = taskDetail.dealerId.toString()
            distributorId = taskDetail.distribId.toString()
            taskId = taskDetail.taskId.toString()


        } else {
            dealerId = DataConstant.dealerId
            distributorId = DataConstant.distributorId
            taskId = DataConstant.taskId

        }
        callApi("Complain Types")
        callApi("Brand Name")
       // checkAndRequestPermissions()


        binding.btnSaveComplain.setOnClickListener {
            if (binding.spinner33.selectedItem == "Select Complain Type") {
                showToastMessage("Select Complain Type")

            } else if (binding.spinner22.selectedItem == "Select Product Name") {
                showToastMessage("Select Product Name")

            } else {
                /*showHud()
                addComplaint(userId, "0", complnType, prodName,dealerId, distributorId,mechId,
                    binding.inputQuantity.text.toString().trim(),
                    binding.inputBatchno.text.toString().trim(),
                    binding.inputRemarks.text.toString().trim(), imageFile)*/
                if (imageFile!=null) {
                    // imageFile = File(imageUri.getPath())
                    showHud()
                    addComplaint(userId, "0", complnType, prodName,dealerId, distributorId,mechId,
                        binding.inputQuantity.text.toString().trim(),
                        binding.inputBatchno.text.toString().trim(),
                        binding.inputRemarks.text.toString().trim(), imageFile)
                }else{
                    showHud()
                    addComplaint(userId, "0", complnType, prodName,dealerId, distributorId,mechId,
                        binding.inputQuantity.text.toString().trim(),
                        binding.inputBatchno.text.toString().trim(),
                        binding.inputRemarks.text.toString().trim(), null)
                }

            }
        }
        binding.btnCaptureImg.setOnClickListener {
            openCamera()

        }

        binding.btnSelectImg.setOnClickListener {
            openGallery()

        }

        complainDetail = arguments!!.get("complainDetail") as ComplainListDetails
        if (complainDetail.CR_ID!= null) {

            binding.inputBatchno.setText(complainDetail.CR_Batch_no.toString())
            binding.inputQuantity.setText(complainDetail.CR_Qty.toString())
            binding.inputRemarks.setText(complainDetail.CR_Remarks.toString())
            binding.inputRemarks.isEnabled=false
            binding.inputQuantity.isEnabled=false
            binding.inputBatchno.isEnabled=false
            binding.btnSaveComplain.visibility = View.GONE
            binding.btnCaptureImg.visibility = View.GONE
            binding.btnSelectImg.visibility = View.GONE
           // Picasso.get().load(complainDetail.imagePath).fit().into(binding.ivExpBill)
            Picasso.with(context).load(complainDetail.imagePath)
                .skipMemoryCache()
                .placeholder(R.drawable.faded_logo_bg)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivExpBill)
            binding.llSpinner33.visibility = View.VISIBLE
            binding.llSpinner22.visibility = View.VISIBLE
            binding.spinner33.visibility = View.GONE
            binding.spinner22.visibility = View.GONE
            binding.tvComplainType.visibility = View.VISIBLE
            binding.tvPdtName.visibility = View.VISIBLE
            binding.tvComplainType.text="Complain Type : "+complainDetail.ComplaintType.toString()
            binding.tvPdtName.text="Product Name : "+complainDetail.prodName.toString()


        }else{
            checkAndRequestPermissions()

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
                Log.e("File", "onActivityResult: "+imageFile )

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
    @SuppressLint("LogNotTimber")
    private fun addComplaint(
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
       // img: String
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

        /*val image: RequestBody = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            img
        )*/
        var filePart: MultipartBody.Part? = null
        if (imageFile != null) {
            filePart = MultipartBody.Part.createFormData(
                "newProfile",
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
        }

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
            hide()
            if (response.data!!.status==1){
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
                                   //showToastMessage("ComId" + complnType)

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

    var hud: KProgressHUD? = null
    fun showHud() {
        if (hud != null) {

            hud!!.show()
        }
    }

    fun hide() {
        hud?.dismiss()

    }

    private var prodNameList: List<DropdownDetails> = emptyList<DropdownDetails>()
    private val prodNameResponse = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            hide()

            if(response.data?.status==1){
                prodNameList = response.data.BeatReportList
                if (prodNameList.size > 0) {
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

}