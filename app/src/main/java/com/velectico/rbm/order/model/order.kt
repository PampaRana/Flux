package com.velectico.rbm.order.model

import com.velectico.rbm.expense.model.Expense
import java.util.*

class Order {

    var dealerId : String?= null;
    var distId : String?= null;
    var dealerName: String?= null;
    var distName : String?= null;
    var orderDate : String?= null;
    var orderStatus : String?= null;
    var orderPaymentMode : String?= null;
    var orderCloseDate : String?= null;
    var orderCancelDate : String?= null;
    var orderPaymentDate : String?= null;
    var orderPaymentCnfStatus : String?= null;
    var orderCreatedBy : String?= null;
    var orderCreatedDate : String?= null;
    var orderModifiedBy : String?= null;
    var orderModifiedDate : String?= null;
    var orderId : String?= null;
    var orderProdId : String?= null;
    var orderProdName : String?= null;
    var orderGstPer : String?= null;
    var orderMrp : String?= null;
    var orderQuantity : String?= null;
    var orderProductImage : String?=null

    fun getDummyOrderList() : List<Order> {
        var cal : Calendar = Calendar.getInstance();
        val orderList = mutableListOf<Order>()
        for (i in 0..10) {
            var tempOrder = Order();
            tempOrder.orderId= "ORD-123456"
            tempOrder.dealerId = "12548"
            tempOrder.distId = "896589"
            tempOrder.orderDate = "12/25/2020"
            tempOrder.orderStatus = "In-Progress"
            tempOrder.orderPaymentMode = "Cash"
            tempOrder.orderCloseDate = "14/02/2020"
            tempOrder.orderCancelDate = "14/02/2020"
            tempOrder.orderPaymentDate = "14/02/2020"
            tempOrder.orderPaymentCnfStatus = "Approved"
            tempOrder.orderCreatedBy = "SP-002"
            tempOrder.orderCreatedDate = "15/02/2020"
            tempOrder.orderModifiedBy = "NA"
            tempOrder.orderModifiedDate = "14/02/2020"
            tempOrder.orderProdId = "P-5896"
            tempOrder.orderProdName = "Caster Oil Pro"
            tempOrder.orderGstPer = "50"
            tempOrder.orderMrp = "200"
            tempOrder.orderQuantity = "2"
            tempOrder.distName = "Sam Dist"
            tempOrder.dealerName = "Oil wala"
            tempOrder.orderProductImage = "Oil wala"

            orderList.add(tempOrder)
        }
        return orderList;
    }



}