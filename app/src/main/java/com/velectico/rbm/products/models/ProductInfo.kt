package com.velectico.rbm.products.models

import com.google.gson.annotations.SerializedName

class ProductInfo {
    @SerializedName("PM_ID")
    var prodId: String? = null
    @SerializedName("PM_Brand")
    var prodName: String? = null
    @SerializedName("PM_GST_Perc")
    var prodGst: String? = null
    @SerializedName("PM_Image_Path")
    var prodImageUrl: String? = null
    @SerializedName("PM_Seg")
    var prodSegment: String? = null
    @SerializedName("PM_UOM")
    var prodPackaging: String? = null
    @SerializedName("PM_MRP")
    var prodNetPrice: String? = null
    @SerializedName("PM_Net_Price")
    var prodTotalPrice: String? = null
    @SerializedName("PM_Scheme")
    var prodSchemeName: String? = null
    @SerializedName("PM_Cat")
    var prodCategoty: String? = null
    @SerializedName("PM_Disc_Price")
    var prodDiscount: String? = null
    var PM_Type: String? = null

    var unit: String? = null
    var partNo: String? = null
    var prodCode: String? = null
    var prodSchemeId: String? = null



    /*fun getDummyData(): ArrayList<ProductInfo> {
        val arrayList = ArrayList<ProductInfo>()

        val data1 = ProductInfo();
        data1.prodId= "ID 1"
        data1.prodName = "data1 prod name"
        data1.prodCode = "Code 1";
        data1.prodImageUrl = "https://images.sftcdn.net/images/t_app-cover-l,f_auto/p/ce2ece60-9b32-11e6-95ab-00163ed833e7/260663710/the-test-fun-for-friends-screenshot.jpg"
        data1.prodNetPrice = "Rs 20,0000";
        data1.prodTotalPrice = "Rs 20,0000"
        data1.unit = "Unit 1 data";
        data1.partNo = "1";
        data1.prodSchemeId = "prodSchemeId 123";
        data1.prodSchemeName = "prodSchemeName 2222"
        data1.prodPackaging = "prodPackaging 2222"
        data1.prodSegment = "prodSegment 1233"
        data1.prodGst = "GST 1";

        arrayList.add(data1);
        arrayList.add(data1);
        arrayList.add(data1);
        arrayList.add(data1);
        arrayList.add(data1);

        return arrayList;
    }*/
}