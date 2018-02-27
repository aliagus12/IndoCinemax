package cinemax.indo.aliagus.com.indocinemax

import android.content.Context
import cinemax.indo.aliagus.com.indocinemax.utils.ProviderObservables
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by ali on 24/02/18.
 */
class MainActivityPresenter(
        val view: MainActivityContract.View,
        var context: Context
) : MainActivityContract.Presenter {

    lateinit var providerObservables: ProviderObservables

    override fun getAllGenresMovies() {
        providerObservables = ProviderObservables(context)
        var observable: Observable<String> = providerObservables.getObservableGenresMovie()
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        object : Observer<String> {
                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onNext(s: String) {

                            }

                            override fun onError(e: Throwable) {
                                view.showToastFragment(e.message)
                            }

                            override fun onComplete() {
                                view.jumpToMainContent()
                            }
                        })
    }
}