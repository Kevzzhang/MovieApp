package com.kevin.movieapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.kevin.movieapp.R
import com.kevin.movieapp.model.movie_genre.MovieGenre


interface OnClickListener {
    fun onItemSelected(data: MovieGenre)
}

class GenresAdapter(private val context: Context?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onClickListener: OnClickListener? = null
    private val items: MutableList<MovieGenre> = arrayListOf()

    fun setListener(listener: OnClickListener){
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_string_border_bottom, parent, false)
        return GenreViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as GenreViewHolder
        holder.bindView(items[position], context)
    }

    fun addItems(newItem: List<MovieGenre>?) {
        newItem?.run {
            clearItems()
            items.addAll(this)
            notifyItemRangeInserted(itemCount, this.size)
        }

    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class GenreViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val title = view.findViewById<AppCompatTextView>(R.id.tv_title)
        private val parentLayout = view.findViewById<LinearLayout>(R.id.linear_layout)

        fun bindView(genre: MovieGenre?, context: Context?) {
            if(genre!=null){
                if(genre.name!= null)
                    title.text = genre.name
                else
                    title.text ="-"
            }

            parentLayout.setOnClickListener {
                if(genre!=null)
                    onClickListener?.onItemSelected(genre)
            }
        }
    }
}


