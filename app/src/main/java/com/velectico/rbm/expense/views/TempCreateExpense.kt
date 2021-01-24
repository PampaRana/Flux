package com.velectico.rbm.expense.views

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

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentAddDealerBinding
import com.velectico.rbm.databinding.FragmentTempCreateExpenseBinding
import com.velectico.rbm.dealer.model.AddDealerResponse
import com.velectico.rbm.dealer.views.DealerImageFragmentDirections
import com.velectico.rbm.expense.model.CreateExpenseResponse
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.AppApiClient
import com.velectico.rbm.network.response.NetworkResponse
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

/**
 * A simple [Fragment] subclass.
 */
class TempCreateExpense : BaseFragment() {
    private lateinit var binding: FragmentTempCreateExpenseBinding
    lateinit var imageUri1: Uri
    var imageFile1: File? = null

    lateinit var imageUri2: Uri
    var imageFile2: File? = null

    lateinit var imageUri3: Uri
    var imageFile3: File? = null

    lateinit var imageUri4: Uri
    var imageFile4: File? = null

    lateinit var imageUri5: Uri
    var imageFile5: File? = null

    lateinit var imageUri6: Uri
    var imageFile6: File? = null

    var galleryImage1=1
    var galleryImage2=2
    var galleryImage3=3
    var galleryImage4=4
    var galleryImage5=5
    var galleryImage6=6

    override fun getLayout(): Int {
        return R.layout.fragment_temp_create_expense
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentTempCreateExpenseBinding

        checkAndRequestPermissions()
        binding.btnCaptureImg1.setOnClickListener{
            openCamera1()

        }
        binding.btnCaptureImg2.setOnClickListener{
            openCamera2()

        }

        binding.btnCaptureImg3.setOnClickListener{
            openCamera3()

        }

        binding.btnCaptureImg4.setOnClickListener{
            openCamera4()

        }
        binding.btnCaptureImg5.setOnClickListener{
            openCamera5()

        }
        binding.btnCaptureImg6.setOnClickListener{
            openCamera6()

        }

        binding.btnSelectImg1.setOnClickListener{
            openGallery1()

        }

        binding.btnSelectImg2.setOnClickListener{
            openGallery2()

        }

        binding.btnSelectImg3.setOnClickListener{
            openGallery3()

        }



        binding.btnSelectImg4.setOnClickListener{
            openGallery4()

        }


        binding.btnSelectImg5.setOnClickListener{
            openGallery5()

        }


        binding.btnSelectImg6.setOnClickListener{
            openGallery6()

        }

        binding.btnGotoList.setOnClickListener {
            val navDirection =  TempCreateExpenseDirections.actionTempCreateExpenseToExpenseListFragment()
            Navigation.findNavController(binding.btnGotoList).navigate(navDirection)

        }
    }

    private fun openCamera1() {

        checkPermission1(Manifest.permission.CAMERA, 0);
    }
    private fun openCamera2() {
        checkPermission2(Manifest.permission.CAMERA, 10);

    }
    private fun openCamera3() {
        checkPermission3(Manifest.permission.CAMERA, 20);
    }

    private fun openCamera4() {
        checkPermission4(Manifest.permission.CAMERA, 30);

    }
    private fun openCamera5() {
        checkPermission5(Manifest.permission.CAMERA, 40);

    }
    private fun openCamera6() {
        checkPermission6(Manifest.permission.CAMERA, 50);

    }


