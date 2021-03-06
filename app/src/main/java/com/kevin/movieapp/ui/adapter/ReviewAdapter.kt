package com.kevin.movieapp.ui.adapter

import LoadingViewHolder
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevin.movieapp.R
import com.kevin.movieapp.model.movie_review.MovieReview
import com.kevin.movieapp.ui.viewholder.MovieReviewViewHolder

class ReviewAdapter(private val context: Context?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<MovieReview?> = arrayListOf()
    companion object {
        val VIEW_TYPE_ITEM = 0
        val VIEW_TYPE_LOADING = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MovieAdapter.VIEW_TYPE_ITEM) {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
            MovieReviewViewHolder(view)
        } else {
            val loadingView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(loadingView)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] == null) {
            MovieAdapter.VIEW_TYPE_LOADING
        } else {
            MovieAdapter.VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_ITEM -> {
                holder as MovieReviewViewHolder
                holder.bindView(items[position], context)
            }
        }
    }

    fun addLoadingView() {
        items.add(null)
        notifyItemInserted(items.size - 1)
    }

    fun removeLoadingView() {
        if (items.size != 0) {
            items.removeAt(items.size - 1)
            notifyItemRemoved(items.size)
        }
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    fun addItems(newItem: List<MovieReview?>?) {
        newItem?.run {
            clearItems()
            items.addAll(this)
            notifyItemRangeInserted(itemCount, this.size)
        }

    }

    fun addItem(newItem: List<MovieReview?>?) {
        newItem?.run {
            items.addAll(this)
            notifyItemRangeInserted(itemCount, this.size)
        }
    }


}


