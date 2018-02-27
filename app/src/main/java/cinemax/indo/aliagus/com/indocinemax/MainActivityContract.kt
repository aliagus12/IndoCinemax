package cinemax.indo.aliagus.com.indocinemax

/**
 * Created by ali on 24/02/18.
 */
interface MainActivityContract {
    interface View{
        fun showToastFragment(message: String?)
        fun jumpToMainContent()
    }

    interface Presenter {
        fun getAllGenresMovies()
    }
}