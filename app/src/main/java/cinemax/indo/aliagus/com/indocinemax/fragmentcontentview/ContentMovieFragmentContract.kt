package cinemax.indo.aliagus.com.indocinemax.fragmentcontentview

import cinemax.indo.aliagus.com.indocinemax.model.Movie

/**
 * Created by ali on 26/02/18.
 */
interface ContentMovieFragmentContract {
    interface View {
        fun loadDataToAdapter(listMovie: MutableList<Movie>?, listType: MutableList<Int>?, message: String?)
        fun showToastFragment(message: String?)
        fun refreshAdapter(listMovie: MutableList<Movie>, listTypes: MutableList<Int>, position: Int)
    }

    interface Presenter {
        fun loadData(urlData: String, filter: String)
        fun saveOrRemoveMovieToFavorite(view: android.view.View, movieCode: Int, filter: String)
    }
}