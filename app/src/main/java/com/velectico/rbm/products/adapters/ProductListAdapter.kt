package com.velectico.rbm.products.adapters

import android.content.Context
import android.graphics.Paint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.beats.model.CreateOrderListDetails
import com.velectico.rbm.databinding.RowProductListBinding
import com.velectico.rbm.products.view.ProductListFragment.Companion.seletedItemsChecked
import com.velectico.rbm.utils.MECHANIC_ROLE
import com.velectico.rbm.utils.SharedPreferencesClass

//https://codelabs.developers.google.com/codelabs/kotlin-android-training-recyclerview-fundamentals/#4
//https://medium.com/@sanjeevy133/an-idiots-guide-to-android-recyclerview-and-databinding-4ebf8db0daff
//https://medium.com/androiddevelopers/android-data-binding-recyclerview-db7c40d9f0e4 @TODO

class ProductListAdapter(
    var setCallback: IProdListActionCallBack,
    var context: Context
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    //    private val userRole = userRole
//    private lateinit var menuViewModel: MenuViewModel
    //private val mActivity = ProductListFragment()
    var callBack: ProductListAdapter.IProdListActionCallBack? = null
    var data = listOf<CreateOrderListDetails>()
    var role = ""
    var checkBoxStateArray = SparseBooleanArray()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    inner class ViewHolder(_binding: RowProductListBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {

            callBack = setCallback;
            binding.card.setOnClickListener {
                callBack?.moveToProdDetails(adapterPosition, "1", binding)
            }


        }

        fun bind(productInfo: CreateOrderListDetails?) {
            binding.productInfo = productInfo
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowProductListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
        Picasso.with(context).load(
            data[position].PM_Image_Path
        )
            .skipMemoryCache() //.placeholder(R.drawable.place_holder)
            .error(R.drawable.faded_logo_bg)
            .into(holder.binding.ivProdImageUrl, object : Callback {
                override fun onSuccess() {
                    holder.binding.contentProgressBar.visibility= View.GONE
                }

                override fun onError() {
                    holder.binding.contentProgressBar.visibility= View.GONE

                }
            })
        /*Picasso.with(context).load(data[position].PM_Image_Path)
            .skipMemoryCache()
            .placeholder(R.drawable.faded_logo_bg)
            .error(R.drawable.faded_logo_bg)
            .into(holder.binding.ivProdImageUrl)*/
        //Picasso.get().load(data[position].PM_Image_Path).fit().into(holder.binding.ivProdImageUrl)
        //holder.binding.cbProdSel.isChecked = checkBoxStateArray.get(position,false)
        role = SharedPreferencesClass.retriveData(context, "UM_Role").toString()
        if (role == MECHANIC_ROLE) {
            holder.binding.llMrp.visibility = View.VISIBLE
            holder.binding.llSp.visibility = View.GONE
            holder.binding.prodPricelayout.visibility = View.GONE
            holder.binding.cbProdSel.visibility=View.GONE

        } else {
            holder.binding.llMrp.visibility = View.GONE
            holder.binding.llSp.visibility = View.VISIBLE
            holder.binding.prodPricelayout.visibility = View.VISIBLE
            holder.binding.cbProdSel.visibility=View.VISIBLE


            if (data[position].PM_Special_Price!!.toFloat() > 0) {
                holder.binding.llSp.visibility = View.VISIBLE
                holder.binding.tvSplPrice.text = "₹ " + data[position].PM_Special_Price
                holder.binding.tvPrice.setPaintFlags(holder.binding.tvPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                    .toString()
            } else {
                holder.binding.llSp.visibility = View.GONE
            }
        }



        holder.binding.cbProdSel.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                //listener.onItemClick()
                seletedItemsChecked.add(data[position])
                // show toast , check box is checked
            } else {
                seletedItemsChecked.remove(data[position])
                // show toast , check box is not checked
            }
        }





    }


    interface IProdListActionCallBack {
        fun moveToProdDetails(position: Int, beatTaskId: String?, binding: RowProductListBinding)

    }


}