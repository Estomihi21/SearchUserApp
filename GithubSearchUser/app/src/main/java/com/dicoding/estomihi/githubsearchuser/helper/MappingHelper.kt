package com.dicoding.estomihi.githubsearchuser.helper

import android.database.Cursor
import com.dicoding.estomihi.githubsearchuser.Data.Favoritedata
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<Favoritedata>{
        val favoriteList = ArrayList<Favoritedata>()
        cursor?.apply {
            while(moveToNext()){
                val id        = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val login     = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOGIN))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                favoriteList.add(Favoritedata(id, login, avatarUrl))
            }
        }
        return favoriteList
    }
    fun mapCursorToObject(notesCursor: Cursor?):Favoritedata {
        var favo = Favoritedata()
        notesCursor?.apply {
            moveToFirst()
            val id     = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
            val login  = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOGIN))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
            favo = Favoritedata(id, login, avatar)
        }
        return favo
    }
}