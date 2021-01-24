package com.velectico.rbm.teamlist.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable

data class TeamListPerformanceRequestParams(
    @SerializedName("userId") var userId: String
) : BaseModel()

data class TeamListPerformanceResponse(
    @SerializedName("Performance_outOf_5")
    val Performance_outOf_5: String,

    @SerializedName("AnnualExpense")
    val AnnualExpense: Int,

    @SerializedName("ExpensePerTask")
    val ExpensePerTask: Double,

    @SerializedName("leaveTaken")
    val leaveTaken: Int,

    @SerializedName("Sales_Order")
    val Sales_Order: SalesOrder,

    @SerializedName("Collection")
    val CollectionPerformance: CollectionPerformance

)

data class TeamListDealDistPerformanceResponse(
    @SerializedName("outOf_100")
    val outOf_100: String,

    @SerializedName("Performance_outOf_4")
    val Performance_outOf_4: String,

    @SerializedName("Sales_Order")
    val Sales_Order: SalesOrder,

    @SerializedName("Collection")
    val CollectionPerformance: CollectionPerformance

)



data class SalesOrder(
    val Monthly: Monthly,
    val Quaterly: Quaterly,
    val Hly: Hly,
    val Annually:Annually
)

data class CollectionPerformance(
    val Monthly: CollectionMonthly,
    val Quaterly: CollectionQuaterly,
    val Hly: CollectionHly,
    val Annually:CollectionAnnually
)

data class Monthly(
    var order_target: String? ,
    var order_target_achieve: String?
) : Serializable

data class Quaterly(
    var order_target: String? ,
    var order_target_achieve: String?
) : Serializable

data class Hly(
    var order_target: String? ,
    var order_target_achieve: String?
) : Serializable

data class Annually(
    var order_target: String? ,
    var order_target_achieve: Int
) : Serializable


data class CollectionMonthly(
    var collection_target: String? ,
    var collection_target_achieve: String?
) : Serializable

data class CollectionQuaterly(
    var collection_target: String? ,
    var collection_target_achieve: String?
) : Serializable

data class CollectionHly(
    var collection_target: String? ,
    var collection_target_achieve: String?
) : Serializable

data class CollectionAnnually(
    var collection_target: String? ,
    var collection_target_achieve: String
) : Serializable