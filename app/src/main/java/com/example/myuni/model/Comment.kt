package com.example.myuni.model

import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.ui.fragment.PostingDetailsFragment
import org.jetbrains.anko.support.v4.alert


class Comment constructor(reviewer: String?, reviewerEmail: String?, reviewerPic: String?, reviewerNation: String?, reviewerUni: String?, review: String?, time: String?) {

    val reviewer = reviewer
    val reviewerEmail = reviewerEmail
    val reviewerPic = reviewerPic
    val reviewerNation = reviewerNation
    val reviewerUni = reviewerUni
    val review = review
    val time = time

    constructor():this("", "", "", "", "", "", "")
}