package com.tunan.spark.sql.mongo

case class Collect(
                    id: String,
                    houseId: String,
                    houseType: String,
                    memberId: String,
                    createTime: String,
                    hsmid: String,
                    hsid: String,
                    area: String,
                    roomNumber: String,
                    price: String,
                    shopMapName: String,
                    status: String,
                    hid: String,
                    stressName: String,
                    houseNumber: String

                  ) {
    override def toString: String = id + ',' +
      houseId + ',' +
      houseType + ',' +
      memberId + ',' +
      createTime + ',' +
      hsmid + ',' +
      hsid + ',' +
      area + ',' +
      roomNumber + ',' +
      price + ',' +
      shopMapName + ',' +
      status + ',' +
      hid + ',' +
      stressName + ',' +
      houseNumber
}
