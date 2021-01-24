package com.velectico.rbm.expense.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentExpenseDetailsBinding
import com.velectico.rbm.expense.adapter.ExpenseDetailsAdapter
import com.velectico.rbm.expense.model.ApproveRejectParams
import com.velectico.rbm.expense.model.ExpenseDetails
import com.velectico.rbm.expense.model.SuccessResponse
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


class ExpenseDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentExpenseDetailsBinding
    private lateinit var adapter: ExpenseDetailsAdapter
    var userId=""
    var expenseDtls = ExpenseDetails()
    override fun getLayout(): Int {
        return R.layout.fragment_expense_details
    }

    @SuppressLint("SetTextI18n", "UseRequireInsteadOfGet")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentExpenseDetailsBinding
        expenseDtls = arguments!!.get("expenseDtls") as ExpenseDetails

        //orderCartList = orderDetails.prod_details
        setUpRecyclerView()



        if (RBMLubricantsApplication.globalRole == "Team" ){
           /* binding.btnApprove.visibility=View.VISIBLE
            binding.btnReject.visibility=View.VISIBLE*/
            if (expenseDtls.expenseStatus=="O"){
                binding.btnApprove.visibility=View.VISIBLE
                binding.btnReject.visibility=View.VISIBLE
            }else if (expenseDtls.expenseStatus=="A"){
                binding.btnApprove.visibility=View.GONE
                binding.btnReject.visibility=View.VISIBLE
            }else {
                binding.btnApprove.visibility=View.VISIBLE
                binding.btnReject.visibility=View.GONE
            }
            userId= GloblalDataRepository.getInstance().teamUserId
        }else{
            binding.btnApprove.visibility=View.GONE
            binding.btnReject.visibility=View.GONE
            userId= SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        binding.btnApprove.setOnClickListener{
            getApproveResponse(userId,"A",expenseDtls.expenseId!!)
        }
        binding.btnReject.setOnClickListener{
            getApproveResponse(userId,"R",expenseDtls.expenseId!!)
        }
        var amount=0.0
        for (i in expenseDtls.expenseDtls!!){
            amount+=i.expAmt!!.toDouble()
        }
        binding.totalamt.text="Total Amount : ₹ "+amount.toString()
        //showToastMessage(DataConstant.img1)
        if (expenseDtls.recPhoto1==null) {

            binding.rlImg1.visibility=View.GONE

        }else{
            binding.rlImg1.visibility=View.VISIBLE

            Picasso.with(context).load(
                expenseDtls.recPhoto1!!
            )
                .skipMemoryCache() //.placeholder(R.drawable.place_holder)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivImg1, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        binding.contentProgressBar1.visibility= View.GONE
                    }

                    override fun onError() {
                        binding.contentProgressBar1.visibility= View.GONE

                    }
                })

           /* Picasso.with(context).load(DataConstant.img1)
                .skipMemoryCache()
                .placeholder(R.drawable.faded_logo_bg)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivImg1)*/

        }
        if (expenseDtls.recPhoto2==null) {
            binding.rlImg2.visibility=View.GONE
        }else{
            binding.rlImg2.visibility=View.VISIBLE
            Picasso.with(context).load(
                expenseDtls.recPhoto2!!
            )
                .skipMemoryCache() //.placeholder(R.drawable.place_holder)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivImg2, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        binding.contentProgressBar2.visibility= View.GONE
                    }

                    override fun onError() {
                        binding.contentProgressBar2.visibility= View.GONE

                    }
                })
           /* Picasso.with(context).load(DataConstant.img2)
                .skipMemoryCache()
                 .placeholder(R.drawable.faded_logo_bg)
                  .error(R.drawable.faded_logo_bg)
                .into(binding.ivImg2)*/
        }
        if (expenseDtls.recPhoto3==null) {
            binding.rlImg3.visibility=View.GONE

        }else{
            binding.rlImg3.visibility=View.VISIBLE
            Picasso.with(context).load(
                expenseDtls.recPhoto3!!
            )
                .skipMemoryCache() //.placeholder(R.drawable.place_holder)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivImg3, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        binding.contentProgressBar3.visibility= View.GONE
                    }

                    override fun onError() {
                        binding.contentProgressBar3.visibility= View.GONE

                    }
                })
            /*Picasso.with(context).load(DataConstant.img3)
                .skipMemoryCache()
                .placeholder(R.drawable.faded_logo_bg)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivImg3)*/
        }
        if (expenseDtls.recPhoto4==null) {
            binding.rlImg4.visibility=View.GONE

        }else{
            binding.rlImg4.visibility=View.VISIBLE
            Picasso.with(context).load(
                expenseDtls.recPhoto4!!
            )
                .skipMemoryCache() //.placeholder(R.drawable.place_holder)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivImg4, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        binding.contentProgressBar4.visibility= View.GONE
                    }

                    override fun onError() {
                        binding.contentProgressBar4.visibility= View.GONE

                    }
                })
            /*Picasso.with(context).load(DataConstant.img4)
                .skipMemoryCache()
                .placeholder(R.drawable.faded_logo_bg)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivImg4)*/
        }
        if (expenseDtls.recPhoto5==null) {
            binding.rlImg5.visibility=View.GONE

        }else{
            binding.rlImg5.visibility=View.VISIBLE
            Picasso.with(context).load(
                expenseDtls.recPhoto5!!
            )
                .skipMemoryCache() //.placeholder(R.drawable.place_holder)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivImg5, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        binding.contentProgressBar5.visibility= View.GONE
                    }

                    override fun onError() {
                        binding.contentProgressBar5.visibility= View.GONE

                    }
                })
            /*Picasso.with(context).load(DataConstant.img5)
                .skipMemoryCache()
                .placeholder(R.drawable.faded_logo_bg)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivImg5)*/
        }
        if (expenseDtls.recPhoto6==null) {
            binding.rlImg6.visibility=View.GONE

        }else{
            binding.rlImg6.visibility=View.VISIBLE
            Picasso.with(context).load(
                expenseDtls.recPhoto6!!
            )
                .skipMemoryCache() //.placeholder(R.drawable.place_holder)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivImg6, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        binding.contentProgressBar6.visibility= View.GONE
                    }

                    override fun onError() {
                        binding.contentProgressBar6.visibility= View.GONE

                    }
                })
            /*Picasso.with(context).load(DataConstant.img6)
                .skipMemoryCache()
                .placeholder(R.drawable.faded_logo_bg)
                .error(R.drawable.faded_logo_bg)
                .into(binding.ivImg6)*/
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
    private fun getApproveResponse(userId: String, expStatus: String, expenseHeadId: String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getApproveReject(
            ApproveRejectParams(userId,expStatus,expenseHeadId
            )
        )
        responseCall.enqueue(createResponse as Callback<SuccessResponse>)


    }
    val createResponse = object : NetworkCallBack<SuccessResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<SuccessResponse>) {
            hide()
            if (response.data!!.status==1){
                Toast.makeText(activity!!, "${response.data?.respMessage}", Toast.LENGTH_SHORT)
                    .show()
                activity!!.onBackPressed()

            }else{
                Toast.makeText(activity!!, "${response.data?.respMessage}", Toast.LENGTH_SHORT)
                    .show()

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }
    private fun setUpRecyclerView() {

        binding.mTableLayout.addView(View(context))
        val tableRowHeader = layoutInflater.inflate(
            R.layout.table_header,
            null
        ) as TableRow
        binding.mTableLayout.addView(tableRowHeader)
        binding.mTableLayout.setVisibility(View.VISIBLE)
        for (i in expenseDtls.expenseDtls!!){
            val tableRow = layoutInflater.inflate(
                R.layout.table_item,
                null
            ) as TableRow

            val tv_date_value = tableRow.findViewById(R.id.tv_date_value) as TextView
            val tv_type_value = tableRow.findViewById(R.id.tv_type_value) as TextView
            val tv_km_travel_value = tableRow.findViewById(R.id.tv_km_travel_value) as TextView
            val tv_amount_value = tableRow.findViewById(R.id.tv_amount_value) as TextView

            tv_amount_value.text=i.expAmt
            val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
            val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
            val stdate =  DateUtils.parseDate(i.expDate,inpFormat,outputformat)
            tv_date_value.text = stdate

            if (i.expType=="Petrol expense") {
                tv_km_travel_value.visibility=View.VISIBLE
                tv_km_travel_value.text = i.km_run + " Km"
            }else{
                tv_km_travel_value.text=""
            }
            tv_type_value.text = i.expType
            binding.mTableLayout.addView(tableRow);

        }
        /*adapter = ExpenseDetailsAdapter();
        binding.rvExpenseDtlsList.adapter = adapter
        adapter.expenDetailsList = DataConstant.expenseDtlsList*/
    }
}