    private fun openGallery1() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, galleryImage1)
    }



    private fun openGallery2() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, galleryImage2)
    }
    private fun openGallery3() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, galleryImage3)
    }



    private fun openGallery4() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, galleryImage4)
    }

    private fun openGallery5() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, galleryImage5)
    }


    private fun openGallery6() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, galleryImage6)
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
    private fun checkPermission1(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(activity as BaseActivity, permission)
            == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                activity as BaseActivity, arrayOf(permission),
                requestCode
            )
        } else {
            val takePicture1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture1, 0) //zero can be replaced with any action code

        }
    }

    private fun checkPermission2(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(activity as BaseActivity, permission)
            == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                activity as BaseActivity, arrayOf(permission),
                requestCode
            )
        } else {
            val takePicture2 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture2, 10) //zero can be replaced with any action code

        }
    }
    private fun checkPermission3(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(activity as BaseActivity, permission)
            == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                activity as BaseActivity, arrayOf(permission),
                requestCode
            )
        } else {
            val takePicture3 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture3, 20) //zero can be replaced with any action code

        }
    }

    private fun checkPermission4(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(activity as BaseActivity, permission)
            == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                activity as BaseActivity, arrayOf(permission),
                requestCode
            )
        } else {
            val takePicture4 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture4, 30) //zero can be replaced with any action code

        }
    }

    private fun checkPermission5(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(activity as BaseActivity, permission)
            == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                activity as BaseActivity, arrayOf(permission),
                requestCode
            )
        } else {
            val takePicture5 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture5, 40) //zero can be replaced with any action code

        }
    }

    private fun checkPermission6(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(activity as BaseActivity, permission)
            == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                activity as BaseActivity, arrayOf(permission),
                requestCode
            )
        } else {
            val takePicture6 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture6, 50) //zero can be replaced with any action code

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
                12
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
            12 -> {
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
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> if (resultCode === Activity.RESULT_OK) {
                val extras: Bundle = data?.extras!!
                val imageBitmap = extras["data"] as Bitmap?
                binding.ivExpBill1.setImageBitmap(imageBitmap)
                imageUri1 = getImageUri(context, imageBitmap)
                imageFile1 = File(getRealPathFromURI(imageUri1))
                showHud()
                uploadImage1(arguments!!.getString("expensId"),
                    SharedPreferenceUtils.getLoggedInUserId(context as Context),"recPhoto1",  imageFile1!!)


            }

            10 -> if (resultCode === Activity.RESULT_OK) {
                val extras: Bundle = data?.extras!!
                val imageBitmap = extras["data"] as Bitmap?
                binding.ivExpBill2.setImageBitmap(imageBitmap)
                imageUri2 = getImageUri(context, imageBitmap)
                imageFile2 = File(getRealPathFromURI(imageUri2))
                 showHud()
                uploadImage2(arguments!!.getString("expensId"),
                   SharedPreferenceUtils.getLoggedInUserId(context as Context),"recPhoto2",  imageFile2!!)
            }
            20 -> if (resultCode === Activity.RESULT_OK) {
                val extras: Bundle = data?.extras!!
                val imageBitmap = extras["data"] as Bitmap?
                binding.ivExpBill3.setImageBitmap(imageBitmap)
                imageUri3 = getImageUri(context, imageBitmap)
                imageFile3 = File(getRealPathFromURI(imageUri3))
                 showHud()
               uploadImage3(arguments!!.getString("expensId"),
                   SharedPreferenceUtils.getLoggedInUserId(context as Context),"recPhoto3",  imageFile3!!)
            }

            30-> if (resultCode === Activity.RESULT_OK) {
                val extras: Bundle = data?.extras!!
                val imageBitmap = extras["data"] as Bitmap?
                binding.ivExpBill4.setImageBitmap(imageBitmap)
                imageUri4 = getImageUri(context, imageBitmap)
                imageFile4 = File(getRealPathFromURI(imageUri4))
                 showHud()
               uploadImage4(arguments!!.getString("expensId"),
                   SharedPreferenceUtils.getLoggedInUserId(context as Context),"recPhoto4",  imageFile4!!)
            }
            40 -> if (resultCode === Activity.RESULT_OK) {
                val extras: Bundle = data?.extras!!
                val imageBitmap = extras["data"] as Bitmap?
                binding.ivExpBill5.setImageBitmap(imageBitmap)
                imageUri5 = getImageUri(context, imageBitmap)
                imageFile5 = File(getRealPathFromURI(imageUri5))
                 showHud()
               uploadImage5(arguments!!.getString("expensId"),
                   SharedPreferenceUtils.getLoggedInUserId(context as Context),"recPhoto5",  imageFile5!!)
            }
            50 -> if (resultCode === Activity.RESULT_OK) {
                val extras: Bundle = data?.extras!!
                val imageBitmap = extras["data"] as Bitmap?
                binding.ivExpBill6.setImageBitmap(imageBitmap)
                imageUri6 = getImageUri(context, imageBitmap)
                imageFile6 = File(getRealPathFromURI(imageUri6))
                 showHud()
                uploadImage6(arguments!!.getString("expensId"),
                   SharedPreferenceUtils.getLoggedInUserId(context as Context),"recPhoto6",  imageFile6!!)

            }

            galleryImage1 -> if (resultCode === Activity.RESULT_OK) {
                val selectedImage: Uri? = data?.data
                binding.ivExpBill1.setImageURI(selectedImage)
                if (selectedImage != null) {
                    imageUri1 = selectedImage
                    imageFile1 = File(getRealPathFromURI(imageUri1))
                    Log.e("Image File", "onActivityResult: $imageFile1")
                    showHud()
                    uploadImage1(arguments!!.getString("expensId"),
                        SharedPreferenceUtils.getLoggedInUserId(context as Context),"recPhoto1",  imageFile1!!)
                }
            }
            galleryImage2 -> if (resultCode === Activity.RESULT_OK) {
                val selectedImage: Uri? = data?.data
                binding.ivExpBill2.setImageURI(selectedImage)
                if (selectedImage != null) {
                    imageUri2 = selectedImage
                    imageFile2 = File(getRealPathFromURI(imageUri2))
                    Log.e("Image File", "onActivityResult: $imageFile2")
                    showHud()
                    uploadImage2(arguments!!.getString("expensId"),
                        SharedPreferenceUtils.getLoggedInUserId(context as Context),"recPhoto2",  imageFile2!!)
                }
            }
            galleryImage3 -> if (resultCode === Activity.RESULT_OK) {
                val selectedImage: Uri? = data?.data
                binding.ivExpBill3.setImageURI(selectedImage)
                if (selectedImage != null) {
                    imageUri3 = selectedImage
                    imageFile3 = File(getRealPathFromURI(imageUri3))
                    Log.e("Image File", "onActivityResult: $imageFile3")
                    showHud()
                    uploadImage3(arguments!!.getString("expensId"),
                        SharedPreferenceUtils.getLoggedInUserId(context as Context),"recPhoto3",  imageFile3!!)
                }
            }
            galleryImage4 -> if (resultCode === Activity.RESULT_OK) {
                val selectedImage: Uri? = data?.data
                binding.ivExpBill4.setImageURI(selectedImage)
                if (selectedImage != null) {
                    imageUri4 = selectedImage
                    imageFile4 = File(getRealPathFromURI(imageUri4))
                    Log.e("Image File", "onActivityResult: $imageFile4")
                    showHud()
                    uploadImage4(arguments!!.getString("expensId"),
                        SharedPreferenceUtils.getLoggedInUserId(context as Context),"recPhoto4",  imageFile4!!)
                }
            }
            galleryImage5 -> if (resultCode === Activity.RESULT_OK) {
                val selectedImage: Uri? = data?.data
                binding.ivExpBill5.setImageURI(selectedImage)
                if (selectedImage != null) {
                    imageUri5 = selectedImage
                    imageFile5 = File(getRealPathFromURI(imageUri5))
                    Log.e("Image File", "onActivityResult: $imageFile5")
                    showHud()
                    uploadImage5(arguments!!.getString("expensId"),
                        SharedPreferenceUtils.getLoggedInUserId(context as Context),"recPhoto5",  imageFile5!!)
                }
            }
            galleryImage6 -> if (resultCode === Activity.RESULT_OK) {
                val selectedImage: Uri? = data?.data
                binding.ivExpBill6.setImageURI(selectedImage)
                if (selectedImage != null) {
                    imageUri6 = selectedImage
                    imageFile6 = File(getRealPathFromURI(imageUri6))
                    Log.e("Image File", "onActivityResult: $imageFile6")
                    showHud()
                    uploadImage6(arguments!!.getString("expensId"),
                        SharedPreferenceUtils.getLoggedInUserId(context as Context),"recPhoto6",  imageFile6!!)
                }
            }



        }
    }
    private fun uploadImage1(expenseId: String?, loggedInUserId: String, photo1: String, imageFile1: File) {
        val expense_Id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            expenseId!!
        )
        val userId: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            loggedInUserId
        )
        val photo_1: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            photo1
        )
        var filePart: MultipartBody.Part? = null
        if (imageFile1 != null) {
            filePart = MultipartBody.Part.createFormData(
                "fileName",
                imageFile1.getName(),
                RequestBody.create("image/*".toMediaTypeOrNull(), imageFile1)
            )
        }
        val responseCall: Call<CreateExpenseResponse> = AppApiClient.getInstance().addExpenseImage(
            expense_Id, userId,photo_1, filePart
        )
        responseCall.enqueue(addExpenseImageResponse as Callback<CreateExpenseResponse>)

    }

    private fun uploadImage2(expenseId: String?, loggedInUserId: String, photo2: String, imageFile2: File) {
        val expense_Id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            expenseId!!
        )
        val userId: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            loggedInUserId
        )
        val photo_1: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            photo2
        )
        var filePart: MultipartBody.Part? = null
        if (imageFile1 != null) {
            filePart = MultipartBody.Part.createFormData(
                "fileName",
                imageFile2.getName(),
                RequestBody.create("image/*".toMediaTypeOrNull(), imageFile2)
            )
        }
        val responseCall: Call<CreateExpenseResponse> = AppApiClient.getInstance().addExpenseImage(
            expense_Id, userId,photo_1, filePart
        )
        responseCall.enqueue(addExpenseImageResponse as Callback<CreateExpenseResponse>)

    }

    private fun uploadImage3(expenseId: String?, loggedInUserId: String, photo3: String, imageFile3: File) {
        val expense_Id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            expenseId!!
        )
        val userId: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            loggedInUserId
        )
        val photo_1: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            photo3
        )
        var filePart: MultipartBody.Part? = null
        if (imageFile1 != null) {
            filePart = MultipartBody.Part.createFormData(
                "fileName",
                imageFile3.getName(),
                RequestBody.create("image/*".toMediaTypeOrNull(), imageFile3)
            )
        }
        val responseCall: Call<CreateExpenseResponse> = AppApiClient.getInstance().addExpenseImage(
            expense_Id, userId,photo_1, filePart
        )
        responseCall.enqueue(addExpenseImageResponse as Callback<CreateExpenseResponse>)

    }
    private fun uploadImage4(expenseId: String?, loggedInUserId: String, photo4: String, imageFile4: File) {

        val expense_Id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            expenseId!!
        )
        val userId: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            loggedInUserId
        )
        val photo_1: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            photo4
        )
        var filePart: MultipartBody.Part? = null
        if (imageFile1 != null) {
            filePart = MultipartBody.Part.createFormData(
                "fileName",
                imageFile4.getName(),
                RequestBody.create("image/*".toMediaTypeOrNull(), imageFile4)
            )
        }
        val responseCall: Call<CreateExpenseResponse> = AppApiClient.getInstance().addExpenseImage(
            expense_Id, userId,photo_1, filePart
        )
        responseCall.enqueue(addExpenseImageResponse as Callback<CreateExpenseResponse>)
    }
    private fun uploadImage5(expenseId: String?, loggedInUserId: String, photo5: String, imageFile5: File) {
        val expense_Id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            expenseId!!
        )
        val userId: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            loggedInUserId
        )
        val photo_1: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            photo5
        )
        var filePart: MultipartBody.Part? = null
        if (imageFile1 != null) {
            filePart = MultipartBody.Part.createFormData(
                "fileName",
                imageFile5.getName(),
                RequestBody.create("image/*".toMediaTypeOrNull(), imageFile5)
            )
        }
        val responseCall: Call<CreateExpenseResponse> = AppApiClient.getInstance().addExpenseImage(
            expense_Id, userId,photo_1, filePart
        )
        responseCall.enqueue(addExpenseImageResponse as Callback<CreateExpenseResponse>)

    }
    private fun uploadImage6(expenseId: String?, loggedInUserId: String, photo6: String, imageFile6: File) {
        val expense_Id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            expenseId!!
        )
        val userId: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            loggedInUserId
        )
        val photo_1: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            photo6
        )
        var filePart: MultipartBody.Part? = null
        if (imageFile1 != null) {
            filePart = MultipartBody.Part.createFormData(
                "fileName",
                imageFile6.getName(),
                RequestBody.create("image/*".toMediaTypeOrNull(), imageFile6)
            )
        }
        val responseCall: Call<CreateExpenseResponse> = AppApiClient.getInstance().addExpenseImage(
            expense_Id, userId,photo_1, filePart
        )
        responseCall.enqueue(addExpenseImageResponse as Callback<CreateExpenseResponse>)

    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun getImageUri(context: Context?, imageBitmap: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 60, bytes)
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

    private val addExpenseImageResponse = object : NetworkCallBack<CreateExpenseResponse>(){
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<CreateExpenseResponse>) {
            hide()

            if (response.data!!.status ==1){
                var dialog = Dialog(context!!)
                dialog.setContentView(R.layout.add_dialog_view)
                val handler = Handler()
                val runnable = Runnable {
                    if (dialog.isShowing()) {
                        dialog.dismiss()
                        showToastMessage(response.data.respMessage.toString())
                    } else {
                        dialog.show()
                    }
                }
                handler.postDelayed(runnable, 1000)
                dialog.show()
                //showToastMessage(response.data.respMessage.toString())
                //activity!!.onBackPressed()


            }else{
                showToastMessage(response.data.respMessage.toString())
            }


        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }
}
