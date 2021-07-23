package com.tunan.doris.utils

import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.impl.client.CloseableHttpClient

class MyDorisSink(headers: Map[String, String],
                  dbName: String,
                  userName: String,
                  password: String,
                  tblName: String,
                  hostName: String,
                  port: Int = 18030,
                  debug: Boolean = true, showPayLoad: Boolean = false) extends Serializable {
    val CHARSET = "UTF-8"
    val BINARY_CT = "application/octet-stream"
    val CONTENT_TYPE = "text/plain"
    var TIMEOUT = 30000

    var api = s"http://${hostName}:${port}/api/${dbName}/${tblName}/_stream_load"
    var httpClient: CloseableHttpClient = _
    var response: CloseableHttpResponse = _
    var status: Boolean = true

    def invoke(value: String): Unit = {
        httpClient = PutUtil.clientGen(userName, password)
        try {
            val res = PutUtil.put(httpClient, value, api, CONTENT_TYPE, headers, debug, showPayLoad)
            status = res._1
            httpClient = res._2
            response = res._3
        } catch {
            case ex: Exception => {
                println("### invoke ERROR:")
                ex.printStackTrace()
            }
        } finally {
            try {
                httpClient.close()
                response.close()
            } catch {
                case ex: Exception => {
                    println("### http close ERROR:")
                    ex.printStackTrace()
                }
            }
        }
    }
}