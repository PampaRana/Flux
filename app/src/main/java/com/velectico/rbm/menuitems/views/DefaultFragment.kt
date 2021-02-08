package com.velectico.rbm.menuitems.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.MenuItemCompat
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.BeatIdentificationParams
import com.velectico.rbm.beats.model.BeatIdentificationResponse
import com.velectico.rbm.beats.model.BeatTaskDetails
import com.velectico.rbm.databinding.DefaultFragmentBinding
import com.velectico.rbm.leave.viewmodel.LeaveViewModel
import com.velectico.rbm.loginreg.viewmodel.LoginViewModel
import com.velectico.rbm.menuitems.viewmodel.AttendancOutResponse
import com.velectico.rbm.menuitems.viewmodel.AttendancResponse
import com.velectico.rbm.menuitems.viewmodel.AttendanceRequestOutParams
import com.velectico.rbm.menuitems.viewmodel.AttendanceRequestParams
import com.velectico.rbm.navdrawer.views.DashboardActivity
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.*
import retrofit2.Callback
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import com.velectico.rbm.loginreg.model.LoginResponse
import androidx.lifecycle.Observer

/**
 * Created by mymacbookpro on 2020-04-26
 * TODO: Add a class header comment!
 */
class DefaultFragment : BaseFragment(){
    private lateinit var binding:DefaultFragmentBinding
    private lateinit var navigationView: NavigationView
    var taskDetails = BeatTaskDetails()
    override fun getLayout(): Int = R.layout.default_fragment
    var role=""
    var userId=""
    var date_list=""
    var ib_attendance_in: ImageView? = null
    var ib_attendance_out: ImageView? = null
    private lateinit var loginViewModel: LoginViewModel

    lateinit var localTime: String
    @SuppressLint("NewApi")
    override fun init(binding: ViewDataBinding) {
        setHasOptionsMenu(true)
        this.binding = binding as DefaultFragmentBinding

        val animationSwing = AnimationUtils.loadAnimation(activity as BaseActivity, R.anim.swing_up_left)

       /* binding.attendanceCard.startAnimation(animationSwing);
        binding.targetCard.startAnimation(animationSwing);
        binding.paymentCard.startAnimation(animationSwing);
        binding.incentiveCard.startAnimation(animationSwing);
        binding.beatCard.startAnimation(animationSwing);
        binding.expenseCard.startAnimation(animationSwing);*/



        printLogMessage(TAG, "init")

        role= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Role").toString()
        userId= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Login_Id").toString()
        getDashBoardPrivilege(role,binding);

        binding.beatButton.setOnClickListener {
            RBMLubricantsApplication.fromBeat = "Beat"
            RBMLubricantsApplication.globalRole = ""
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToDateWiseBeatListFragment("")
            Navigation.findNavController(binding.beatButton).navigate(navDirection)
        }

        binding.orderButton.setOnClickListener {
            RBMLubricantsApplication.fromBeat = ""
            RBMLubricantsApplication.globalRole=""

            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToOrderListFragment(taskDetails)
            Navigation.findNavController(binding.orderButton).navigate(navDirection)
        }

        binding.leaveButton.setOnClickListener {
            RBMLubricantsApplication.globalRole = ""
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToLeaveListFragment()
            Navigation.findNavController(binding.leaveButton).navigate(navDirection)
        }

        binding.reminderButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToReminderListFragment()
            Navigation.findNavController(binding.reminderButton).navigate(navDirection)
        }

