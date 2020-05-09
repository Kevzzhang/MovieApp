package com.kevin.base_application.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kevin.movieapp.R

class ImageLoader {

    var mListener: ImageLoaderListener? = null

    fun setListener(listener: ImageLoaderListener) {
        this.mListener = listener
    }

    fun loadImage(context: Context, image: Any, target: ImageView, screenName: String) {
        Glide.with(context)
            .load(image)
            .fitCenter()
            .placeholder(R.drawable.movie_placeholder)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    mListener?.let {
                        it.onError(e?.message!!)
                    }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    mListener?.let {
                        it.onSuccess()
                    }
                    return false
                }

            })
            .into(target)

    }

    interface ImageLoaderListener {
        fun onError(error: String)
        fun onSuccess()
    }

}