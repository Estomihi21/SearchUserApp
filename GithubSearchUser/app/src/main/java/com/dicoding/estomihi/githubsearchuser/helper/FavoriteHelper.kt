package com.dicoding.estomihi.githubsearchuser.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion.LOGIN
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion._ID
import java.sql.SQLException

class FavoriteHelper(context: Context) {
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null
        internal val TAG = FavoriteHelper::class.java.simpleName

        fun getInstance(context: Context): FavoriteHelper = INSTANCE ?: synchronized(this){
            INSTANCE ?: FavoriteHelper(context)
        }
    }
    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }
    fun close(){
        databaseHelper.close()
        if(database.isOpen)
            database.close()
    }
    fun isOpen(): Boolean{
        return try{
            database.isOpen
        } catch(e: Exception){
            false
        }
    }
    fun queryAll(): Cursor{
        return database.query(DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC",
            null
        )
    }
    fun queryByLogin(login: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$LOGIN = ?",
            arrayOf(login),
            null,
            null,
            null,
            null
        )
    }
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteByLogin(login: String): Int {
        Log.d(TAG,"deleteByLogin")
        return database.delete(DATABASE_TABLE, "$LOGIN = '$login'", null)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$LOGIN = ?", arrayOf(id))
    }
}