package com.velectico.rbm.navdrawer.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.beats.model.BeatIdentificationParams
import com.velectico.rbm.beats.model.BeatIdentificationResponse
import com.velectico.rbm.databinding.ActivityDashboardBinding
import com.velectico.rbm.loginreg.model.LoginResponse
import com.velectico.rbm.loginreg.view.LoginActivity
import com.velectico.rbm.loginreg.viewmodel.LoginViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.*
import com.velectico.rbm.utils.SharedPreferencesClass.clearData
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer


/**
 * Created by mymacbookpro on 2020-04-26
 * Dashboard of the project
 */
class DashboardActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding: ActivityDashboardBinding
    lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private var loginResponse:LoginResponse? = null
    var doublePressedBackToExit = false
    var hView: View? = null
    var role=""
    private lateinit var loginViewModel: LoginViewModel

    override fun getLayout(): Int  = R.layout.activity_dashboard

    override fun init(savedInstanceState: Bundle?, binding: ViewDataBinding) {
        this.binding = binding as ActivityDashboardBinding
        RBMLubricantsApplication.fromBeat = ""
        RBMLubricantsApplication.globalRole = ""
        intent?.let {
            loginResponse = it.getParcelableExtra((EXTRA_LOGIN_RESPONSE))
            Log.e("test","loginResponse-->"+loginResponse)

           // Log.e("test","loginResponse-->"+loginResponse?.status)
        }
        role= SharedPreferencesClass.retriveData(applicationContext,"UM_Role").toString()

        setUp()

        getLoginInfo()


    }

    private fun getLoginInfo() {
        loginViewModel = LoginViewModel.getInstance(this)

        loginViewModel.loginAPICall(SharedPreferencesClass.retriveData(this,"UM_Login_Id").toString(),
            SharedPreferencesClass.retriveData(this,"UM_Pass").toString())
        loginViewModel.userDataResponse.observe(this, Observer { listResponse ->
            listResponse?.let {
                onLoginSuccess(it)
            }
        })
    }
    private fun onLoginSuccess(response: LoginResponse?) {

        SharedPreferencesClass.insertData(this,"SUM_Attendance_Lock",  response?.userDetails?.get(0)?.SUM_Attendance_Lock);
        SharedPreferencesClass.insertData(this,"SUM_Location_Lock",  response?.userDetails?.get(0)?.SUM_Location_Lock);
        SharedPreferencesClass.insertData(this,"SUM_ScreenShot_Lock",  response?.userDetails?.get(0)?.SUM_ScreenShot_Lock);

    }

