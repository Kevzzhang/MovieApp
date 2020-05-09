import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kevin.base_application.utils.ImageLoader
import com.kevin.movieapp.R
import com.kevin.movieapp.model.movie_discover.MovieDiscover
import com.kevin.movieapp.ui.activity.MovieDetailActivity
import com.kevin.movieapp.utils.AppConstant
import com.kevin.movieapp.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view), BindMovieHolder {

    private val parent = view.findViewById<ConstraintLayout>(R.id.movie_parent)
    private val movieCover = view.findViewById<AppCompatImageView>(R.id.img_movie_cover)
    private val movieTitle = view.findViewById<AppCompatTextView>(R.id.tv_movie_title)
    private val movieReleaseDate = view.findViewById<AppCompatTextView>(R.id.tv_release_date)

    override fun bindView(movie: MovieDiscover?, context: Context?) {
        if (movie != null && context != null) {
            val url = AppConstant.BASE_IMAGE_URL + movie.cover
            ImageLoader().loadImage(context, url, movieCover, "")

            setMovieText(movie.title)
            movie.date?.let {
                movieReleaseDate.text = DateUtils.getParseFormattedDate(movie.date)
            }

            parent.setOnClickListener {
                redirectToMovieDetail(movie.id, context)
            }
        }
    }

    private fun redirectToMovieDetail(movieID: Int?, context: Context) {
        movieID?.let {
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(MovieDetailActivity.ARG_MOVIE_ID,it)
            context.startActivity(intent)
        }
    }

    private fun setMovieText(title: String?) {
        if (!title.isNullOrEmpty())
            movieTitle.text = title
        else
            movieTitle.text = "-"
    }
}

interface BindMovieHolder {
    fun bindView(movie: MovieDiscover?, context: Context?)
}