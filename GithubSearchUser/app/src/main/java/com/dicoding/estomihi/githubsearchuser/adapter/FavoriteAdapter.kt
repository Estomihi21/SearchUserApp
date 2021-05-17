package com.dicoding.estomihi.githubsearchuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dicoding.estomihi.githubsearchuser.Data.Favoritedata
import com.dicoding.estomihi.githubsearchuser.R
import kotlinx.android.synthetic.main.item_row_user.view.*

class FavoriteAdapter(private val list:(Favoritedata) -> Unit): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    var mData = ArrayList<Favoritedata>()
    set(mData){
        if(mData.size >= 0){
            this.mData.clear()
        }
        this.mData.addAll(mData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = this.mData.size

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(mData[position], list)
    }
    inner class FavoriteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(favoriteuser: Favoritedata, list: (Favoritedata) -> Unit){
            with(itemView){
                Glide.with(itemView.context)
                    .load(favoriteuser.avatar_url)
                    .apply(
                        RequestOptions()
                        .override(56,56)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH))
                    .into(item_row_avatar)
                item_row_name.text = favoriteuser.login

                setOnClickListener { list(favoriteuser) }
            }
        }
    }
}