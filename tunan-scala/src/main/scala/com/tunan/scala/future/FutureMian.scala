package com.tunan.scala.future




object FutureMian {

    def main(args: Array[String]): Unit = {
        println("Step 1: Define a method which returns a Future")
        import scala.concurrent.Future
        import scala.concurrent.ExecutionContext.Implicits.global
        def donutStock(donut: String): Future[Int] = Future {
            println("checking donut stock")
            // 模拟长任务
            Thread.sleep(4000)
            10
        }

//        println("\nStep 2: Call method which returns a Future")
//        import scala.concurrent.Await
//        import scala.concurrent.duration.DurationInt
//        // 阻塞获取,等待相应的时长,超时没有获取数据就报错
//        val vanillaDonutStock = Await.result(donutStock("vanilla donut"), 5 second)
//        println(s"Stock of vanilla donut = $vanillaDonutStock")


        println("\nStep 2: Non blocking future result")
        import scala.util.{Failure, Success}
        // 非阻塞获取,future执行完了会回调,前提是主线程没退出
        donutStock("vanilla donut").onComplete {
            case Success(stock) => println(s"Stock for vanilla donut = $stock")
            case Failure(e) => println(s"Failed to find vanilla donut stock, exception = $e")
        }

        println("主线程在这")
        // 主线程不退出，其他线程的结果就能返回
        Thread.sleep(5000)
    }
}