package com.velectico.rbm.dealer.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.data
import com.velectico.rbm.databinding.DealerListLayoutBinding
import com.velectico.rbm.databinding.FragmentDealerListBinding
import com.velectico.rbm.dealer.adapter.DealerListAdapter
import com.velectico.rbm.dealer.model.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import com.velectico.rbm.utils.SharedPreferencesClass
import retrofit2.Callback
import java.util.*


class DealerListFragment : BaseFragment() {
    private lateinit var binding: FragmentDealerListBinding;
    private var dealerList : List<DealerListDetails> = emptyList()
    private lateinit var adapter: DealerListAdapter
    var areaValue=""
    var userId=""
    var districtValue=""

    override fun getLayout(): Int {
        return R.layout.fragment_dealer_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentDealerListBinding
        if (RBMLubricantsApplication.globalRole == "Team" ){
            userId = GloblalDataRepository.getInstance().teamUserId

        }
        else{
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        //showToastMessage(userId)
        //callApiArea(userId)
        binding.existDealerButton.setOnClickListener {
            val navDirection=DealerListFragmentDirections.actionDealerListFragmentToExistDealerListFragment()
            Navigation.findNavController(binding.existDealerButton).navigate(navDirection)
        }

        //callApiList(SharedPreferenceUtils.getLoggedInUserId(context as Context),"0")
        callApiDistrict(userId)
    }
    private fun callApiDistrict(userId: String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getDistrict(
            DealerDistrictParams(userId)
        )
        responseCall.enqueue(districtResponse as Callback<DistrictResponse>)

    }

    var districtList : List<DistrictDetails> = emptyList<DistrictDetails>()

    val districtResponse = object : NetworkCallBack<DistrictResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DistrictResponse>) {
            response.data?.status?.let { status ->

                hide()
                districtList  = response.data.DistrictList
                var statList1: MutableList<String> = ArrayList()
                statList1.add("Select District")

                var statList: MutableList<String> = ArrayList()
                Collections.sort(districtList,
                    Comparator { o1, o2 -> o1.DM_District_Name!!.compareTo(o2.DM_District_Name!!) })
                for (i in districtList){
                    //showToastMessage(i.toString())
                    statList.add(i.DM_District_Name!!)
                }
                // Collections.sort(statList, String.CASE_INSENSITIVE_ORDER);
                statList= (statList1+statList).toMutableList()
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }

                binding.spinnerDistrict.adapter = adapter2
                binding.existDealerButton.visibility=View.VISIBLE
                if (SharedPreferencesClass.retriveData(
                        context as Context,
                        "district_name") != null) {
                    val spinnerPosition: Int = adapter2!!.getPosition(SharedPreferencesClass.retriveData(
                        context as Context,
                        "district_name"))
                    binding.spinnerDistrict.setSelection(spinnerPosition)
                }


                binding.spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                        if (binding.spinnerDistrict.selectedItem == "Select District") {
                            SharedPreferencesClass.insertData(
                                context as Context,
                                "district_name","Select District")
                            callApiList(userId,"0")
                            binding.llArea.visibility=View.GONE

                        } else {
                            val x = districtList[position-1]
                            districtValue = x.DM_ID!!
                            SharedPreferencesClass.insertData(
                                context as Context,
                                "district_name",x.DM_District_Name)
                            callApiArea(userId, districtValue)

                            //showToastMessage(x.AM_ID)

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


    private fun callApiArea(userId: String, districtId: String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getArea(
            DealerAreaParams(userId, districtId)
        )
        responseCall.enqueue(areaResponse as Callback<AreaResponse>)

    }
    var areaList : List<AreaDetails> = emptyList<AreaDetails>()

    val areaResponse = object : NetworkCallBack<AreaResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<AreaResponse>) {
            response.data?.status?.let { status ->

                hide()
                areaList  = response.data.AreaList
                var statList1: MutableList<String> = ArrayList()
                statList1.add("Select Area")

                var statList: MutableList<String> = ArrayList()
                Collections.sort(areaList,
                    Comparator { o1, o2 -> o1.AM_Area_Name!!.compareTo(o2.AM_Area_Name!!) })
                for (i in areaList){
                    //showToastMessage(i.toString())
                    statList.add(i.AM_Area_Name!!)
                }
               // Collections.sort(statList, String.CASE_INSENSITIVE_ORDER);
                statList= (statList1+statList).toMutableList()
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }

                binding.spinnerArea.adapter = adapter2
                binding.llArea.visibility=View.VISIBLE
                binding.existDealerButton.visibility=View.VISIBLE
                if (SharedPreferencesClass.retriveData(
                        context as Context,
                        "area_name") != null) {
                    val spinnerPosition: Int = adapter2!!.getPosition(SharedPreferencesClass.retriveData(
                        context as Context,
                        "area_name"))
                    binding.spinnerArea.setSelection(spinnerPosition)
                }


                binding.spinnerArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                        if (binding.spinnerArea.selectedItem == "Select Area") {
                            SharedPreferencesClass.insertData(
                                context as Context,
                                "area_name","Select Area")
                            callApiList(userId,"0")

                        } else {
                            val x = areaList[position-1]
                            areaValue = x.AM_ID!!
                            SharedPreferencesClass.insertData(
                                context as Context,
                                "area_name",x.AM_Area_Name)
                            callApiList(userId, areaValue)

                            //showToastMessage(x.AM_ID)

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
    private fun callApiList(loggedInUserId: String, areaValue: String) {

        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealerListInfo(

            DealerRequestParams(loggedInUserId,areaValue)
        )
        responseCall.enqueue(dealerListResponse as Callback<DealerListResponse>)

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
    private val dealerListResponse = object : NetworkCallBack<DealerListResponse>(){
        @SuppressLint("RestrictedApi", "LogNotTimber")
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DealerListResponse>) {
            hide()
            response.data?.status?.let { status ->
                dealerList.toMutableList().clear()
                if (response.data.count > 0){
                    binding.rvDealerList.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.GONE
                    //var dealerList: MutableList<DealerListDetails> = ArrayList()
                    dealerList = response.data.DealerList!!.toMutableList()
                    Collections.reverse(dealerList);
                    setUpRecyclerView()
                }
                else{
                    binding.rvDealerList.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE

                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
             hide()
        }

    }

    private fun setUpRecyclerView() {


        adapter = DealerListAdapter(object : DealerListAdapter.IDealerListActionCallBack{
            override fun moveToDealerDetails(
                position: Int,
                ddId: String?,
                binding: DealerListLayoutBinding
            ) {
                val navDirection=DealerListFragmentDirections.actionDealerListFragmentToEditDealerFragment(
                    dealerList[position])
                Navigation.findNavController(binding.card).navigate(navDirection)
            }

            override fun moveToCall(
                adapterPosition: Int,
                s: String,
                binding: DealerListLayoutBinding
            ) {

                    val u = Uri.parse("tel:" + binding.tvMobile.text.toString())
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

            override fun moveToCallOption(
                adapterPosition: Int,
                s: String,
                binding: DealerListLayoutBinding
            ) {
                val u = Uri.parse("tel:" + binding.tvMobileOption.text.toString())

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

            override fun moveToDealerEdit(
                position: Int,
                s: String,
                binding: DealerListLayoutBinding
            ) {
                val navDirection=DealerListFragmentDirections.actionDealerListFragmentToEditDealerFragment(
                    dealerList[position])
                Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)
            }

        }, context as Context)
        binding.rvDealerList.adapter = adapter
        adapter.dealerList = dealerList;
    }
}


