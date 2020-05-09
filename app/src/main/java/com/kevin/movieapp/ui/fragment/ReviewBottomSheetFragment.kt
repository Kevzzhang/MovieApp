package com.kevin.movieapp.ui.fragment

import EndlessScrollRV
import OnLoadMoreListener
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.kevin.movieapp.R
import com.kevin.movieapp.repo.MovieReviewRepo
import com.kevin.movieapp.ui.adapter.ReviewAdapter
import com.kevin.movieapp.viewmodel.MovieReviewViewModel
import kotlinx.android.synthetic.main.bottom_sheet_review.*
import kotlinx.android.synthetic.main.reusable_error_layout.*


class ReviewBottomSheetFragment : BottomSheetDialogFragment() {

    private var reviewAdapter: ReviewAdapter? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var rvScrollListener: EndlessScrollRV
    private lateinit var viewModel: MovieReviewViewModel
    private var movieId :Int?= null

    companion object {
        val ARG_ID="arg_id"

        fun newInstance(id : Int): ReviewBottomSheetFragment {
            val bundle = Bundle()
            bundle.putInt(ARG_ID,id)
            val fragment = ReviewBottomSheetFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_review, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = BottomSheetDialog(activity!!)
        dialog.setContentView(R.layout.bottom_sheet_genre)
        dialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?

            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)


        }
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieId = arguments?.getInt(ARG_ID,0)

        setupViewModel()
        initUI()
        configureViewModel()
        setupRecyclerview()
        setupCallback()

    }

    private fun initUI() {
        viewModel.updateMovieId(movieId)
        viewModel.initCheckReviewsLocalData()

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(MovieReviewViewModel::class.java)
        viewModel.setupRepo(MovieReviewRepo())
    }

    private fun configureViewModel() {
        viewModel.movieReviewLiveData.observe(viewLifecycleOwner, Observer { reviews ->
            if (reviews != null && reviews.isNotEmpty()) {
                removeRVLoading()
                reviewAdapter?.addItems(reviews)
            } else {
                showSuccessSnackbar(context!!, review_sheet_parent, getString(R.string.no_more_data))
            }
        })

        viewModel.movieReviewUpdateLiveData.observe(viewLifecycleOwner, Observer { reviews ->
            removeRVLoading()
            if (reviews != null && reviews.isNotEmpty()) {
                reviewAdapter?.addItem(reviews)
            } else {
                showSuccessSnackbar(context!!, review_sheet_parent, getString(R.string.no_more_data))
            }
        })

        viewModel.errorResp.observe(viewLifecycleOwner, Observer { message ->
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
            removeRVLoading()
        })

        viewModel.showLoading.observe(viewLifecycleOwner, Observer { show ->
            show?.let {
                if (it)
                    viewflipper.displayedChild = 1
                else
                    viewflipper.displayedChild = 0
            }
        })

        viewModel.showErrorLayout.observe(viewLifecycleOwner, Observer { show ->
            show?.run {
                reviewAdapter?.removeLoadingView()
                if (this)
                    viewflipper.displayedChild = 2
                else {
                    viewflipper.displayedChild = 0
                }
            }
        })

        viewModel.showEmptyReviewPage.observe(viewLifecycleOwner, Observer { show->
            show?.run{
                if(this) viewflipper.displayedChild = 3 else viewflipper.displayedChild = 0
            }
        })
    }

    private fun setErrorMessageToView(error: String?) {
        removeRVLoading()
        if (error != null) {
            error_message.text = error
            showErrorSnackbar(context!!, review_sheet_parent, error)
        } else {
            error_message.text = getString(R.string.error_api_message)
            showErrorSnackbar(context!!, review_sheet_parent, getString(R.string.error_api_message))
        }
    }


    private fun setupCallback() {
        rvScrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.getIsHasMoreData()) {
                    reviewAdapter?.addLoadingView()
                    viewModel.fetchMovieReview()
                }
            }
        })
        rv_reviews.addOnScrollListener(rvScrollListener)
    }


    private fun setupRecyclerview() {
        val rvManager = LinearLayoutManager(context)
        reviewAdapter = ReviewAdapter(context)
        rv_reviews.apply {
            setHasFixedSize(true)
            layoutManager = rvManager
            adapter = reviewAdapter
        }
        rvScrollListener = EndlessScrollRV(rvManager)
    }

    private fun removeRVLoading() {
        reviewAdapter?.removeLoadingView()
        rvScrollListener.setLoaded()
    }

    private fun showErrorSnackbar(context: Context, view: View, message: String) {
        val mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val v = mSnackbar.view
        v.setBackgroundColor(ContextCompat.getColor(context, R.color.bsp_red))
        mSnackbar.show()
    }

    private fun showSuccessSnackbar(context: Context, view: View, message: String) {
        val mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val v = mSnackbar.view
        v.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        mSnackbar.show()
    }
}