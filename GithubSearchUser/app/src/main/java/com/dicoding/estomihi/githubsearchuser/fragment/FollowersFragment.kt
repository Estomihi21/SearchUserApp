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
import com.dicoding.estomihi.githubsearchuser.model.FollowerModel
import kotlinx.android.synthetic.main.fragment_main.*

class FollowersFragment: Fragment() {
    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
    private lateinit var adapter: FollowersAdapter
    private lateinit var followersModel: FollowerModel
    private lateinit var username: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        return inflater.inflate(R.layout.fragment_main, container,false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)

        followersModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(FollowerModel::class.java)

        username = activity?.intent?.getStringExtra(EXTRA_USERNAME).toString()
        showLoading(true)
        followersModel.setFollowersUser(username)
        followersModel.getFollowersUser().observe(viewLifecycleOwner, Observer { followersItems ->
            if (followersItems != null) {
                adapter.setData(followersItems)
                showLoading(false)
            }
        })

        recyclerViewConfig()
    }

    private fun recyclerViewConfig() {
        adapter = FollowersAdapter()

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