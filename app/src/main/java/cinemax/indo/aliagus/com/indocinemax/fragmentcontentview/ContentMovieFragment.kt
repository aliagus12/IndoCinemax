package cinemax.indo.aliagus.com.indocinemax.fragmentcontentview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import cinemax.indo.aliagus.com.indocinemax.R
import cinemax.indo.aliagus.com.indocinemax.adapter.AdapterContentMovie
import cinemax.indo.aliagus.com.indocinemax.fragmentdetail.DetailFragment
import cinemax.indo.aliagus.com.indocinemax.maincontent.MainContentActivity
import cinemax.indo.aliagus.com.indocinemax.model.Movie
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_movie_content.*

/**
 * Created by ali on 24/02/18.
 */
class ContentMovieFragment : Fragment(), ContentMovieFragmentContract.View, AdapterContentMovie.ListenerAdapterContentMovie {

    var viewRoot: View? = null
    private var urlData: String? = null
    private var filter: String? = null
    private var isFirstOpen: Boolean = false
    private lateinit var pDialog: SpotsDialog
    private lateinit var adapterContentMovie: AdapterContentMovie
    private var animationIn: Animation? = null
    private lateinit var detailFragment: DetailFragment
    private val TAG = ContentMovieFragment::class.java.simpleName
    private val ADD_MOVIE = 1
    private val REMOVE_MOVIE = 2
    private var animationOut: Animation? = null
    private val contentMovieFragmentPresenter: ContentMovieFragmentPresenter by lazy {
        ContentMovieFragmentPresenter(this, context)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewRoot = inflater?.inflate(R.layout.fragment_movie_content, container, false)
        initProgressDialog()
        urlData?.let { filter?.let { it1 -> contentMovieFragmentPresenter.loadData(it, it1) } }
        return viewRoot
    }

    private fun initProgressDialog() {
        pDialog = SpotsDialog(context, R.style.ProgressDialogStyle)
        pDialog.setCancelable(true)
        pDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pDialog.show()
    }

    fun setUrlData(urlData: String) {
        this.urlData = urlData
    }

    fun setFilter(filter: String) {
        this.filter = filter
    }

    fun setIsFirstOpen(isFirstOpen: Boolean) {
        this.isFirstOpen = isFirstOpen
    }

    fun refreshAdapterPosition(position: Int?) {
        if (filter == "favorite") {
            adapterContentMovie.refreshAllFavorite()
        } else {
            adapterContentMovie.refreshPosition(position)
        }
    }

    override fun refreshAdapter(
            listMovie: MutableList<Movie>,
            listTypes: MutableList<Int>,
            position: Int
    ) {
        checkMovieList(listMovie)
        adapterContentMovie.refresh(
                listMovie,
                listTypes,
                position
        )
    }

    override fun loadDataToAdapter(
            listMovie: MutableList<Movie>?,
            listType: MutableList<Int>?,
            message: String?
    ) {
        val colomn = 2
        if (message != "") {
            showToastFragment(message)
        }
        checkMovieList(listMovie)
        adapterContentMovie = AdapterContentMovie(context, listMovie, listType, this)
        filter?.let { adapterContentMovie.setFilter(it) }
        recycler_now_playing.setHasFixedSize(true)
        recycler_now_playing.layoutManager = GridLayoutManager(context, colomn)
        recycler_now_playing.adapter = adapterContentMovie
        animationIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
        animationIn?.duration = 300
        recycler_now_playing.startAnimation(animationIn)
        checkProgressDialog()
    }

    private fun checkMovieList(listMovie: MutableList<Movie>?) {
        if (listMovie!!.isEmpty()) {
            fragment_content_movie_logo.visibility = View.VISIBLE
        } else {
            fragment_content_movie_logo.visibility = View.GONE
        }
    }

    private fun checkProgressDialog() {
        if (pDialog?.isShowing) {
            pDialog.dismiss()
        }
    }

    override fun showToastFragment(message: String?) {
        if (activity != null) {
            message?.let {
                (activity as MainContentActivity).showToastFragment(it)
            }
            checkProgressDialog()
        }
    }

    override fun onHolderClick(view: View) {
        detailFragment = DetailFragment()
        detailFragment?.setMovie(view)
        detailFragment?.show(
                fragmentManager,
                detailFragment?.tag
        )
    }

    override fun onImageFavoriteRedNotFull(view: View) {
        filter?.let {
            contentMovieFragmentPresenter.saveOrRemoveMovieToFavorite(
                view,
                ADD_MOVIE,
                    it
        )
        }
    }

    override fun onImageFavoriteRedFull(view: View) {
        filter?.let {
            contentMovieFragmentPresenter.saveOrRemoveMovieToFavorite(
                view,
                REMOVE_MOVIE,
                    it)
        }
    }

    override fun onStop() {
        super.onStop()
        animationOut = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right)
        animationOut?.duration = 300
        recycler_now_playing.startAnimation(animationOut)
    }
}
