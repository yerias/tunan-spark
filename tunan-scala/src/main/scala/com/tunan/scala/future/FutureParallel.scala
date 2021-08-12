package com.tunan.scala.future

import scala.util.{Failure, Success}

object FutureParallel extends App {

    println("Step 1: Define a method which returns a Future")

    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.Future

    def donutStock(donut: Int): Future[Int] = Future {
        println("checking donut stock "+donut)
        donut
    }


    println(s"\nStep 2: Create a List of future operations")
    val futureOperations = List(
        donutStock(10),
        donutStock(20),
        donutStock(30)
    )

    println(s"\nStep 3: Call Future.sequence to run the future operations in parallel")
    // TODO 多个Future并行执行
//    val futureSequenceResults = Future.sequence(futureOperations)

    // TODO Future.traverse()可以对要执行的Future进行操作
//    val futureTraverseResult = Future.traverse(futureOperations){ futureSomeQty =>
//        futureSomeQty.map(someQty => someQty+100)
//    }
    // TODO 返回第一个处理完成的future结果
    val futureFirstCompletedResult = Future.firstCompletedOf(futureOperations)

    futureFirstCompletedResult.onComplete {
        case Success(results) => println(s"Results $results")
        case Failure(e)       => println(s"Error processing future operations, error = ${e.getMessage}")
    }

    // 主线程等等回调
    Thread.sleep(1000)

}
