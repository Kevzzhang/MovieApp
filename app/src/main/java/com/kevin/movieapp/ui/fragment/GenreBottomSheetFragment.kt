package com.kevin.movieapp.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kevin.movieapp.R
import com.kevin.movieapp.model.movie_genre.MovieGenre
import com.kevin.movieapp.ui.adapter.GenresAdapter
import com.kevin.movieapp.ui.adapter.OnClickListener
import kotlinx.android.synthetic.main.bottom_sheet_genre.*

interface BottomSheetOnClickListener{
    fun onItemSelected(data : MovieGenre?)
}

class GenreBottomSheetFragment : BottomSheetDialogFragment() {

    private var genreAdapter : GenresAdapter?= null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var onClickListener : BottomSheetOnClickListener?= null
    private var data : ArrayList<MovieGenre> = arrayListOf()

    companion object{
        val ARG_GENRES = "arg_genres"

        fun newInstance(genres : ArrayList<MovieGenre>):GenreBottomSheetFragment{
            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_GENRES,genres)
            val fragment = GenreBottomSheetFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_genre, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = BottomSheetDialog(activity!!)
        dialog.setContentView(R.layout.bottom_sheet_genre)
        dialog.setOnShowListener{ dialog->
            val d =dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?

            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)

        }

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerview()

        data = arguments?.getParcelableArrayList<MovieGenre>(ARG_GENRES) as ArrayList<MovieGenre>
        setData(data)
        genreAdapter?.setListener(object : OnClickListener{
            override fun onItemSelected(data: MovieGenre) {
                Toast.makeText(context,data.name,Toast.LENGTH_LONG)
                onClickListener?.onItemSelected(data)
            }

        })
    }

    private fun setData(data: List<MovieGenre>?){
        if(data!=null){
            genreAdapter?.addItems(data)
        }
    }

    fun setListener(listener: BottomSheetOnClickListener){
        onClickListener = listener
    }

    private fun setupRecyclerview() {
        val rvManager = LinearLayoutManager(context)
        genreAdapter = GenresAdapter(context)
        rv_genres.apply {
            setHasFixedSize(true)
            layoutManager = rvManager
            adapter = genreAdapter
        }
        rv_genres.isNestedScrollingEnabled = true
    }
}