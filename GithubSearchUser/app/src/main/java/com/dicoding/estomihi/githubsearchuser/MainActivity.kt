package com.dicoding.estomihi.githubsearchuser

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.estomihi.githubsearchuser.adapter.ListAdapter
import com.dicoding.estomihi.githubsearchuser.model.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ListAdapter
    private lateinit var viewModel: UserViewModel


    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_AVATAR = "extra_avatar"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setData()
        recyclerViewOptions()
        initToolbar()
    }
    private fun setData(){
        viewModel = ViewModelProvider(this
        ,ViewModelProvider.NewInstanceFactory())
            .get(UserViewModel::class.java)

        btnSearch.setOnClickListener{
            val username = edtSearch.text.toString()
            if(username.isEmpty()) return@setOnClickListener
            showLoading(true)
            viewModel.setUser(username)
            it.hideKeyboard()
        }
        viewModel.getUser().observe(this, Observer { userItems ->
            if(userItems != null){
                adapter.setData(userItems)
                showLoading(false)
            }
        })
    }
    private fun recyclerViewOptions() {
        recycleView.layoutManager = LinearLayoutManager(this)
        adapter = ListAdapter {
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.apply {
                putExtra(EXTRA_USERNAME, it.login)
                putExtra(EXTRA_AVATAR, it.avatars)
            }
            startActivity(intent)
        }
        recycleView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when(item.itemId){
        R.id.FavoriteMenu->{
            val intent = Intent(this, FavoriteUserActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.settings->{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            true
        }
        else-> return super.onOptionsItemSelected(item)
    }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun View.hideKeyboard() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
    private fun initToolbar(){
        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.GithubApp)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }


}