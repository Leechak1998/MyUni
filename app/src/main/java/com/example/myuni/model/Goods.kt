package com.example.myuni.model


class Goods constructor(name: String?, price: String?, description: String?, image: Long?) {

    val name = name
    val price = price
    val description = description
    val image = image

    constructor():this("", "", "", null)
}