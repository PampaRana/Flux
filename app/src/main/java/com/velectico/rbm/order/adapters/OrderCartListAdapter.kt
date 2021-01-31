package com.velectico.rbm.order.adapters

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.velectico.rbm.beats.model.CreateOrderListDetails
import com.velectico.rbm.beats.model.PSM_Scheme_DetailsResponse
import com.velectico.rbm.database.DB_Manager
import com.velectico.rbm.database.Helper_Cart_DB
import com.velectico.rbm.databinding.RowProductCartBinding
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.productItemClickListener
import java.util.*

class OrderCartListAdapter(
    val context: Context,
    var listener: productItemClickListener,
    var tvProdId: TextView,
    var btnCheckOut: AppCompatButton,
    val hashMap: HashMap<Int, Double> = HashMap<Int, Double>(),
    var productSchemeId: String = "",
    var productSchemeName: String="",
    var prodQuantityType: String = "",
    var db: Helper_Cart_DB? = null

) : RecyclerView.Adapter<OrderCartListAdapter.ViewHolder>() {

    var orderCart = listOf<CreateOrderListDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowProductCartBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
        }

        fun bind(orderCart: CreateOrderListDetails?) {
            binding.orderCartInfo = orderCart
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderCartListAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowProductCartBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderCart.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderCart[position])
        db = Helper_Cart_DB(context);
        DB_Manager.initializeInstance(db);
        holder.binding.plistWeightText.setText("Packaging : " + orderCart[position].PM_Quantity_Val!!.toString() + " " + orderCart[position].PM_UOM_Detail)

        if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
            holder.binding.plistPriceTextStrike.setVisibility(View.VISIBLE)
            holder.binding.plistPriceTextStrike.setPaintFlags(holder.binding.plistPriceTextStrike.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                .toString()
            holder.binding.plistSpecialPriceText.setVisibility(View.VISIBLE)
            //Calculate Special Price


        } else {
            if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                holder.binding.plistSpecialPriceText.setVisibility(View.GONE)
                holder.binding.plistPriceTextStrike.setVisibility(View.VISIBLE)
                //Calculate Net Price
            }
        }

        //============================================Scheme========================================================

        /*if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
            holder.binding.spBeatName.visibility = View.VISIBLE
            var schemeList: MutableList<String> = ArrayList()
            schemeList.add("Select Scheme")
            for (i in orderCart[position].PSM_Scheme_Details!!) {
                schemeList.add(i.schemeName!!)
            }

            var adapterScheme =
                ArrayAdapter<String>(context, R.layout.simple_spinner_dropdown_item, schemeList);

            holder.binding.spBeatName.adapter = adapterScheme

            holder.binding.spBeatName.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        adapterView: AdapterView<*>,
                        view: View?,
                        selectPosition: Int,
                        id: Long
                    ) {
                        if (holder.binding!!.spBeatName.selectedItem == "Select Scheme") {
                            productSchemeId = ""
                            productSchemeName=""
                        } else {
                            productSchemeId =
                                orderCart[position].PSM_Scheme_Details!![selectPosition - 1].schemeId!!
                           // showToastMessage("productSchemeId" + productSchemeId)
                            productSchemeName= orderCart[position].PSM_Scheme_Details!![selectPosition - 1].schemeName!!
                        }

                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>) {}
                }
        } else {
            holder.binding.spBeatName.visibility = View.GONE
            productSchemeId = ""
            productSchemeName=""
        }*/

        //=============================================Pieces/Carton/Bucket=========================================

        var typeList: MutableList<String> = ArrayList()
        if (orderCart[position].PM_Pcs_OR_Bucket.equals("pcs")) {
            if (orderCart[position].PM_Unit_For_Carton != null && orderCart[position].PM_Carton_Price != null) {
                typeList.add(0, "Select Pieces/Carton")
                typeList.add(1, "Pieces")
                typeList.add(2, "Carton")
                holder.binding.cartonPriceText.visibility = View.VISIBLE
                holder.binding.cartonUnitText.visibility = View.VISIBLE

            } else {
                typeList.add(0, "Pieces")
                holder.binding.cartonPriceText.visibility = View.GONE
                holder.binding.cartonUnitText.visibility = View.GONE
            }
        }else if (orderCart[position].PM_Pcs_OR_Bucket.equals("set")) {
            if (orderCart[position].PM_Unit_For_Carton != null && orderCart[position].PM_Carton_Price != null) {
                typeList.add(0, "Select Set/Carton")
                typeList.add(1, "Set")
                typeList.add(2, "Carton")
                holder.binding.cartonPriceText.visibility = View.VISIBLE
                holder.binding.cartonUnitText.visibility = View.VISIBLE

            } else {
                typeList.add(0, "Set")
                holder.binding.cartonPriceText.visibility = View.GONE
                holder.binding.cartonUnitText.visibility = View.GONE
            }
        } else {
            typeList.add(0, "Bucket")
            holder.binding.cartonPriceText.visibility = View.GONE
            holder.binding.cartonUnitText.visibility = View.GONE
        }


        var y = ArrayAdapter<String>(context, R.layout.simple_spinner_dropdown_item, typeList);

        holder.binding.spPcsOrBucket.adapter = y
        var count = 0
        var convertLtr = 0.0

        holder.binding.spPcsOrBucket.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    // showToastMessage(holder.binding.spPcsOrBucket.selectedItem.toString())
                    if (holder.binding.spPcsOrBucket.selectedItem.toString().equals("Pieces")) {
                        holder.binding.cartProductQuantityTv.setText("0")

                        count = holder.binding.cartProductQuantityTv.text.toString().toInt()
                        if (orderCart[position].PM_Pcs_OR_Bucket.equals("pcs"))  //pcs
                        {

                            if (orderCart[position].PM_Unit_For_Carton != null && orderCart[position].PM_Carton_Price != null) {

                                if (holder.binding.spPcsOrBucket.selectedItemPosition == 0) {
                                    showToastMessage("Select pieces or carton first")
                                } else {


                                    if (holder.binding.spPcsOrBucket.selectedItemPosition == 1) // pcs
                                    {
                                        var totalPrice = 0.0

                                        prodQuantityType = "pcs"
                                        holder.binding.cartProductQuantityTv.setText(count.toString())

                                        if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                            convertLtr =
                                                (String.format(
                                                    "%.2f",
                                                    orderCart[position].PM_Quantity_Val!!.toFloat()
                                                ).toDouble() / 1000) * count.toDouble()
                                        } else {
                                            convertLtr =
                                                orderCart[position].PM_Quantity_Val!!.toFloat()
                                                    .toDouble() * count.toDouble()
                                        }

                                        if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                                            //Calculate Special Price
                                            totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count


                                        } else {
                                            if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                                                totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count


                                            }
                                        }
                                        if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                            onPutPrice(
                                                position,
                                                orderCart[position].PM_ID!!.toInt(),
                                                totalPrice,
                                                count.toString(),
                                                productSchemeId,
                                                prodQuantityType,
                                                orderCart[position].PM_MRP,
                                                orderCart[position].PM_Disc_Price,
                                                orderCart[position].PM_Net_Price,
                                                orderCart[position].PM_GST_Perc.toString()/*productGst*/,
                                                convertLtr.toString(),
                                                orderCart[position].PM_Brand_name,
                                                orderCart[position].PM_Image_Path,
                                                orderCart[position].PM_Unit_For_Carton.toString(),
                                                orderCart[position].PM_Carton_Price.toString(),
                                                orderCart[position].PM_Special_Price.toString(),
                                                orderCart[position].PM_UOM_Detail.toString(),
                                                productSchemeName
                                            )
                                        }else{
                                            productSchemeId=""
                                            productSchemeName=""
                                            onPutPrice(
                                                position,
                                                orderCart[position].PM_ID!!.toInt(),
                                                totalPrice,
                                                count.toString(),
                                                productSchemeId,
                                                prodQuantityType,
                                                orderCart[position].PM_MRP,
                                                orderCart[position].PM_Disc_Price,
                                                orderCart[position].PM_Net_Price,
                                                orderCart[position].PM_GST_Perc.toString()/*productGst*/,
                                                convertLtr.toString(),
                                                orderCart[position].PM_Brand_name,
                                                orderCart[position].PM_Image_Path,
                                                orderCart[position].PM_Unit_For_Carton.toString(),
                                                orderCart[position].PM_Carton_Price.toString(),
                                                orderCart[position].PM_Special_Price.toString(),
                                                orderCart[position].PM_UOM_Detail.toString(),
                                                productSchemeName
                                            )
                                        }

//// carton==================================================
                                    } else {
                                        var totalPrice = 0.0
                                        prodQuantityType = "carton"
                                        holder.binding.cartProductQuantityTv.setText(count.toString())

                                        if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                            convertLtr =
                                                (String.format(
                                                    "%.2f",
                                                    orderCart[position].PM_Quantity_Val!!.toFloat()
                                                ).toDouble() / 1000) * count.toDouble() *
                                                        orderCart[position].PM_Unit_For_Carton!!.toFloat()
                                        } else {

                                            convertLtr =
                                                (orderCart[position].PM_Quantity_Val!!.toFloat()).toDouble() * count.toDouble() *
                                                        orderCart[position].PM_Unit_For_Carton!!.toFloat()
                                        }
                                        totalPrice += (orderCart[position].PM_Carton_Price!!.toFloat()) * count
                                        if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                            onPutPrice(
                                                position,
                                                orderCart[position].PM_ID!!.toInt(),
                                                totalPrice,
                                                count.toString(),
                                                productSchemeId,
                                                prodQuantityType,
                                                orderCart[position].PM_MRP,
                                                orderCart[position].PM_Disc_Price,
                                                orderCart[position].PM_Net_Price,
                                                orderCart[position].PM_GST_Perc.toString(),
                                                convertLtr.toString(),
                                                orderCart[position].PM_Brand_name,
                                                orderCart[position].PM_Image_Path,
                                                orderCart[position].PM_Unit_For_Carton.toString(),
                                                orderCart[position].PM_Carton_Price.toString(),
                                                orderCart[position].PM_Special_Price.toString(),
                                                orderCart[position].PM_UOM_Detail.toString(),
                                                productSchemeName
                                            )
                                        }else{
                                            productSchemeId=""
                                            productSchemeName=""
                                            onPutPrice(
                                                position,
                                                orderCart[position].PM_ID!!.toInt(),
                                                totalPrice,
                                                count.toString(),
                                                productSchemeId,
                                                prodQuantityType,
                                                orderCart[position].PM_MRP,
                                                orderCart[position].PM_Disc_Price,
                                                orderCart[position].PM_Net_Price,
                                                orderCart[position].PM_GST_Perc.toString(),
                                                convertLtr.toString(),
                                                orderCart[position].PM_Brand_name,
                                                orderCart[position].PM_Image_Path,
                                                orderCart[position].PM_Unit_For_Carton.toString(),
                                                orderCart[position].PM_Carton_Price.toString(),
                                                orderCart[position].PM_Special_Price.toString(),
                                                orderCart[position].PM_UOM_Detail.toString(),
                                                productSchemeName
                                            )
                                        }
                                    }



                                }


                            } else {


                                if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                    convertLtr =
                                        (String.format(
                                            "%.2f",
                                            orderCart[position].PM_Quantity_Val!!.toFloat()
                                        )
                                            .toDouble() / 1000) * count.toDouble()
                                } else {
                                    convertLtr = orderCart[position].PM_Quantity_Val!!.toFloat()
                                        .toDouble() * count.toDouble()
                                }
                                var totalPrice = 0.0
                                if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                                    //Calculate Special Price
                                    totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count


                                } else {
                                    if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                                        //Calculate Net Price
                                        totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count

                                    }
                                }
                                prodQuantityType = "pcs"
                                orderCart[position].PM_Unit_For_Carton = ""
                                if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                    onPutPrice(
                                        position,
                                        orderCart[position].PM_ID!!.toInt(),
                                        totalPrice,
                                        count.toString(),
                                        productSchemeId,
                                        prodQuantityType,
                                        orderCart[position].PM_MRP,
                                        orderCart[position].PM_Disc_Price,
                                        orderCart[position].PM_Net_Price,
                                        orderCart[position].PM_GST_Perc.toString(),
                                        convertLtr.toString(),
                                        orderCart[position].PM_Brand_name,
                                        orderCart[position].PM_Image_Path,
                                        orderCart[position].PM_Unit_For_Carton.toString(),
                                        orderCart[position].PM_Carton_Price.toString(),
                                        orderCart[position].PM_Special_Price.toString(),
                                        orderCart[position].PM_UOM_Detail.toString(),
                                        productSchemeName
                                    )
                                }else{
                                    productSchemeId=""
                                    productSchemeName=""
                                    onPutPrice(
                                        position,
                                        orderCart[position].PM_ID!!.toInt(),
                                        totalPrice,
                                        count.toString(),
                                        productSchemeId,
                                        prodQuantityType,
                                        orderCart[position].PM_MRP,
                                        orderCart[position].PM_Disc_Price,
                                        orderCart[position].PM_Net_Price,
                                        orderCart[position].PM_GST_Perc.toString(),
                                        convertLtr.toString(),
                                        orderCart[position].PM_Brand_name,
                                        orderCart[position].PM_Image_Path,
                                        orderCart[position].PM_Unit_For_Carton.toString(),
                                        orderCart[position].PM_Carton_Price.toString(),
                                        orderCart[position].PM_Special_Price.toString(),
                                        orderCart[position].PM_UOM_Detail.toString(),
                                        productSchemeName
                                    )
                                }


                            }
                        }
                    } else if (holder.binding.spPcsOrBucket.selectedItem.toString().equals("Set")) {
                        holder.binding.cartProductQuantityTv.setText("0")

                        count = holder.binding.cartProductQuantityTv.text.toString().toInt()
                        if (orderCart[position].PM_Pcs_OR_Bucket.equals("set"))  //pcs
                        {

                            if (orderCart[position].PM_Unit_For_Carton != null && orderCart[position].PM_Carton_Price != null) {

                                if (holder.binding.spPcsOrBucket.selectedItemPosition == 0) {
                                    showToastMessage("Select set or carton first")
                                } else {


                                    if (holder.binding.spPcsOrBucket.selectedItemPosition == 1) // pcs
                                    {
                                        var totalPrice = 0.0

                                        prodQuantityType = "set"
                                        holder.binding.cartProductQuantityTv.setText(count.toString())

                                        if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                            convertLtr =
                                                (String.format(
                                                    "%.2f",
                                                    orderCart[position].PM_Quantity_Val!!.toFloat()
                                                ).toDouble() / 1000) * count.toDouble()
                                        } else {
                                            convertLtr =
                                                orderCart[position].PM_Quantity_Val!!.toFloat()
                                                    .toDouble() * count.toDouble()
                                        }

                                        if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                                            //Calculate Special Price
                                            totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count


                                        } else {
                                            if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                                                totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count


                                            }
                                        }
                                        if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                            onPutPrice(
                                                position,
                                                orderCart[position].PM_ID!!.toInt(),
                                                totalPrice,
                                                count.toString(),
                                                productSchemeId,
                                                prodQuantityType,
                                                orderCart[position].PM_MRP,
                                                orderCart[position].PM_Disc_Price,
                                                orderCart[position].PM_Net_Price,
                                                orderCart[position].PM_GST_Perc.toString()/*productGst*/,
                                                convertLtr.toString(),
                                                orderCart[position].PM_Brand_name,
                                                orderCart[position].PM_Image_Path,
                                                orderCart[position].PM_Unit_For_Carton.toString(),
                                                orderCart[position].PM_Carton_Price.toString(),
                                                orderCart[position].PM_Special_Price.toString(),
                                                orderCart[position].PM_UOM_Detail.toString(),
                                                productSchemeName
                                            )
                                        }else{
                                            productSchemeId=""
                                            productSchemeName=""
                                            onPutPrice(
                                                position,
                                                orderCart[position].PM_ID!!.toInt(),
                                                totalPrice,
                                                count.toString(),
                                                productSchemeId,
                                                prodQuantityType,
                                                orderCart[position].PM_MRP,
                                                orderCart[position].PM_Disc_Price,
                                                orderCart[position].PM_Net_Price,
                                                orderCart[position].PM_GST_Perc.toString()/*productGst*/,
                                                convertLtr.toString(),
                                                orderCart[position].PM_Brand_name,
                                                orderCart[position].PM_Image_Path,
                                                orderCart[position].PM_Unit_For_Carton.toString(),
                                                orderCart[position].PM_Carton_Price.toString(),
                                                orderCart[position].PM_Special_Price.toString(),
                                                orderCart[position].PM_UOM_Detail.toString(),
                                                productSchemeName
                                            )
                                        }

//// carton==================================================
                                    } else {
                                        var totalPrice = 0.0
                                        prodQuantityType = "carton"
                                        holder.binding.cartProductQuantityTv.setText(count.toString())

                                        if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                            convertLtr =
                                                (String.format(
                                                    "%.2f",
                                                    orderCart[position].PM_Quantity_Val!!.toFloat()
                                                ).toDouble() / 1000) * count.toDouble() *
                                                        orderCart[position].PM_Unit_For_Carton!!.toFloat()
                                        } else {

                                            convertLtr =
                                                (orderCart[position].PM_Quantity_Val!!.toFloat()).toDouble() * count.toDouble() *
                                                        orderCart[position].PM_Unit_For_Carton!!.toFloat()
                                        }
                                        totalPrice += (orderCart[position].PM_Carton_Price!!.toFloat()) * count
                                        if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                            onPutPrice(
                                                position,
                                                orderCart[position].PM_ID!!.toInt(),
                                                totalPrice,
                                                count.toString(),
                                                productSchemeId,
                                                prodQuantityType,
                                                orderCart[position].PM_MRP,
                                                orderCart[position].PM_Disc_Price,
                                                orderCart[position].PM_Net_Price,
                                                orderCart[position].PM_GST_Perc.toString(),
                                                convertLtr.toString(),
                                                orderCart[position].PM_Brand_name,
                                                orderCart[position].PM_Image_Path,
                                                orderCart[position].PM_Unit_For_Carton.toString(),
                                                orderCart[position].PM_Carton_Price.toString(),
                                                orderCart[position].PM_Special_Price.toString(),
                                                orderCart[position].PM_UOM_Detail.toString(),
                                                productSchemeName
                                            )
                                        }else{
                                            productSchemeId=""
                                            productSchemeName=""
                                            onPutPrice(
                                                position,
                                                orderCart[position].PM_ID!!.toInt(),
                                                totalPrice,
                                                count.toString(),
                                                productSchemeId,
                                                prodQuantityType,
                                                orderCart[position].PM_MRP,
                                                orderCart[position].PM_Disc_Price,
                                                orderCart[position].PM_Net_Price,
                                                orderCart[position].PM_GST_Perc.toString(),
                                                convertLtr.toString(),
                                                orderCart[position].PM_Brand_name,
                                                orderCart[position].PM_Image_Path,
                                                orderCart[position].PM_Unit_For_Carton.toString(),
                                                orderCart[position].PM_Carton_Price.toString(),
                                                orderCart[position].PM_Special_Price.toString(),
                                                orderCart[position].PM_UOM_Detail.toString(),
                                                productSchemeName
                                            )
                                        }
                                    }



                                }


                            } else {


                                if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                    convertLtr =
                                        (String.format(
                                            "%.2f",
                                            orderCart[position].PM_Quantity_Val!!.toFloat()
                                        )
                                            .toDouble() / 1000) * count.toDouble()
                                } else {
                                    convertLtr = orderCart[position].PM_Quantity_Val!!.toFloat()
                                        .toDouble() * count.toDouble()
                                }
                                var totalPrice = 0.0
                                if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                                    //Calculate Special Price
                                    totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count


                                } else {
                                    if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                                        //Calculate Net Price
                                        totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count

                                    }
                                }
                                prodQuantityType = "set"
                                orderCart[position].PM_Unit_For_Carton = ""
                                if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                    onPutPrice(
                                        position,
                                        orderCart[position].PM_ID!!.toInt(),
                                        totalPrice,
                                        count.toString(),
                                        productSchemeId,
                                        prodQuantityType,
                                        orderCart[position].PM_MRP,
                                        orderCart[position].PM_Disc_Price,
                                        orderCart[position].PM_Net_Price,
                                        orderCart[position].PM_GST_Perc.toString(),
                                        convertLtr.toString(),
                                        orderCart[position].PM_Brand_name,
                                        orderCart[position].PM_Image_Path,
                                        orderCart[position].PM_Unit_For_Carton.toString(),
                                        orderCart[position].PM_Carton_Price.toString(),
                                        orderCart[position].PM_Special_Price.toString(),
                                        orderCart[position].PM_UOM_Detail.toString(),
                                        productSchemeName
                                    )
                                }else{
                                    productSchemeId=""
                                    productSchemeName=""
                                    onPutPrice(
                                        position,
                                        orderCart[position].PM_ID!!.toInt(),
                                        totalPrice,
                                        count.toString(),
                                        productSchemeId,
                                        prodQuantityType,
                                        orderCart[position].PM_MRP,
                                        orderCart[position].PM_Disc_Price,
                                        orderCart[position].PM_Net_Price,
                                        orderCart[position].PM_GST_Perc.toString(),
                                        convertLtr.toString(),
                                        orderCart[position].PM_Brand_name,
                                        orderCart[position].PM_Image_Path,
                                        orderCart[position].PM_Unit_For_Carton.toString(),
                                        orderCart[position].PM_Carton_Price.toString(),
                                        orderCart[position].PM_Special_Price.toString(),
                                        orderCart[position].PM_UOM_Detail.toString(),
                                        productSchemeName
                                    )
                                }


                            }
                        }
                    }else {
                        if (holder.binding.spPcsOrBucket.selectedItem.toString().equals("Carton")) {
                            holder.binding.cartProductQuantityTv.setText("0")
                            count = holder.binding.cartProductQuantityTv.text.toString().toInt()
                            if (orderCart[position].PM_Pcs_OR_Bucket.equals("pcs"))  //pcs
                            {

                                if (orderCart[position].PM_Unit_For_Carton != null && orderCart[position].PM_Carton_Price != null) {

                                    if (holder.binding.spPcsOrBucket.selectedItemPosition == 0) {
                                        showToastMessage("Select pieces or carton first")
                                    } else {

                                        if (holder.binding.spPcsOrBucket.selectedItemPosition == 1) // pcs
                                        {
                                            var totalPrice = 0.0

                                            prodQuantityType = "pcs"
                                            holder.binding.cartProductQuantityTv.setText(count.toString())

                                            if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                                convertLtr =
                                                    (String.format(
                                                        "%.2f",
                                                        orderCart[position].PM_Quantity_Val!!.toFloat()
                                                    ).toDouble() / 1000) * count.toDouble()
                                            } else {
                                                convertLtr =
                                                    orderCart[position].PM_Quantity_Val!!.toFloat()
                                                        .toDouble() * count.toDouble()
                                            }

                                            if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                                                //Calculate Special Price
                                                totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count


                                            } else {
                                                if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                                                    totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count


                                                }
                                            }
                                            if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                                onPutPrice(
                                                    position,
                                                    orderCart[position].PM_ID!!.toInt(),
                                                    totalPrice,
                                                    count.toString(),
                                                    productSchemeId,
                                                    prodQuantityType,
                                                    orderCart[position].PM_MRP,
                                                    orderCart[position].PM_Disc_Price,
                                                    orderCart[position].PM_Net_Price,
                                                    orderCart[position].PM_GST_Perc.toString(),
                                                    convertLtr.toString(),
                                                    orderCart[position].PM_Brand_name,
                                                    orderCart[position].PM_Image_Path,
                                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                                    orderCart[position].PM_Carton_Price.toString(),
                                                    orderCart[position].PM_Special_Price.toString(),
                                                    orderCart[position].PM_UOM_Detail.toString(),
                                                    productSchemeName
                                                )
                                            }else{
                                                productSchemeId=""
                                                productSchemeName=""
                                                onPutPrice(
                                                    position,
                                                    orderCart[position].PM_ID!!.toInt(),
                                                    totalPrice,
                                                    count.toString(),
                                                    productSchemeId,
                                                    prodQuantityType,
                                                    orderCart[position].PM_MRP,
                                                    orderCart[position].PM_Disc_Price,
                                                    orderCart[position].PM_Net_Price,
                                                    orderCart[position].PM_GST_Perc.toString(),
                                                    convertLtr.toString(),
                                                    orderCart[position].PM_Brand_name,
                                                    orderCart[position].PM_Image_Path,
                                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                                    orderCart[position].PM_Carton_Price.toString(),
                                                    orderCart[position].PM_Special_Price.toString(),
                                                    orderCart[position].PM_UOM_Detail.toString(),
                                                    productSchemeName
                                                )
                                            }

//// carton==================================================
                                        } else {
                                            var totalPrice = 0.0
                                            prodQuantityType = "carton"
                                            holder.binding.cartProductQuantityTv.setText(count.toString())

                                            if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                                convertLtr =
                                                    (String.format(
                                                        "%.2f",
                                                        orderCart[position].PM_Quantity_Val!!.toFloat()
                                                    ).toDouble() / 1000) * count.toDouble() *
                                                            orderCart[position].PM_Unit_For_Carton!!.toFloat()
                                            } else {

                                                convertLtr =
                                                    (orderCart[position].PM_Quantity_Val!!.toFloat()).toDouble() * count.toDouble() *
                                                            orderCart[position].PM_Unit_For_Carton!!.toFloat()
                                            }
                                            totalPrice += (orderCart[position].PM_Carton_Price!!.toFloat()) * count
                                            if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                                onPutPrice(
                                                    position,
                                                    orderCart[position].PM_ID!!.toInt(),
                                                    totalPrice,
                                                    count.toString(),
                                                    productSchemeId,
                                                    prodQuantityType,
                                                    orderCart[position].PM_MRP,
                                                    orderCart[position].PM_Disc_Price,
                                                    orderCart[position].PM_Net_Price,
                                                    orderCart[position].PM_GST_Perc.toString(),
                                                    convertLtr.toString(),
                                                    orderCart[position].PM_Brand_name,
                                                    orderCart[position].PM_Image_Path,
                                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                                    orderCart[position].PM_Carton_Price.toString(),
                                                    orderCart[position].PM_Special_Price.toString(),
                                                    orderCart[position].PM_UOM_Detail.toString(),
                                                    productSchemeName
                                                )
                                            }else{
                                                productSchemeId=""
                                                productSchemeName=""
                                                onPutPrice(
                                                    position,
                                                    orderCart[position].PM_ID!!.toInt(),
                                                    totalPrice,
                                                    count.toString(),
                                                    productSchemeId,
                                                    prodQuantityType,
                                                    orderCart[position].PM_MRP,
                                                    orderCart[position].PM_Disc_Price,
                                                    orderCart[position].PM_Net_Price,
                                                    orderCart[position].PM_GST_Perc.toString(),
                                                    convertLtr.toString(),
                                                    orderCart[position].PM_Brand_name,
                                                    orderCart[position].PM_Image_Path,
                                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                                    orderCart[position].PM_Carton_Price.toString(),
                                                    orderCart[position].PM_Special_Price.toString(),
                                                    orderCart[position].PM_UOM_Detail.toString(),
                                                    productSchemeName
                                                )
                                            }
                                        }


                                    }


                                } else {

                                    if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                        convertLtr =
                                            (String.format(
                                                "%.2f",
                                                orderCart[position].PM_Quantity_Val!!.toFloat()
                                            )
                                                .toDouble() / 1000) * count.toDouble()
                                    } else {
                                        convertLtr = orderCart[position].PM_Quantity_Val!!.toFloat()
                                            .toDouble() * count.toDouble()
                                    }
                                    var totalPrice = 0.0
                                    if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                                        //Calculate Special Price
                                        totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count


                                    } else {
                                        if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                                            //Calculate Net Price
                                            totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count

                                        }
                                    }
                                    prodQuantityType = "pcs"
                                    orderCart[position].PM_Unit_For_Carton = ""
                                    if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                        onPutPrice(
                                            position,
                                            orderCart[position].PM_ID!!.toInt(),
                                            totalPrice,
                                            count.toString(),
                                            productSchemeId,
                                            prodQuantityType,
                                            orderCart[position].PM_MRP,
                                            orderCart[position].PM_Disc_Price,
                                            orderCart[position].PM_Net_Price,
                                            orderCart[position].PM_GST_Perc.toString(),
                                            convertLtr.toString(),
                                            orderCart[position].PM_Brand_name,
                                            orderCart[position].PM_Image_Path,
                                            orderCart[position].PM_Unit_For_Carton.toString(),
                                            orderCart[position].PM_Carton_Price.toString(),
                                            orderCart[position].PM_Special_Price.toString(),
                                            orderCart[position].PM_UOM_Detail.toString(),
                                            productSchemeName
                                        )
                                    }else{
                                        productSchemeId=""
                                        productSchemeName=""
                                        onPutPrice(
                                            position,
                                            orderCart[position].PM_ID!!.toInt(),
                                            totalPrice,
                                            count.toString(),
                                            productSchemeId,
                                            prodQuantityType,
                                            orderCart[position].PM_MRP,
                                            orderCart[position].PM_Disc_Price,
                                            orderCart[position].PM_Net_Price,
                                            orderCart[position].PM_GST_Perc.toString(),
                                            convertLtr.toString(),
                                            orderCart[position].PM_Brand_name,
                                            orderCart[position].PM_Image_Path,
                                            orderCart[position].PM_Unit_For_Carton.toString(),
                                            orderCart[position].PM_Carton_Price.toString(),
                                            orderCart[position].PM_Special_Price.toString(),
                                            orderCart[position].PM_UOM_Detail.toString(),
                                            productSchemeName
                                        )
                                    }


                                }
                            }else if (orderCart[position].PM_Pcs_OR_Bucket.equals("set"))  //pcs
                            {

                                if (orderCart[position].PM_Unit_For_Carton != null && orderCart[position].PM_Carton_Price != null) {

                                    if (holder.binding.spPcsOrBucket.selectedItemPosition == 0) {
                                        showToastMessage("Select set or carton first")
                                    } else {

                                        if (holder.binding.spPcsOrBucket.selectedItemPosition == 1) // pcs
                                        {
                                            var totalPrice = 0.0

                                            prodQuantityType = "set"
                                            holder.binding.cartProductQuantityTv.setText(count.toString())

                                            if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                                convertLtr =
                                                    (String.format(
                                                        "%.2f",
                                                        orderCart[position].PM_Quantity_Val!!.toFloat()
                                                    ).toDouble() / 1000) * count.toDouble()
                                            } else {
                                                convertLtr =
                                                    orderCart[position].PM_Quantity_Val!!.toFloat()
                                                        .toDouble() * count.toDouble()
                                            }

                                            if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                                                //Calculate Special Price
                                                totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count


                                            } else {
                                                if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                                                    totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count


                                                }
                                            }
                                            if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                                onPutPrice(
                                                    position,
                                                    orderCart[position].PM_ID!!.toInt(),
                                                    totalPrice,
                                                    count.toString(),
                                                    productSchemeId,
                                                    prodQuantityType,
                                                    orderCart[position].PM_MRP,
                                                    orderCart[position].PM_Disc_Price,
                                                    orderCart[position].PM_Net_Price,
                                                    orderCart[position].PM_GST_Perc.toString(),
                                                    convertLtr.toString(),
                                                    orderCart[position].PM_Brand_name,
                                                    orderCart[position].PM_Image_Path,
                                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                                    orderCart[position].PM_Carton_Price.toString(),
                                                    orderCart[position].PM_Special_Price.toString(),
                                                    orderCart[position].PM_UOM_Detail.toString(),
                                                    productSchemeName
                                                )
                                            }else{
                                                productSchemeId=""
                                                productSchemeName=""
                                                onPutPrice(
                                                    position,
                                                    orderCart[position].PM_ID!!.toInt(),
                                                    totalPrice,
                                                    count.toString(),
                                                    productSchemeId,
                                                    prodQuantityType,
                                                    orderCart[position].PM_MRP,
                                                    orderCart[position].PM_Disc_Price,
                                                    orderCart[position].PM_Net_Price,
                                                    orderCart[position].PM_GST_Perc.toString(),
                                                    convertLtr.toString(),
                                                    orderCart[position].PM_Brand_name,
                                                    orderCart[position].PM_Image_Path,
                                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                                    orderCart[position].PM_Carton_Price.toString(),
                                                    orderCart[position].PM_Special_Price.toString(),
                                                    orderCart[position].PM_UOM_Detail.toString(),
                                                    productSchemeName
                                                )
                                            }

