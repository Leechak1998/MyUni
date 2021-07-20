package com.example.myuni.model

import android.os.AsyncTask
import java.io.Closeable
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket


class Connect {
    private var sc: Socket? = null
    private var dos: DataOutputStream? = null
    private var dis: DataInputStream? = null
    private var isConnected = false
    private var sendName: String? = "send Test"
    private var sender: String = ""

//    private var receivedName: String? = "received Test"

    fun talk() {
        println("开始连接服务器！")
        try {
            isClose()
            val sc = Socket("10.0.2.2", 8888)
            sc!!.soTimeout = 10000

            dos = DataOutputStream(sc!!.getOutputStream())
            dis = DataInputStream(sc!!.getInputStream())
            isConnected = true
            dos!!.writeUTF(sendName)
//            dos!!.writeUTF(receivedName)
        }catch (e: Exception ){
            println("连接失败...")
            isConnected = false
            closeAll(sc)
        }

//        object : Thread() {
//            override fun run() {
//                super.run()
//                var line: String = receive()
//                println("接受的信息：$line")
//            }
//        }.start()
    }

    fun send(content: String){
        if (isConnected){
            try {
                dos!!.writeUTF(content)
                dos!!.flush()
            }catch (e: Exception){
                println("发送信息失败...")
                isConnected = false
                closeAll(sc)
            }
        }
    }

    private fun isClose() {
        if (dos != null)
            dos!!.close()
        if (dis != null)
            dis!!.close()
        if (sc != null && !sc!!.isClosed)
            sc!!.close()
    }

    private fun closeAll(vararg io: Closeable?) {
        for (temp in io) {
            if (null != temp) {
                try {
                    temp.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun receive() : String {
        var content: String = ""
        try {
            content = dis!!.readUTF()
            sender = dis!!.readUTF()
            content = detectBeatData(sender,content)

        }catch (e: Exception){
            println("接受信息失败...")
            //sc = Socket("10.0.2.2", 8888)
            sc!!.sendUrgentData(0xFF)
            //isConnected = false
            //closeAll(sc)
        }
        return content
    }

    fun setName(_sendName: String){
        sendName = _sendName
//        receivedName = _receivedName
    }

    private fun detectBeatData(sender: String, content: String): String{
        if (sender.equals("Server"))
            return ""
        return content
    }


    companion object {
        //服务端地址及接口
        private const val ip = "10.0.2.2"  //本地服务端地址
        private const val port = 8888
    }

}