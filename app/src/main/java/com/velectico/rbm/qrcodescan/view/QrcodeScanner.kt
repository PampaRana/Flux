package com.velectico.rbm.qrcodescan.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.QrcodeScannerFragmentBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.redeem.model.SendQrRequestParams
import com.velectico.rbm.redeem.model.SendQrResponse
import com.velectico.rbm.utils.SharedPreferenceUtils
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Callback


class QrcodeScanner : BaseFragment(){
    private lateinit var binding: QrcodeScannerFragmentBinding
    private lateinit var codeScanner: CodeScanner

    private val RC_CAMERA_PERM = 123
    override fun getLayout(): Int {
        return R.layout.qrcode_scanner_fragment
    }

    override fun init(binding: ViewDataBinding) {

        this.binding = binding as QrcodeScannerFragmentBinding
        checkPermission()
        cameraTask()
        //sendQR("YC9WMH86E15J2ZB","20","50")


    }

    private fun hasCameraPermission():Boolean {
        return EasyPermissions.hasPermissions(baseActivity, Manifest.permission.CAMERA)
    }
    fun cameraTask() {
        if (hasCameraPermission())
        {
            // Have permission, do the thing!
            //Toast.makeText(this, "TODO: Camera things", Toast.LENGTH_LONG).show()
            val scannerView = binding.scannerView
            val activity = requireActivity()
            codeScanner = CodeScanner(activity, scannerView)
            codeScanner.decodeCallback = DecodeCallback {
                activity.runOnUiThread {
                    Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
                    val currentString = it.text
                    val separated =
                        currentString.split(":".toRegex()).toTypedArray()

                    val code=separated[1]

                    val codeParts: List<String> = code.split(".")
                    val codePart1 = codeParts[0]

                    val value=separated[2]
                    val valueParts: List<String> = value.split(".")
                    val valuePart1 = valueParts[0]

                    //Toast.makeText(activity, separated[0], Toast.LENGTH_LONG).show()
                   // Toast.makeText(activity, codePart1, Toast.LENGTH_LONG).show()
                    //Toast.makeText(activity, part2.toString(), Toast.LENGTH_LONG).show()
                    //Toast.makeText(activity, valuePart1, Toast.LENGTH_LONG).show()
                   // Toast.makeText(activity, separated[3], Toast.LENGTH_LONG).show()
                   // separated[1] = separated[1].split(".".toRegex()).toString()

                    sendQR(codePart1.trim(),valuePart1.trim(),separated[3].trim())
                }
            }
            codeScanner.startPreview()
        }
        else
        {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                baseActivity,
                getString(R.string.rationale_camera),
                RC_CAMERA_PERM,
                Manifest.permission.CAMERA)
        }
    }


    private fun checkPermission(){
        val checkSelfPermission = ContextCompat.checkSelfPermission(baseActivity, android.Manifest.permission.CAMERA)
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(baseActivity, arrayOf(android.Manifest.permission.CAMERA), 1)
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


    fun sendQR(QrCode:String,Qrvalue:String,Qrpoint:String){

           // showToastMessage(SharedPreferenceUtils.getLoggedInUserId(context as Context)+"\n"+QrCode+"\n"+Qrvalue+"\n"+Qrpoint)

            showHud()
            val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
            val responseCall = apiInterface.sendQrDetails(
                SendQrRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),QrCode,Qrvalue,Qrpoint)
            )
            responseCall.enqueue(SendQrResponse as Callback<SendQrResponse>)

    }

    private val SendQrResponse = object : NetworkCallBack<SendQrResponse>(){
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<SendQrResponse>
        ) {
            hide()
            if(response.data!!.status==1){
                showToastMessage(response.data.respMessage!!)
                //showToastMessage(response.data.status.toString())
                activity!!.onBackPressed()

            }else{
                showToastMessage(response.data.respMessage!!)
                //showToastMessage(response.data.status.toString())

            }


        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

}
