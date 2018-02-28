package cinemax.indo.aliagus.com.indocinemax.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cinemax.indo.aliagus.com.indocinemax.R
import cinemax.indo.aliagus.com.indocinemax.database.DatabaseManagerHelper
import cinemax.indo.aliagus.com.indocinemax.lib.StringSource
import cinemax.indo.aliagus.com.indocinemax.model.Movie
import cinemax.indo.aliagus.com.indocinemax.utils.Animated
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

/**
 * Created by ali on 26/02/18.
 */
class AdapterContentMovie(
        private var context: Context,
        private var movieList: MutableList<Movie>?,
        private var listTypes: MutableList<Int>?,
        private val mListener: ListenerAdapterContentMovie
) : RecyclerView.Adapter<AdapterContentMovie.ViewHolder>(), View.OnClickListener {
    private var filter: String? = null
    private var viewHolderContent: ViewHolderContent? = null

    companion object {
        private val TAG = AdapterContentMovie::class.java.simpleName
        const val MOVIE_CONTENT = 1
        const val LOADING_CONTENT = 2
    }
    val databaseManagerHelper = context?.let {
        DatabaseManagerHelper.getInstance(it)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        return when (viewType) {
            MOVIE_CONTENT -> ViewHolderContent(
                    layoutInflater.inflate(R.layout.custom_content_layout, null)
            )
            else -> ViewHolderLoading(
                    layoutInflater.inflate(R.layout.custom_loading, null)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            MOVIE_CONTENT -> {
                viewHolderContent = holder as ViewHolderContent
                val movie = movieList!![position]
                viewHolderContent!!.bind(movie)
                viewHolderContent!!.itemView.tag = movie
                viewHolderContent!!.itemView.setTag(R.integer.key_position, position)
                viewHolderContent!!.itemView.setOnClickListener(this)
            }

            LOADING_CONTENT -> {
                val viewHolderLoading = holder as ViewHolderLoading
            }
        }
    }

    override fun getItemCount(): Int {
        return movieList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return listTypes!![position]
    }

    fun setContext(context: Context) {
        this.context = context
    }

    override fun onClick(view: View) {
        mListener.onHolderClick(view)
    }

    fun refresh(
            listMovie: MutableList<Movie>,
            listTypes: MutableList<Int>,
            position: Int
    ) {
        notifyItemRemoved(position)
        this.movieList = listMovie
        this.listTypes = listTypes
    }

    fun refreshPosition(position: Int?) {
        notifyItemChanged(position!!)
    }

    fun refreshAllFavorite() {
        var movieList = databaseManagerHelper?.getAllMovieFromDatabase(StringSource.colomnFavorites) as MutableList<Movie>
        val listTypes= ArrayList<Int>()
        for (a in this.movieList!!.indices) {
            listTypes.add(MOVIE_CONTENT)
        }
        this.movieList = movieList
        this.listTypes = listTypes
        notifyDataSetChanged()
    }

    fun setFilter(filter: String) {
        this.filter = filter
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderContent(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        private val mImageFavoriteRedNotFull: ImageView = itemView.findViewById<View>(R.id.image_favorite_red_not_full) as ImageView
        private val mImageFavoriteRedFull: ImageView = itemView.findViewById<View>(R.id.image_favorite_red_full) as ImageView
        private val mImageContent: ImageView = itemView.findViewById<View>(R.id.image_content) as ImageView
        private val mTitleContent: TextView = itemView.findViewById<View>(R.id.title_content) as TextView

        fun bind(movie: Movie) {
            val listId = databaseManagerHelper?.getListId(StringSource.colomnFavorites)
            if (listId!!.contains(movie.id)) {
                mImageFavoriteRedNotFull.visibility = View.GONE
                mImageFavoriteRedFull.visibility = View.VISIBLE
            } else {
                mImageFavoriteRedFull.visibility = View.GONE
                mImageFavoriteRedNotFull.visibility = View.VISIBLE
            }
            val image = movie.posterPath
            val urlImage = StringSource.GET_IMAGE_MOVIE + StringSource.SIZE_IMAGE_ADAPTER + image
            val title = movie.title
            mImageFavoriteRedNotFull.tag = movie
            mImageFavoriteRedFull.tag = movie
            mImageFavoriteRedNotFull.setTag(R.integer.key_position, adapterPosition)
            mImageFavoriteRedFull.setTag(R.integer.key_position, adapterPosition)
            mImageFavoriteRedNotFull.setOnClickListener(this)
            mImageFavoriteRedFull.setOnClickListener(this)
            mTitleContent.text = title
            mImageContent.scaleType = ImageView.ScaleType.FIT_XY
            val glideUrlPhotoProfile = GlideUrl(
                    urlImage,
                    LazyHeaders.Builder()
                            .build()
            )
            Glide.with(context)
                    .load<GlideUrl>(glideUrlPhotoProfile)
                    .error(R.drawable.ic_broken_image_gray_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(mImageContent)
        }

        override fun onClick(view: View) {
            when (view.id) {
                R.id.image_favorite_red_not_full -> if (filter != "favorite") {
                    mImageFavoriteRedFull.visibility = View.VISIBLE
                    view.visibility = View.GONE
                    mListener.onImageFavoriteRedNotFull(view)
                }

                R.id.image_favorite_red_full -> if (filter == "favorite") {
                    mListener.onImageFavoriteRedFull(view)
                    Animated.animatedView(view, this@AdapterContentMovie)
                } else {
                    mImageFavoriteRedNotFull.visibility = View.VISIBLE
                    view.visibility = View.GONE
                    mListener.onImageFavoriteRedFull(view)
                }
            }
        }
    }

    inner class ViewHolderLoading(itemView: View) : ViewHolder(itemView)

    interface ListenerAdapterContentMovie {
        fun onHolderClick(view: View)

        fun onImageFavoriteRedNotFull(view: View)

        fun onImageFavoriteRedFull(view: View)
    }

}