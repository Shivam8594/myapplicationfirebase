package com.example.myapplicationfirebase.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Orders(
    val user_id: String = "",
    val items: ArrayList<CartItem> = ArrayList(),
    val address: Address = Address(),
    val title: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    var id: String = ""
) : Parcelable