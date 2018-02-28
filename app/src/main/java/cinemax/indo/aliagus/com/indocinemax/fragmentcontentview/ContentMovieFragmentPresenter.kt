package cinemax.indo.aliagus.com.indocinemax.fragmentcontentview

import android.content.Context
import android.util.Log
import android.view.View
import cinemax.indo.aliagus.com.indocinemax.R
import cinemax.indo.aliagus.com.indocinemax.model.Movie
import cinemax.indo.aliagus.com.indocinemax.utils.ProviderObservables
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by ali on 26/02/18.
 */
class ContentMovieFragmentPresenter(
        val view: ContentMovieFragmentContract.View,
        var context: Context,
        private var providerObservables: ProviderObservables = ProviderObservables(context)
) : ContentMovieFragmentContract.Presenter {

    override fun loadData(urlData: String, filter: String) {
        val observable: Observable<HashMap<String, Any>> = providerObservables.getObservableMovie(urlData, filter)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HashMap<String, Any>> {

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(map: HashMap<String, Any>) {
                        view.loadDataToAdapter(
                                map["listMovie"] as MutableList<Movie>?,
                                map["listType"] as MutableList<Int>?,
                                map["message"] as String?
                        )
                    }

                    override fun onError(e: Throwable) {
                        Log.d("testin ", "masil")
                        view?.showToastFragment(e?.message)
                    }

                    override fun onComplete() {

                    }
                })
    }

    override fun saveOrRemoveMovieToFavorite(view1: View, movieCode: Int, filter: String) {
        val movie = view1.tag as Movie
        val position = view1.getTag(R.integer.key_position) as Int
        val observable = providerObservables.saveMovieToFavoriteDatabase(movie, movieCode, filter)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HashMap<String, Any>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(map: HashMap<String, Any>) {
                        if (filter == "favorite") {
                            view.refreshAdapter(
                                    map["listMovie"] as MutableList<Movie>,
                                    map["listType"] as MutableList<Int>,
                                    position
                            )
                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {}
                })

    }
}