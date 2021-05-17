package com.dicoding.estomihi.githubsearchuser

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.AUTHORITY
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.dicoding.estomihi.githubsearchuser.helper.FavoriteHelper

class FavoriteProvider:ContentProvider() {
    companion object{
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private lateinit var favoHelper: FavoriteHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVORITE_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoHelper = FavoriteHelper.getInstance(context as Context)
        favoHelper.open()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when(sUriMatcher.match(uri)){
            FAVORITE -> cursor = favoHelper.queryAll()
            FAVORITE_ID -> cursor = favoHelper.queryByLogin(uri.lastPathSegment.toString())
            else -> cursor = null
        }
        return cursor
    }


    override fun getType(uri: Uri): String? {
       return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when(FAVORITE){
            sUriMatcher.match(uri) -> favoHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return  Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val deleted: Int = when(FAVORITE_ID){
            sUriMatcher.match(uri)-> favoHelper.deleteByLogin(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val updated: Int = when(FAVORITE_ID){
            sUriMatcher.match(uri)-> favoHelper.update(uri.lastPathSegment.toString(), values)
            else->0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }
}