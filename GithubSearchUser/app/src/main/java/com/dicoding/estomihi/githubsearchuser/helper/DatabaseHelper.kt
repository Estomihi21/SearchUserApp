package com.dicoding.estomihi.githubsearchuser.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion.LOGIN
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion._ID

internal class DatabaseHelper(context: Context)
    :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
   companion object{

       private const val DATABASE_NAME = "DBGithub"
       private const val DATABASE_VERSION = 1
       private const val DATABASE_TABLE = "CREATE TABLE $TABLE_NAME" +
               " ($_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
               "  $LOGIN TEXT NOT NULL UNIQUE," +
               "  $AVATAR TEXT NOT NULL)"
   }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DATABASE_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}