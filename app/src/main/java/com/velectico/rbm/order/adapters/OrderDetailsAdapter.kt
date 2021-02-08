package com.velectico.rbm.order.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.beats.model.OrderProductListDetails
import com.velectico.rbm.databinding.RowOrderDetailsBinding
import com.velectico.rbm.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailsAdapter(var context: Context) : RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {


    var orderCart =  listOf<OrderProductListDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowOrderDetailsBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {


        }

        fun bind(orderCart:  OrderProductListDetails?) {
            binding.orderCartInfo = orderCart
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowOrderDetailsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderCart.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderDetailsAdapter.ViewHolder, position: Int) {
        holder.bind(orderCart[position])
        Picasso.with(context).load(orderCart[position].prodImage)
            .skipMemoryCache()
            .placeholder(R.drawable.faded_logo_bg)
            .error(R.drawable.faded_logo_bg)
            .into(holder.binding.listImage)
        //var qty=orderCart[position].OD_Qty!!.toFloat()
        holder.binding.tvOrdrAmt.text=String.format("%.0f",orderCart[position].OD_Qty!!.toFloat()).toString()+" "+orderCart[position].OD_QuantityType
        if (orderCart[position].OD_QuantityType=="pcs"){
            holder.binding.collectionAmt.text="₹ "+orderCart[position].OD_Disc_Price

        }else if (orderCart[position].OD_QuantityType=="carton"){
            holder.binding.collectionAmt.text="₹ "+orderCart[position].OD_Net_Price

        }else if (orderCart[position].OD_QuantityType=="set"){
            holder.binding.collectionAmt.text="₹ "+orderCart[position].OD_Disc_Price

        }else{
            holder.binding.collectionAmt.text="₹ "+orderCart[position].OD_Net_Price
        }
        if (orderCart[position].PM_Scheme!=""){
            holder.binding.llScheme.visibility=View.VISIBLE
            holder.binding.tvSchemeName.text=orderCart[position].PM_Scheme
        }else{
            holder.binding.llScheme.visibility=View.GONE

        }


        //Picasso.get().load(orderCart[position].prodImage).fit().into(holder.binding.listImage)
    }


}