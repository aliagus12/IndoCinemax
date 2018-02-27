package cinemax.indo.aliagus.com.indocinemax.fragmentdetail

import cinemax.indo.aliagus.com.indocinemax.model.Movie

/**
 * Created by ali on 26/02/18.
 */
interface DetailFragmentContract {
    interface View {
        fun loadDataToView(listGenres: List<String>, listPlayers: List<String>?, duration: String, favorite: Boolean)
        fun showToast()
    }

    interface Presenter {
        fun loadData(movie: Movie)
        fun getDataTrailler(id: String)
        fun addOrRemoveMovie(movie: Movie, codeMovie: Int)
    }
}