package com.tunan.scala.future

import java.util.concurrent.Executors
import scala.util.{Failure, Success}

object FutureThreadPoll extends App {

    println("Step 1: Define an ExecutionContext")
    val executor = Executors.newSingleThreadExecutor()
    implicit val ec = scala.concurrent.ExecutionContext.fromExecutor(executor)



    println("\nStep 2: Define a method which returns a Future")
    import scala.concurrent.Future
    def donutStock(donut: String): Future[Int] = Future {
        // assume some long running database operation
        println("checking donut stock")
        10
    }



    println("\nStep 3: Call method which returns a Future")
    val donutStockOperation = donutStock("vanilla donut")
    donutStockOperation.onComplete {
        case Success(donutStock)  => println(s"Results $donutStock")
        case Failure(e)           => println(s"Error processing future operations, error = ${e.getMessage}")
    }

    Thread.sleep(3000)
    executor.shutdownNow()
}
