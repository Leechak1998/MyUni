package com.example.myuni.utils

class Uni {
    companion object{
        val uniMap = HashMap<String, String>().apply {
            this["University of Southampton"] = "@soton.ac.uk"
            this["Solent University"] = "@solent.ac.uk"
            this["Queen Mary University of London"] = "@qmul.ac.uk"
            this["University College London"] = "@ucl.ac.uk"
        }

        val uniName = listOf("All Universities",
                "University of Southampton",
                "Solent University",
                "Queen Mary University of London",
                "University College London")
    }
}