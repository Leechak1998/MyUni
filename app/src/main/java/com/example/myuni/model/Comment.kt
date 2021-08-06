package com.example.myuni.model

class Comment constructor(reviewer: String?, reviewerPic: String?, review: String?, time: String?) {

    val reviewer = reviewer
    val reviewerPic = reviewerPic
    val review = review
    val time = time

    constructor():this("", "", "", "")
}