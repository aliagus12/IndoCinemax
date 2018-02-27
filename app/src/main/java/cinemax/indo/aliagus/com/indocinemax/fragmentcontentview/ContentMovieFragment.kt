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
class ContentMovieFragment : Fragment(), ContentMovieFragmentContract.View, AdapterContentMovie.ListenerAdapterContentMovie{

    var viewRoot : View? = null
    private lateinit var urlData: String
    private lateinit var filter: String
    private var isFirstOpen: Boolean = false
    private lateinit var pDialog: SpotsDialog
    private lateinit var adapterContentMovie: AdapterContentMovie
    private var animationIn: Animation? = null
    private lateinit var detailFragment: DetailFragment
    private val TAG = ContentMovieFragment::class.java.simpleName
    private val ADD_MOVIE = 1
    private val REMOVE_MOVIE = 2
    private val contentMovieFragmentPresenter: ContentMovieFragmentPresenter by lazy {
        ContentMovieFragmentPresenter(this, context)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewRoot = inflater?.inflate(R.layout.fragment_movie_content, container, false)
        initProgressDialog()
        contentMovieFragmentPresenter.loadData(urlData, filter)
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
        adapterContentMovie.refreshPosition(position)
    }

    override fun refreshAdapter(listMovie: List<Movie>, listTypes: List<Int>) {
        checkMovieList(listMovie)
        adapterContentMovie.refresh(listMovie, listTypes)
    }

    override fun loadDataToAdapter(listMovie: List<Movie>?, listType: List<Int>?, message: String?) {
        val colomn = 2
        if (message != "") {
            showToastFragment(message)
        }
        checkMovieList(listMovie)
        adapterContentMovie = AdapterContentMovie(listMovie, listType, this)
        adapterContentMovie.setContext(context)
        adapterContentMovie.setFilter(filter)
        recycler_now_playing.setHasFixedSize(true)
        recycler_now_playing.layoutManager = GridLayoutManager(context, colomn)
        recycler_now_playing.adapter = adapterContentMovie
        animationIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
        animationIn?.duration = 1000
        recycler_now_playing.startAnimation(animationIn)
        checkProgressDialog()
    }

    private fun checkMovieList(listMovie: List<Movie>?) {
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

    override fun onImageFavoriteWhiteNotFull(view: View){
        contentMovieFragmentPresenter.saveOrRemoveMovieToFavorite(
                view,
                ADD_MOVIE,
                filter
        )
    }

    override fun onImageFavoriteWhiteFull(view: View) {
        contentMovieFragmentPresenter.saveOrRemoveMovieToFavorite(
                view,
                REMOVE_MOVIE,
                filter)
    }
}