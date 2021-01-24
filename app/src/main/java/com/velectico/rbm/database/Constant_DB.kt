package com.velectico.rbm.database
/* id: Int,
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
        pmImagePath: String?*/

object Constant_Cart_DB {
    const val CART_ID = "id"
    const val CART_PRODUCT_ID = "cart_product_id"
    const val CART_PRODUCT_SCHEME_ID = "cart_product_scheme_id"
    const val CART_PRODUCT_NAME = "cart_product_name"
    const val CART_PRODUCT_QUANTITY = "cart_product_quantity"
    const val CART_PRODUCT_QUANTITY_TYPE = "cart_product_quantity_type"
    const val CART_PRODUCT_IMAGE = "cart_product_image"
    const val CART_PRODUCT_MRP_PRICE = "cart_product_mrp_price"
    const val CART_PRODUCT_DISC_PRICE = "cart_product_disc_price"
    const val CART_PRODUCT_NET_PRICE = "cart_product_net_price"
    const val CART_PRODUCT_TOTAL_PRICE = "cart_product_total_price"
    const val CART_PRODUCT_GST = "cart_product_gst"
    const val CART_PRODUCT_LTR = "cart_product_ltr"
    const val CART_PRODUCT_UNIT_FOR_CARTON = "cart_product_unit_for_carton"
    const val CART_PRODUCT_CARTON_PRICE = "cart_product_carton_price"
    const val CART_PRODUCT_SPECIAL_PRICE = "cart_product_special_price"
    //const val CART_PRODUCT_QUANTITY_VAL_LTR="cart_product_quantity_val_ltr"
    const val CART_PRODUCT_QUANTITY_LTR = "cart_product_quantity_ltr"
    const val CART_PRODUCT_SCHEME_NAME="cart_product_scheme_name"
    const val CART_DB_NAME = "CARTS_DB"
    const val CART_TB_NAME = "CARTS_TB"
    const val DB_VERSION = 1

    val CREATE_TB = "CREATE TABLE " + CART_TB_NAME +
            "(" +
            CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            CART_PRODUCT_ID + " TEXT," +
            CART_PRODUCT_SCHEME_ID + " TEXT," +
            CART_PRODUCT_NAME + " TEXT," +
            CART_PRODUCT_QUANTITY + " TEXT," +
            CART_PRODUCT_QUANTITY_TYPE + " TEXT," +
            CART_PRODUCT_IMAGE + " TEXT," +
            CART_PRODUCT_MRP_PRICE + " TEXT," +
            CART_PRODUCT_DISC_PRICE + " TEXT," +
            CART_PRODUCT_NET_PRICE + " TEXT," +
            CART_PRODUCT_TOTAL_PRICE + " DOUBLE," +
            CART_PRODUCT_GST + " TEXT," +
            CART_PRODUCT_LTR + " TEXT," +
            CART_PRODUCT_UNIT_FOR_CARTON + " TEXT," +
            CART_PRODUCT_CARTON_PRICE + " TEXT," +
            CART_PRODUCT_SPECIAL_PRICE + " TEXT," +
           // CART_PRODUCT_QUANTITY_VAL_LTR + " TEXT" +
            CART_PRODUCT_QUANTITY_LTR + " TEXT," +
            CART_PRODUCT_SCHEME_NAME + " TEXT" +
            ")"



}
