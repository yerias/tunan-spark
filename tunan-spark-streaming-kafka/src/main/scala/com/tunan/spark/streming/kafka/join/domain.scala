package com.tunan.spark.streming.kafka.join

object domain {

    // 订单表
    case class Order(orderId: String, userId: String)

    // 订单
    case class OrderInfo(infoId: String, orderId: String, skuId: String, skuName: String, skuNum: Int)


    case class OrderDetail(var orderId: String = "",
                           var userId: String = "",
                           var infoId: String = "",
                           var skuId: String = "",
                           var skuName: String = "",
                           var skuNum: Int = 0) {

        def mergeOrder(order: Order): OrderDetail = {
            if (order != null) {
                this.orderId = order.orderId
                this.userId = order.userId
            }
            this
        }

        def mergeOrderInfo(orderInfo: OrderInfo): OrderDetail = {
            if (orderInfo != null) {
                this.infoId = orderInfo.infoId
                this.skuId = orderInfo.skuId
                this.skuName = orderInfo.skuName
                this.skuNum = orderInfo.skuNum
            }
            this
        }
    }
}
