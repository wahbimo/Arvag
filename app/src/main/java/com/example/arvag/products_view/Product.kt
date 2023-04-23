package com.example.arvag.products_view
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("cat")
    var category: String? = null,

    @SerializedName("brands")
    var brand: String? = null,

    @SerializedName("categories")
    var categories: String? = null,

    @SerializedName("allergens")
    var allergens: String? = null,

    @SerializedName("ecoscore_grade")
    var ecoscore_grade: String? = null,

    @SerializedName("nutriscore_grade")
    var nutriscore_grade: String? = null,

    @SerializedName("ingredients")
    var ingredients: String? = null,

    @SerializedName("quantity")
    var quantity: String? = null,

    @SerializedName("packaging")
    var packaging: String? = null,

    @SerializedName("ecoscore_score")
    var ecoscore_score: String? = null,

    @SerializedName("nutriscore_score")
    var nutriscore_score: String? = null

) : Parcelable {

    companion object : Parceler<Product> {

        override fun Product.write(parcel: Parcel, flags: Int) {
            parcel.writeInt(id ?: -1)
            parcel.writeString(name)
            parcel.writeString(category)
            parcel.writeString(brand)
            parcel.writeString(categories)
            parcel.writeString(allergens)
            parcel.writeString(ecoscore_grade)
            parcel.writeString(nutriscore_grade)
            parcel.writeString(ingredients)
            parcel.writeString(quantity)
            parcel.writeString(packaging)
            parcel.writeString(ecoscore_score)
            parcel.writeString(nutriscore_score)
        }

        override fun create(parcel: Parcel): Product {
            return Product(
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()
            )
        }
    }
}
