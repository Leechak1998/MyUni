package com.example.myuni.utils

class Uni {
    companion object{
        val uniName = listOf("All Universities",
            "University of Southampton",
            "Solent University",
            "Queen Mary University of London",
            "University College London")

        val uniNameMap = HashMap<String, String>().apply {
            this["University of Southampton"] = "@soton.ac.uk"
            this["Solent University"] = "@solent.ac.uk"
            this["Queen Mary University of London"] = "@qmul.ac.uk"
            this["University College London"] = "@ucl.ac.uk"
        }

        val uniNews = listOf("The coronavirus situation in th UK",
            "Testing information of COVID-19",
            "Daily cases of COVID-19",
            "Healthcare information of COVID-19",
            "Vaccinations information of COVID-19",
            "Deaths case of COVID-19")

        val uniNewsUrls = listOf("https://coronavirus.data.gov.uk/",
                                            "https://coronavirus.data.gov.uk/details/testing",
                                            "https://coronavirus.data.gov.uk/details/cases",
                                            "https://coronavirus.data.gov.uk/details/healthcare",
                                            "https://coronavirus.data.gov.uk/details/vaccinations",
                                            "https://coronavirus.data.gov.uk/details/deaths")

    }
}