package com.velectico.rbm.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*


class Helper_Cart_DB(context: Context?) :
    SQLiteOpenHelper(context, Constant_Cart_DB.CART_DB_NAME, null, Constant_Cart_DB.DB_VERSION) {
    var db: SQLiteDatabase? = null
    var cursor: Cursor? = null
    var cv: ContentValues? = null
    var total=0.0
    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL(Constant_Cart_DB.CREATE_TB)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + Constant_Cart_DB.CART_TB_NAME)
        onCreate(db)
    }

    //*********** Clear all User Cart ********//
    fun clearCart() {
        // get and open SQLiteDatabase Instance from static method of DB_Manager class
        db = DB_Manager.getInstance()!!.openDatabase()
        db!!.delete(Constant_Cart_DB.CART_TB_NAME, null, null)

        // close the Database
        DB_Manager.getInstance()!!.closeDatabase()
    }
    //Count
    val cartCount: Int
        get() {
            db = writableDatabase
            val countQuery = "SELECT * FROM " + Constant_Cart_DB.CART_TB_NAME
            db = this.writableDatabase
            cursor = db!!.rawQuery(countQuery, null)
            val count: Int = cursor!!.getCount()
            cursor!!.close()
            return count
        }// Select All Query


    fun getCartItems(): MutableList<CartProduct> {
        val list: MutableList<CartProduct> = ArrayList()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM " + Constant_Cart_DB.CART_TB_NAME
        val result = db.rawQuery(selectQuery, null)
        Log.e("LocalDb", "getCartItems: "+result )
        if (result.moveToFirst()) {
            do {
                val cartGetSet = CartProduct()
                cartGetSet.cart_id=result.getInt(result.getColumnIndex(Constant_Cart_DB.CART_ID)).toInt()
                cartGetSet.cart_product_id=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_ID)).toString()
                cartGetSet.cart_product_scheme_id=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_SCHEME_ID)).toString()
                cartGetSet.cart_product_name=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_NAME)).toString()
                cartGetSet.cart_product_quantity=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_QUANTITY)).toString()
                cartGetSet.cart_product_quantity_type=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_QUANTITY_TYPE)).toString()
                cartGetSet.cart_product_image=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_IMAGE)).toString()
                cartGetSet.cart_product_mrp_price=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_MRP_PRICE)).toString()
                cartGetSet.cart_product_disc_price=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_DISC_PRICE)).toString()
                cartGetSet.cart_product_net_price=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_NET_PRICE)).toString()
                cartGetSet.cart_product_total_price=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_TOTAL_PRICE)).toString()
                cartGetSet.cart_product_gst=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_GST)).toString()
                cartGetSet.cart_product_ltr=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_LTR)).toString()
                cartGetSet.cart_unit_carton=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_UNIT_FOR_CARTON)).toString()
                cartGetSet.cart_carton_price=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_CARTON_PRICE)).toString()
                cartGetSet.cart_special_price=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_SPECIAL_PRICE)).toString()
                cartGetSet.cart_product_quantity_ltr=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_QUANTITY_LTR)).toString()
                cartGetSet.cart_product_scheme_name=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_SCHEME_NAME)).toString()
                cartGetSet.cart_product_total_ltr=result.getString(result.getColumnIndex(Constant_Cart_DB.CART_PRODUCT_TOTAL_LTR)).toString()

                list.add(cartGetSet)

            }
            while (result.moveToNext())
        }
        Log.e("DB LIST", "getCartItems: "+list )
        return list
    }


    @SuppressLint("LogNotTimber")
    fun checkIfRecordExist(
        nameOfTable: String,
        columnName: String,
        columnValue: String
    ): Boolean {
        try {
            db = this.readableDatabase
            val cursor = db!!.rawQuery(
                "SELECT $columnName FROM $nameOfTable WHERE $columnName='$columnValue'",
                null
            )
            if (cursor.moveToFirst()) {
                db!!.close()
                Log.d(
                    "Record  Already Exists",
                    "Table is:$nameOfTable ColumnName:$columnName"
                )
                return true //record Exists
            }
            Log.d(
                "New Record  ",
                "Table is:$nameOfTable ColumnName:$columnName Column Value:$columnValue"
            )
            db!!.close()
        } catch (errorException: Exception) {
            Log.d("Exception occured", "Exception occured $errorException")
            db!!.close()
        }
        return false
    }

    fun deleteCart(cart_id: Int) {
        db = DB_Manager.getInstance()!!.openDatabase()
        db!!.delete(
            Constant_Cart_DB.CART_TB_NAME,
            Constant_Cart_DB.CART_PRODUCT_ID + " = ?",
            arrayOf(cart_id.toString())
        )
        DB_Manager.getInstance()!!.closeDatabase()
    }


    fun getCartTotalPrice(): Double {
        val selectQuery =
            "SELECT Sum(cart_product_total_price)  AS cart_product_total_price FROM " + Constant_Cart_DB.CART_TB_NAME
        db = DB_Manager.getInstance()!!.openDatabase()
        cursor = db!!.rawQuery(selectQuery, null)
        if (cursor!!.moveToFirst()){
            do {
                total = cursor!!.getDouble(cursor!!.getColumnIndex("cart_product_total_price"))
            } while (cursor!!.moveToNext())
        }
        DB_Manager.getInstance()!!.closeDatabase()

        return total
    }

    fun addProduct(
        productId: String,
        productSchemeId: String,
        pmBrandName: String?,
        productQuantity: String,
        prodQuantityType: String,
        pmImagePath: String?,
        pmMrp: String?,
        pmDiscPrice: String?,
        pmNetPrice: String?,
        productTotalPrice: Double,
        productGst: String,
        totalLtr: String,
        unit_carton: String,
        cartonPrice: String,
        specialPrice: String,
        unitLtr: String,
        productSchemeName: String,
        pdtTotalLtr: String
    ) {
        db = DB_Manager.getInstance()!!.openDatabase()
        cv = ContentValues()
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_ID, productId)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_SCHEME_ID, productSchemeId)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_NAME, pmBrandName)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_QUANTITY, productQuantity)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_QUANTITY_TYPE, prodQuantityType)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_IMAGE, pmImagePath)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_MRP_PRICE, pmMrp)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_DISC_PRICE, pmDiscPrice)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_NET_PRICE, pmNetPrice)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_TOTAL_PRICE, productTotalPrice)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_GST, productGst)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_LTR, totalLtr)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_UNIT_FOR_CARTON, unit_carton)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_CARTON_PRICE, cartonPrice)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_SPECIAL_PRICE, specialPrice)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_QUANTITY_LTR, unitLtr)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_SCHEME_NAME, productSchemeName)
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_TOTAL_LTR, pdtTotalLtr)
        db!!.insert(Constant_Cart_DB.CART_TB_NAME, null, cv)
        DB_Manager.getInstance()!!.closeDatabase()
    }

    /*fun updateProduct(
        productId: String,
        productSchemeId: String,
        pmBrandName: String?,
        productQuantity: String,
        prodQuantityType: String,
        pmImagePath: String?,
        pmMrp: String?,
        pmDiscPrice: String?,
        pmNetPrice: String?,
        productTotalPrice: Double,
        productGst: String,
        totalLtr: String
    ): Boolean {

        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(Constant_Cart_DB.CART_PRODUCT_ID, productId)
        cv.put(Constant_Cart_DB.CART_PRODUCT_SCHEME_ID, productSchemeId)
        cv.put(Constant_Cart_DB.CART_PRODUCT_NAME, pmBrandName)
        cv.put(Constant_Cart_DB.CART_PRODUCT_QUANTITY, productQuantity)
        cv.put(Constant_Cart_DB.CART_PRODUCT_QUANTITY_TYPE, prodQuantityType)
        cv.put(Constant_Cart_DB.CART_PRODUCT_IMAGE, pmImagePath)
        cv.put(Constant_Cart_DB.CART_PRODUCT_MRP_PRICE, pmMrp)
        cv.put(Constant_Cart_DB.CART_PRODUCT_DISC_PRICE, pmDiscPrice)
        cv.put(Constant_Cart_DB.CART_PRODUCT_NET_PRICE, pmNetPrice)
        cv.put(Constant_Cart_DB.CART_PRODUCT_TOTAL_PRICE, productTotalPrice)
        cv.put(Constant_Cart_DB.CART_PRODUCT_GST, productGst)
        cv.put(Constant_Cart_DB.CART_PRODUCT_LTR, totalLtr)
       // db.update(Constant_Cart_DB.CART_TB_NAME,cv, "cart_product_id= ?", String[]{productId});

       // db.update(Constant_Cart_DB.CART_TB_NAME, cv, "cart_product_id=$productId",null)
         db.update(Constant_Cart_DB.CART_TB_NAME,cv, "cart_product_id= ?",
             arrayOf(java.lang.String.valueOf{productId}))

        return true
    }*/


    /*fun ifCartExists(product_id: String): Boolean {
        var cursor: Cursor? = null
        val checkQuery =
            "SELECT " + Constant_Cart_DB.CART_PRODUCT_ID.toString() + " FROM " + Constant_Cart_DB.CART_TB_NAME.toString() + " WHERE " + Constant_Cart_DB.CART_PRODUCT_ID.toString() + "= '" + product_id + "'"
        cursor = db!!.rawQuery(checkQuery, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }


    fun updateCart(cartProduct: CartProduct): Int {
        db = DB_Manager.getInstance()!!.openDatabase()
        cv = ContentValues()
        cv!!.put(Constant_Cart_DB.CART_PRODUCT_QUANTITY, cartProduct.cart_product_quantity)
        cv!!.put(
            Constant_Cart_DB.CART_PRODUCT_TOTAL_PRICE,
            cartProduct.cart_product_total_price
        )
        // updating row
        return db!!.update(
            Constant_Cart_DB.CART_TB_NAME,
            cv,
            Constant_Cart_DB.CART_ID + " = ?",
            arrayOf(java.lang.String.valueOf(cartProduct.cart_id))
        )
    }*/

    /*fun updateProduct(cartProduct: CartProduct): Int {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(Constant_Cart_DB.CART_PRODUCT_ID, cartProduct.cart_product_id)
        cv.put(Constant_Cart_DB.CART_PRODUCT_SCHEME_ID, cartProduct.cart_product_scheme_id)
        cv.put(Constant_Cart_DB.CART_PRODUCT_NAME, cartProduct.cart_product_name)
        cv.put(Constant_Cart_DB.CART_PRODUCT_QUANTITY, cartProduct.cart_product_quantity)
        cv.put(Constant_Cart_DB.CART_PRODUCT_QUANTITY_TYPE, cartProduct.cart_product_quantity_type)
        cv.put(Constant_Cart_DB.CART_PRODUCT_IMAGE, cartProduct.cart_product_image)
        cv.put(Constant_Cart_DB.CART_PRODUCT_MRP_PRICE, cartProduct.cart_product_mrp_price)
        cv.put(Constant_Cart_DB.CART_PRODUCT_DISC_PRICE, cartProduct.cart_product_disc_price)
        cv.put(Constant_Cart_DB.CART_PRODUCT_NET_PRICE, cartProduct.cart_product_net_price)
        cv.put(Constant_Cart_DB.CART_PRODUCT_TOTAL_PRICE, cartProduct.cart_product_net_price)
        cv.put(Constant_Cart_DB.CART_PRODUCT_GST, cartProduct.cart_product_gst)
        cv.put(Constant_Cart_DB.CART_PRODUCT_LTR, cartProduct.cart_product_ltr)

        // Updating Row
        val success = db.update(Constant_Cart_DB.CART_TB_NAME, cv,"cart_product_id="+cartProduct.cart_product_id,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success

    }*/
}


