package com.velectico.rbm.dealer.views

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.DropdownDetails
import com.velectico.rbm.beats.model.OrderVSQualityRequestParams
import com.velectico.rbm.beats.model.OrderVSQualityResponse
import com.velectico.rbm.databinding.FragmentEditDealerBinding
import com.velectico.rbm.databinding.FragmentEditExistDealerBinding
import com.velectico.rbm.dealer.model.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.util.ArrayList


class EditExistDealerFragment : BaseFragment() {
    private lateinit var binding: FragmentEditExistDealerBinding
    var existDealerInfo = ExistDealerDetails()
    var userId=""
    var segId=""


    override fun getLayout(): Int {
        return R.layout.fragment_edit_exist_dealer
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentEditExistDealerBinding
        existDealerInfo = arguments!!.get("existDealerInfo") as ExistDealerDetails
        if (RBMLubricantsApplication.globalRole == "Team") {
            userId = GloblalDataRepository.getInstance().teamUserId

        } else {
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        binding.inputDealerName.setText(existDealerInfo.UM_Name)
        binding.inputDealerMobile.setText(existDealerInfo.UM_Login_Id)
        binding.inputDealerAddress.setText(existDealerInfo.DM_Address)
        binding.inputContactName.setText(existDealerInfo.DM_Contact_Person)
        binding.tvArea.text = existDealerInfo.DM_Area_Name

        callApiSegment("Product Segment")

        binding.btnAdd.setOnClickListener {

            showHud()
            val apiInterface =
                ApiClient.getInstance().client.create(ApiInterface::class.java)
            val responseCall = apiInterface.updateExistingDealerList(
                UpdateExistingDealerParams(
                    existDealerInfo.UM_ID.toString(),
                   // binding.inputDealerName.text.toString().trim(),
                    //existDealerInfo.DM_Area.toString(),
                    binding.inputDealerMobile.text.toString().trim(),
                    binding.inputContactName.text.toString().trim(),
                    binding.inputDealerAddress.text.toString().trim(),
                    segId


                )
            )
            responseCall.enqueue(editDealerResponse as Callback<UpdateExistingDealerResponse>)
        }

    }


    private val editDealerResponse = object : NetworkCallBack<UpdateExistingDealerResponse>() {
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<UpdateExistingDealerResponse>) {
            hide()

            if (response.data!!.status == 1) {
                var dialog = Dialog(context!!)
                dialog.setContentView(R.layout.add_dialog_view)
                val handler = Handler()
                val runnable = Runnable {
                    if (dialog.isShowing()) {
                        dialog.dismiss()
                        val navDirection =
                            EditExistDealerFragmentDirections.actionEditExistDealerFragmentToExistDealerListFragment()
                        Navigation.findNavController(binding.btnAdd).navigate(navDirection)
                    } else {
                        dialog.show()
                    }
                }
                handler.postDelayed(runnable, 1000)
                dialog.show()


            } else {
                showToastMessage(response.data.respMessage.toString())
            }


        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    private fun callApiSegment(type: String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(segmentResponse as retrofit2.Callback<OrderVSQualityResponse>)

    }
    var dataList: List<DropdownDetails> = emptyList<DropdownDetails>()
    val segmentResponse = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                dataList = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Segment")
                for (i in dataList) {
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerSegment.adapter = adapter2
                if (existDealerInfo.DM_Segment_Name != null) {
                    val spinnerPosition: Int = adapter2!!.getPosition(existDealerInfo.DM_Segment_Name)
                    binding.spinnerSegment.setSelection(spinnerPosition)
                }
                binding.spinnerSegment.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {

                            if (binding.spinnerSegment.selectedItem == "Select Segment") {
                                segId = ""
                            } else {

                                val x = dataList[position - 1]
                                segId = x.Exp_Head_Id!!
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
    var hud: KProgressHUD? = null
    fun showHud() {
        hud = KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show();
    }

    fun hide() {
        hud?.dismiss()
    }
}