//// carton==================================================
                                        } else {
                                            var totalPrice = 0.0
                                            prodQuantityType = "carton"
                                            holder.binding.cartProductQuantityTv.setText(count.toString())

                                            if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                                convertLtr =
                                                    (String.format(
                                                        "%.2f",
                                                        orderCart[position].PM_Quantity_Val!!.toFloat()
                                                    ).toDouble() / 1000) * count.toDouble() *
                                                            orderCart[position].PM_Unit_For_Carton!!.toFloat()
                                            } else {

                                                convertLtr =
                                                    (orderCart[position].PM_Quantity_Val!!.toFloat()).toDouble() * count.toDouble() *
                                                            orderCart[position].PM_Unit_For_Carton!!.toFloat()
                                            }
                                            totalPrice += (orderCart[position].PM_Carton_Price!!.toFloat()) * count
                                            if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                                onPutPrice(
                                                    position,
                                                    orderCart[position].PM_ID!!.toInt(),
                                                    totalPrice,
                                                    count.toString(),
                                                    productSchemeId,
                                                    prodQuantityType,
                                                    orderCart[position].PM_MRP,
                                                    orderCart[position].PM_Disc_Price,
                                                    orderCart[position].PM_Net_Price,
                                                    orderCart[position].PM_GST_Perc.toString(),
                                                    convertLtr.toString(),
                                                    orderCart[position].PM_Brand_name,
                                                    orderCart[position].PM_Image_Path,
                                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                                    orderCart[position].PM_Carton_Price.toString(),
                                                    orderCart[position].PM_Special_Price.toString(),
                                                    orderCart[position].PM_UOM_Detail.toString(),
                                                    productSchemeName
                                                )
                                            }else{
                                                productSchemeId=""
                                                productSchemeName=""
                                                onPutPrice(
                                                    position,
                                                    orderCart[position].PM_ID!!.toInt(),
                                                    totalPrice,
                                                    count.toString(),
                                                    productSchemeId,
                                                    prodQuantityType,
                                                    orderCart[position].PM_MRP,
                                                    orderCart[position].PM_Disc_Price,
                                                    orderCart[position].PM_Net_Price,
                                                    orderCart[position].PM_GST_Perc.toString(),
                                                    convertLtr.toString(),
                                                    orderCart[position].PM_Brand_name,
                                                    orderCart[position].PM_Image_Path,
                                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                                    orderCart[position].PM_Carton_Price.toString(),
                                                    orderCart[position].PM_Special_Price.toString(),
                                                    orderCart[position].PM_UOM_Detail.toString(),
                                                    productSchemeName
                                                )
                                            }
                                        }


                                    }


                                } else {

                                    if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                        convertLtr =
                                            (String.format(
                                                "%.2f",
                                                orderCart[position].PM_Quantity_Val!!.toFloat()
                                            )
                                                .toDouble() / 1000) * count.toDouble()
                                    } else {
                                        convertLtr = orderCart[position].PM_Quantity_Val!!.toFloat()
                                            .toDouble() * count.toDouble()
                                    }
                                    var totalPrice = 0.0
                                    if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                                        //Calculate Special Price
                                        totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count


                                    } else {
                                        if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                                            //Calculate Net Price
                                            totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count

                                        }
                                    }
                                    prodQuantityType = "set"
                                    orderCart[position].PM_Unit_For_Carton = ""
                                    if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                        onPutPrice(
                                            position,
                                            orderCart[position].PM_ID!!.toInt(),
                                            totalPrice,
                                            count.toString(),
                                            productSchemeId,
                                            prodQuantityType,
                                            orderCart[position].PM_MRP,
                                            orderCart[position].PM_Disc_Price,
                                            orderCart[position].PM_Net_Price,
                                            orderCart[position].PM_GST_Perc.toString(),
                                            convertLtr.toString(),
                                            orderCart[position].PM_Brand_name,
                                            orderCart[position].PM_Image_Path,
                                            orderCart[position].PM_Unit_For_Carton.toString(),
                                            orderCart[position].PM_Carton_Price.toString(),
                                            orderCart[position].PM_Special_Price.toString(),
                                            orderCart[position].PM_UOM_Detail.toString(),
                                            productSchemeName
                                        )
                                    }else{
                                        productSchemeId=""
                                        productSchemeName=""
                                        onPutPrice(
                                            position,
                                            orderCart[position].PM_ID!!.toInt(),
                                            totalPrice,
                                            count.toString(),
                                            productSchemeId,
                                            prodQuantityType,
                                            orderCart[position].PM_MRP,
                                            orderCart[position].PM_Disc_Price,
                                            orderCart[position].PM_Net_Price,
                                            orderCart[position].PM_GST_Perc.toString(),
                                            convertLtr.toString(),
                                            orderCart[position].PM_Brand_name,
                                            orderCart[position].PM_Image_Path,
                                            orderCart[position].PM_Unit_For_Carton.toString(),
                                            orderCart[position].PM_Carton_Price.toString(),
                                            orderCart[position].PM_Special_Price.toString(),
                                            orderCart[position].PM_UOM_Detail.toString(),
                                            productSchemeName
                                        )
                                    }


                                }
                            }
                        }
                    }

                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {

                }
            }

        //Picasso.get().load(orderCart[position].PM_Image_Path).fit().into(holder.binding.listImage)
        Picasso.with(context).load(orderCart[position].PM_Image_Path)
            .skipMemoryCache()
            .placeholder(com.velectico.rbm.R.drawable.faded_logo_bg)
            .error(com.velectico.rbm.R.drawable.faded_logo_bg)
            .into(holder.binding.listImage)


        holder.binding.cartProductQuantityTv.setText(count.toString())

        holder.binding.cartPlusImg.setOnClickListener {
            if (orderCart[position].PM_Pcs_OR_Bucket.equals("pcs"))  //pcs
            {
                if (orderCart[position].PM_Unit_For_Carton != null && orderCart[position].PM_Carton_Price != null) {

                    if (holder.binding.spPcsOrBucket.selectedItemPosition == 0) {
                        showToastMessage("Select pieces or carton first")
                    }
                    else {

                        count += 1
                        holder.binding.cartProductQuantityTv.setText("0")

                        if (holder.binding.spPcsOrBucket.selectedItemPosition == 1) // pcs
                        {
                            var totalPrice = 0.0

                            prodQuantityType = "pcs"
                            holder.binding.cartProductQuantityTv.setText(count.toString())

                            if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                convertLtr =
                                    (String.format(
                                        "%.2f",
                                        orderCart[position].PM_Quantity_Val!!.toFloat()
                                    ).toDouble() / 1000) * count.toDouble()
                            } else {
                                convertLtr = orderCart[position].PM_Quantity_Val!!.toFloat()
                                    .toDouble() * count.toDouble()
                            }

                            if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                                //Calculate Special Price
                                totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count


                            } else {
                                if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                                    totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count


                                }
                            }


                            if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                /*for (i in orderCart[position].PSM_Scheme_Details!!){
                                    var schemeName=i.schemeName
                                    showToastMessage("Scheme"+schemeName)

                                    val separated =
                                        i.schemeName!!.split(" ".toRegex()).toTypedArray()
                                    if (separated.size>1) {
                                        var quantity = separated[0]
                                        var type = separated[1]
                                        Log.e("Scheme", "onBindViewHolder: "+quantity+"\n"+type )
                                        if (quantity==count.toString() && type=="Pieces" ){

                                            holder.binding.tvSchemeName.text=i.schemeName
                                        }


                                    }
                                }*/
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }else{
                                productSchemeId=""
                                productSchemeName=""
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }

//// carton==================================================
                        } else {
                            var totalPrice = 0.0
                            prodQuantityType = "carton"
                            holder.binding.cartProductQuantityTv.setText(count.toString())

                            if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                convertLtr =
                                    (String.format(
                                        "%.2f",
                                        orderCart[position].PM_Quantity_Val!!.toFloat()
                                    ).toDouble() / 1000) * count.toDouble() *
                                            orderCart[position].PM_Unit_For_Carton!!.toFloat()
                            } else {

                                convertLtr =
                                    (orderCart[position].PM_Quantity_Val!!.toFloat()).toDouble() * count.toDouble() *
                                            orderCart[position].PM_Unit_For_Carton!!.toFloat()
                            }

                            totalPrice += (orderCart[position].PM_Carton_Price!!.toFloat()) * count
                            if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                /*for (i in orderCart[position].PSM_Scheme_Details!!){
                                    var schemeName=i.schemeName
                                    showToastMessage("Scheme"+schemeName)

                                    val separated =
                                        i.schemeName!!.split(" ".toRegex()).toTypedArray()
                                    if (separated.size>1) {
                                        var quantity = separated[0]
                                        var type = separated[1]
                                        Log.e("Scheme", "onBindViewHolder: "+quantity+"\n"+type )
                                        if (quantity==count.toString() && type=="cartoon" ){

                                            holder.binding.tvSchemeName.text=i.schemeName
                                        }


                                    }
                                }*/
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }else{
                                productSchemeId=""
                                productSchemeName=""
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }
                        }


                    }


                } else {

                    count += 1
                    holder.binding.cartProductQuantityTv.setText(count.toString())
                    if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                        convertLtr =
                            (String.format("%.2f", orderCart[position].PM_Quantity_Val!!.toFloat())
                                .toDouble() / 1000) * count.toDouble()
                    } else {
                        convertLtr = orderCart[position].PM_Quantity_Val!!.toFloat()
                            .toDouble() * count.toDouble()
                    }
                    var totalPrice = 0.0
                    if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                        //Calculate Special Price
                        totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count


                    } else {
                        if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                            //Calculate Net Price
                            totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count

                        }
                    }
                    prodQuantityType = "pcs"
                    orderCart[position].PM_Unit_For_Carton = ""
                    if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                        onPutPrice(
                            position,
                            orderCart[position].PM_ID!!.toInt(),
                            totalPrice,
                            count.toString(),
                            productSchemeId,
                            prodQuantityType,
                            orderCart[position].PM_MRP,
                            orderCart[position].PM_Disc_Price,
                            orderCart[position].PM_Net_Price,
                            orderCart[position].PM_GST_Perc.toString(),
                            convertLtr.toString(),
                            orderCart[position].PM_Brand_name,
                            orderCart[position].PM_Image_Path,
                            orderCart[position].PM_Unit_For_Carton.toString(),
                            orderCart[position].PM_Carton_Price.toString(),
                            orderCart[position].PM_Special_Price.toString(),
                            orderCart[position].PM_UOM_Detail.toString(),
                            productSchemeName
                        )
                    }else{
                        productSchemeId=""
                        productSchemeName=""
                        onPutPrice(
                            position,
                            orderCart[position].PM_ID!!.toInt(),
                            totalPrice,
                            count.toString(),
                            productSchemeId,
                            prodQuantityType,
                            orderCart[position].PM_MRP,
                            orderCart[position].PM_Disc_Price,
                            orderCart[position].PM_Net_Price,
                            orderCart[position].PM_GST_Perc.toString(),
                            convertLtr.toString(),
                            orderCart[position].PM_Brand_name,
                            orderCart[position].PM_Image_Path,
                            orderCart[position].PM_Unit_For_Carton.toString(),
                            orderCart[position].PM_Carton_Price.toString(),
                            orderCart[position].PM_Special_Price.toString(),
                            orderCart[position].PM_UOM_Detail.toString(),
                            productSchemeName
                        )
                    }


                }

            } //Set
            else if (orderCart[position].PM_Pcs_OR_Bucket.equals("set"))  //pcs
            {
                if (orderCart[position].PM_Unit_For_Carton != null && orderCart[position].PM_Carton_Price != null) {

                    if (holder.binding.spPcsOrBucket.selectedItemPosition == 0) {
                        showToastMessage("Select set or carton first")
                    } else {

                        count += 1
                        holder.binding.cartProductQuantityTv.setText("0")

                        if (holder.binding.spPcsOrBucket.selectedItemPosition == 1) // pcs
                        {
                            var totalPrice = 0.0

                            prodQuantityType = "set"
                            holder.binding.cartProductQuantityTv.setText(count.toString())

                            if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                convertLtr =
                                    (String.format(
                                        "%.2f",
                                        orderCart[position].PM_Quantity_Val!!.toFloat()
                                    ).toDouble() / 1000) * count.toDouble()
                            } else {
                                convertLtr = orderCart[position].PM_Quantity_Val!!.toFloat()
                                    .toDouble() * count.toDouble()
                            }

                            if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                                //Calculate Special Price
                                totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count


                            } else {
                                if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                                    totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count


                                }
                            }


                            if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }else{
                                productSchemeId=""
                                productSchemeName=""
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }

