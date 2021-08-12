package com.tunan.scala.future

import scala.concurrent.Promise
import scala.util.{Failure, Success, Try}

object FuturePromise extends App {

    println("Step 1: Define a method which returns a Future")
    import scala.concurrent.ExecutionContext.Implicits.global
    def donutStock(donut: String): Int = {
        if(donut == "vanilla donut") 10
        else throw new IllegalStateException("Out of stock")
    }


    println(s"\nStep 2: Define a Promise of type Int")
    val donutStockPromise = Promise[Int]()



    println("\nStep 3: Define a future from Promise")
    val donutStockFuture = donutStockPromise.future
    donutStockFuture.onComplete {
        case Success(stock) => println(s"Stock for vanilla donut = $stock")
        case Failure(e)     => println(s"Failed to find vanilla donut stock, exception = $e")
    }



    println("\nStep 4: Use Promise.success or Promise.failure to control execution of your future")
    val donut = "vanilla donut"
    if(donut == "vanilla donut") {
        donutStockPromise.success(donutStock(donut))
    } else {
        donutStockPromise.failure(Try(donutStock(donut)).failed.get)
    }



    println("\nStep 5: Completing Promise using Promise.complete() method")
    val donutStockPromise2 = Promise[Int]()
    val donutStockFuture2 = donutStockPromise2.future
    donutStockFuture2.onComplete {
        case Success(stock) => println(s"Stock for vanilla donut = $stock")
        case Failure(e)     => println(s"Failed to find vanilla donut stock, exception = $e")
    }
    donutStockPromise2.complete(Try(donutStock("unknown donut")))
}
