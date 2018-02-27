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
        private var movieList: List<Movie>?,
        private var listTypes: List<Int>?,
        private val mListener: ListenerAdapterContentMovie
) : RecyclerView.Adapter<AdapterContentMovie.ViewHolder>(), View.OnClickListener {
    private var view: View? = null
    private var context: Context? = null
    private var filter: String? = null
    private var viewHolderContent: ViewHolderContent? = null

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

    fun refresh(listMovie: List<Movie>, listTypes: List<Int>) {
        this.movieList = listMovie
        this.listTypes = listTypes
        notifyDataSetChanged()
    }

    fun refreshPosition(position: Int?) {
        notifyItemChanged(position!!)
    }

    fun setFilter(filter: String) {
        this.filter = filter
    }

    fun removeMovie(position: Int) {
        listTypes!!.drop(position)
        movieList!!.drop(position)
        notifyItemRemoved(position)
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderContent(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        private val mImageFavoriteWhiteNotFull: ImageView
        private val mImageFavoriteWhiteFull: ImageView
        private val mImageContent: ImageView
        private val mTitleContent: TextView

        init {
            mImageContent = itemView.findViewById<View>(R.id.image_content) as ImageView
            mTitleContent = itemView.findViewById<View>(R.id.title_content) as TextView
            mImageFavoriteWhiteNotFull = itemView.findViewById<View>(R.id.image_favorite_white_not_full) as ImageView
            mImageFavoriteWhiteFull = itemView.findViewById<View>(R.id.image_favorite_white_full) as ImageView
        }

        fun bind(movie: Movie) {
            val databaseManagerHelper = context?.let {
                DatabaseManagerHelper.getInstance(it)
            }
            val listId = databaseManagerHelper?.getListId(StringSource.colomnFavorites)
            if (listId!!.contains(movie.id)) {
                mImageFavoriteWhiteNotFull.visibility = View.GONE
                mImageFavoriteWhiteFull.visibility = View.VISIBLE
            } else {
                mImageFavoriteWhiteFull.visibility = View.GONE
                mImageFavoriteWhiteNotFull.visibility = View.VISIBLE
            }
            val image = movie.posterPath
            val urlImage = StringSource.GET_IMAGE_MOVIE + StringSource.SIZE_IMAGE_ADAPTER + image
            val title = movie.title
            mImageFavoriteWhiteNotFull.tag = movie
            mImageFavoriteWhiteNotFull.setTag(R.integer.key_position, adapterPosition)
            mImageFavoriteWhiteFull.tag = movie
            mImageFavoriteWhiteFull.setTag(R.integer.key_position, adapterPosition)
            mImageFavoriteWhiteNotFull.setOnClickListener(this)
            mImageFavoriteWhiteFull.setOnClickListener(this)
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
                R.id.image_favorite_white_not_full -> if (filter != "favorite") {
                    Animated.animatedView(mImageFavoriteWhiteFull, VISIBLE)
                    Animated.animatedView(view, GONE)
                    mListener.onImageFavoriteWhiteNotFull(view)
                }

                R.id.image_favorite_white_full -> if (filter == "favorite") {
                    Animated.animatedView(view, mListener)
                    removeMovie(adapterPosition)
                } else {
                    Animated.animatedView(mImageFavoriteWhiteNotFull, VISIBLE)
                    Animated.animatedView(view, GONE)
                    mListener.onImageFavoriteWhiteFull(view)
                }
            }
        }
    }

    inner class ViewHolderLoading(itemView: View) : ViewHolder(itemView)

    interface ListenerAdapterContentMovie {
        fun onHolderClick(view: View)

        fun onImageFavoriteWhiteNotFull(view: View)

        fun onImageFavoriteWhiteFull(view: View)
    }

    companion object {
        private val TAG = AdapterContentMovie::class.java.simpleName
        const val MOVIE_CONTENT = 1
        const val LOADING_CONTENT = 2
        const val VISIBLE = 1
        const val GONE = 2
    }
}