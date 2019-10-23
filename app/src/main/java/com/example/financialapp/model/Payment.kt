package com.example.financialapp.model

import java.util.*

interface Payment {
    val id: String?
    var description: String?
    var price: Double
    val date: Date
    var category: String?
    var image: String?
}


