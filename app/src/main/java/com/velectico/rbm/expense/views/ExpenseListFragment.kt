package com.velectico.rbm.expense.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentExpenseListBinding
import com.velectico.rbm.databinding.RowExpenseListBinding
import com.velectico.rbm.expense.adapter.ExpenseListAdapter
import com.velectico.rbm.expense.model.ExpenseDetails
import com.velectico.rbm.expense.model.ExpenseResponse
import com.velectico.rbm.menuitems.viewmodel.AttendanceRequestParams
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

/**
 * Expense List
 */
class ExpenseListFragment : BaseFragment() {
    private lateinit var binding: FragmentExpenseListBinding
    private lateinit var adapter: ExpenseListAdapter
    var userId=""
    private var expenseList : List<ExpenseDetails> = emptyList()

    override fun getLayout(): Int {
        return R.layout.fragment_expense_list
    }



    @SuppressLint("RestrictedApi")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentExpenseListBinding

        if (SharedPreferencesClass.retriveData(context as Context,"UM_Role")=="L"){
            if (RBMLubricantsApplication.globalRole == "Team" ){
                binding.fab.visibility=View.GONE
                userId= GloblalDataRepository.getInstance().teamUserId
            }else{
                binding.fab.visibility=View.VISIBLE
                userId= SharedPreferenceUtils.getLoggedInUserId(context as Context)
            }
        }else if (SharedPreferencesClass.retriveData(context as Context,"UM_Role")=="D"){
            if (RBMLubricantsApplication.globalRole == "Team" ){
                binding.fab.visibility=View.GONE
                userId= GloblalDataRepository.getInstance().teamUserId
            }else{
                binding.fab.visibility=View.VISIBLE
                userId= SharedPreferenceUtils.getLoggedInUserId(context as Context)
            }
        }
        else {
            userId= SharedPreferenceUtils.getLoggedInUserId(context as Context)

        }

        getBeatList()

        //showToastMessage(RBMLubricantsApplication.globalRole)
        binding.fab.setOnClickListener {
           moveToCreateExpense()
        }


    }


    private fun setUpRecyclerView() {


        adapter = ExpenseListAdapter(object : ExpenseListAdapter.IExpenseActionCallBack{
            override fun moveToExpenseDetails(
                adapterPosition: Int,
                expenseDetails: ExpenseDetails,
                binding: RowExpenseListBinding
            ) {
                /*DataConstant.expenseDtlsList= expenseList[adapterPosition].expenseDtls!!
                DataConstant.expenseStatus=expenseList[adapterPosition].expenseStatus.toString()
                DataConstant.expenseHeadId=expenseList[adapterPosition].expenseId.toString()
                DataConstant.expenseTotalAmt=expenseList[adapterPosition].totalexpAmt.toString()
                DataConstant.img1=expenseList[adapterPosition].recPhoto1.toString()
                DataConstant.img2=expenseList[adapterPosition].recPhoto2.toString()
                DataConstant.img3=expenseList[adapterPosition].recPhoto3.toString()
                DataConstant.img4=expenseList[adapterPosition].recPhoto4.toString()
                DataConstant.img5=expenseList[adapterPosition].recPhoto5.toString()
                DataConstant.img6=expenseList[adapterPosition].recPhoto6.toString()*/

                val navDirection =  ExpenseListFragmentDirections.actionExpenseListFragmentToExpenseDetailsFragment(expenseList[adapterPosition])
                Navigation.findNavController(binding.card).navigate(navDirection)
            }

            override fun moveToEditExpense(
                adapterPosition: Int,
                expenseDetails: ExpenseDetails,
                binding: RowExpenseListBinding
            ) {
                val navDirection =  ExpenseListFragmentDirections.actionExpenseListFragmentToEditExpenseFragment(expenseList[adapterPosition])
                Navigation.findNavController(binding.navigateToEdit).navigate(navDirection)
            }
            /* override fun moveToExpenseDetails(
                 adapterPosition: Int,
                 expenseDtls: List<ExpenseDetails>?,
                 binding: RowExpenseListBinding
             ) {
                 DataConstant.expenseDtlsList= expenseList[adapterPosition].expenseDtls!!
                 DataConstant.expenseStatus=expenseList[adapterPosition].expenseStatus.toString()
                 DataConstant.expenseHeadId=expenseList[adapterPosition].expenseId.toString()
                 DataConstant.expenseTotalAmt=expenseList[adapterPosition].totalexpAmt.toString()
                 DataConstant.img1=expenseList[adapterPosition].recPhoto1.toString()
                 DataConstant.img2=expenseList[adapterPosition].recPhoto2.toString()
                 DataConstant.img3=expenseList[adapterPosition].recPhoto3.toString()
                 DataConstant.img4=expenseList[adapterPosition].recPhoto4.toString()
                 DataConstant.img5=expenseList[adapterPosition].recPhoto5.toString()
                 DataConstant.img6=expenseList[adapterPosition].recPhoto6.toString()

                 val navDirection =  ExpenseListFragmentDirections.actionExpenseListFragmentToExpenseDetailsFragment(expenseList[adapterPosition])
                 Navigation.findNavController(binding.card).navigate(navDirection)
             }*/



        });
        binding.rvExpenseList.adapter = adapter
        adapter.expenseList = expenseList

    }

    fun moveToCreateExpense(){
        val direction : NavDirections = ExpenseListFragmentDirections.actionExpenseListFragmentToCreateMultipleExpenseFragment()
        Navigation.findNavController(binding.fab).navigate(direction)

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

    fun getBeatList(){

        showHud()

        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getChuttiList(
            AttendanceRequestParams(userId
            )
        )
        responseCall.enqueue(createResponse as Callback<ExpenseResponse>)

    }


    val createResponse = object : NetworkCallBack<ExpenseResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ExpenseResponse>) {
            hide()


                expenseList.toMutableList().clear()
                if (response.data!!.status==1){
                    binding.tvNoData.visibility = View.GONE
                    expenseList = response.data.expenseDetails!!.toMutableList()
                    Collections.reverse(expenseList)
                    binding.rvExpenseList.visibility=View.VISIBLE
                    setUpRecyclerView()
                }
                else{
                     showToastMessage("No data found")
                    binding.rvExpenseList.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE

                }
        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }

}
