package com.example.myuni.utils

class encode {
    companion object {
        fun EncodeString(string: String) : String{
            return string.replace(".", ",")
        }

        fun DecodeString(string: String) : String{
            return string.replace(",", ".")
        }
    }
}