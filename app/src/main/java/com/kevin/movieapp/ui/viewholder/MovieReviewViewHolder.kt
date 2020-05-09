package com.kevin.movieapp.ui.viewholder

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.kevin.movieapp.R
import com.kevin.movieapp.model.movie_review.MovieReview

class MovieReviewViewHolder(view: View) : RecyclerView.ViewHolder(view), MovieReviewInterface{

    private val title = view.findViewById<AppCompatTextView>(R.id.tv_username)
    private val userComment = view.findViewById<AppCompatTextView>(R.id.tv_user_review)

   override fun bindView(review: MovieReview?, context: Context?) {
        if(review!=null){
            if(review.author!= null)
                title.text = review.author
            else
                title.text ="-"

            if(review.content!= null)
                userComment.text = review.content
            else
                userComment.text ="-"
        }
    }
}

interface MovieReviewInterface{
    fun bindView(review : MovieReview?, context : Context?)
}