        binding.profileButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToUserProfileFragment()
            Navigation.findNavController(binding.profileButton).navigate(navDirection)
        }
        binding.mechProfileButtonLong.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToUserProfileFragment()
            Navigation.findNavController(binding.mechProfileButtonLong).navigate(navDirection)
        }
        binding.teamButton.setOnClickListener {
            RBMLubricantsApplication.globalRole = "Team"
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToTeamDashboard()
            Navigation.findNavController(binding.teamButton).navigate(navDirection)
        }

        binding.expenseButton.setOnClickListener {
            RBMLubricantsApplication.globalRole=""
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToExpenseListFragment()
            Navigation.findNavController(binding.expenseButton).navigate(navDirection)
        }

        binding.complainButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToComplaintList()
            Navigation.findNavController(binding.complainButton).navigate(navDirection)
        }

        binding.profileButtonLong.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToUserProfileFragment()
            Navigation.findNavController(binding.profileButtonLong).navigate(navDirection)
        }

        binding.complainMechButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToComplaintList()
            Navigation.findNavController(binding.complainMechButton).navigate(navDirection)
        }

        binding.complainDealButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToComplaintList()
            Navigation.findNavController(binding.complainDealButton).navigate(navDirection)
        }
        binding.dealMyDtlsButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToUserProfileFragment()
            Navigation.findNavController(binding.dealMyDtlsButton).navigate(navDirection)
        }
        
        binding.scanQRLong.setOnClickListener {
            //showToastMessage("scan")
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToQrDashboardFragment()
            Navigation.findNavController(binding.scanQRLong).navigate(navDirection)
        }
        binding.redeemButton.setOnClickListener {
            //showToastMessage("redeem")
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToRedeemPoints()
            Navigation.findNavController(binding.redeemButton).navigate(navDirection)
        }
        binding.productButton.setOnClickListener {
            RBMLubricantsApplication.filterFrom = "Product"
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToProductFilterFragment()
            Navigation.findNavController(binding.productButton).navigate(navDirection)
        }
        binding.orderDealerButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToOrderListFragment(taskDetails)
            Navigation.findNavController(binding.orderDealerButton).navigate(navDirection)
        }
        binding.paymentDealerButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToFragmentPaymentList()
            Navigation.findNavController(binding.paymentDealerButton).navigate(navDirection)
        }
        binding.paymentButton.setOnClickListener {
            RBMLubricantsApplication.fromBeat = ""
            RBMLubricantsApplication.globalRole=""
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToFragmentPaymentList()
            Navigation.findNavController(binding.paymentButton).navigate(navDirection)
        }


        binding.performanceButton.setOnClickListener {
            RBMLubricantsApplication.fromBeat = ""
            RBMLubricantsApplication.globalRole = ""
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToTeamPerformanceFragment(userId)
            Navigation.findNavController(binding.performanceButton).navigate(navDirection)
        }
        binding.performanceButtonLong.setOnClickListener {
            RBMLubricantsApplication.fromBeat = ""
            RBMLubricantsApplication.globalRole = ""
            val navDirection =
                DefaultFragmentDirections.actionHomeFragmentToTeamPerformanceFragment(userId)
            Navigation.findNavController(binding.performanceButtonLong).navigate(navDirection)


        }
        val currentTime = Calendar.getInstance().time
        val date: DateFormat = SimpleDateFormat("HH:mm a")
        localTime= date.format(currentTime)

        //showToastMessage(localTime)


        /* binding.dealerPerformanceButton.setOnClickListener {
            RBMLubricantsApplication.fromBeat = ""
            RBMLubricantsApplication.globalRole = ""
            val navDirection =
                DefaultFragmentDirections.actionHomeFragmentToTeamPerformanceFragment(userId)
            Navigation.findNavController(binding.dealerPerformanceButton).navigate(navDirection)


        }*/
        /*binding.dealerQrManualButton.setOnClickListener {
            RBMLubricantsApplication.fromBeat = ""
            RBMLubricantsApplication.globalRole = ""
            val navDirection =
                DefaultFragmentDirections.actionHomeFragmentToManualScanFragment()
            Navigation.findNavController(binding.dealerQrManualButton).navigate(navDirection)


        }
        binding.qrManualButton.setOnClickListener {
            RBMLubricantsApplication.fromBeat = ""
            RBMLubricantsApplication.globalRole = ""
            val navDirection =
                DefaultFragmentDirections.actionHomeFragmentToManualScanFragment()
            Navigation.findNavController(binding.qrManualButton).navigate(navDirection)


        }*/

        binding.performButton.setOnClickListener {
            RBMLubricantsApplication.fromBeat = ""
            RBMLubricantsApplication.globalRole = ""
            val navDirection =
                DefaultFragmentDirections.actionHomeFragmentToTeamPerformanceFragment(userId)
            Navigation.findNavController(binding.performButton).navigate(navDirection)


        }
        binding.dealerButton.setOnClickListener {
            RBMLubricantsApplication.fromBeat = ""
            RBMLubricantsApplication.globalRole = ""
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToDealerDashboardFragment()
            Navigation.findNavController(binding.dealerButton).navigate(navDirection)
        }

        binding.orderButtonLong.setOnClickListener {
            RBMLubricantsApplication.fromBeat = ""
            RBMLubricantsApplication.globalRole = ""
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToOrderListFragment(taskDetails)
            Navigation.findNavController(binding.orderButtonLong).navigate(navDirection)
        }
        if (DataConstant.beatCount >= 1) {
            binding.tvNotiCount.visibility = View.VISIBLE
            binding.tvNotiCount.text = DataConstant.beatCount.toString()
        } else {
            binding.tvNotiCount.visibility = View.GONE

        }


        getLoginInfo()

    }
     private fun getLoginInfo() {
            loginViewModel = LoginViewModel.getInstance(activity as BaseActivity)

            loginViewModel.loginAPICall(SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Login_Id").toString(),
                SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Pass").toString())
            loginViewModel.userDataResponse.observe(this, Observer { listResponse ->
                listResponse?.let {
                    onLoginSuccess(it)
                }
            })
        }
        private fun onLoginSuccess(response: LoginResponse?) {

            SharedPreferencesClass.insertData(context as Context,"SUM_Attendance_Lock",  response?.userDetails?.get(0)?.SUM_Attendance_Lock);
            SharedPreferencesClass.insertData(context as Context,"SUM_Location_Lock",  response?.userDetails?.get(0)?.SUM_Location_Lock);
            SharedPreferencesClass.insertData(context as Context,"SUM_ScreenShot_Lock",  response?.userDetails?.get(0)?.SUM_ScreenShot_Lock);

        }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        if(role == SALES_LEAD_ROLE ||role == SALES_PERSON_ROLE){
            inflater.inflate(R.menu.attendance_btn, menu)
            val item = menu.findItem(R.id.action_attendance)
            MenuItemCompat.setActionView(item, R.layout.badge_layout)
            val rl_attendance: RelativeLayout = MenuItemCompat.getActionView(item) as RelativeLayout
            ib_attendance_in = rl_attendance.findViewById(R.id.ib_attendance_in)
            ib_attendance_out = rl_attendance.findViewById(R.id.ib_attendance_out)

            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())
            val prevDate = SharedPreferenceUtils.getData(context!!,"LAST_ATTENDANCE")
            val format =  SimpleDateFormat("dd/M/yyyy");
            val format2 =  SimpleDateFormat("dd/M/yyyy");
            val attData = SharedPreferenceUtils.getCountAttendance(context!!,"Attendance")

            /*if (prevDate == currentDate) {
                if (role== SALES_LEAD_ROLE || role == SALES_PERSON_ROLE) {
                    if (attData == "1") {
                        ib_attendance_in!!.visibility = View.GONE
                        ib_attendance_out!!.visibility = View.VISIBLE
                        binding.beatButton.isEnabled = true
                        binding.expenseButton.isEnabled = true
                        binding.orderButton.isEnabled = true
                        binding.leaveButton.isEnabled = true
                        binding.paymentButton.isEnabled = true
                        binding.complainButton.isEnabled = true
                        binding.dealerButton.isEnabled = true
                        binding.reminderButton.isEnabled = true
                        binding.profileButton.isEnabled = true
                        binding.teamButton.isEnabled = true
                        binding.performanceButtonLong.isEnabled = true
                        binding.profileButtonLong.isEnabled = true
                        binding.performanceButton.isEnabled = true

                    } else if (attData == "2") {
                        ib_attendance_in!!.visibility = View.VISIBLE
                        ib_attendance_out!!.visibility = View.GONE
                        binding.beatButton.isEnabled = false
                        binding.expenseButton.isEnabled = false
                        binding.orderButton.isEnabled = false
                        binding.leaveButton.isEnabled = false
                        binding.paymentButton.isEnabled = false
                        binding.complainButton.isEnabled = false
                        binding.dealerButton.isEnabled = false
                        binding.reminderButton.isEnabled = false
                        binding.profileButton.isEnabled = false
                        binding.teamButton.isEnabled = false
                        binding.performanceButtonLong.isEnabled = false
                        binding.profileButtonLong.isEnabled = false
                        binding.performanceButton.isEnabled = false

                    } else {
                        ib_attendance_in!!.visibility = View.VISIBLE
                        ib_attendance_out!!.visibility = View.GONE
                        binding.beatButton.isEnabled = false
                        binding.expenseButton.isEnabled = false
                        binding.orderButton.isEnabled = false
                        binding.leaveButton.isEnabled = false
                        binding.paymentButton.isEnabled = false
                        binding.complainButton.isEnabled = false
                        binding.dealerButton.isEnabled = false
                        binding.reminderButton.isEnabled = false
                        binding.profileButton.isEnabled = false
                        binding.teamButton.isEnabled = false
                        binding.performanceButtonLong.isEnabled = false
                        binding.profileButtonLong.isEnabled = false
                        binding.performanceButton.isEnabled = false

                    }
                }else {
                    SharedPreferenceUtils.saveCountAttendance(context!!,"Attendance","0")
                }

            } else {
                ib_attendance_in!!.visibility = View.VISIBLE
                ib_attendance_out!!.visibility = View.GONE
                binding.beatButton.isEnabled = false
                binding.expenseButton.isEnabled = false
                binding.orderButton.isEnabled = false
                binding.leaveButton.isEnabled = false
                binding.paymentButton.isEnabled = false
                binding.complainButton.isEnabled = false
                binding.dealerButton.isEnabled = false
                binding.reminderButton.isEnabled = false
                binding.profileButton.isEnabled = false
                binding.teamButton.isEnabled = false
                binding.performanceButtonLong.isEnabled = false
                binding.profileButtonLong.isEnabled = false
                binding.performanceButton.isEnabled = false

            }*/

            if (prevDate==currentDate){
                if (SharedPreferencesClass.retriveData(baseActivity,"SUM_Attendance_Lock").toString()=="Y") {
                    if (localTime>="06:30 pm"){
                        callAttendanceOut()
                        //showToastMessage("Enable")
                    }else{
                        if (attData == "1") {
                            ib_attendance_in!!.visibility = View.GONE
                            ib_attendance_out!!.visibility = View.VISIBLE
                            binding.beatButton.isEnabled = true
                            binding.expenseButton.isEnabled = true
                            binding.orderButton.isEnabled = true
                            binding.leaveButton.isEnabled = true
                            binding.paymentButton.isEnabled = true
                            binding.complainButton.isEnabled = true
                            binding.dealerButton.isEnabled = true
                            binding.reminderButton.isEnabled = true
                            binding.profileButton.isEnabled = true
                            binding.teamButton.isEnabled = true
                            binding.performanceButtonLong.isEnabled = true
                            binding.profileButtonLong.isEnabled = true
                            binding.performanceButton.isEnabled = true

                        } else if (attData == "2") {
                            ib_attendance_in!!.visibility = View.VISIBLE
                            ib_attendance_out!!.visibility = View.GONE
                            binding.beatButton.isEnabled = false
                            binding.expenseButton.isEnabled = false
                            binding.orderButton.isEnabled = false
                            binding.leaveButton.isEnabled = false
                            binding.paymentButton.isEnabled = false
                            binding.complainButton.isEnabled = false
                            binding.dealerButton.isEnabled = false
                            binding.reminderButton.isEnabled = false
                            binding.profileButton.isEnabled = false
                            binding.teamButton.isEnabled = false
                            binding.performanceButtonLong.isEnabled = false
                            binding.profileButtonLong.isEnabled = false
                            binding.performanceButton.isEnabled = false


                        } else {
                            ib_attendance_in!!.visibility = View.VISIBLE
                            ib_attendance_out!!.visibility = View.GONE
                            binding.beatButton.isEnabled = false
                            binding.expenseButton.isEnabled = false
                            binding.orderButton.isEnabled = false
                            binding.leaveButton.isEnabled = false
                            binding.paymentButton.isEnabled = false
                            binding.complainButton.isEnabled = false
                            binding.dealerButton.isEnabled = false
                            binding.reminderButton.isEnabled = false
                            binding.profileButton.isEnabled = false
                            binding.teamButton.isEnabled = false
                            binding.performanceButtonLong.isEnabled = false
                            binding.profileButtonLong.isEnabled = false
                            binding.performanceButton.isEnabled = false


                        }
                    }
                }else{
                    binding.beatButton.isEnabled = true
                    binding.expenseButton.isEnabled = true
                    binding.orderButton.isEnabled = true
                    binding.leaveButton.isEnabled = true
                    binding.paymentButton.isEnabled = true
                    binding.complainButton.isEnabled = true
                    binding.dealerButton.isEnabled = true
                    binding.reminderButton.isEnabled = true
                    binding.profileButton.isEnabled = true
                    binding.teamButton.isEnabled = true
                    binding.performanceButtonLong.isEnabled = true
                    binding.profileButtonLong.isEnabled = true
                    binding.performanceButton.isEnabled = true
                }

            }else{
                if (SharedPreferencesClass.retriveData(baseActivity,"SUM_Attendance_Lock").toString()=="Y") {
                    ib_attendance_in!!.visibility = View.VISIBLE
                    ib_attendance_out!!.visibility = View.GONE
                    binding.beatButton.isEnabled = false
                    binding.expenseButton.isEnabled = false
                    binding.orderButton.isEnabled = false
                    binding.leaveButton.isEnabled = false
                    binding.paymentButton.isEnabled = false
                    binding.complainButton.isEnabled = false
                    binding.dealerButton.isEnabled = false
                    binding.reminderButton.isEnabled = false
                    binding.profileButton.isEnabled = false
                    binding.teamButton.isEnabled = false
                    binding.performanceButtonLong.isEnabled = false
                    binding.profileButtonLong.isEnabled = false
                    binding.performanceButton.isEnabled = false
                }else{
                    binding.beatButton.isEnabled = true
                    binding.expenseButton.isEnabled = true
                    binding.orderButton.isEnabled = true
                    binding.leaveButton.isEnabled = true
                    binding.paymentButton.isEnabled = true
                    binding.complainButton.isEnabled = true
                    binding.dealerButton.isEnabled = true
                    binding.reminderButton.isEnabled = true
                    binding.profileButton.isEnabled = true
                    binding.teamButton.isEnabled = true
                    binding.performanceButtonLong.isEnabled = true
                    binding.profileButtonLong.isEnabled = true
                    binding.performanceButton.isEnabled = true
                }

            }

            ib_attendance_in!!.setOnClickListener {
                try {
                    if (prevDate==currentDate){
                        showToastMessage("You have given attendance already")

                    }else{
                        callAttendanceIn()

                    }
                    /*if (prevDate == "0"){
                        callAttendanceIn()
                    }else{
                        val date = format.parse(currentDate);
                        val date2 = format2.parse(prevDate);
                        val miliSeconds = date.getTime() -date2.getTime();
                        val seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds);
                        val minute = seconds/60;

                        if (minute >1440){
                            callAttendanceIn()
                        }else{
                            showToastMessage("You have given attendance already")
                        }

                    }*/


                } catch ( e:Exception) {
                    e.printStackTrace();
                }
            }
            ib_attendance_out!!.setOnClickListener {

                try {
                    callAttendanceOut()

                    /*if (prevDate==currentDate){
                        showToastMessage("You have given attendance already")

                    }else{
                        callAttendanceOut()

                    }*/
                   /* if (prevDate == "0"){
                        callAttendanceOut()
                    }else{

                        val date = format.parse(currentDate);
                        val date2 = format2.parse(prevDate);
                        val miliSeconds = date.getTime() -date2.getTime();
                        val seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds);
                        val minute = seconds/60;

                        if (minute >1440){
                            callAttendanceOut()
                        }else{
                            showToastMessage("You have given attendance already")
                        }

                    }*/


                } catch ( e:Exception) {
                    e.printStackTrace();
                }

            }

        }

        return super.onCreateOptionsMenu(menu,inflater)
    }




    companion object{
        const val TAG = "DefaultFragment"
    }


    fun getDashBoardPrivilege(uRole:String,binding: DefaultFragmentBinding){

       when (uRole) {
            //when (TEMP_CURRENT_LOGGED_IN) {

            SALES_LEAD_ROLE->{
                binding.rlBeat.visibility = View.VISIBLE
                binding.expenseButton.visibility = View.VISIBLE
                binding.orderButton.visibility = View.VISIBLE
                binding.leaveButton.visibility = View.VISIBLE
                binding.paymentButton.visibility = View.VISIBLE
                binding.complainButton.visibility = View.VISIBLE
                binding.dealerButton.visibility = View.VISIBLE
                binding.reminderButton.visibility = View.VISIBLE
                binding.profileButton.visibility = View.VISIBLE
                binding.teamButton.visibility = View.VISIBLE
                binding.profileButtonLong.visibility = View.GONE
                binding.lay10.visibility=View.VISIBLE
                binding.lay6.visibility=View.GONE
                binding.lay12.visibility=View.GONE
                binding.lay11.visibility = View.GONE
                binding.lay13.visibility = View.GONE
                binding.lay14.visibility=View.GONE


                getBeatResponse(userId)

            }
            SALES_PERSON_ROLE->{
                binding.rlBeat.visibility = View.VISIBLE
                binding.expenseButton.visibility = View.VISIBLE
                binding.orderButton.visibility = View.VISIBLE
                binding.leaveButton.visibility = View.VISIBLE
                binding.paymentButton.visibility = View.VISIBLE
                binding.complainButton.visibility = View.VISIBLE
                binding.dealerButton.visibility = View.VISIBLE
                binding.reminderButton.visibility = View.VISIBLE
                binding.profileButton.visibility = View.GONE
                binding.teamButton.visibility = View.GONE
                binding.lay5.visibility = View.GONE
                binding.profileButtonLong.visibility = View.VISIBLE
                binding.lay10.visibility=View.GONE
                binding.lay6.visibility=View.VISIBLE
                binding.lay12.visibility=View.GONE
                binding.lay11.visibility = View.GONE
                binding.lay13.visibility = View.GONE
                binding.lay14.visibility=View.GONE


                getBeatResponse(userId)

            }
            DISTRIBUTER_ROLE->{
                binding.lay1.visibility = View.GONE
                binding.lay2.visibility = View.GONE
                binding.lay3.visibility = View.VISIBLE
                binding.lay4.visibility = View.GONE
                binding.lay5.visibility = View.VISIBLE
                binding.lay6.visibility = View.GONE
                binding.lay7.visibility = View.GONE
                binding.lay8.visibility = View.GONE
                binding.lay9.visibility = View.GONE
                binding.lay10.visibility = View.GONE
                binding.lay11.visibility = View.VISIBLE
                binding.lay12.visibility=View.GONE
                binding.lay13.visibility = View.GONE
                binding.lay14.visibility=View.GONE

            }
            DEALER_ROLE->{
                binding.lay1.visibility = View.GONE
                binding.lay2.visibility = View.GONE
                binding.lay3.visibility = View.GONE
                binding.lay4.visibility = View.GONE
                binding.lay5.visibility = View.GONE
                 binding.lay6.visibility = View.GONE
                binding.lay7.visibility = View.GONE
                binding.lay8.visibility = View.VISIBLE
                binding.lay9.visibility = View.VISIBLE
                binding.lay12.visibility=View.VISIBLE
                binding.lay10.visibility=View.VISIBLE
                binding.lay11.visibility = View.GONE
                binding.lay13.visibility = View.GONE
                binding.lay14.visibility=View.GONE


            }
            MECHANIC_ROLE ->{
                binding.rlBeat.visibility = View.GONE
                binding.lay1.visibility = View.GONE
                binding.lay2.visibility = View.GONE
                binding.lay3.visibility = View.GONE
                binding.lay4.visibility = View.GONE
                binding.lay5.visibility = View.GONE
                binding.lay6.visibility = View.GONE
                binding.lay7.visibility = View.VISIBLE
                binding.lay8.visibility = View.VISIBLE
                binding.lay12.visibility=View.GONE
                binding.lay10.visibility=View.GONE
                binding.lay11.visibility = View.GONE
                binding.lay13.visibility = View.VISIBLE
                binding.lay14.visibility=View.GONE

            }

            else -> { // Note the block
                print("x is neither 1 nor 2")
            }
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
    private fun getBeatResponse(userId: String) {

        binding.progressLayout.visibility=View.VISIBLE
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getBeatIdentificationList(
            BeatIdentificationParams(userId)
        )
        responseCall.enqueue(beatIdentificationResponse as Callback<BeatIdentificationResponse>)

    }
    private val beatIdentificationResponse = object : NetworkCallBack<BeatIdentificationResponse>() {
        @SuppressLint("RestrictedApi")
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<BeatIdentificationResponse>
        ) {
            binding.progressLayout.visibility=View.GONE

            if (response.data!!.status==1) {

                if (response.data.incompleteBeat>=1){
                    binding.tvNotiCount.visibility=View.VISIBLE
                    binding.tvNotiCount.text=response.data.incompleteBeat.toString()
                }else{
                    binding.tvNotiCount.visibility=View.GONE

                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }


    }




    fun callAttendanceIn(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.doAttendance(
            AttendanceRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context))
        )
        responseCall.enqueue(AttendancResponse as Callback<AttendancResponse>)

    }

    private val AttendancResponse = object : NetworkCallBack<AttendancResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<AttendancResponse>) {
            if (response.data!!.status==1){
                SharedPreferencesClass.insertData(context as Context,"attendance_in",response.data.AM_ID.toString());
                SharedPreferencesClass.deleteData(context as Context,"attendance_out");
                val sdf = SimpleDateFormat("dd/M/yyyy")
                val currentDate = sdf.format(Date())
                SharedPreferenceUtils.saveData(context!!,"LAST_ATTENDANCE","${currentDate}")
                SharedPreferenceUtils.saveCountAttendance(context!!,"Attendance","1")
                showToastMessage( response.data.respMessage)
                ib_attendance_in!!.visibility=View.GONE
                ib_attendance_out!!.visibility=View.VISIBLE

                binding.beatButton.isEnabled=true
                binding.expenseButton.isEnabled=true
                binding.orderButton.isEnabled=true
                binding.leaveButton.isEnabled=true
                binding.paymentButton.isEnabled=true
                binding.complainButton.isEnabled=true
                binding.dealerButton.isEnabled=true
                binding.reminderButton.isEnabled=true
                binding.profileButton.isEnabled=true
                binding.teamButton.isEnabled=true
                binding.performanceButtonLong.isEnabled=true
                binding.profileButtonLong.isEnabled=true
                binding.performanceButton.isEnabled=true
                val intent = Intent(context, DashboardActivity::class.java)
                startActivity(intent)
                activity!!.onBackPressed()

            }else{
                showToastMessage( response.data.respMessage)

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callAttendanceOut(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.attendanceOut(
            AttendanceRequestOutParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),
                SharedPreferencesClass.retriveData(context as Context,"attendance_in").toString())
        )
        responseCall.enqueue(AttendancOutResponse as Callback<AttendancOutResponse>)

    }

    private val AttendancOutResponse = object : NetworkCallBack<AttendancOutResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<AttendancOutResponse>) {
            if (response.data!!.status==1){
                SharedPreferencesClass.insertData(context as Context,"attendance_out",response.data.AM_ID);
                SharedPreferencesClass.deleteData(context as Context,"attendance_in");
                showToastMessage( response.data.respMessage)
                ib_attendance_in!!.visibility=View.VISIBLE
                ib_attendance_out!!.visibility=View.GONE
                val sdf = SimpleDateFormat("dd/M/yyyy")
                val currentDate = sdf.format(Date())
                SharedPreferenceUtils.saveData(context!!,"LAST_ATTENDANCE","${currentDate}")
                SharedPreferenceUtils.saveCountAttendance(context!!,"Attendance","2")
                showToastMessage( response.data.respMessage)
                binding.beatButton.isEnabled=false
                binding.expenseButton.isEnabled=false
                binding.orderButton.isEnabled=false
                binding.leaveButton.isEnabled=false
                binding.paymentButton.isEnabled=false
                binding.complainButton.isEnabled=false
                binding.dealerButton.isEnabled=false
                binding.reminderButton.isEnabled=false
                binding.profileButton.isEnabled=false
                binding.teamButton.isEnabled=false
                binding.performanceButtonLong.isEnabled=false
                binding.profileButtonLong.isEnabled=false
                binding.performanceButton.isEnabled=false
                val intent = Intent(context, DashboardActivity::class.java)
                startActivity(intent)
                activity!!.onBackPressed()
            }else{
                showToastMessage( response.data.respMessage)

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }




}