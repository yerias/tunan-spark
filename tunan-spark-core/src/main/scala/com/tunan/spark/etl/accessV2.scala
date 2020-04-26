package com.tunan.spark.etl

import java.text.SimpleDateFormat
import java.util.Calendar

import com.hadoop.compression.lzo.LzopCodec
import com.tunan.spark.utils.{ContextUtils, IpParseUtil}
import com.tunan.spark.utils.hadoop.CheckHDFSOutPath
import org.apache.commons.lang3.StringUtils
import org.apache.hadoop.io.compress.SnappyCodec

/**
 * [16/02/2020:01:01:49 +0800]	165.10.167.126	-	331	-	put	https://www.bilibili.com/video/av52167219?wd=spark	200	351	7738	HIT
 * //[16/02/2020:00:51:12 +0800]
 * // 175.5.81.162
 * // -
 * // 249
 * // -
 * // get
 * // https://www.bilibili.com/video/av76542615
 * // 400
 * // 5829
 * // 7541
 * // MISS
 *
 */

object accessV2 {

    def main(args: Array[String]): Unit = {

        val sc = ContextUtils.getSparkContext(this.getClass.getSimpleName)

        sc.hadoopConfiguration.set("dfs.blocksize","67108864")

        //        val conf = new SparkConf()
        //        val sc = new SparkContext(conf)

        val in = "tunan-spark-core/ip/access.txt"
        val out = "tunan-spark-sql/extds/text_access"
        CheckHDFSOutPath.ifExistsDeletePath(sc.hadoopConfiguration,out)

        val userTextRDD = sc.textFile("tunan-spark-sql/extds/userid.txt")

        val hashMap = sc.broadcast(userTextRDD.map(x => {
            val words = x.split(",")
            (words(2), words(0))
        }).collectAsMap())


        val linesRDD = sc.textFile(in)

        val mapRDD: Unit = linesRDD.map(line => {
            val fields = line.split("\t")

            //时间
            val time = fields(0)

            val format = new SimpleDateFormat("[dd/MM/yyyy:HH:mm:ss +0800]")
            val date = format.parse(time)
            val calendar = Calendar.getInstance
            calendar.setTime(date)
            //year
            val year = String.valueOf(calendar.get(Calendar.YEAR))

            //month
            var month = calendar.get(Calendar.MONTH) + 1.toString
            if (month < 10.toString) {
                month = "0" + month
            }

            //day
            var day = calendar.get(Calendar.DAY_OF_MONTH).toString
            if (day < 10.toString) {
                day = "0" + day
            }

            //ip地址
            val ip = fields(1)
            val ipParse = IpParseUtil.IpParse(ip)
            //country
            val country: String = ipParse(0)
            //province
            val province: String = ipParse(1)
            //city
            val city: String = ipParse(2)
            //area
            val area: String = ipParse(3)

            //代理ip地址
            val proxy_ip = fields(2)

            //请求花费时间
            val requestTime = fields(3).toLong

            //上一级url
            val referer = fields(4)

            //请求方式
            val method = fields(5)

            //网址
            val url: String = fields(6)
            val uris = url.split("/")
            var http = ""
            if (StringUtils.isEmpty(uris(0))) http = "-"
            else if (uris(0).contentEquals("s")) http = uris(0).substring(0, 5)
            else http = uris(0).substring(0, 4)

            //还可以按 "://" 切分

            //domain
            var domain: String = ""
            if (StringUtils.isEmpty(uris(2))) domain = "-"
            else domain = uris(2)

            var userId = 0L
            try {
                userId  = hashMap.value(domain).toLong
            } catch {
                case e:Exception => userId = 0L
            }

            //path  ==> 空的怎么处理
            var path: String = ""
            if (uris.length == 3) path = "-"
            else {
                val urlTmp = uris(0) + "//" + uris(2)
                path = url.substring(urlTmp.length + 1, url.indexOf("?"))
            }

            //返回码
            val httpCode = fields(7)

            //请求数据量
            val requestSize = fields(8).toLong

            //返回数据量
            var responseSize = 0L
            try {
                responseSize = fields(9).toLong
            } catch {
                case ex: Exception => ""
            }

            //是否命中缓存
            val cache = fields(10)

            etl(year, month, day, country, province, city, area, proxy_ip, requestTime, referer, method, http, domain, path, httpCode, requestSize, responseSize, cache,userId)
        }).filter(x => x.responseSize != 0).saveAsTextFile(out,classOf[LzopCodec])
        //输出结果
        //        mapRDD.print()


        //城市统计
        /*val provinceRDD = mapRDD.map(x => (x.country, x.responseSize))
        val reduceByRDD = provinceRDD.reduceByKey(_ + _)
        reduceByRDD.map(x => {
            country(x._1, x._2)
        }).coalesce(1).saveAsTextFile(out)*/
    }

    case class etl(year: String, month: String, day: String, country: String, province: String, city: String, area: String, proxy_ip: String, requestTime: Long, referer: String, method: String, http: String, domain: String, path: String, httpCode: String, requestSize: Long, responseSize: Long, cache: String, userId: Long) {
        override def toString: String = s"$year, $month, $day, $country, $province, $city, $area, $proxy_ip, $requestTime, $referer, $method, $http, $domain, $path, $httpCode, $requestSize, $responseSize, $cache,$userId"
    }

    case class country(country: String, responseSize: Long) {
        override def toString: String = s"$country --> $responseSize"
    }

}