//    override fun onSupportNavigateUp(): Boolean {
//        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }

    private fun setUp() {
        toolbar = binding.toolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        hView = navigationView.getHeaderView(0);
        var appCompatTextViewName = hView!!.findViewById(R.id.appCompatTextViewName) as AppCompatTextView
        var appCompatTextViewEmail = hView!!.findViewById(R.id.appCompatTextViewEmail) as AppCompatTextView
        var appCompatTextViewMobile = hView!!.findViewById(R.id.appCompatTextViewMobile) as AppCompatTextView


        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(navigationView, navController)

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        val prevDate = SharedPreferenceUtils.getData(this,"LAST_ATTENDANCE")
        val format =  SimpleDateFormat("dd/M/yyyy");
        val format2 =  SimpleDateFormat("dd/M/yyyy");
        val attData = SharedPreferenceUtils.getCountAttendance(this,"Attendance")
       // showToastMessage(attData)
        if (prevDate==currentDate) {
            if (SharedPreferencesClass.retriveData(this, "SUM_Attendance_Lock")
                    .toString() == "Y"
            ) {
                if (attData == "1") {
                    navigationView.isEnabled=true
                    navigationView.setNavigationItemSelectedListener(this)

                }else if (attData == "2") {
                    navigationView.isEnabled=false

                }else{
                    navigationView.isEnabled=true
                    navigationView.setNavigationItemSelectedListener(this)

                }
            }else{
                navigationView.isEnabled=true
                navigationView.setNavigationItemSelectedListener(this)

            }
        }else{
            if (SharedPreferencesClass.retriveData(this,"SUM_Attendance_Lock").toString()=="Y") {
                navigationView.isEnabled=false

            }else{
                navigationView.isEnabled=true
                navigationView.setNavigationItemSelectedListener(this)

            }
        }

        navigationView.menu.removeItem(R.id.nav_order)

        if(SharedPreferencesClass.retriveData(applicationContext,"UM_Role").toString()=="M"){
            navigationView.menu.removeItem(R.id.nav_leave)
            navigationView.menu.removeItem(R.id.nav_order)
        }
        appCompatTextViewName.text=SharedPreferencesClass.retriveData(applicationContext,"UM_Name").toString()
        appCompatTextViewEmail.text=SharedPreferencesClass.retriveData(applicationContext,"UM_Email").toString()
        appCompatTextViewMobile.text=SharedPreferencesClass.retriveData(applicationContext,"UM_Phone").toString()


        manageNavDrawerPrivilege(SharedPreferencesClass.retriveData(applicationContext,"UM_Role").toString())

    }





    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout)
        toggleDrawer()
    }

    private fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Drawer is open
        }
        else
        {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        menuItem.isChecked = true
        drawerLayout.closeDrawers()

        when (menuItem.itemId) {
           /* R.id.first -> navController.navigate(R.id.firstFragment)

            R.id.second -> navController.navigate(R.id.secondFragment)

            R.id.third -> navController.navigate(R.id.thirdFragment)*/
            R.id.nav_home -> navController.navigate((R.id.homeFragment))

           // R.id.nav_product -> goProduct()

            R.id.nav_product -> gotoorderFilter()//navController.navigate(R.id.productFilterFragment)

            R.id.nav_beat -> navController.navigate(R.id.dateWiseBeatListFragment)

            R.id.nav_leave -> navController.navigate(R.id.leaveListFragment)

            R.id.nav_expense -> navController.navigate(R.id.expenseListFragment)

            R.id.nav_myprofile -> navController.navigate(R.id.userProfileFragment)

            //R.id.nav_order -> navController.navigate(R.id.orderCreateFragment)
            //R.id.nav_order -> navController.navigate(R.id.orderListFragment)
            R.id.nav_payment -> navController.navigate(R.id.fragmentPaymentList)
            R.id.nav_payment_confirmation -> navController.navigate(R.id.paymentConfirmationFragment)
            R.id.nav_complaints -> navController.navigate(R.id.complaintList)
            //R.id.nav_performance -> commingSoonToast()
            R.id.nav_scanQRCode -> navController.navigate(R.id.qrcodeScanner)
            R.id.nav_resetPassword -> navController.navigate(R.id.resetPasswordFragment)

            R.id.nav_logout -> logout()
        }
        return true
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

    private fun logout() {
        SharedPreferencesClass.clearData(applicationContext)
       /* SharedPreferencesClass.deleteData(applicationContext,"attendance_in")
        SharedPreferencesClass.deleteData(applicationContext,"attendance_out")*/
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

    }




    public fun commingSoonToast(){
        Toast.makeText(applicationContext,"Work in-progress.Feature Will be available shortly!",Toast.LENGTH_SHORT).show()
    }


  public fun gotoorderFilter(){
        RBMLubricantsApplication.filterFrom = "Product"
        navController.navigate(R.id.productFilterFragment)
    }



    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == SALES_LEAD_ROLE || menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == SALES_PERSON_ROLE){
            inflater.inflate(R.menu.attendance_btn, menu)
        }

      /*  if(TEMP_CURRENT_LOGGED_IN == SALES_LEAD_ROLE || TEMP_CURRENT_LOGGED_IN == SALES_PERSON_ROLE){
            inflater.inflate(R.menu.attendance_btn, menu)
        }*/

        return true
    }*/


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null && item.itemId === android.R.id.home) {
            toggle()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggle() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }



    fun manageNavDrawerPrivilege(uRole:String){

        when (uRole) {
        //when (TEMP_CURRENT_LOGGED_IN) {

            SALES_LEAD_ROLE ->{

                navigationView.menu.removeItem(R.id.nav_scanQRCode)
                //navigationView.menu.removeItem(R.id.nav_payment_confirmation)

            }

            SALES_PERSON_ROLE ->{

                navigationView.menu.removeItem(R.id.nav_scanQRCode)
            }


            DISTRIBUTER_ROLE ->{
                navigationView.menu.removeItem(R.id.nav_scanQRCode)
                navigationView.menu.removeItem(R.id.nav_payment_confirmation)
                navigationView.menu.removeItem(R.id.nav_beat)
                navigationView.menu.removeItem(R.id.nav_leave)
                navigationView.menu.removeItem(R.id.nav_expense)
            }
            DEALER_ROLE ->{
                navigationView.menu.removeItem(R.id.nav_order)
                navigationView.menu.removeItem(R.id.nav_leave)
                navigationView.menu.removeItem(R.id.nav_expense)
                navigationView.menu.removeItem(R.id.nav_payment)
                navigationView.menu.removeItem(R.id.nav_myprofile)
                navigationView.menu.removeItem(R.id.nav_product)
                navigationView.menu.removeItem(R.id.nav_beat)
                navigationView.menu.removeItem(R.id.nav_payment_confirmation)

            }
            MECHANIC_ROLE ->{
                navigationView.menu.removeItem(R.id.nav_order)
                navigationView.menu.removeItem(R.id.nav_beat)
                navigationView.menu.removeItem(R.id.nav_expense)
                navigationView.menu.removeItem(R.id.nav_leave)
                navigationView.menu.removeItem(R.id.nav_payment)
                navigationView.menu.removeItem(R.id.nav_myprofile)
                navigationView.menu.removeItem(R.id.nav_payment_confirmation)


            }
            else -> { // Note the block
                print("x is neither 1 nor 2")
            }
        }
    }



}