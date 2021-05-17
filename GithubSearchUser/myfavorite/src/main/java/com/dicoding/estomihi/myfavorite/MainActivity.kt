package com.dicoding.estomihi.myfavorite

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.estomihi.myfavorite.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter
    companion object{
        const val EXTRA_USERNAME= "extra_username"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = FavoriteAdapter()
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        rv_favorite.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler){
            override fun onChange(self:Boolean){
                loadFavoriteAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        if(savedInstanceState == null){
            loadFavoriteAsync()
        }
        else{
            val listFavo = savedInstanceState.getParcelableArrayList<Favoritedata>(EXTRA_USERNAME)
            if (listFavo != null){
                adapter.mData = listFavo
            }
        }
    }

    private fun loadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.INVISIBLE
            val favorites = deferredFavorites.await()
            if (favorites.size > 0) {
                adapter.mData = favorites
            } else {
                adapter.mData = ArrayList()
                Snackbar.make(rv_favorite, getString(R.string.empty), Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_USERNAME, adapter.mData)

    }
    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}