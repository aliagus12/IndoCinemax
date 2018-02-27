package cinemax.indo.aliagus.com.indocinemax.fragmentdetail

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import cinemax.indo.aliagus.com.indocinemax.database.DatabaseManagerHelper
import cinemax.indo.aliagus.com.indocinemax.lib.StringSource
import cinemax.indo.aliagus.com.indocinemax.model.Movie
import cinemax.indo.aliagus.com.indocinemax.utils.ProviderObservables
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Created by ali on 26/02/18.
 */
class DetailFragmentPresenter(var view: DetailFragmentContract.View, var context: Context): DetailFragmentContract.Presenter{

    private lateinit var providerObservables: ProviderObservables
    private lateinit var databaseManagerHelper: DatabaseManagerHelper

    override fun loadData(movie: Movie) {
        providerObservables = ProviderObservables(context)
        databaseManagerHelper = DatabaseManagerHelper.getInstance(context)
        val listGenres = databaseManagerHelper.getListGenres(
                StringSource.colomnGenres,
                movie.genresList
        )
        val listId = databaseManagerHelper.getListId(StringSource.colomnFavorites)
        val isFavorite = listId.contains(movie.id)
        val observableListPlayers: Observable<JSONObject> = providerObservables.getObservablesListPlayers(movie.id)
        observableListPlayers
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<JSONObject> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(jsonObjectData: JSONObject) {
                        var listPlayers: List<String> = ArrayList()
                        var duration = "0"
                        try {
                            listPlayers = jsonObjectData.get("listPlayers") as List<String>
                            duration = jsonObjectData.getString("duration")
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        view.loadDataToView(listGenres, listPlayers, duration, isFavorite)
                    }

                    override fun onError(e: Throwable) {
                        view.loadDataToView(listGenres, null, "0", isFavorite)
                    }

                    override fun onComplete() {

                    }
                })
    }

    override fun getDataTrailler(id: String) {
        providerObservables = ProviderObservables(context)
        val observableTrailer: Observable<List<String>> = providerObservables.getObservableTrailer(id)
        observableTrailer.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<String>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(keyList: List<String>) {
                        if (keyList.isNotEmpty()) {
                            for (key in keyList) {
                                val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key))
                                val webIntent = Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://www.youtube.com/watch?v=" + key))
                                try {
                                    context.startActivity(appIntent)
                                    break
                                } catch (ex: ActivityNotFoundException) {
                                    context.startActivity(webIntent)
                                    break
                                }

                            }
                        } else {
                            onError(Throwable(""))
                        }
                    }

                    override fun onError(e: Throwable) {
                        view.showToast()
                    }

                    override fun onComplete() {

                    }
                })

    }

    override fun addOrRemoveMovie(movie: Movie, codeMovie: Int) {
        providerObservables = ProviderObservables(context)
        val observable: Observable<HashMap<String, Any>> = providerObservables.saveMovieToFavoriteDatabase(
                movie,
                codeMovie,
                ""
        )
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HashMap<String, Any>> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(map: HashMap<String, Any>) {}

                    override fun onError(e: Throwable) {}

                    override fun onComplete() {}
                })
    }
}