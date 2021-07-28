package com.example.myuni.utils

public class Encode {
    companion object {
        public fun EncodeString(string: String) : String{
            return string.replace(".", ",")
        }

        fun DecodeString(string: String) : String{
            return string.replace(",", ".")
        }
    }
}