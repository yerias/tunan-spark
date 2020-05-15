import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}

import scala.util.Random

object mokeData {

  def main(args: Array[String]): Unit = {

    val r = new Random()
    val r2 = new Random()
    val r3 = new Random()

    var productId = ""

    val ProductCategory = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("tunan-spark-sql/moke2/product_category.txt"), true)))
    val userClick = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("tunan-spark-sql/moke2/user_click.txt"), true)))

    for (i <- 1 to 30000000){
      val userId = "175"+r.nextInt(999999)
      productId = "15072"+r2.nextInt(5000)
      userClick.write(s"${userId},${productId}")
      userClick.newLine()

      val categoryId = "29535"+r3.nextInt(100)
      ProductCategory.write(s"${productId},${categoryId}")
      ProductCategory.newLine()
    }

    userClick.flush()
    ProductCategory.flush()
    userClick.close()
    ProductCategory.close()
  }
}
