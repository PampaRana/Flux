package com.velectico.rbm.order.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.beats.model.OrderListDetails
import com.velectico.rbm.databinding.RowOrderHeadListBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.order.model.DistConfirmRequestParams
import com.velectico.rbm.order.model.DistConfirmResponse
import com.velectico.rbm.order.views.OrderEditFragmentDirections
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.SharedPreferencesClass
import retrofit2.Callback
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class OrderHeadListAdapter(
    var setCallback: IBeatDateListActionCallBack,
    var context: Context
) : RecyclerView.Adapter<OrderHeadListAdapter.ViewHolder>() {

    var callBack : OrderHeadListAdapter.IBeatDateListActionCallBack?=null
    var orderList =  listOf<OrderListDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowOrderHeadListBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
            callBack = setCallback;
            binding.navigateToDetails.setOnClickListener {
                callBack?.moveToOrderDetails(adapterPosition, "1",binding )

            }
            binding.btnConfirm.setOnClickListener {
                callBack?.moveToOrderConfirm(adapterPosition, "1",binding )
            }


        }

        fun bind(orderHead:  OrderListDetails?) {
            binding.orderHeadInfo = orderHead
            binding.executePendingBindings()
            if (orderHead!!.dealName.toString()!=""){
                binding.tvPersonName.text="Dealer Name"
                binding.tvProdCode.text=orderHead.dealName

            }else{
                binding.tvPersonName.text="Distributor Name"
                binding.tvProdCode.text=orderHead.distribName

            }
            val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
            val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
            val stdate =  DateUtils.parseDate(orderHead.orderDate,inpFormat,outputformat)
            binding.tvProdNetPrice.text = stdate
            val separated =
                orderHead.prod_details[0].Create_Date!!.split(" ".toRegex()).toTypedArray()
            val code=separated[1]
            //Toast.makeText(context, code, Toast.LENGTH_LONG).show()
            val sdf = SimpleDateFormat("hh:mm:ss")
            val sdfs = SimpleDateFormat("hh:mm a")
            val dt: Date
            try {
                dt = sdf.parse(code)
                println("Time Display: " + sdfs.format(dt)) // <-- I got result here
                // Toast.makeText(context, sdfs.format(dt), Toast.LENGTH_LONG).show()
                binding.tvTime.text = sdfs.format(dt)

            } catch (e: ParseException) {
                e.printStackTrace()
            }
            if (SharedPreferencesClass.retriveData(context, "UM_Role").toString()=="D"){
                if (orderHead.isConfirmed=="C"){
                    binding.llDistButton.visibility=View.GONE
                    binding.icCheck.visibility=View.VISIBLE


                }else{
                    binding.llDistButton.visibility=View.VISIBLE
                    binding.icCheck.visibility=View.GONE

                }
            }else if (SharedPreferencesClass.retriveData(context, "UM_Role").toString()=="R"){
                if (orderHead.isConfirmed=="C"){
                    binding.llDistButton.visibility=View.GONE
                    binding.icCheck.visibility=View.VISIBLE


                }else{
                    binding.llDistButton.visibility=View.VISIBLE
                    binding.icCheck.visibility=View.GONE

                }
            }else{
                binding.llDistButton.visibility=View.GONE
                binding.icCheck.visibility=View.GONE
            }





        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHeadListAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowOrderHeadListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderHeadListAdapter.ViewHolder, position: Int) {
        holder.bind(orderList[position])
       // holder.binding.tvUnit.text =
    }

    interface IBeatDateListActionCallBack{
        fun moveToOrderDetails(position:Int,beatTaskId:String?,binding: RowOrderHeadListBinding)
        fun moveToOrderConfirm(adapterPosition: Int, beatTaskId : String, binding: RowOrderHeadListBinding)

    }









}