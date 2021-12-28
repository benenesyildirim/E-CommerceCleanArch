package com.mvvm.ecommerce.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val price: String,
    val currency: String,
    val image: String,
    var amount: Int = 1,
): Parcelable