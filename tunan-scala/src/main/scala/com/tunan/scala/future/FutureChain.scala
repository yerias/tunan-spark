package com.tunan.scala.future

object FutureChain extends App {

    println("Step 1: Define a method which returns a Future")

    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.Future

    def donutStock(donut: String): Future[Int] = Future {
        // assume some long running database operation
        println("checking donut stock")
        10
    }

    println("\nStep 2: Define another method which returns a Future")

    def buyDonuts(quantity: Int): Future[Boolean] = Future {
        println(s"buying $quantity donuts")
        true
    }

//    println("\nStep 3: Chaining Futures using flatMap")
//    val buyingDonuts: Future[Boolean] = donutStock("plain donut").flatMap(qty => buyDonuts(qty))
//    import scala.concurrent.Await
//    import scala.concurrent.duration.DurationInt
//    val isSuccess = Await.result(buyingDonuts, 5 seconds)
//    println(s"Buying vanilla donut was successful = $isSuccess")

//
//    println("\nStep 3: Chaining Futures using for comprehension")
//    for {
//        stock     <- donutStock("vanilla donut")
//        isSuccess <- buyDonuts(stock)
//    } yield println(s"Buying vanilla donut was successful = $isSuccess")

    // flatMap
//    val buyingDonuts: Future[Boolean] = donutStock("plain donut").flatMap(qty => buyDonuts(qty))

    // map
//    val buyingDonuts: Future[Future[Boolean]] = donutStock("plain donut").map(qty => buyDonuts(qty))



    Thread.sleep(3000)
}