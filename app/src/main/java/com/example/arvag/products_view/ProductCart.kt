package com.example.arvag.products_view

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "product_table")
class ProductCart {
 @PrimaryKey(autoGenerate = true)
 var id = 0
 var name: String? = null
 var category: String? = null
 var brand: String? = null
 var categories: String? = null
 var allergens: String? = null
 var totalItemPrice = 0.0
 var ecoscore_grade: String? = null
 var nutriscore_grade: String? = null
 var ingredients: String? = null
 var quantity: String? = null
 var packaging: String? = null
 var ecoscore_score: String? = null
 var nutriscore_score: String? = null
 //var photos: Int? = null

}