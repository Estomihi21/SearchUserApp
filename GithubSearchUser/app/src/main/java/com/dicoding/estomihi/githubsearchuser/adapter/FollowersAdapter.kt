package com.dicoding.estomihi.githubsearchuser.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dicoding.estomihi.githubsearchuser.Data.followersdata
import com.dicoding.estomihi.githubsearchuser.R
import kotlinx.android.synthetic.main.item_row_user.view.*

class FollowersAdapter:RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {
   private val mData = ArrayList<followersdata>()

    fun setData(items: ArrayList<followersdata>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersAdapter.FollowersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return FollowersViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowersAdapter.FollowersViewHolder, position: Int) {
        holder.bind(
                mData[position]
        )
    }

    override fun getItemCount(): Int = mData.size

    inner class FollowersViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(followers:followersdata) {
            with(itemView) {
                item_row_name.text = followers.login
                Glide.with(context)
                        .load(followers.avatars)
                        .apply(RequestOptions()
                                .override(70,70)
                                .placeholder(R.drawable.ic_baseline_account_circle_24)
                                .error(R.drawable.ic_baseline_account_circle_24)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .priority(Priority.HIGH))
                        .into(item_row_avatar)
            }
        }
    }

}