package com.velectico.rbm.order.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.database.CartProduct
import com.velectico.rbm.database.DB_Manager
import com.velectico.rbm.database.Helper_Cart_DB
import com.velectico.rbm.order.views.OrderEditFragmentDirections
import com.velectico.rbm.utils.DataConstant
import java.util.*

class OrderEditListAdapter(
    context: Context?,
    orderCart: ArrayList<CartProduct>?,
    tvProdId: TextView,
    btnConfirmPlace: AppCompatButton
) :
    RecyclerView.Adapter<OrderEditListAdapter.pdtViewHolder>() {
    var context: Context
    var orderCart: List<CartProduct>
    var tvProdId: TextView
    lateinit var btnConfirmPlace: AppCompatButton
    var totalPrice = 0.0
    var prodQuantityType=""
    val hashMap: HashMap<Int, Double> = HashMap<Int, Double>()
    var db: Helper_Cart_DB? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): pdtViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.edit_product_view, parent, false)
        return pdtViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: pdtViewHolder,
        position: Int
    ) {
        db =  Helper_Cart_DB(context);
        DB_Manager.initializeInstance(db);

        holder.plistWeightText.setText("Packaging : " + orderCart[position].cart_product_ltr + " " + orderCart[position].cart_product_quantity_ltr)

        /*if (orderCart[position].cart_product_quantity_ltr=="ML"){
           // holder.plistWeightText.setText("Packaging : "+orderCart[position].cart_product_ltr +" Litre")

            if (orderCart[position].cart_product_ltr!!.toFloat()>1){
                holder.plistWeightText.setText("Packaging : "+orderCart[position].cart_product_ltr +" Litre")

            }else{
                var convertMl = (orderCart[position].cart_product_ltr!!.toDouble() * 1000)
                holder.plistWeightText.setText("Packaging : "+convertMl.toString() +" ML")
               // Toast.makeText(context, convertMl.toString() ,Toast.LENGTH_LONG).show()

            }
        }else {
            holder.plistWeightText.setText("Packaging : " + orderCart[position].cart_product_ltr + " " + orderCart[position].cart_product_quantity_ltr)
        }*/
        holder.plistPriceTextStrike.text="DLP : ₹ "+orderCart[position].cart_product_net_price
        holder.plistSpecialPriceText.text="SP : ₹ "+orderCart[position].cart_special_price
        holder.cartProductQuantityTv.text=orderCart[position].cart_product_quantity

        if (orderCart[position].cart_special_price!!.toFloat() > 0) {
            holder.plistPriceTextStrike.setVisibility(View.VISIBLE)
            holder.plistPriceTextStrike.setPaintFlags(holder.plistPriceTextStrike.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                .toString()
            holder.plistSpecialPriceText.setVisibility(View.VISIBLE)
            //Calculate Special Price


        } else {
            if (orderCart[position].cart_product_net_price!!.toFloat() > 0) {
                holder.plistSpecialPriceText.setVisibility(View.GONE)
                holder.plistPriceTextStrike.setVisibility(View.VISIBLE)


                //Calculate Net Price
            }
        }
        Picasso.with(context).load(orderCart[position].cart_product_image)
            .skipMemoryCache()
            .placeholder(R.drawable.faded_logo_bg)
            .error(R.drawable.faded_logo_bg)
            .into(holder.listImage)
       // Picasso.get().load(orderCart[position].cart_product_image).fit().into(holder.listImage)
        holder.from_name.setText(orderCart[position].cart_product_name)


        //Toast.makeText(context,orderCart.size.toString(),Toast.LENGTH_LONG).show()
        totalPrice+=orderCart[position].cart_product_total_price!!.toFloat()


        tvProdId.text=totalPrice.toString()


        var typeList: MutableList<String> = java.util.ArrayList()
        if (orderCart[position].cart_unit_carton!="") {
            if (orderCart[position].cart_product_quantity_type.equals("pcs")){
                if (orderCart[position].cart_product_quantity_type.equals("carton")){
                    typeList.add(0, "Carton")
                    typeList.add(1, "Pieces")
                }else {
                    typeList.add(0, "Pieces")
                    typeList.add(1, "Carton")
                }
                holder.cartonPriceText.visibility = View.VISIBLE
                holder.cartonUnitText.visibility = View.VISIBLE
                holder.cartonPriceText.text="Carton Price : ₹ "+orderCart[position].cart_carton_price
                holder.cartonUnitText.text="Unit for Carton : "+orderCart[position].cart_unit_carton

                // holder.spPcsOrBucket.setSelection(0)

            }else  if (orderCart[position].cart_product_quantity_type.equals("set")){
                if (orderCart[position].cart_product_quantity_type.equals("carton")){
                    typeList.add(0, "Carton")
                    typeList.add(1, "Set")
                }else {
                    typeList.add(0, "Set")
                    typeList.add(1, "Carton")
                }
                holder.cartonPriceText.visibility = View.VISIBLE
                holder.cartonUnitText.visibility = View.VISIBLE
                holder.cartonPriceText.text="Carton Price : ₹ "+orderCart[position].cart_carton_price
                holder.cartonUnitText.text="Unit for Carton : "+orderCart[position].cart_unit_carton
            }else  if (orderCart[position].cart_product_quantity_type.equals("carton")){
                if (orderCart[position].cart_product_quantity_type.equals("set")){
                    typeList.add(0, "Carton")
                    typeList.add(1, "Set")
                }else {
                    typeList.add(0, "Carton")
                    typeList.add(1, "Pieces")
                }
                holder.cartonPriceText.visibility = View.VISIBLE
                holder.cartonUnitText.visibility = View.VISIBLE
                holder.cartonPriceText.text="Carton Price : ₹ "+orderCart[position].cart_carton_price
                holder.cartonUnitText.text="Unit for Carton : "+orderCart[position].cart_unit_carton
            }

        }else{
            if (orderCart[position].cart_product_quantity_type.equals("pcs")){
                typeList.add(0, "Pieces")
                holder.cartonPriceText.visibility = View.GONE
                holder.cartonUnitText.visibility = View.GONE

            }else  if (orderCart[position].cart_product_quantity_type.equals("set")){
                typeList.add(0, "Set")
                holder.cartonPriceText.visibility = View.GONE
                holder.cartonUnitText.visibility = View.GONE

            }else  if (orderCart[position].cart_product_quantity_type.equals("bucket")){
                typeList.add(0, "Bucket")
                holder.cartonPriceText.visibility = View.GONE
                holder.cartonUnitText.visibility = View.GONE

            }
        }
        var convertLtr = 0.0

        var y = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, typeList)
        holder.spPcsOrBucket.adapter = y

        var count=orderCart[position].cart_product_quantity!!.toInt()

        holder.spPcsOrBucket.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    if (orderCart[position].cart_unit_carton!="") {
                        if (holder.spPcsOrBucket.selectedItem == "Pieces") {
                           //Toast.makeText(context,"pcs",Toast.LENGTH_LONG).show()
                            //prodQuantityType = "pcs"



                        } else if (holder.spPcsOrBucket.selectedItem == "Carton") {
                           // Toast.makeText(context,"carton",Toast.LENGTH_LONG).show()

                            //prodQuantityType = "carton"


                        }else if (holder.spPcsOrBucket.selectedItem == "Set") {
                           // Toast.makeText(context,"set",Toast.LENGTH_LONG).show()

                            //prodQuantityType = "carton"


                        }
                    }



                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {

                }
            }



        holder.cartPlusImg.setOnClickListener{
            if (orderCart[position].cart_unit_carton!=""){
                if (orderCart[position].cart_product_quantity_type.equals("pcs")){
                    count += 1
                    holder.cartProductQuantityTv.text=count.toString()
                    var totalPrice = 0.0
                    if (count > 0) {
                        convertLtr = String.format(
                            "%.2f",orderCart[position].cart_product_ltr!!.toFloat())
                            .toDouble() * count.toDouble()
                    }
                    if (orderCart[position].cart_special_price!!.toFloat() > 0) {
                        //Calculate Special Price
                        totalPrice += (orderCart[position].cart_special_price!!.toFloat()) * count


                    } else {
                        if (orderCart[position].cart_product_net_price!!.toFloat() > 0) {
                            totalPrice += (orderCart[position].cart_product_net_price!!.toFloat()) * count


                        }
                    }
                    prodQuantityType="pcs"


                    onPutPrice(position,orderCart[position].cart_product_id!!.toInt(), totalPrice,
                        count.toString(), orderCart[position].cart_product_scheme_id.toString(),
                        prodQuantityType, orderCart[position].cart_product_mrp_price,
                        orderCart[position].cart_product_disc_price, orderCart[position].cart_product_net_price,
                        orderCart[position].cart_product_gst.toString(), convertLtr.toString(), orderCart[position].cart_product_name,
                        orderCart[position].cart_product_image,
                        orderCart[position].cart_unit_carton.toString(),
                        orderCart[position].cart_carton_price.toString(),
                        orderCart[position].cart_special_price.toString(),
                        orderCart[position].cart_product_quantity_ltr.toString(),
                        orderCart[position].cart_product_scheme_name.toString())
                    val navDirection= OrderEditFragmentDirections.actionOrderEditFragmentSelf()
                    Navigation.findNavController(holder.cartPlusImg).navigate(navDirection)
                    Navigation.findNavController(holder.cartPlusImg as ImageButton).popBackStack()


                }else if (orderCart[position].cart_product_quantity_type.equals("set")){
                    count += 1
                    holder.cartProductQuantityTv.text=count.toString()
                    var totalPrice = 0.0
                    if (count > 0) {
                        convertLtr = String.format(
                            "%.2f",orderCart[position].cart_product_ltr!!.toFloat())
                            .toDouble() * count.toDouble()
                    }
                    if (orderCart[position].cart_special_price!!.toFloat() > 0) {
                        //Calculate Special Price
                        totalPrice += (orderCart[position].cart_special_price!!.toFloat()) * count


                    } else {
                        if (orderCart[position].cart_product_net_price!!.toFloat() > 0) {
                            totalPrice += (orderCart[position].cart_product_net_price!!.toFloat()) * count


                        }
                    }
                    prodQuantityType="set"


                    onPutPrice(position,orderCart[position].cart_product_id!!.toInt(), totalPrice,
                        count.toString(), orderCart[position].cart_product_scheme_id.toString(),
                        prodQuantityType, orderCart[position].cart_product_mrp_price,
                        orderCart[position].cart_product_disc_price, orderCart[position].cart_product_net_price,
                        orderCart[position].cart_product_gst.toString(), convertLtr.toString(), orderCart[position].cart_product_name,
                        orderCart[position].cart_product_image,
                        orderCart[position].cart_unit_carton.toString(),
                        orderCart[position].cart_carton_price.toString(),
                        orderCart[position].cart_special_price.toString(),
                        orderCart[position].cart_product_quantity_ltr.toString(),
                        orderCart[position].cart_product_scheme_name.toString())
                    val navDirection= OrderEditFragmentDirections.actionOrderEditFragmentSelf()
                    Navigation.findNavController(holder.cartPlusImg).navigate(navDirection)
                    Navigation.findNavController(holder.cartPlusImg as ImageButton).popBackStack()


                }else  if (orderCart[position].cart_product_quantity_type.equals("carton")){
                    count += 1
                    holder.cartProductQuantityTv.text=count.toString()
                    prodQuantityType="carton"
                    if (count > 0) {
                        convertLtr = String.format(
                            "%.2f",orderCart[position].cart_product_ltr!!.toFloat())
                            .toDouble() * count.toDouble()
                    }
                    var totalPrice = 0.0
                    totalPrice += (orderCart[position].cart_carton_price!!.toFloat()) * count

                    onPutPrice(position,orderCart[position].cart_product_id!!.toInt(), totalPrice,
                        count.toString(), orderCart[position].cart_product_scheme_id.toString(),
                        prodQuantityType, orderCart[position].cart_product_mrp_price,
                        orderCart[position].cart_product_disc_price, orderCart[position].cart_carton_price,
                        orderCart[position].cart_product_gst.toString(), convertLtr.toString(),  orderCart[position].cart_product_name,
                        orderCart[position].cart_product_image,
                        orderCart[position].cart_unit_carton.toString(),
                        orderCart[position].cart_carton_price.toString(),
                        orderCart[position].cart_special_price.toString(),
                        orderCart[position].cart_product_quantity_ltr.toString(),
                        orderCart[position].cart_product_scheme_name.toString())
                    val navDirection= OrderEditFragmentDirections.actionOrderEditFragmentSelf()
                    Navigation.findNavController(holder.cartPlusImg).navigate(navDirection)
                    Navigation.findNavController(holder.cartPlusImg as ImageButton).popBackStack()

                }
            }else{
                if (orderCart[position].cart_product_quantity_type.equals("pcs")){
                    count += 1
                    holder.cartProductQuantityTv.text=count.toString()

                    prodQuantityType="pcs"
                    var totalPrice = 0.0
                    if (count > 0) {
                        convertLtr = String.format(
                            "%.2f",orderCart[position].cart_product_ltr!!.toFloat())
                            .toDouble() * count.toDouble()
                    }
                    if (orderCart[position].cart_special_price!!.toFloat() > 0) {
                        //Calculate Special Price
                        totalPrice += (orderCart[position].cart_special_price!!.toFloat()) * count


                    } else {
                        if (orderCart[position].cart_product_net_price!!.toFloat() > 0) {
                            //Calculate Net Price
                            totalPrice += (orderCart[position].cart_product_net_price!!.toFloat()) * count

                        }
                    }
                    onPutPrice(position,orderCart[position].cart_product_id!!.toInt(), totalPrice,
                        count.toString(), orderCart[position].cart_product_scheme_id.toString(),prodQuantityType,
                        orderCart[position].cart_product_mrp_price,
                        orderCart[position].cart_product_disc_price, orderCart[position].cart_product_net_price,
                        orderCart[position].cart_product_gst.toString(), convertLtr.toString(),
                        orderCart[position].cart_product_name,
                        orderCart[position].cart_product_image,
                        orderCart[position].cart_unit_carton.toString(),
                        orderCart[position].cart_carton_price.toString(),
                        orderCart[position].cart_special_price.toString(),
                        orderCart[position].cart_product_quantity_ltr.toString(),
                        orderCart[position].cart_product_scheme_name.toString())
                    val navDirection= OrderEditFragmentDirections.actionOrderEditFragmentSelf()
                    Navigation.findNavController(holder.cartPlusImg).navigate(navDirection)
                    Navigation.findNavController(holder.cartPlusImg as ImageButton).popBackStack()


                }else if (orderCart[position].cart_product_quantity_type.equals("set")){
                    count += 1
                    holder.cartProductQuantityTv.text=count.toString()

                    prodQuantityType="set"
                    var totalPrice = 0.0
                    if (count > 0) {
                        convertLtr = String.format(
                            "%.2f",orderCart[position].cart_product_ltr!!.toFloat())
                            .toDouble() * count.toDouble()
                    }
                    if (orderCart[position].cart_special_price!!.toFloat() > 0) {
                        //Calculate Special Price
                        totalPrice += (orderCart[position].cart_special_price!!.toFloat()) * count


                    } else {
                        if (orderCart[position].cart_product_net_price!!.toFloat() > 0) {
                            //Calculate Net Price
                            totalPrice += (orderCart[position].cart_product_net_price!!.toFloat()) * count

                        }
                    }
                    onPutPrice(position,orderCart[position].cart_product_id!!.toInt(), totalPrice,
                        count.toString(), orderCart[position].cart_product_scheme_id.toString(),prodQuantityType,
                        orderCart[position].cart_product_mrp_price,
                        orderCart[position].cart_product_disc_price, orderCart[position].cart_product_net_price,
                        orderCart[position].cart_product_gst.toString(), convertLtr.toString(),
                        orderCart[position].cart_product_name,
                        orderCart[position].cart_product_image,
                        orderCart[position].cart_unit_carton.toString(),
                        orderCart[position].cart_carton_price.toString(),
                        orderCart[position].cart_special_price.toString(),
                        orderCart[position].cart_product_quantity_ltr.toString() ,
                        orderCart[position].cart_product_scheme_name.toString())
                    val navDirection= OrderEditFragmentDirections.actionOrderEditFragmentSelf()
                    Navigation.findNavController(holder.cartPlusImg).navigate(navDirection)
                    Navigation.findNavController(holder.cartPlusImg as ImageButton).popBackStack()


                }  else if (orderCart[position].cart_product_quantity_type.equals("bucket")){
                    count += 1
                    holder.cartProductQuantityTv.text=count.toString()
                    prodQuantityType="bucket"
                    var totalPrice = 0.0
                    if (count > 0) {
                        convertLtr = String.format(
                            "%.2f",orderCart[position].cart_product_ltr!!.toFloat())
                            .toDouble() * count.toDouble()
                    }
                    if (orderCart[position].cart_special_price!!.toFloat() > 0) {
                        //Calculate Special Price
                        totalPrice += (orderCart[position].cart_special_price!!.toFloat()) * count


                    } else {
                        if (orderCart[position].cart_product_net_price!!.toFloat() > 0) {

                            totalPrice += (orderCart[position].cart_product_net_price!!.toFloat()) * count


                        }
                    }

                    onPutPrice(position,orderCart[position].cart_product_id!!.toInt(), totalPrice,
                        count.toString(), orderCart[position].cart_product_scheme_id.toString(),
                        prodQuantityType, orderCart[position].cart_product_mrp_price,
                        orderCart[position].cart_product_disc_price, orderCart[position].cart_product_net_price,
                        orderCart[position].cart_product_gst.toString(), convertLtr.toString(),  orderCart[position].cart_product_name,
                        orderCart[position].cart_product_image,orderCart[position].cart_unit_carton.toString(),
                        orderCart[position].cart_carton_price.toString(), orderCart[position].cart_special_price.toString(),
                        orderCart[position].cart_product_quantity_ltr.toString(),
                        orderCart[position].cart_product_scheme_name.toString())
                    val navDirection= OrderEditFragmentDirections.actionOrderEditFragmentSelf()
                    Navigation.findNavController(holder.cartPlusImg).navigate(navDirection)
                    Navigation.findNavController(holder.cartPlusImg as ImageButton).popBackStack()

                }
            }


        }

        holder.cartMinusImg.setOnClickListener{
            if (orderCart[position].cart_unit_carton!=""){
                if (orderCart[position].cart_product_quantity_type.equals("pcs")){
                    if (count > 0) {
                        count -= 1
                    }
                    holder.cartProductQuantityTv.text=count.toString()
                    var totalPrice = 0.0
                    if (count > 0) {
                        convertLtr = String.format(
                            "%.2f",orderCart[position].cart_product_ltr!!.toFloat())
                            .toDouble() * count.toDouble()
                    }
                    if (orderCart[position].cart_special_price!!.toFloat() > 0) {
                        //Calculate Special Price
                        totalPrice += (orderCart[position].cart_special_price!!.toFloat()) * count


                    } else {
                        if (orderCart[position].cart_product_net_price!!.toFloat() > 0) {
                            totalPrice += (orderCart[position].cart_product_net_price!!.toFloat()) * count


                        }
                    }
                    prodQuantityType="pcs"


                    onPutPrice(position,orderCart[position].cart_product_id!!.toInt(), totalPrice,
                        count.toString(), orderCart[position].cart_product_scheme_id.toString(),
                        prodQuantityType, orderCart[position].cart_product_mrp_price,
                        orderCart[position].cart_product_disc_price, orderCart[position].cart_product_net_price,
                        orderCart[position].cart_product_gst.toString(), convertLtr.toString(), orderCart[position].cart_product_name,
                        orderCart[position].cart_product_image,
                        orderCart[position].cart_unit_carton.toString(),
                        orderCart[position].cart_carton_price.toString(),
                        orderCart[position].cart_special_price.toString(),
                        orderCart[position].cart_product_quantity_ltr.toString(),
                        orderCart[position].cart_product_scheme_name.toString())
                    val navDirection= OrderEditFragmentDirections.actionOrderEditFragmentSelf()
                    Navigation.findNavController(holder.cartPlusImg).navigate(navDirection)
                    Navigation.findNavController(holder.cartPlusImg as ImageButton).popBackStack()


                }else if (orderCart[position].cart_product_quantity_type.equals("set")){
                    if (count > 0) {
                        count -= 1
                    }
                    holder.cartProductQuantityTv.text=count.toString()
                    var totalPrice = 0.0
                    if (count > 0) {
                        convertLtr = String.format(
                            "%.2f",orderCart[position].cart_product_ltr!!.toFloat())
                            .toDouble() * count.toDouble()
                    }
                    if (orderCart[position].cart_special_price!!.toFloat() > 0) {
                        //Calculate Special Price
                        totalPrice += (orderCart[position].cart_special_price!!.toFloat()) * count


                    } else {
                        if (orderCart[position].cart_product_net_price!!.toFloat() > 0) {
                            totalPrice += (orderCart[position].cart_product_net_price!!.toFloat()) * count


                        }
                    }
                    prodQuantityType="set"


                    onPutPrice(position,orderCart[position].cart_product_id!!.toInt(), totalPrice,
                        count.toString(), orderCart[position].cart_product_scheme_id.toString(),
                        prodQuantityType, orderCart[position].cart_product_mrp_price,
                        orderCart[position].cart_product_disc_price, orderCart[position].cart_product_net_price,
                        orderCart[position].cart_product_gst.toString(), convertLtr.toString(), orderCart[position].cart_product_name,
                        orderCart[position].cart_product_image,
                        orderCart[position].cart_unit_carton.toString(),
                        orderCart[position].cart_carton_price.toString(),
                        orderCart[position].cart_special_price.toString(),
                        orderCart[position].cart_product_quantity_ltr.toString(),
                        orderCart[position].cart_product_scheme_name.toString())
                    val navDirection= OrderEditFragmentDirections.actionOrderEditFragmentSelf()
                    Navigation.findNavController(holder.cartPlusImg).navigate(navDirection)
                    Navigation.findNavController(holder.cartPlusImg as ImageButton).popBackStack()


                } else if (orderCart[position].cart_product_quantity_type.equals("carton")){
                    if (count > 0) {
                        count -= 1
                    }
                    holder.cartProductQuantityTv.text=count.toString()
                    prodQuantityType="carton"
                    if (count > 0) {
                        convertLtr = String.format(
                            "%.2f",orderCart[position].cart_product_ltr!!.toFloat())
                            .toDouble() * count.toDouble()
                    }
                    var totalPrice = 0.0
                    totalPrice += (orderCart[position].cart_carton_price!!.toFloat()) * count

                    onPutPrice(position,orderCart[position].cart_product_id!!.toInt(), totalPrice,
                        count.toString(), orderCart[position].cart_product_scheme_id.toString(),
                        prodQuantityType, orderCart[position].cart_product_mrp_price,
                        orderCart[position].cart_product_disc_price, orderCart[position].cart_carton_price,
                        orderCart[position].cart_product_gst.toString(), convertLtr.toString(),  orderCart[position].cart_product_name,
                        orderCart[position].cart_product_image,
                        orderCart[position].cart_unit_carton.toString(),
                        orderCart[position].cart_carton_price.toString(),
                        orderCart[position].cart_special_price.toString(),
                        orderCart[position].cart_product_quantity_ltr.toString() ,
                        orderCart[position].cart_product_scheme_name.toString())
                    val navDirection= OrderEditFragmentDirections.actionOrderEditFragmentSelf()
                    Navigation.findNavController(holder.cartPlusImg).navigate(navDirection)
                    Navigation.findNavController(holder.cartPlusImg as ImageButton).popBackStack()

                }
            }else{
                if (orderCart[position].cart_product_quantity_type.equals("pcs")){
                    if (count > 0) {
                        count -= 1
                    }
                    holder.cartProductQuantityTv.text=count.toString()

                    prodQuantityType="pcs"
                    var totalPrice = 0.0
                    if (count > 0) {
                        convertLtr = String.format(
                            "%.2f",orderCart[position].cart_product_ltr!!.toFloat())
                            .toDouble() * count.toDouble()
                    }
                    if (orderCart[position].cart_special_price!!.toFloat() > 0) {
                        //Calculate Special Price
                        totalPrice += (orderCart[position].cart_special_price!!.toFloat()) * count


                    } else {
                        if (orderCart[position].cart_product_net_price!!.toFloat() > 0) {
                            //Calculate Net Price
                            totalPrice += (orderCart[position].cart_product_net_price!!.toFloat()) * count

                        }
                    }
                    onPutPrice(position,orderCart[position].cart_product_id!!.toInt(), totalPrice,
                        count.toString(), orderCart[position].cart_product_scheme_id.toString(),prodQuantityType,
                        orderCart[position].cart_product_mrp_price,
                        orderCart[position].cart_product_disc_price, orderCart[position].cart_product_net_price,
                        orderCart[position].cart_product_gst.toString(), convertLtr.toString(),
                        orderCart[position].cart_product_name,
                        orderCart[position].cart_product_image,
                        orderCart[position].cart_unit_carton.toString(),
                        orderCart[position].cart_carton_price.toString(),
                        orderCart[position].cart_special_price.toString(),
                        orderCart[position].cart_product_quantity_ltr.toString(),
                        orderCart[position].cart_product_scheme_name.toString())
                    val navDirection= OrderEditFragmentDirections.actionOrderEditFragmentSelf()
                    Navigation.findNavController(holder.cartPlusImg).navigate(navDirection)
                    Navigation.findNavController(holder.cartPlusImg as ImageButton).popBackStack()


                }else if (orderCart[position].cart_product_quantity_type.equals("set")){
                    if (count > 0) {
                        count -= 1
                    }
                    holder.cartProductQuantityTv.text=count.toString()

                    prodQuantityType="set"
                    var totalPrice = 0.0
                    if (count > 0) {
                        convertLtr = String.format(
                            "%.2f",orderCart[position].cart_product_ltr!!.toFloat())
                            .toDouble() * count.toDouble()
                    }
                    if (orderCart[position].cart_special_price!!.toFloat() > 0) {
                        //Calculate Special Price
                        totalPrice += (orderCart[position].cart_special_price!!.toFloat()) * count


                    } else {
                        if (orderCart[position].cart_product_net_price!!.toFloat() > 0) {
                            //Calculate Net Price
                            totalPrice += (orderCart[position].cart_product_net_price!!.toFloat()) * count

                        }
                    }
                    onPutPrice(position,orderCart[position].cart_product_id!!.toInt(), totalPrice,
                        count.toString(), orderCart[position].cart_product_scheme_id.toString(),prodQuantityType,
                        orderCart[position].cart_product_mrp_price,
                        orderCart[position].cart_product_disc_price, orderCart[position].cart_product_net_price,
                        orderCart[position].cart_product_gst.toString(), convertLtr.toString(),
                        orderCart[position].cart_product_name,
                        orderCart[position].cart_product_image,
                        orderCart[position].cart_unit_carton.toString(),
                        orderCart[position].cart_carton_price.toString(),
                        orderCart[position].cart_special_price.toString(),
                        orderCart[position].cart_product_quantity_ltr.toString() ,
                        orderCart[position].cart_product_scheme_name.toString())
                    val navDirection= OrderEditFragmentDirections.actionOrderEditFragmentSelf()
                    Navigation.findNavController(holder.cartPlusImg).navigate(navDirection)
                    Navigation.findNavController(holder.cartPlusImg as ImageButton).popBackStack()


                }else  if (orderCart[position].cart_product_quantity_type.equals("bucket")){
                    if (count > 0) {
                        count -= 1
                    }
                    holder.cartProductQuantityTv.text=count.toString()
                    prodQuantityType="bucket"
                    var totalPrice = 0.0
                    if (count > 0) {
                        convertLtr = String.format(
                            "%.2f",orderCart[position].cart_product_ltr!!.toFloat())
                            .toDouble() * count.toDouble()
                    }
                    if (orderCart[position].cart_special_price!!.toFloat() > 0) {
                        //Calculate Special Price
                        totalPrice += (orderCart[position].cart_special_price!!.toFloat()) * count


                    } else {
                        if (orderCart[position].cart_product_net_price!!.toFloat() > 0) {

                            totalPrice += (orderCart[position].cart_product_net_price!!.toFloat()) * count
                        }
                    }

                    onPutPrice(position,orderCart[position].cart_product_id!!.toInt(), totalPrice,
                        count.toString(), orderCart[position].cart_product_scheme_id.toString(),
                        prodQuantityType, orderCart[position].cart_product_mrp_price,
                        orderCart[position].cart_product_disc_price, orderCart[position].cart_product_net_price,
                        orderCart[position].cart_product_gst.toString(), convertLtr.toString(),  orderCart[position].cart_product_name,
                        orderCart[position].cart_product_image,orderCart[position].cart_unit_carton.toString(),
                        orderCart[position].cart_carton_price.toString(), orderCart[position].cart_special_price.toString(),
                        orderCart[position].cart_product_quantity_ltr.toString(),
                        orderCart[position].cart_product_scheme_name.toString())
                    val navDirection= OrderEditFragmentDirections.actionOrderEditFragmentSelf()
                    Navigation.findNavController(holder.cartPlusImg).navigate(navDirection)
                    Navigation.findNavController(holder.cartPlusImg as ImageButton).popBackStack()

                }
            }


        }


    }

    override fun getItemCount(): Int {
        return orderCart.size
    }

    @SuppressLint("NewApi")
    private fun onPutPrice(
        position: Int,
        productId: Int,
        productTotalPrice: Double,
        productQuantity: String,
        productSchemeId: String,
        prodQuantityType: String,
        pmMrp: String?,
        pmDiscPrice: String?,
        pmNetPrice: String?,
        productGst: String,
        totalLtr: String,
        pmBrandName: String?,
        pmImagePath: String?,
        unit_carton: String,
        cartonPrice: String,
        specialPrice: String,
        unitLtr: String,
        productSchemeName: String
    ) {
        hashMap.put(productId, productTotalPrice)

        for (key in hashMap.keys) {
            if (hashMap.containsKey(productId)) {
                hashMap.replace(productId, productTotalPrice)
                db!!.addProduct(productId.toString(),productSchemeId,pmBrandName, productQuantity, prodQuantityType,pmImagePath,
                    pmMrp, pmDiscPrice,pmNetPrice,productTotalPrice,productGst, totalLtr,unit_carton, cartonPrice, specialPrice,unitLtr, productSchemeName , "")
                db!!.deleteCart(productId)
                db!!.addProduct(productId.toString(),productSchemeId,pmBrandName, productQuantity, prodQuantityType,pmImagePath,
                    pmMrp, pmDiscPrice,pmNetPrice,productTotalPrice,productGst, totalLtr,unit_carton, cartonPrice, specialPrice,unitLtr, productSchemeName,"")

            } else {
                hashMap.put(productId, productTotalPrice)


            }

        }
        calGrandTotal(productId)

    }

    fun calGrandTotal(productId: Int) {
        var sum = 0.0
        for (key in hashMap.keys) {
            var floatStr1 = hashMap[key].toString()
            var total = floatStr1.toDouble()
            sum += total

        }
        println(sum)
       // Toast.makeText(context,sum.toString(),Toast.LENGTH_LONG).show()
        if (sum==0.0){
            db!!.deleteCart(productId)
           // btnConfirmPlace.isEnabled=false

        }

        if (db!!.getCartTotalPrice() == 0.0) {
            tvProdId.setText("")
            db!!.clearCart()
           // btnConfirmPlace.isEnabled=false

        } else {
            tvProdId.setText("₹ "+db!!.getCartTotalPrice().toString())
            //Toast.makeText(context,db!!.getCartTotalPrice().toString(),Toast.LENGTH_LONG).show()
            DataConstant.grandTotalPrice=db!!.getCartTotalPrice().toString()
            //btnConfirmPlace.isEnabled=true
        }
    }


    inner class pdtViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var listImage: ImageView
        var plistWeightText: TextView
        var plistPriceTextStrike: TextView
        var plistSpecialPriceText: TextView
        var cartProductQuantityTv: TextView
        var from_name: TextView
        var spPcsOrBucket: Spinner
        var cartPlusImg:ImageButton
        var cartMinusImg:ImageButton
        var cartonPriceText: TextView
        var cartonUnitText: TextView

        init {
            listImage = itemView.findViewById(R.id.list_image)
            plistWeightText = itemView.findViewById(R.id.plist_weight_text)
            plistPriceTextStrike = itemView.findViewById(R.id.plist_price_text_strike)
            plistSpecialPriceText = itemView.findViewById(R.id.plist_special_price_text)
            cartProductQuantityTv = itemView.findViewById(R.id.cart_product_quantity_tv)
            spPcsOrBucket = itemView.findViewById(R.id.sp_pcs_or_bucket)
            cartonPriceText= itemView.findViewById(R.id.carton_price_text)
            cartonUnitText= itemView.findViewById(R.id.carton_unit_text)
            from_name=itemView.findViewById(R.id.from_name)
            cartPlusImg=itemView.findViewById(R.id.cart_plus_img)
            cartMinusImg=itemView.findViewById(R.id.cart_minus_img)
        }
    }

    init {
        this.context = context!!
        this.orderCart = orderCart!!
        this.tvProdId=tvProdId
    }


}