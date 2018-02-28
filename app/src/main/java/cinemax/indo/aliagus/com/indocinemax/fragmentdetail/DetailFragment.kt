package cinemax.indo.aliagus.com.indocinemax.fragmentdetail

import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.util.Log
import android.view.View
import android.widget.*
import cinemax.indo.aliagus.com.indocinemax.R
import cinemax.indo.aliagus.com.indocinemax.lib.StringSource
import cinemax.indo.aliagus.com.indocinemax.maincontent.MainContentActivity
import cinemax.indo.aliagus.com.indocinemax.model.Movie
import cinemax.indo.aliagus.com.indocinemax.utils.Animated
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

/**
 * Created by ali on 26/02/18.
 */
class DetailFragment : BottomSheetDialogFragment(), DetailFragmentContract.View, View.OnClickListener {

    private val detailFragmentPresenter: DetailFragmentPresenter by lazy {
        DetailFragmentPresenter(this, context)
    }

    companion object {
        private val TAG = DetailFragment::class.java.simpleName
        const val VISIBLE = 1
        const val GONE = 2
        private const val ADD_MOVIE = 1
        private const val REMOVE_MOVIE = 2
    }

    private lateinit var viewRoot: View
    private var isFirstTime = true
    private lateinit var toast: Toast
    private lateinit var textViewPlayers: TextView
    private lateinit var imageViewFavoriteDetailFull: ImageView
    private lateinit var imageViewFavoriteDetailNotFull: ImageView
    private lateinit var imageContentDetail: ImageView
    private lateinit var textViewDurationDetail: TextView
    private lateinit var textViewTitleContentDetail: TextView
    private lateinit var textViewGenreContentDetail: TextView
    private lateinit var synopsisDetail: TextView
    private lateinit var dateRelease: TextView
    private lateinit var rateMovie: TextView
    lateinit var movie: Movie
    private lateinit var players: String
    private lateinit var progressBarImageLoading: ProgressBar
    private var position: Int = 0
    private lateinit var viewFrom: View

    override fun setupDialog(dialog: Dialog, style: Int) {
        viewRoot = View.inflate(context, R.layout.fragment_detail_layout, null)
        textViewPlayers = viewRoot.findViewById(R.id.text_players_detail)
        imageContentDetail = viewRoot.findViewById(R.id.image_content_detail)
        imageViewFavoriteDetailFull = viewRoot.findViewById(R.id.image_favorite_detail_full)
        imageViewFavoriteDetailNotFull = viewRoot.findViewById(R.id.image_favorite_detail_not_full)
        textViewDurationDetail = viewRoot.findViewById(R.id.duration_content_detail)
        textViewTitleContentDetail = viewRoot.findViewById(R.id.title_content_detail)
        textViewGenreContentDetail = viewRoot.findViewById(R.id.genre_content_detail)
        synopsisDetail = viewRoot.findViewById(R.id.text_synopsis_detail)
        dateRelease = viewRoot.findViewById(R.id.date_content_detail)
        rateMovie = viewRoot.findViewById(R.id.rate_content_rate)
        progressBarImageLoading = viewRoot.findViewById(R.id.progress_image_loading)
        viewRoot?.findViewById<ImageView>(R.id.image_trailler).setOnClickListener(this)
        viewRoot?.findViewById<ImageView>(R.id.image_favorite_detail_not_full).setOnClickListener(this)
        viewRoot?.findViewById<ImageView>(R.id.image_favorite_detail_full).setOnClickListener(this)
        dialog.setContentView(viewRoot!!)
        Log.d(TAG, "created")
        initProgressDialog()
        setLayout()
        detailFragmentPresenter!!.loadData(movie)
    }

    private fun setLayout() {
        var relativeLayoutContainerDetail: RelativeLayout = viewRoot!!.findViewById(R.id.container_detail)
        val layoutParams = (viewRoot!!.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.setMargins(0, 0, 0, 0)
        val behavior = layoutParams.behavior!!
        (behavior as BottomSheetBehavior<*>).setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //nothing
            }
        })
        relativeLayoutContainerDetail!!.post {
            val heightCoordinatorLayoutContainer = relativeLayoutContainerDetail!!
                    .height
            behavior.peekHeight = heightCoordinatorLayoutContainer
        }
    }

    override fun loadDataToView(
            listGenres: List<String>,
            listPlayers: List<String>?,
            duration: String,
            isFavorite: Boolean
    ) {
        if (listPlayers != null) {
            players = listPlayers.toString()
            players = players!!.replace("\\[".toRegex(), " ")
            players = players!!.replace("]".toRegex(), " ")
            textViewPlayers!!.text = players
        }
        if (isFavorite) {
            imageViewFavoriteDetailFull!!.visibility = View.VISIBLE
            imageViewFavoriteDetailNotFull!!.visibility = View.GONE
        } else {
            imageViewFavoriteDetailFull!!.visibility = View.GONE
            imageViewFavoriteDetailNotFull!!.visibility = View.VISIBLE
        }
        textViewTitleContentDetail!!.text = movie!!.title
        textViewDurationDetail!!.text = " $duration min"
        synopsisDetail!!.text = movie!!.overView
        dateRelease!!.text = movie!!.releaseDate
        var genres = listGenres.toString()
        genres = genres.replace("\\[".toRegex(), " ")
        genres = genres.replace("]".toRegex(), " ")
        textViewGenreContentDetail!!.text = genres
        rateMovie!!.text = movie!!.voteAverage + "/10"
        setImageDetail(movie!!.posterPath)
    }

    private fun setImageDetail(image: String) {
        val urlImage = StringSource.GET_IMAGE_MOVIE + StringSource.SIZE_IMAGE_DETAIL + image
        imageContentDetail!!.scaleType = ImageView.ScaleType.FIT_XY

        val glideUrlPhotoProfile = GlideUrl(
                urlImage,
                LazyHeaders.Builder()
                        .build()
        )
        Glide.with(context)
                .load<GlideUrl>(glideUrlPhotoProfile)
                .error(R.drawable.ic_broken_image_gray_24dp)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageContentDetail!!)
        progressBarImageLoading!!.visibility = View.GONE
        checkProgressDialog()
    }

    fun setMovie(view: View) {
        this.viewFrom = view
        this.movie = view.tag as Movie
        this.position = view.getTag(R.integer.key_position) as Int
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.image_trailler -> {
                detailFragmentPresenter!!.getDataTrailler(movie!!.id)
                dismissAllowingStateLoss()
            }
            R.id.image_favorite_detail_not_full -> {
                Animated.animatedView(view, GONE)
                Animated.animatedView(imageViewFavoriteDetailFull, VISIBLE)
                detailFragmentPresenter!!.addOrRemoveMovie(movie, ADD_MOVIE)
            }
            R.id.image_favorite_detail_full -> {
                Animated.animatedView(view, GONE)
                Animated.animatedView(imageViewFavoriteDetailNotFull, VISIBLE)
                detailFragmentPresenter!!.addOrRemoveMovie(movie, REMOVE_MOVIE)
            }
        }
    }

    override fun showToast() {
        toast = Toast.makeText(context, R.string.error_trailler, Toast.LENGTH_SHORT)
        toast!!.show()
    }

    override fun onResume() {
        super.onResume()
        if (isFirstTime) progressDialog!!.show()
    }

    private lateinit var progressDialog: ProgressDialog

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)
    }

    override fun onPause() {
        super.onPause()
        if (isAdded) {
            isFirstTime = false
        }
    }

    private fun checkProgressDialog() {
        if (progressDialog?.isShowing) {
            progressDialog.dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        (activity as MainContentActivity).notifyPosition(position)
    }

}