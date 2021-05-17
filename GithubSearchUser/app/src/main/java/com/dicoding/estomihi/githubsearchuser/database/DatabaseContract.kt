package com.dicoding.estomihi.githubsearchuser.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.dicoding.estomihi.githubsearchuser"
    const val SCHEME = "content"

    internal class FavoriteColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val LOGIN = "login"
            const val AVATAR = "avatar_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}