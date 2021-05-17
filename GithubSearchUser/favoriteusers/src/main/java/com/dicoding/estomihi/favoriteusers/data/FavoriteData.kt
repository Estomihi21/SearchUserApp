package com.dicoding.estomihi.favoriteusers.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteData(
    var id:Int = 0,
    var login:String? = null,
    var avatar_url:String? = null
) : Parcelable