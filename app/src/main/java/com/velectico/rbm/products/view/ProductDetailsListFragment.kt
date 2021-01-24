package com.velectico.rbm.products.view

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.CreateOrderListDetails
import com.velectico.rbm.beats.model.PSM_Scheme_DetailsResponse
import com.velectico.rbm.databinding.FragmentProductDetailsListBinding
import com.velectico.rbm.databinding.RowProductListBinding
import com.velectico.rbm.products.adapters.ProductSchemeListAdapter
import com.velectico.rbm.utils.MECHANIC_ROLE
import com.velectico.rbm.utils.SharedPreferencesClass
import kotlinx.android.synthetic.main.fragment_product_details_list.*

/**
 * A simple [Fragment] subclass.
 */
class ProductDetailsListFragment : BaseFragment() {

    private lateinit var binding: FragmentProductDetailsListBinding
    var productDetail = CreateOrderListDetails()
   // var schmDetail = PSM_Scheme_DetailsResponse()
    private lateinit var adapter : ProductSchemeListAdapter
    private var schemeList : List<PSM_Scheme_DetailsResponse> = emptyList()
    var role=""
    override fun getLayout(): Int {
        return R.layout.fragment_product_details_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentProductDetailsListBinding
        productDetail = arguments!!.get("productDetails")  as CreateOrderListDetails
        schemeList = productDetail.PSM_Scheme_Details?.toMutableList()!!


        binding.tvProdName.text = productDetail.PM_Seg_Name
        binding.tvTypes.text = productDetail.PM_Type_Name
        binding.tvProdCat.text = productDetail.PM_Cat_Name
        binding.tvUOM.text =productDetail.PM_Quantity_Val+" "+ productDetail.PM_UOM_Detail
        binding.tvBrand.text = productDetail.PM_Brand_name
        binding.tvMrp.text = "₹ " +productDetail.PM_MRP
        binding.tvHsnNo.text = productDetail.PM_HSN
        binding.tvGstNo.text = productDetail.PM_GST_Perc +"%"
        binding.tvDiscPrice.text = "₹ " +productDetail.PM_Net_Price
        //binding.tvNetPrice.text = "₹ " +productDetail.PM_Net_Price
        if (productDetail.PM_Unit_For_Carton != null && productDetail.PM_Carton_Price != null) {
            binding.llCartonUnit.visibility=View.VISIBLE
            binding.tvCartonUnit.text=productDetail.PM_Unit_For_Carton
            binding.llCarton.visibility=View.VISIBLE
            binding.tvCartonPrice.text="₹ " +productDetail.PM_Carton_Price

        }else{
            binding.llCartonUnit.visibility=View.GONE
            binding.llCarton.visibility=View.GONE

        }

        if (productDetail.PM_Coupon_Point != null){
            binding.llCoupon.visibility=View.VISIBLE
            binding.tvCouponPoint.text=productDetail.PM_Coupon_Point
        }else{
            binding.llCoupon.visibility=View.GONE

        }

        if (productDetail.PM_Special_Price!!.toFloat() > 0) {
            binding.llSp.visibility=View.VISIBLE
            binding.tvSplPrice.text = "₹ " +productDetail.PM_Special_Price
            binding.tvDiscPrice.setPaintFlags(binding.tvDiscPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG).toString()
        }else{
            binding.llSp.visibility=View.GONE
        }
        binding.gst.text = productDetail.PM_Feature
       // Picasso.get().load(productDetail.PM_Image_Path).fit().into(binding.ivProdImageUrl)
        Picasso.with(context).load(productDetail.PM_Image_Path)
            .skipMemoryCache()
            .placeholder(R.drawable.faded_logo_bg)
            .error(R.drawable.faded_logo_bg)
            .into(binding.ivProdImageUrl)
        binding.descLayout.setBackgroundResource(R.drawable.customtab_bg)


        role= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Role").toString()

        if(role == MECHANIC_ROLE){
           binding.schemeLayout.visibility = View.GONE
           // binding.llGst.visibility = View.GONE
           // binding.llMrp.visibility = View.GONE
            binding.llDisc.visibility = View.GONE
            binding.llSp.visibility = View.GONE
       }
        binding.descLayout.setOnClickListener {
            cardDescription.visibility = View.VISIBLE;
            featurecard.visibility = View.GONE;
            schemecard.visibility = View.GONE;
            binding.descLayout.setBackgroundResource(R.drawable.customtab_bg)
            binding.featureLayout.setBackgroundResource(R.drawable.complaint_spinner)
            binding.schemeLayout.setBackgroundResource(R.drawable.complaint_spinner)

        }

        binding.featureLayout.setOnClickListener {
            cardDescription.visibility = View.GONE;
            featurecard.visibility = View.VISIBLE;
            schemecard.visibility = View.GONE;
            binding.featureLayout.setBackgroundResource(R.drawable.customtab_bg)
            binding.descLayout.setBackgroundResource(R.drawable.complaint_spinner)
            binding.schemeLayout.setBackgroundResource(R.drawable.complaint_spinner)

        }

        binding.schemeLayout.setOnClickListener {
            if (schemeList.count() > 0){
                setUpRecyclerView()
            }
            else{
                showToastMessage("No scheme Found")
            }
            cardDescription.visibility = View.GONE;
            featurecard.visibility = View.GONE;
            schemecard.visibility = View.VISIBLE;
            binding.schemeLayout.setBackgroundResource(R.drawable.customtab_bg)
            binding.featureLayout.setBackgroundResource(R.drawable.complaint_spinner)
            binding.descLayout.setBackgroundResource(R.drawable.complaint_spinner)

        }




    }

    private fun setUpRecyclerView() {
        /* adapter = ProductListAdapter(
             requireActivity()
         );*/

        adapter =
            ProductSchemeListAdapter(object : ProductSchemeListAdapter.IProdListActionCallBack {
                override fun moveToProdDetails(
                    position: Int,
                    beatTaskId: String?,
                    binding: RowProductListBinding
                ) {


                }
            }, context as Context);


        binding?.rvProductSchemeList?.adapter = adapter
        adapter.data = schemeList
    }

}
