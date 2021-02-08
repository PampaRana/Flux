package com.velectico.rbm.teamlist.view

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentTeamListBinding
import com.velectico.rbm.databinding.RowTeamListBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.products.view.ProductFilterFragmentDirections
import com.velectico.rbm.teamlist.adapter.TeamListAdapter
import com.velectico.rbm.teamlist.model.TeamListDetails
import com.velectico.rbm.teamlist.model.TeamListRequestParams
import com.velectico.rbm.teamlist.model.TeamListResponse
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback

/**
 * A simple [Fragment] subclass.
 */
class TeamListFragment : BaseFragment()  {

    private lateinit var binding: FragmentTeamListBinding
    private var teamList : List<TeamListDetails> = emptyList()
    private lateinit var adapter: TeamListAdapter

    var getstring = ""
    override fun getLayout(): Int {
        return R.layout.fragment_team_list
    }


    override fun init(binding: ViewDataBinding) {
        getstring = arguments?.getString(  "name").toString()
        this.binding = binding as FragmentTeamListBinding
        //teamList = TeamListModel().getDummyTeamList()
        callApiList()
        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        adapter = TeamListAdapter(object : TeamListAdapter.ITeamListActionCallBack{
            override fun moveToBeatDetails(position: Int, beatTaskId: String?,binding: RowTeamListBinding) {
                //Log.e("test","onAddTask"+beatTaskId)
                GloblalDataRepository.getInstance().teamUserId = teamList[position].UM_Login_Id
                DataConstant.teamUmid=teamList[position].UM_ID.toString()
                DataConstant.teamUserId=teamList[position].UM_Login_Id.toString()
               // GloblalDataRepository.getInstance().teamUserId = teamList[position].UM_ID
                if (getstring == "Performance") {
                    /*val navDirection =  TeamListFragmentDirections.actionTeamListFragmentToTeamPerformanceFragment("")
                    Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)*/
                    val navDirection =  TeamListFragmentDirections.actionTeamListFragmentToTeamPerformanceFragment(
                        teamList[position].UM_Login_Id.toString()
                    )
                    Navigation.findNavController(binding.beatDateRow).navigate(navDirection)
                }
                else if (getstring == "BEATS") {
                    val navDirection =  TeamListFragmentDirections.actionTeamListFragmentToDateWiseBeatListFragment("")
                    Navigation.findNavController(binding.beatDateRow).navigate(navDirection)
                }
                else if (getstring == "Leave") {

                    val navDirection =  TeamListFragmentDirections.actionTeamListFragmentToLeaveListFragment()
                    Navigation.findNavController(binding.beatDateRow).navigate(navDirection)
                }
                else if (getstring == "Expense") {
                   // DataConstant.teamUserId="Expense"
                    val navDirection =  TeamListFragmentDirections.actionTeamListFragmentToExpenseListFragment()
                    Navigation.findNavController(binding.beatDateRow).navigate(navDirection)
                }else if (getstring == "Dealer") {
                    val navDirection =  TeamListFragmentDirections.actionTeamListFragmentToDealerListFragment()
                    Navigation.findNavController(binding.beatDateRow).navigate(navDirection)
                }else if (getstring == "Payment\nConfirmation") {
                    val navDirection =  TeamListFragmentDirections.actionTeamListFragmentToTeamPaymentConfirmFragment()
                    Navigation.findNavController(binding.beatDateRow).navigate(navDirection)
                }else if (getstring == "Exceed\nCredit Days") {
                    val navDirection =  TeamListFragmentDirections.actionTeamListFragmentToPaymentFailedFragment()
                    Navigation.findNavController(binding.beatDateRow).navigate(navDirection)
                }
            }
        });
        binding.rvTeamList.adapter = adapter
        adapter.teamList = teamList
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
    fun callApiList(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getTeamList(

            TeamListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context)))
        responseCall.enqueue(TeamListResponse as Callback<TeamListResponse>)
    }

    private val TeamListResponse = object : NetworkCallBack<TeamListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<TeamListResponse>) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test0000","TeamListResponse status="+response.data)
                teamList.toMutableList().clear()
                if (response.data.count > 0){
                    teamList = response.data.TeamList.toMutableList()

                    setUpRecyclerView()
                    binding.rvTeamList.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.GONE

                }
                else{
                    showToastMessage("No data found")
                    //binding.resolvButton.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.rvTeamList.visibility = View.GONE
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }

    }


}
