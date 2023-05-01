package com.example.arvag.products_view
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "product_table")
data class Product(

    @SerializedName("id")
    @PrimaryKey val id: Int? = null,

    @SerializedName("name")
    @ColumnInfo(name = "name") val name: String? = null,

    @SerializedName("cat")
    @ColumnInfo(name = "cat") val category: String? = null,

    @SerializedName("brands")
    @ColumnInfo(name = "brand") val brand: String? = null,

    @SerializedName("categories")
    @ColumnInfo(name = "categories") val categories: String? = null,

    @SerializedName("allergens")
    @ColumnInfo(name = "allergens") val allergens: String? = null,

    @SerializedName("ecoscore_grade")
    @ColumnInfo(name = "ecoscore_grade") val ecoscore_grade: String? = null,

    @SerializedName("nutriscore_grade")
    @ColumnInfo(name = "nutriscore_grade") val nutriscore_grade: String? = null,

    @SerializedName("ingredients")
    @ColumnInfo(name = "ingredients") val ingredients: String? = null,

    @SerializedName("quantity")
    @ColumnInfo(name = "quantity") val quantity: String? = null,

    @SerializedName("packaging")
    @ColumnInfo(name = "packaging") val packaging: String? = null,

    @SerializedName("ecoscore_score")
    @ColumnInfo(name = "ecoscore_score") val ecoscore_score: String? = null,

    @SerializedName("nutriscore_score")
    @ColumnInfo(name = "nutriscore_score") val nutriscore_score: String? = null

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