//// carton==================================================
                        } else {
                            var totalPrice = 0.0
                            prodQuantityType = "carton"
                            holder.binding.cartProductQuantityTv.setText(count.toString())

                            if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                convertLtr =
                                    (String.format(
                                        "%.2f",
                                        orderCart[position].PM_Quantity_Val!!.toFloat()
                                    ).toDouble() / 1000) * count.toDouble() *
                                            orderCart[position].PM_Unit_For_Carton!!.toFloat()
                            } else {

                                convertLtr =
                                    (orderCart[position].PM_Quantity_Val!!.toFloat()).toDouble() * count.toDouble() *
                                            orderCart[position].PM_Unit_For_Carton!!.toFloat()
                            }
                            totalPrice += (orderCart[position].PM_Carton_Price!!.toFloat()) * count
                            if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName

                                )
                            }else{
                                productSchemeId=""
                                productSchemeName=""
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }
                        }


                    }


                } else {

                    count += 1
                    holder.binding.cartProductQuantityTv.setText(count.toString())
                    if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                        convertLtr =
                            (String.format("%.2f", orderCart[position].PM_Quantity_Val!!.toFloat())
                                .toDouble() / 1000) * count.toDouble()
                    } else {
                        convertLtr = orderCart[position].PM_Quantity_Val!!.toFloat()
                            .toDouble() * count.toDouble()
                    }
                    var totalPrice = 0.0
                    if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                        //Calculate Special Price
                        totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count


                    } else {
                        if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                            //Calculate Net Price
                            totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count

                        }
                    }
                    prodQuantityType = "set"
                    orderCart[position].PM_Unit_For_Carton = ""
                    if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                        onPutPrice(
                            position,
                            orderCart[position].PM_ID!!.toInt(),
                            totalPrice,
                            count.toString(),
                            productSchemeId,
                            prodQuantityType,
                            orderCart[position].PM_MRP,
                            orderCart[position].PM_Disc_Price,
                            orderCart[position].PM_Net_Price,
                            orderCart[position].PM_GST_Perc.toString(),
                            convertLtr.toString(),
                            orderCart[position].PM_Brand_name,
                            orderCart[position].PM_Image_Path,
                            orderCart[position].PM_Unit_For_Carton.toString(),
                            orderCart[position].PM_Carton_Price.toString(),
                            orderCart[position].PM_Special_Price.toString(),
                            orderCart[position].PM_UOM_Detail.toString(),
                            productSchemeName
                        )
                    }else{
                        productSchemeId=""
                        productSchemeName=""
                        onPutPrice(
                            position,
                            orderCart[position].PM_ID!!.toInt(),
                            totalPrice,
                            count.toString(),
                            productSchemeId,
                            prodQuantityType,
                            orderCart[position].PM_MRP,
                            orderCart[position].PM_Disc_Price,
                            orderCart[position].PM_Net_Price,
                            orderCart[position].PM_GST_Perc.toString(),
                            convertLtr.toString(),
                            orderCart[position].PM_Brand_name,
                            orderCart[position].PM_Image_Path,
                            orderCart[position].PM_Unit_For_Carton.toString(),
                            orderCart[position].PM_Carton_Price.toString(),
                            orderCart[position].PM_Special_Price.toString(),
                            orderCart[position].PM_UOM_Detail.toString(),
                            productSchemeName
                        )
                    }


                }

            }

            else // bucket
            {
                var totalPrice = 0.0
                count += 1
                holder.binding.cartProductQuantityTv.setText(count.toString())
                if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                    convertLtr =
                        (String.format("%.2f", orderCart[position].PM_Quantity_Val!!.toFloat())
                            .toDouble() / 1000) * count.toDouble()
                } else {
                    convertLtr = orderCart[position].PM_Quantity_Val!!.toFloat().toDouble()
                }
                if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                    //Calculate Special Price
                    totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count


                } else {
                    if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {

                        totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count


                    }
                }
                prodQuantityType = "bucket"
                orderCart[position].PM_Unit_For_Carton = ""
                if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                    onPutPrice(
                        position,
                        orderCart[position].PM_ID!!.toInt(),
                        totalPrice,
                        count.toString(),
                        productSchemeId,
                        prodQuantityType,
                        orderCart[position].PM_MRP,
                        orderCart[position].PM_Disc_Price,
                        orderCart[position].PM_Net_Price,
                        orderCart[position].PM_GST_Perc.toString(),
                        convertLtr.toString(),
                        orderCart[position].PM_Brand_name,
                        orderCart[position].PM_Image_Path,
                        orderCart[position].PM_Unit_For_Carton.toString(),
                        orderCart[position].PM_Carton_Price.toString(),
                        orderCart[position].PM_Special_Price.toString(),
                        orderCart[position].PM_UOM_Detail.toString(),
                        productSchemeName
                    )
                }else{
                    productSchemeId=""
                    productSchemeName=""
                    onPutPrice(
                        position,
                        orderCart[position].PM_ID!!.toInt(),
                        totalPrice,
                        count.toString(),
                        productSchemeId,
                        prodQuantityType,
                        orderCart[position].PM_MRP,
                        orderCart[position].PM_Disc_Price,
                        orderCart[position].PM_Net_Price,
                        orderCart[position].PM_GST_Perc.toString(),
                        convertLtr.toString(),
                        orderCart[position].PM_Brand_name,
                        orderCart[position].PM_Image_Path,
                        orderCart[position].PM_Unit_For_Carton.toString(),
                        orderCart[position].PM_Carton_Price.toString(),
                        orderCart[position].PM_Special_Price.toString(),
                        orderCart[position].PM_UOM_Detail.toString(),
                        productSchemeName
                    )
                }


            }


        }


        holder.binding.cartMinusImg.setOnClickListener {
            if (orderCart[position].PM_Pcs_OR_Bucket.equals("pcs"))  //pcs
            {

                if (orderCart[position].PM_Unit_For_Carton != null && orderCart[position].PM_Carton_Price != null) {

                    if (holder.binding.spPcsOrBucket.selectedItemPosition == 0) {
                        showToastMessage("Select pieces or carton first")
                    } else {
                        if (count > 0) {
                            count -= 1
                        } else if (count == 0) {
                            db!!.deleteCart(orderCart[position].PM_ID!!.toInt())

                        }

                        if (holder.binding.spPcsOrBucket.selectedItemPosition == 1) // pcs
                        {
                            var totalPrice = 0.0

                            holder.binding.cartProductQuantityTv.setText(count.toString())


                            if (count > 0) {
                                if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                    convertLtr =
                                        (String.format(
                                            "%.2f",
                                            orderCart[position].PM_Quantity_Val!!.toFloat()
                                        ).toDouble() / 1000) * count.toDouble()
                                } else {
                                    convertLtr = orderCart[position].PM_Quantity_Val!!.toFloat()
                                        .toDouble() * count.toDouble()
                                }
                            }

                            if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                                if (count > 0) {
                                    totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count
                                }

                            } else {
                                if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                                    if (count > 0) {
                                        totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count
                                    }

                                }
                            }
                            prodQuantityType = "pcs"
                            if (orderCart[position].PSM_Scheme_Details!!.size > 0) {

                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }else{
                                productSchemeId=""
                                productSchemeName=""
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }


                        } else                                                        // carton
                        {
                            //
                            var totalPrice = 0.0

                            holder.binding.cartProductQuantityTv.setText(count.toString())
                            if (count > 0) {
                                if (orderCart[position].PM_UOM_Detail.equals("ML")) {

                                    convertLtr =
                                        (String.format(
                                            "%.2f",
                                            orderCart[position].PM_Quantity_Val!!.toFloat()
                                        ).toDouble() / 1000) * count.toDouble() *
                                                orderCart[position].PM_Unit_For_Carton!!.toFloat()
                                } else {

                                    convertLtr =
                                        (orderCart[position].PM_Quantity_Val!!.toFloat()) * count.toDouble() *
                                                orderCart[position].PM_Unit_For_Carton!!.toFloat()
                                }
                            }
                            totalPrice += (orderCart[position].PM_Carton_Price!!.toFloat()) * count
                            prodQuantityType = "carton"
                            if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }else{
                               productSchemeId=""
                                productSchemeName=""
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }
                        }

                    }

                } else {
                    if (count > 0) {
                        count -= 1
                    } else if (count == 0) {
                        db!!.deleteCart(orderCart[position].PM_ID!!.toInt())

                    }
                    holder.binding.cartProductQuantityTv.setText(count.toString())
                    if (count > 0) {
                        if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                            convertLtr =
                                (String.format(
                                    "%.2f",
                                    orderCart[position].PM_Quantity_Val!!.toFloat()
                                )
                                    .toDouble() / 1000) * count.toDouble()
                        } else {
                            convertLtr = orderCart[position].PM_Quantity_Val!!.toFloat()
                                .toDouble() * count.toDouble()
                        }
                    }
                    var totalPrice = 0.0
                    if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                        if (count > 0) {
                            totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count
                        }


                    } else {
                        if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                            if (count > 0) {
                                totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count
                            }

                        }
                    }
                    prodQuantityType = "pcs"
                    orderCart[position].PM_Unit_For_Carton = ""
                    if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                        onPutPrice(
                            position,
                            orderCart[position].PM_ID!!.toInt(),
                            totalPrice,
                            count.toString(),
                            productSchemeId,
                            prodQuantityType,
                            orderCart[position].PM_MRP,
                            orderCart[position].PM_Disc_Price,
                            orderCart[position].PM_Net_Price,
                            orderCart[position].PM_GST_Perc.toString(),
                            convertLtr.toString(),
                            orderCart[position].PM_Brand_name,
                            orderCart[position].PM_Image_Path,
                            orderCart[position].PM_Unit_For_Carton.toString(),
                            orderCart[position].PM_Carton_Price.toString(),
                            orderCart[position].PM_Special_Price.toString(),
                            orderCart[position].PM_UOM_Detail.toString(),
                            productSchemeName
                        )
                    }else{
                        productSchemeId=""
                        productSchemeName=""
                        onPutPrice(
                            position,
                            orderCart[position].PM_ID!!.toInt(),
                            totalPrice,
                            count.toString(),
                            productSchemeId,
                            prodQuantityType,
                            orderCart[position].PM_MRP,
                            orderCart[position].PM_Disc_Price,
                            orderCart[position].PM_Net_Price,
                            orderCart[position].PM_GST_Perc.toString(),
                            convertLtr.toString(),
                            orderCart[position].PM_Brand_name,
                            orderCart[position].PM_Image_Path,
                            orderCart[position].PM_Unit_For_Carton.toString(),
                            orderCart[position].PM_Carton_Price.toString(),
                            orderCart[position].PM_Special_Price.toString(),
                            orderCart[position].PM_UOM_Detail.toString(),
                            productSchemeName
                        )
                    }

                }
            }else if (orderCart[position].PM_Pcs_OR_Bucket.equals("set"))  //set
            {

                if (orderCart[position].PM_Unit_For_Carton != null && orderCart[position].PM_Carton_Price != null) {

                    if (holder.binding.spPcsOrBucket.selectedItemPosition == 0) {
                        showToastMessage("Select set or carton first")
                    } else {
                        if (count > 0) {
                            count -= 1
                        } else if (count == 0) {
                            db!!.deleteCart(orderCart[position].PM_ID!!.toInt())

                        }

                        if (holder.binding.spPcsOrBucket.selectedItemPosition == 1) // pcs
                        {
                            var totalPrice = 0.0

                            holder.binding.cartProductQuantityTv.setText(count.toString())


                            if (count > 0) {
                                if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                                    convertLtr =
                                        (String.format(
                                            "%.2f",
                                            orderCart[position].PM_Quantity_Val!!.toFloat()
                                        ).toDouble() / 1000) * count.toDouble()
                                } else {
                                    convertLtr = orderCart[position].PM_Quantity_Val!!.toFloat()
                                        .toDouble() * count.toDouble()
                                }
                            }

                            if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                                if (count > 0) {
                                    totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count
                                }

                            } else {
                                if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                                    if (count > 0) {
                                        totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count
                                    }

                                }
                            }
                            prodQuantityType = "set"
                            if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }else{
                                productSchemeId=""
                                productSchemeName=""
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }


                        } else                                                        // carton
                        {
                            //
                            var totalPrice = 0.0

                            holder.binding.cartProductQuantityTv.setText(count.toString())
                            if (count > 0) {
                                if (orderCart[position].PM_UOM_Detail.equals("ML")) {

                                    convertLtr =
                                        (String.format(
                                            "%.2f",
                                            orderCart[position].PM_Quantity_Val!!.toFloat()
                                        ).toDouble() / 1000) * count.toDouble() *
                                                orderCart[position].PM_Unit_For_Carton!!.toFloat()
                                } else {

                                    convertLtr =
                                        (orderCart[position].PM_Quantity_Val!!.toFloat()) * count.toDouble() *
                                                orderCart[position].PM_Unit_For_Carton!!.toFloat()
                                }
                            }
                            totalPrice += (orderCart[position].PM_Carton_Price!!.toFloat()) * count
                            prodQuantityType = "carton"
                            if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }else{
                                productSchemeId=""
                                productSchemeName=""
                                onPutPrice(
                                    position,
                                    orderCart[position].PM_ID!!.toInt(),
                                    totalPrice,
                                    count.toString(),
                                    productSchemeId,
                                    prodQuantityType,
                                    orderCart[position].PM_MRP,
                                    orderCart[position].PM_Disc_Price,
                                    orderCart[position].PM_Net_Price,
                                    orderCart[position].PM_GST_Perc.toString(),
                                    convertLtr.toString(),
                                    orderCart[position].PM_Brand_name,
                                    orderCart[position].PM_Image_Path,
                                    orderCart[position].PM_Unit_For_Carton.toString(),
                                    orderCart[position].PM_Carton_Price.toString(),
                                    orderCart[position].PM_Special_Price.toString(),
                                    orderCart[position].PM_UOM_Detail.toString(),
                                    productSchemeName
                                )
                            }
                        }

                    }

                } else {
                    if (count > 0) {
                        count -= 1
                    } else if (count == 0) {
                        db!!.deleteCart(orderCart[position].PM_ID!!.toInt())

                    }
                    holder.binding.cartProductQuantityTv.setText(count.toString())
                    if (count > 0) {
                        if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                            convertLtr =
                                (String.format(
                                    "%.2f",
                                    orderCart[position].PM_Quantity_Val!!.toFloat()
                                )
                                    .toDouble() / 1000) * count.toDouble()
                        } else {
                            convertLtr = orderCart[position].PM_Quantity_Val!!.toFloat()
                                .toDouble() * count.toDouble()
                        }
                    }
                    var totalPrice = 0.0
                    if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                        if (count > 0) {
                            totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count
                        }


                    } else {
                        if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                            if (count > 0) {
                                totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count
                            }

                        }
                    }
                    prodQuantityType = "set"
                    orderCart[position].PM_Unit_For_Carton = ""
                    if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                        onPutPrice(
                            position,
                            orderCart[position].PM_ID!!.toInt(),
                            totalPrice,
                            count.toString(),
                            productSchemeId,
                            prodQuantityType,
                            orderCart[position].PM_MRP,
                            orderCart[position].PM_Disc_Price,
                            orderCart[position].PM_Net_Price,
                            orderCart[position].PM_GST_Perc.toString(),
                            convertLtr.toString(),
                            orderCart[position].PM_Brand_name,
                            orderCart[position].PM_Image_Path,
                            orderCart[position].PM_Unit_For_Carton.toString(),
                            orderCart[position].PM_Carton_Price.toString(),
                            orderCart[position].PM_Special_Price.toString(),
                            orderCart[position].PM_UOM_Detail.toString(),
                            productSchemeName
                        )
                    }else{
                        productSchemeId=""
                        productSchemeName=""
                        onPutPrice(
                            position,
                            orderCart[position].PM_ID!!.toInt(),
                            totalPrice,
                            count.toString(),
                            productSchemeId,
                            prodQuantityType,
                            orderCart[position].PM_MRP,
                            orderCart[position].PM_Disc_Price,
                            orderCart[position].PM_Net_Price,
                            orderCart[position].PM_GST_Perc.toString(),
                            convertLtr.toString(),
                            orderCart[position].PM_Brand_name,
                            orderCart[position].PM_Image_Path,
                            orderCart[position].PM_Unit_For_Carton.toString(),
                            orderCart[position].PM_Carton_Price.toString(),
                            orderCart[position].PM_Special_Price.toString(),
                            orderCart[position].PM_UOM_Detail.toString(),
                            productSchemeName
                        )
                    }

                }
            }
            else // bucket
            {
                var totalPrice = 0.0
                if (count > 0) {
                    count -= 1
                } else if (count == 0) {
                    db!!.deleteCart(orderCart[position].PM_ID!!.toInt())

                }
                holder.binding.cartProductQuantityTv.setText(count.toString())
                if (count > 0) {
                    if (orderCart[position].PM_UOM_Detail.equals("ML")) {
                        convertLtr =
                            (String.format("%.2f", orderCart[position].PM_Quantity_Val!!.toFloat())
                                .toDouble() / 1000) * count.toDouble()
                    } else {
                        convertLtr = orderCart[position].PM_Quantity_Val!!.toFloat().toDouble()
                    }
                }
                if (orderCart[position].PM_Special_Price!!.toFloat() > 0) {
                    if (count > 0) {
                        totalPrice += (orderCart[position].PM_Special_Price!!.toFloat()) * count
                    }


                } else {
                    if (orderCart[position].PM_Net_Price!!.toFloat() > 0) {
                        if (count > 0) {
                            totalPrice += (orderCart[position].PM_Net_Price!!.toFloat()) * count
                        }

                    }
                }
                prodQuantityType = "bucket"
                orderCart[position].PM_Unit_For_Carton = ""

                if (orderCart[position].PSM_Scheme_Details!!.size > 0) {
                    onPutPrice(
                        position,
                        orderCart[position].PM_ID!!.toInt(),
                        totalPrice,
                        count.toString(),
                        productSchemeId,
                        prodQuantityType,
                        orderCart[position].PM_MRP,
                        orderCart[position].PM_Disc_Price,
                        orderCart[position].PM_Net_Price,
                        orderCart[position].PM_GST_Perc.toString(),
                        convertLtr.toString(),
                        orderCart[position].PM_Brand_name,
                        orderCart[position].PM_Image_Path,
                        orderCart[position].PM_Unit_For_Carton.toString(),
                        orderCart[position].PM_Carton_Price.toString(),
                        orderCart[position].PM_Special_Price.toString(),
                        orderCart[position].PM_UOM_Detail.toString(),
                        productSchemeName

                    )
                }else{
                   productSchemeId=""
                    productSchemeName=""
                    onPutPrice(
                        position,
                        orderCart[position].PM_ID!!.toInt(),
                        totalPrice,
                        count.toString(),
                        productSchemeId,
                        prodQuantityType,
                        orderCart[position].PM_MRP,
                        orderCart[position].PM_Disc_Price,
                        orderCart[position].PM_Net_Price,
                        orderCart[position].PM_GST_Perc.toString(),
                        convertLtr.toString(),
                        orderCart[position].PM_Brand_name,
                        orderCart[position].PM_Image_Path,
                        orderCart[position].PM_Unit_For_Carton.toString(),
                        orderCart[position].PM_Carton_Price.toString(),
                        orderCart[position].PM_Special_Price.toString(),
                        orderCart[position].PM_UOM_Detail.toString(),
                        productSchemeName
                    )
                }


            }


        }

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
        productSchemeName:String
    ) {
        hashMap.put(productId, productTotalPrice)

        for (key in hashMap.keys) {
            if (hashMap.containsKey(productId)) {
                hashMap.replace(productId, productTotalPrice)
                db!!.addProduct(
                    productId.toString(),
                    productSchemeId,
                    pmBrandName,
                    productQuantity,
                    prodQuantityType,
                    pmImagePath,
                    pmMrp,
                    pmDiscPrice,
                    pmNetPrice,
                    productTotalPrice,
                    productGst,
                    totalLtr,
                    unit_carton,
                    cartonPrice,
                    specialPrice,
                    unitLtr,
                    productSchemeName
                )
                db!!.deleteCart(productId)
                db!!.addProduct(
                    productId.toString(),
                    productSchemeId,
                    pmBrandName,
                    productQuantity,
                    prodQuantityType,
                    pmImagePath,
                    pmMrp,
                    pmDiscPrice,
                    pmNetPrice,
                    productTotalPrice,
                    productGst,
                    totalLtr,
                    unit_carton,
                    cartonPrice,
                    specialPrice,
                    unitLtr,
                    productSchemeName
                )
            } else {
                hashMap.put(productId, productTotalPrice)

            }

        }
        calGrandTotal()
    }
    fun calGrandTotal() {
        var sum = 0.0
        for (key in hashMap.keys) {
            var floatStr1 = hashMap[key].toString()
            var total = floatStr1.toDouble()
            sum += total

        }
        println(sum)
        if (sum == 0.0) {
            tvProdId.setText("")
            db!!.clearCart()
            btnCheckOut.isEnabled = false

        } else {
            tvProdId.setText("₹ " + sum.toString())
            DataConstant.grandTotalPrice = sum.toString()
            btnCheckOut.isEnabled = true
        }
    }

    protected fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


}