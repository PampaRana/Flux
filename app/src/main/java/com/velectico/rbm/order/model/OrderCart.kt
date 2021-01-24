package com.velectico.rbm.order.model

import java.util.*

class OrderCart {

    var productImage : String ?= null
    var productName : String?= null
    var productNetPrice : String?= null
    var productGst : String?= null
    var productTotalPrice : String?=null

    fun getDummyOrderCart() : List<OrderCart> {
        val OrderCart = mutableListOf<OrderCart>()
        for (i in 0..10) {
            var tempOrder = OrderCart();
            tempOrder.productImage = "ORD-123456"
            tempOrder.productName = "Prod Lube "+i
            tempOrder.productNetPrice = "500"
            tempOrder.productGst = "50"
            tempOrder.productTotalPrice = "550"
            OrderCart.add(tempOrder)
        }
        return OrderCart;
    }

}