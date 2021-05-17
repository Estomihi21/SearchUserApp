package com.dicoding.estomihi.githubsearchuser

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dicoding.estomihi.githubsearchuser.Data.Favoritedata
import com.dicoding.estomihi.githubsearchuser.adapter.SectionAdapter
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.estomihi.githubsearchuser.helper.FavoriteHelper
import com.dicoding.estomihi.githubsearchuser.helper.MappingHelper
import com.dicoding.estomihi.githubsearchuser.model.DetailModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.tab_layout.*

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailModel
    private lateinit var favoHelper: FavoriteHelper
    private lateinit var uriWithId: Uri
    private lateinit var username: String
    private lateinit var avatarurl: String
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private var favorite: Favoritedata? = null

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_AVATAR = "extra_avatar"
        internal val TAG = DetailActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + favorite?.id)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        if (cursor != null) {
            favorite = MappingHelper.mapCursorToObject(cursor)
            cursor.close()
        }
        setData()
        favoriteOptions()
        initToolbar()
        pagerAdapter()
    }

    private fun setData() {
        favoHelper = FavoriteHelper.getInstance(applicationContext)
        favoHelper.open()

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailModel::class.java)

        username = intent?.getStringExtra(EXTRA_USERNAME).toString()
        detailViewModel.setUserDetail(username)
        detailViewModel.getDetailUser().observe(this, { detailUserItems ->
            if (detailUserItems != null) {
                Glide.with(this)
                    .load(detailUserItems[0].avatars)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_baseline_account_circle_24)
                            .error(R.drawable.ic_baseline_account_circle_24)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                    )
                    .into(detail_avatar)
                detail_name.text = detailUserItems[0].name
                detail_username.text = detailUserItems[0].login
                detail_location.text = detailUserItems[0].location
                detail_company.text = detailUserItems[0].company
                detail_repository.text = detailUserItems[0].repository.toString()
                detail_followers.text = detailUserItems[0].followers.toString()
                detail_following.text = detailUserItems[0].following.toString()
            }
        })
    }

    private fun favoriteOptions() {
        username = intent?.getStringExtra(EXTRA_USERNAME).toString()
        val result = favoHelper.queryByLogin(username)
        val favorites = (1..result.count).map {
            result.apply {
                moveToNext()
                getInt(result.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOGIN))
            }
        }
        if (favorites.isNotEmpty()) isFavorite = true
    }

    private fun favoriteIcon() {
        if (isFavorite) {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(
                this,
                R.drawable.ic_baseline_favorite_red
            )
        } else {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(
                this,
                R.drawable.ic_baseline_favorite_border_24
            )
        }
    }

    private fun addFavoriteUser() {
        username = intent.getStringExtra(EXTRA_USERNAME).toString()
        avatarurl = intent.getStringExtra(EXTRA_AVATAR).toString()

        val values = ContentValues().apply {
            put(DatabaseContract.FavoriteColumns.LOGIN, username)
            put(DatabaseContract.FavoriteColumns.AVATAR, avatarurl)
        }
        contentResolver.insert(CONTENT_URI, values)
        showSnackMessage(getString(R.string.added))
        Log.d("INSERT VALUES ::::: ", values.toString())

    }

    private fun showSnackMessage(message: String) {
        Snackbar.make(viewPager, message, Snackbar.LENGTH_SHORT)
            .show()

    }

    private fun pagerAdapter() {
        val sectionsPagerAdapter = SectionAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tab_layout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu_detail, menu)
        menuItem = menu
        favoriteIcon()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                if (isFavorite) removeFavoriteUser()
                else addFavoriteUser()
                isFavorite = !isFavorite
                favoriteIcon()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun removeFavoriteUser() {
        username = intent.getStringExtra(EXTRA_USERNAME).toString()
        contentResolver.delete(uriWithId, null, null)
        showSnackMessage("User Successfully deleted from DB")
        Log.d(TAG, "Deleted:$uriWithId")

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun initToolbar() {
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.title_detail)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }


}