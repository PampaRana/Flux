package com.velectico.rbm.products.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.beats.model.CreateOrderListDetails
import com.velectico.rbm.beats.model.PSM_Scheme_DetailsResponse
import com.velectico.rbm.databinding.RowProductListBinding
import com.velectico.rbm.databinding.RowProductschemeItemBinding

class ProductSchemeListAdapter (var setCallback: ProductSchemeListAdapter.IProdListActionCallBack,
var context: Context
) : RecyclerView.Adapter<ProductSchemeListAdapter.ViewHolder>() {

    var callBack: ProductSchemeListAdapter.IProdListActionCallBack? = null
    var data = listOf<PSM_Scheme_DetailsResponse>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    inner class ViewHolder(_binding: RowProductschemeItemBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
            callBack = setCallback;
//            binding.navigateToDetails.setOnClickListener {
//                callBack?.moveToProdDetails(adapterPosition, "1", binding)
//            }
        }

        fun bind(productInfo: PSM_Scheme_DetailsResponse?) {
            binding.schemeInfo = productInfo
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowProductschemeItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
        //Picasso.get().load(data[position].imagePath).fit().into(holder.binding.ivProdImageUrl)
        Picasso.with(context).load(data[position].imagePath)
            .skipMemoryCache()
            .placeholder(R.drawable.faded_logo_bg)
            .error(R.drawable.faded_logo_bg)
            .into(holder.binding.ivProdImageUrl)
    }


    interface IProdListActionCallBack {
        fun moveToProdDetails(position: Int, beatTaskId: String?, binding: RowProductListBinding)

    }
}