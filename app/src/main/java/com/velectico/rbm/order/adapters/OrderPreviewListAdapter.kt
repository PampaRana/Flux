package com.velectico.rbm.order.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.database.CartProduct
import com.velectico.rbm.database.DB_Manager
import com.velectico.rbm.database.Helper_Cart_DB
import com.velectico.rbm.order.views.OrderEditFragmentDirections
import com.velectico.rbm.order.views.OrderPreviewFragmentDirections

class OrderPreviewListAdapter(context: Context?, orderCartList: ArrayList<CartProduct>?) :
    RecyclerView.Adapter<OrderPreviewListAdapter.pdtViewHolder>() {
    var context: Context
    var orderCartList: List<CartProduct>
    var db: Helper_Cart_DB? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): pdtViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.order_preview, parent, false)
        return pdtViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: pdtViewHolder,
        position: Int
    ) {
        db =  Helper_Cart_DB(context);
        DB_Manager.initializeInstance(db);
        holder.tv_prodName.setText("Brand Name : "+orderCartList[position].cart_product_name )
        if (orderCartList[position].cart_product_scheme_name!="") {
            holder.tv_scheme_name.visibility=View.VISIBLE
            holder.tv_scheme_name.setText("Scheme : " + orderCartList[position].cart_product_scheme_name)
        }else{
            holder.tv_scheme_name.visibility=View.GONE
        }

        //Toast.makeText(context, "Total Ltr"+orderCartList[position].cart_product_total_ltr,Toast.LENGTH_LONG).show()

        var convertLtr=0.0
        if (orderCartList[position].cart_product_quantity_ltr.equals("ML")) {
            if(orderCartList[position].cart_product_quantity_type.equals("carton")){
                convertLtr =
                    (String.format(
                        "%.2f",
                        orderCartList[position].cart_product_ltr!!.toFloat()
                    ).toDouble() / 1000) * orderCartList[position].cart_product_quantity!!.toDouble() *
                            orderCartList[position].cart_unit_carton!!.toFloat()

            }else {
                convertLtr =
                    (String.format(
                        "%.2f",
                        orderCartList[position].cart_product_ltr!!.toFloat()
                    )
                        .toDouble() / 1000) * orderCartList[position].cart_product_quantity!!.toDouble()

            }
            holder.tv_ltr.setText("Packaging : " + convertLtr+" Litre")
            //orderCartList[position].cart_product_total_ltr=convertLtr.toString()

        } else {
            if(orderCartList[position].cart_product_quantity_type.equals("carton")){
                convertLtr =
                    (orderCartList[position].cart_product_ltr!!.toFloat()).toDouble()*
                 orderCartList[position].cart_product_quantity!!.toDouble() *
                            orderCartList[position].cart_unit_carton!!.toFloat()

            }else {
                convertLtr = orderCartList[position].cart_product_ltr!!.toFloat()
                    .toDouble() * orderCartList[position].cart_product_quantity!!.toDouble()

            }
            holder.tv_ltr.setText("Packaging : " + convertLtr+ " "+orderCartList[position].cart_product_quantity_ltr)
            orderCartList[position].cart_product_total_ltr=convertLtr.toString()

        }

        if (orderCartList[position].cart_product_quantity_type.equals("pcs")){
           // holder.tv_pdt_type.setText("Pieces")
            holder.tv_quantity.setText("Quantity : "+orderCartList[position].cart_product_quantity + " Pieces")

        }else if(orderCartList[position].cart_product_quantity_type.equals("carton")){
           // holder.tv_pdt_type.setText("Carton")
            holder.tv_quantity.setText("Quantity : "+orderCartList[position].cart_product_quantity + " Carton")


        }else if(orderCartList[position].cart_product_quantity_type.equals("set")){
            // holder.tv_pdt_type.setText("Carton")
            holder.tv_quantity.setText("Quantity : "+orderCartList[position].cart_product_quantity + " Set")


        }else{
            holder.tv_quantity.setText("Quantity : "+orderCartList[position].cart_product_quantity + " Bucket")
            //holder.tv_pdt_type.setText("Bucket")

        }

        if (orderCartList[position].cart_product_total_price=="0.0"){
            db!!.deleteCart(orderCartList[position].cart_product_id!!.toInt())

        }
        holder.tv_price.setText("Amount : ₹ "+orderCartList[position].cart_product_total_price)
        val price=orderCartList[position].cart_product_total_price!!.toDouble() / orderCartList[position].cart_product_quantity!!.toInt()
        holder.tv_single_price.text=("Price : ₹ "+price.toString())
       /* Picasso.get().load(orderCartList[position].cart_product_image).
            //error(R.drawable.ic_launcher_foreground).
        fit().

        into(holder.iv_prodImageUrl)*/


        Picasso.with(context).load(orderCartList[position].cart_product_image)
            .skipMemoryCache()
            .placeholder(R.drawable.faded_logo_bg)
            .error(R.drawable.faded_logo_bg)
            .into(holder.iv_prodImageUrl)
    }

    override fun getItemCount(): Int {
        return orderCartList.size
    }

    inner class pdtViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_prodImageUrl: ImageView
        var tv_prodName: TextView
        var tv_quantity: TextView
        var tv_ltr: TextView
        var tv_pdt_type: TextView
        var tv_price: TextView
        var tv_single_price: TextView
        var tv_scheme_name: TextView

        init {
            iv_prodImageUrl = itemView.findViewById(R.id.iv_prodImageUrl)
            tv_prodName = itemView.findViewById(R.id.tv_prodName)
            tv_quantity = itemView.findViewById(R.id.tv_quantity)
            tv_ltr = itemView.findViewById(R.id.tv_ltr)
            tv_pdt_type = itemView.findViewById(R.id.tv_pdt_type)
            tv_price = itemView.findViewById(R.id.tv_price)
            tv_single_price=itemView.findViewById(R.id.tv_single_price)
            tv_scheme_name=itemView.findViewById(R.id.tv_scheme_name)

        }
    }

    init {
        this.context = context!!
        this.orderCartList = orderCartList!!
    }

}