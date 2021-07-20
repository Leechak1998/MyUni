package com.example.myuni.utils

import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class HttpConnection {
    fun doGet(httpUrl: String): String {
        //链接
        var connection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        var br: BufferedReader? = null
        val result = StringBuffer()
        try {
            //创建连接
            val url = URL(httpUrl)
            connection = url.openConnection() as HttpURLConnection
            //设置请求方式
            connection.requestMethod = "GET"
            //设置连接超时时间
            connection!!.readTimeout = 15000
            //开始连接
            connection.connect()
            //获取响应数据
            if (connection.responseCode == 200) {
                println("Connection success")
                //获取返回的数据
                inputStream = connection.inputStream
                if (null != inputStream) {
                    br = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
                    var temp: String? = null
                    while (null != br.readLine().also { temp = it }) {
                        result.append(temp)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (null != br) {
                try {
                    br.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            //关闭远程连接
            connection!!.disconnect()
        }
        return result.toString()
    }

    fun test(parameter: HashMap<String, String>){
        var buffer = StringBuffer()

        for(key in parameter.keys){
            buffer!!.append(key).append("=").append(parameter[key]).append("&")
        }
        buffer.deleteCharAt(buffer.length - 1)
        println("-----$buffer---")

    }

    fun doPost(httpUrl: String, parameter: HashMap<String, String>): String{
        var connection: HttpURLConnection? = null
        var dos: DataOutputStream? = null
        var br: BufferedReader? = null
        var result: String? = null
        var buffer = StringBuffer()

        try {
            //处理requestbody
            for(key in parameter.keys){
                buffer.append(key).append("=").append(parameter[key]).append("&")
            }
            buffer.deleteCharAt(buffer.length - 1)
            println("-----$buffer---")

            val data = buffer.toString().toByteArray()

            val url = URL(httpUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("Charset", "UTF-8")
            connection.setRequestProperty("connection", "Keep-Alive")
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

            connection.doOutput = true
            connection.doInput = true
            connection.useCaches = false
            connection.requestMethod = "POST"

            dos = DataOutputStream(connection.outputStream)
            dos.write(data, 0, data.size)
            dos.flush()
            dos.close()

            result = connection.responseMessage.toString()
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            if (null != br) {
                try {
                    br.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (null != dos) {
                try {
                    dos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            //关闭远程连接
            connection!!.disconnect()
        }
        return result!!
    }
}