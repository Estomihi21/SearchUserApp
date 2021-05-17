package com.dicoding.estomihi.githubsearchuser

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.estomihi.githubsearchuser.Data.Favoritedata
import com.dicoding.estomihi.githubsearchuser.adapter.FavoriteAdapter
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.estomihi.githubsearchuser.helper.FavoriteHelper
import com.dicoding.estomihi.githubsearchuser.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter
    private lateinit var favoHelper: FavoriteHelper
    companion object{
        const val EXTRA_USERNAME= "extra_username"
        const val EXTRA_AVATAR = "extra_avatar"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_user)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler){
            override fun onChange(self:Boolean){
                loadFavoriteAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        favoHelper= FavoriteHelper.getInstance(applicationContext)
        favoHelper.open()
        if(savedInstanceState == null){
            loadFavoriteAsync()
        }
        else{
            val listFavo = savedInstanceState.getParcelableArrayList<Favoritedata>(EXTRA_USERNAME)
            if (listFavo != null){
                adapter.mData = listFavo
            }
        }
        recyclerViewOptions()
        initToolbar()
    }

    private fun initToolbar() {
        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.favorite_items)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun recyclerViewOptions() {
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)

        adapter = FavoriteAdapter{
            val intent = Intent(this@FavoriteUserActivity, DetailActivity::class.java)
            intent.apply {
                putExtra(EXTRA_USERNAME, it.login)
                putExtra(EXTRA_AVATAR, it.avatar_url)
            }
            startActivity(intent)
        }

        rv_favorite.adapter = adapter
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
            if (favorites.size >= 0) {
                adapter.mData = favorites
            } else {
                adapter.mData = ArrayList()
                Snackbar.make(rv_favorite, getString(R.string.empty), Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putParcelableArrayList(EXTRA_USERNAME, adapter.mData)
            putParcelableArrayList(EXTRA_AVATAR, adapter.mData)
        }
    }

    override fun onResume() {
        super.onResume()
        recyclerViewOptions()
        loadFavoriteAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        favoHelper.close()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}