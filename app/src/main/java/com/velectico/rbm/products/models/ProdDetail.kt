package com.velectico.rbm.products.models

import com.google.gson.annotations.SerializedName

class ProdDetail(
    @SerializedName("PM_ID")
    var prodId: String? = null,
    @SerializedName("PM_Brand")
    var prodName: String? = null,
    @SerializedName("PM_GST_Perc")
    var prodGst: String? = null,
    @SerializedName("PM_Image_Path")
    val PM_Image_Path: String,
    @SerializedName("PM_Seg")
    var prodSegment: String? = null,
    @SerializedName("PM_UOM")
    var prodPackaging: String? = null,
    @SerializedName("PM_MRP")
    var prodNetPrice: String? = null,
    @SerializedName("PM_Net_Price")
    var prodTotalPrice: String? = null,
    @SerializedName("PM_Scheme")
    var prodSchemeName: String? = null,
    @SerializedName("PM_Cat")
    var prodCategoty: String? = null,
    @SerializedName("PM_Disc_Price")
    var prodDiscount: String? = null,
    val PM_Type: String
)

/*
https://jsoneditoronline.org/#left=cloud.6109a1ae8c824fa98c4610a7054e2ee8&right=local.qovete
* {
  "status": 1,
  "count": 10,
  "prodDetails": [
    {
      "PM_ID": "14",
      "PM_Type": "Product Type 1",
      "PM_Cat": "prod cat12",
      "PM_UOM": "UOM 1",
      "PM_Seg": "prod seg2",
      "PM_Brand": "BN2",
      "PM_Scheme": "Prod scheme 1",
      "PM_Image_Path": "www.velectico.top/RBM-Lubricants/upload/products/14_prd-1.jpg",
      "PM_GST_Perc": "10.00",
      "PM_MRP": "3000.00",
      "PM_Disc_Price": "5.00",
      "PM_Net_Price": "3300.00"
    }
  ]
}
* */