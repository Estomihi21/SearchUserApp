package com.dicoding.estomihi.githubsearchuser.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.estomihi.githubsearchuser.R
import com.dicoding.estomihi.githubsearchuser.adapter.FollowersAdapter
import com.dicoding.estomihi.githubsearchuser.adapter.FollowingAdapter
import com.dicoding.estomihi.githubsearchuser.model.FollowerModel
import com.dicoding.estomihi.githubsearchuser.model.FollowingModel
import kotlinx.android.synthetic.main.fragment_main.*

class FollowingFragment: Fragment() {
    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
    private lateinit var followingModel: FollowingModel
    private lateinit var adapter: FollowingAdapter
    private lateinit var username: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        return inflater.inflate(R.layout.fragment_main, container,false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)

        followingModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(FollowingModel::class.java)

        username = activity?.intent?.getStringExtra(EXTRA_USERNAME).toString()
        showLoading(true)
        followingModel.setFollowingUser(username)
        followingModel.getFollowingUser().observe(viewLifecycleOwner, Observer { followingItems ->
            if (followingItems != null) {
                adapter.setData(followingItems)
                showLoading(false)
            }
        })

        recyclerViewConfig()
    }

    private fun recyclerViewConfig() {
        adapter = FollowingAdapter()
        recycleView_followers.layoutManager = LinearLayoutManager(context)
        recycleView_followers.adapter = adapter
        recycleView_followers.setHasFixedSize(true)
        adapter.notifyDataSetChanged()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}