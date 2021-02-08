package com.velectico.rbm.order.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.BeatTaskDetails
import com.velectico.rbm.beats.model.OrderListDetails
import com.velectico.rbm.beats.model.OrderProductListDetails
import com.velectico.rbm.databinding.FragmentOrderDetailsBinding
import com.velectico.rbm.order.adapters.OrderDetailsAdapter
import com.velectico.rbm.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentOrderDetailsBinding
    private lateinit var orderCartList : List<OrderProductListDetails>
    private lateinit var adapter: OrderDetailsAdapter
    var taskDetails = BeatTaskDetails()
    var orderDetails = OrderListDetails()
    var status = ""
    override fun getLayout(): Int {
        return R.layout.fragment_order_details
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentOrderDetailsBinding
        taskDetails = arguments!!.get("taskDetail") as BeatTaskDetails
        orderDetails = arguments!!.get("orderListDetail") as OrderListDetails
        if (taskDetails.dealerAddress != null){
            binding.tvAddressTxt.text = taskDetails.dealerAddress
        }
        else{
            binding.tvAddressTxt.visibility = View.GONE
        }

        binding.orederID.text = orderDetails.OH_Order_No
        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        val stdate =  DateUtils.parseDate(orderDetails.orderDate,inpFormat,outputformat)
        binding.tvOrddate.text = stdate
        binding.tvProdpriceTotal.text = "₹" +orderDetails.totalPrice
        binding.tvProdGst.text = "( Inclusive of GST )"
        if (orderDetails.OH_Status == "O"){
            status = "Pending"
            binding.tvOrdStat.setTextColor(Color.parseColor("#FF0000"))

        }
        else if (orderDetails.OH_Status == "C"){
            status = "Completed"
            binding.tvOrdStat.setTextColor(Color.parseColor("#006600"))

        }
        binding.tvOrdStat.text = status
        binding.btnCancel.visibility = View.GONE//Pampa

        if (orderDetails!!.dealName.toString()!=""){
            binding.tvPersonName.text="Dealer Name"
            binding.distName.text=orderDetails.dealName

        }else{
            binding.tvPersonName.text="Distributor Name"
            binding.distName.text=orderDetails.distribName

        }


        if (RBMLubricantsApplication.globalRole == "Team" ) {
            binding.btnCancel.visibility = View.GONE

        }
        orderCartList = orderDetails.prod_details
        setUpRecyclerView()


    }

    private fun setUpRecyclerView() {
        adapter = OrderDetailsAdapter(context as Context);
        binding.rvCartList.adapter = adapter
        adapter.orderCart = orderCartList
    }


    private fun setUp(){


    }
}