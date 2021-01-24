package com.velectico.rbm.order.model

import java.util.*

class OrderHead {
    var orderId : String?= null
    var dealerId : String?= null;
    var distId : String?= null;
    var dealerName: String?= null;
    var distName : String?= null;
    var orderDate : String?= null;
    var orderStatus : String?= null;
    var orderCreatedBy : String?= null;
    var orderCreatedDate : String?= null;
    var totalAmt : String? = null

    fun getDummyOrderList() : List<OrderHead> {
        var cal : Calendar = Calendar.getInstance();
        val orderHeadList = mutableListOf<OrderHead>()
        for (i in 0..10) {
            var tempOrder = OrderHead();
            tempOrder.orderId= "ORD-123456"
            tempOrder.distId = "896589"
            tempOrder.dealerId = "12548"
            tempOrder.distName = ""
            tempOrder.orderDate = "25 Jul 20"
            tempOrder.orderStatus = "In-Progress"
            tempOrder.orderCreatedBy = "DealerName/SalespersonName"
            tempOrder.orderCreatedDate = "15/02/2020"
            tempOrder.totalAmt = "896.23"
            orderHeadList.add(tempOrder)
        }
        return orderHeadList;
    }

}