package com.velectico.rbm.dealer.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentAddDealerBinding
import com.velectico.rbm.databinding.FragmentDealerImageBinding
import com.velectico.rbm.dealer.model.AddDealerResponse
import com.velectico.rbm.expense.model.SuccessResponse
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.AppApiClient
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.order.views.OrderCheckOutFragmentDirections
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.ArrayList
import java.util.HashMap


class DealerImageFragment :  BaseFragment() {
    private lateinit var binding: FragmentDealerImageBinding
    lateinit var imageUri: Uri
    var imageFile: File? = null
    var userId=""
    override fun getLayout(): Int {
        return R.layout.fragment_dealer_image
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentDealerImageBinding
        if (RBMLubricantsApplication.globalRole == "Team" ){
            userId = GloblalDataRepository.getInstance().teamUserId

        }
        else{
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        checkAndRequestPermissions()
        binding.btnCaptureImg.setOnClickListener{
            openCamera()

        }

        binding.btnSelectImg.setOnClickListener{
            openGallery()

        }

        if (arguments!!.getString("dealerImage")!=""){
            binding.rlImg.visibility=View.VISIBLE
            Picasso.with(context).load(
                arguments!!.getString("dealerImage")
            )
                .skipMemoryCache() //.placeholder(R.drawable.place_holder)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivDealerImg, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        binding.contentProgressBar.visibility = View.GONE
                    }

                    override fun onError() {
                        binding.contentProgressBar.visibility = View.GONE

                    }
                })
        }else{
            binding.rlImg.visibility=View.GONE

        }



        binding.btnAdd.setOnClickListener {
            if (imageFile==null){
                showToastMessage("Please Select Image")
            }else{
                showHud()
                addDealerImage(arguments!!.getString("DD_ID"),userId,  imageFile!!)

            }
        }
    }

    private fun addDealerImage(dd_id: String?, loggedInUserId: String, imageFile: File) {
        val ddId: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            dd_id!!
        )
        val userId: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            loggedInUserId
        )
        var filePart: MultipartBody.Part? = null
        if (imageFile != null) {
            filePart = MultipartBody.Part.createFormData(
                "fileName",
                imageFile.getName(),
                RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
            )
        }
        val responseCall: Call<AddDealerResponse> = AppApiClient.getInstance().addDealerInfoImage(
            ddId, userId,
             filePart
        )
        responseCall.enqueue(addDealerImageResponse as Callback<AddDealerResponse>)

    }
    private val addDealerImageResponse = object : NetworkCallBack<AddDealerResponse>(){
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<AddDealerResponse>) {
            hide()

            if (response.data!!.status ==1){
                var dialog = Dialog(context!!)
                dialog.setContentView(R.layout.add_dialog_view)
                val handler = Handler()
                val runnable = Runnable {
                    if (dialog.isShowing()) {
                        dialog.dismiss()
                        val navDirection =
                            DealerImageFragmentDirections.actionDealerImageFragmentToDealerListFragment()
                        Navigation.findNavController(binding.btnAdd).navigate(navDirection)
                    } else {
                        dialog.show()
                    }
                }
                handler.postDelayed(runnable, 1000)
                dialog.show()
                //showToastMessage("Dealer Image Added Successfully... ")
                //activity!!.onBackPressed()


            }else{
                showToastMessage(response.data.respMessage)
            }


        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
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

}