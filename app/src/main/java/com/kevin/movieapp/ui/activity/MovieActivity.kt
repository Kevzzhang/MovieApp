package com.kevin.movieapp.ui.activity

import EndlessScrollRV
import OnLoadMoreListener
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.kevin.movieapp.R
import com.kevin.movieapp.model.movie_genre.MovieGenre
import com.kevin.movieapp.repo.MovieDiscoverRepo
import com.kevin.movieapp.ui.BaseActivity
import com.kevin.movieapp.ui.adapter.MovieAdapter
import com.kevin.movieapp.ui.fragment.BottomSheetOnClickListener
import com.kevin.movieapp.ui.fragment.GenreBottomSheetFragment
import com.kevin.movieapp.viewmodel.MovieDiscoverViewModel
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.reusable_error_layout.*


class MovieActivity : BaseActivity() {

    companion object {
        val TAG = MovieActivity::class.java.simpleName
    }

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter(this)
    }

    private lateinit var viewModel: MovieDiscoverViewModel
    private lateinit var rvScrollListener: EndlessScrollRV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        initViewModel()
        initUI()
        setupRecyclerview()
        configureViewModel()
        initCallback()
    }

    private fun initUI() {
        viewModel.initCheckMovieLocalData()
        viewModel.fetchMovieGenres()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MovieDiscoverViewModel::class.java)
        viewModel.setupRepo(MovieDiscoverRepo())
    }

    private fun showNoMoreData() {
        showSuccessSnackbar(this, parent_layout, getString(R.string.no_more_data))
    }

    private fun configureViewModel() {
        viewModel.movieDiscoverLiveData.observe(this, Observer { movies ->
            if (movies != null && movies.isNotEmpty()) {
                removeRVLoading()
                movieAdapter.addItems(movies)
            } else {
                showNoMoreData()
            }
        })

        viewModel.movieDiscoverUpdateLiveData.observe(this, Observer { movies ->
            if (movies != null && movies.isNotEmpty()) {
                removeRVLoading()
                movieAdapter.addItem(movies)
            } else {
                showNoMoreData()
            }
        })

        viewModel.errorResp.observe(this, Observer { message ->
            if (message != null) {
                if (message is String) {
                    error_message.text = message
                    setErrorMessageToView(message)
                } else if (message is Int) {
                    val errorMessage = getString(message)
                    error_message.text = errorMessage
                    setErrorMessageToView(errorMessage)
                }
            }
        })

        viewModel.showLoading.observe(this, Observer { show ->
            show?.let {
                if (it)
                    viewflipper.displayedChild = 1
                else
                    viewflipper.displayedChild = 0
            }
        })

        viewModel.showErrorLayout.observe(this, Observer { show ->
            show?.run {
                movieAdapter.removeLoadingView()
                if (this)
                    viewflipper.displayedChild = 2
                else {
                    viewflipper.displayedChild = 0
                }
            }
        })
    }

    private fun removeRVLoading() {
        movieAdapter.removeLoadingView()
        rvScrollListener.setLoaded()
    }

    private fun setErrorMessageToView(error: String?) {
        removeRVLoading()
        if (error != null) {
            error_message.text = error
            showErrorSnackbar(this, parent_layout, error)
        } else {
            error_message.text = getString(R.string.error_api_message)
            showErrorSnackbar(this, parent_layout, getString(R.string.error_api_message))
        }
    }

    private fun initCallback() {
        rvScrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.getIsHasMoreData()) {
                    rvScrollListener.setLoaded()
                    movieAdapter.addLoadingView()
                    viewModel.fetchMovieDiscover()
                }
            }
        })
        rv_movie.addOnScrollListener(rvScrollListener)

        genre_picker.setOnClickListener {
            showBottomSheetGenre()
        }

        btn_retry.setOnClickListener {
            if (!viewModel.getIsMovieDiscoverSuccess())
                viewModel.fetchMovieDiscover()
            else if (!viewModel.getIsMovieGenreSuccess())
                viewModel.fetchMovieGenres()
        }

    }


    private fun setupRecyclerview() {
        val rvManager = GridLayoutManager(this, 2)
        rv_movie.apply {
            setHasFixedSize(true)
            layoutManager = rvManager
            adapter = movieAdapter
        }

        rvScrollListener = EndlessScrollRV(rvManager)
        rvManager.spanSizeLookup = (object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (movieAdapter.getItemViewType(position)) {
                    MovieAdapter.VIEW_TYPE_ITEM -> return 1
                    MovieAdapter.VIEW_TYPE_LOADING -> return 2
                    else -> return -1
                }
            }

        })
    }

    private fun showBottomSheetGenre() {
        val genres = viewModel.getGenreList() as ArrayList<MovieGenre>
        val bottomSheetGenre = GenreBottomSheetFragment.newInstance(genres)

        bottomSheetGenre.setListener(object : BottomSheetOnClickListener {
            override fun onItemSelected(data: MovieGenre?) {
                if (data != null) {
                    setMovieTitle(data.name)
                    viewModel.fetchMovieWithGenre(data.id.toString())
                }
                bottomSheetGenre.dismiss()
            }
        })

        bottomSheetGenre.show(supportFragmentManager, TAG)
    }

    private fun setMovieTitle(newMovieTitle: String?) {
        val defaultTitle = getString(R.string.movie_list)
        if (newMovieTitle.isNullOrEmpty()) {
            tv_movie_title.text = defaultTitle
        } else {
            val newTitle = "$newMovieTitle $defaultTitle"
            tv_movie_title.text = newTitle
        }
    }
}
