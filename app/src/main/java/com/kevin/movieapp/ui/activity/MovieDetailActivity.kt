package com.kevin.movieapp.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kevin.base_application.utils.ImageLoader
import com.kevin.movieapp.R
import com.kevin.movieapp.model.movie_detail.MovieDetail
import com.kevin.movieapp.repo.MovieDetailRepo
import com.kevin.movieapp.ui.BaseActivity
import com.kevin.movieapp.ui.fragment.ReviewBottomSheetFragment
import com.kevin.movieapp.utils.AppConstant
import com.kevin.movieapp.utils.DateUtils
import com.kevin.movieapp.viewmodel.MovieDetailViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.reusable_error_layout.*


class MovieDetailActivity : BaseActivity() {

    companion object {
        val ARG_MOVIE_ID = "arg_movie_id"
        val TAG = MovieDetailActivity::class.java.simpleName
    }

    private var movieId = 0
    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        setupViewModel()
        movieId = intent.getIntExtra(ARG_MOVIE_ID, 0)
        viewModel.updateMovieId(movieId)
        initUI()
        configureViewModel()
        initCallback()

    }

    private fun initUI() {
        viewModel.initCheckLocalData()
        lifecycle.addObserver(youtube_player_view)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)
        viewModel.setupRepo(MovieDetailRepo())
    }

    private fun playYoutubeVideo(videoId: String) {
        youtube_player_view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0f)
            }
        })
    }

    private fun configureViewModel() {
        viewModel.movieDetailResp.observe(this, Observer { movieDetail ->
            if (movieDetail != null)
                setDataToUI(movieDetail)
        })

        viewModel.movieVideoResp.observe(this, Observer { movieVideo ->
            if (movieVideo != null) {
                playYoutubeVideo(movieVideo.url)
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
                if (this)
                    viewflipper.displayedChild = 2
                else {
                    viewflipper.displayedChild = 0
                }
            }
        })
    }

    private fun initCallback() {
        btn_show_review.setOnClickListener {}

        btn_retry.setOnClickListener {
            if (!viewModel.getIsMovieDetailSuccess())
                viewModel.fetchMovieDetail()
            else if (!viewModel.getIsMovieVideoSuccess())
                viewModel.fetchMovieVideo()
        }

        btn_show_review.setOnClickListener {
            showBottomSheetGenre()
        }
    }

    private fun setErrorMessageToView(error: String?) {
        if (error != null) {
            error_message.text = error
            showErrorSnackbar(this, parent_layout, error)
        } else {
            error_message.text = getString(R.string.error_api_message)
            showErrorSnackbar(this, parent_layout, getString(R.string.error_api_message))
        }
    }

    private fun showBottomSheetGenre() {
        val movieId = viewModel.getMovieId()

        val bottomSheetReview = ReviewBottomSheetFragment.newInstance(movieId)
        bottomSheetReview.show(supportFragmentManager, TAG)
    }

    private fun setDataToUI(data: MovieDetail) {
        if (!data.poster.isNullOrEmpty()) {
            var imageUrl = "${AppConstant.BASE_IMAGE_URL}${data.poster}"
            ImageLoader().loadImage(this, imageUrl, img_movie_cover, "")
        }

        if (!data.title.isNullOrEmpty()) tv_movie_title.text = data.title else tv_movie_title.text =
            "-"

        if (data.rating != null) {
            val ratingTxt = "${data.rating} Rated"
            tv_rating.text = ratingTxt
            ratingbar.rating = data.rating.toFloat()/2
        } else {
            tv_rating.text = getString(R.string.default_rating)
            ratingbar.rating = 10.0f
        }

        if (data.releaseDate != null) {
            val formattedDate: String = DateUtils.getParseFormattedDate(data.releaseDate)
            tv_release_date.text = "Release Date : " + formattedDate
        } else {
            tv_release_date.text = "Release Date : -"
        }

        if (!data.overview.isNullOrEmpty()) tv_summary_detail.text =
            data.overview else tv_summary_detail.text = "-"

        if (data.companies != null && data.companies.size > 0) {
            val resultStr = data.companies.joinToString { it ->
                val company = it.name
                val origin = it.country
                "{$company ( $origin )}"
            }
            tv_production_company.text = resultStr.trim().replace("{", "").replace("}", "")
        } else {
            tv_production_company.text = "-"
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        youtube_player_view.release()
    }
}
