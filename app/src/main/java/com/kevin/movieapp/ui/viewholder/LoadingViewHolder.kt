
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.kevin.movieapp.R

class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view), BindProgressHolder {
    private val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
    override fun bindView() {}
}

interface BindProgressHolder{
    fun bindView()
}