package com.velectico.rbm.base.views

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R
import com.velectico.rbm.database.Constant_Cart_DB
import com.velectico.rbm.database.DB_Manager
import com.velectico.rbm.database.Helper_Cart_DB
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.SharedPreferencesClass
import com.velectico.rbm.utils.TrackerGPS_V3
import com.velectico.rbm.utils.TrackerGPS_V3.LocationCallBack
import java.util.*


/**
 * Created by mymacbookpro on 2020-04-26
 * Base class of all the activities within this project
 */
abstract class BaseActivity : AppCompatActivity(), LocationCallBack{
    var gps_v3: TrackerGPS_V3? = null
    var db_helper: Helper_Cart_DB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
       // window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ViewDataBinding>(this, getLayout())
        init(savedInstanceState, binding)
        db_helper= Helper_Cart_DB(this);
        DB_Manager.initializeInstance(db_helper);
        this.deleteDatabase(Constant_Cart_DB.CART_DB_NAME);
        gps_v3 = TrackerGPS_V3(this)
        /*if (SharedPreferencesClass.retriveData(this,"SUM_ScreenShot_Lock").toString()=="Y"){
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        }*/
        /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);*/
      /*  (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .clearApplicationUserData()*/
    }

    protected abstract fun getLayout():Int

    protected abstract fun init(savedInstanceState: Bundle?, binding:ViewDataBinding)

    protected fun showToastMessage(message:String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showAlertDialog(msg:String?){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(getString(R.string.alert_title))
            .setMessage(msg)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.ok_button)) {d, _ -> }
            .show()
    }

    fun addFragment(fragment: BaseFragment, viewId: Int){
        supportFragmentManager.beginTransaction()
            .add(viewId, fragment, fragment::class.java.simpleName)
            .commit()
    }

    fun addFragmentWithBackStack(fragment: BaseFragment, viewId: Int, tag:String){
        supportFragmentManager.beginTransaction()
            .add(viewId, fragment, fragment::class.java.simpleName)
            .addToBackStack(tag)
            .commit()
    }

    fun replaceFragment(fragment: BaseFragment, tag:String, viewId: Int){
        supportFragmentManager.beginTransaction()
            .replace(viewId, fragment, fragment::class.java.simpleName)
            .commit()
    }

    fun removeFragment(fragment: BaseFragment, tag:String, viewId: Int){
        supportFragmentManager.beginTransaction()
            .remove(fragment)
            .commitNow()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun updateLocation(lat: Double, lng: Double) {
        initializeAddress(lat, lng)
        Log.e("LatLong", "updateLocation: $lat\n$lng")
        /*Toast.makeText(
            this, """
     $lat
     $lng
     """.trimIndent(), Toast.LENGTH_SHORT
        ).show()*/
        DataConstant.lat = lat
        DataConstant.lng = lng
    }

    open fun initializeAddress(la: Double, lo: Double) {
        Log.d("data", "Inside")
        val geocoder: Geocoder
        val addresses: List<Address>?
        geocoder = Geocoder(this, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(
                la,
                lo,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            Log.d("Address", "initializeAddress: $la\t$lo")
            if (addresses == null || addresses.size == 0) {
                //getLocationFromWeb(lat, lon);
            } else {
                val address = addresses[0]
                val addressFragments =
                    ArrayList<String?>()
                val gps_Locality = address.locality
                val gps_AdminArea = address.adminArea
                val gps_postalCode = address.postalCode
                val gps_country = address.countryName
                val locationData =
                    gps_Locality + gps_AdminArea + gps_postalCode + gps_country
                Log.d("data1 >", locationData)
                Log.d("data1 >>", address.subAdminArea + address.subLocality)
                for (i in 0..address.maxAddressLineIndex) {
                    addressFragments.add(address.getAddressLine(i))
                    Log.d(
                        "Address",
                        "initializeAddress: " + i + ": " + address.getAddressLine(i)
                    )
                }
                val mapAdd = TextUtils.join(" ", addressFragments)
                val city_name = gps_Locality.toString()
                val postal_code = gps_postalCode.toString()
                //mapAdd = tempAd;//addresses.get(0).getAddressLine(1);
                Log.d("data2 >", mapAdd)
                // showAddress.setText(city_name);
            }
        } catch (e: Exception) {
            e.printStackTrace()
            /*if (mapAdd.contentEquals("WebService.NO_ADDRESS_FOUND") || mapAdd.contentEquals(" ") || mapAdd.isEmpty()) {
                getLocationFromWeb(lat, lon);
            }*/
        }
    }
}