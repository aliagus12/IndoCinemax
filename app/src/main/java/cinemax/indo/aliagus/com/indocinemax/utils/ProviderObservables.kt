package cinemax.indo.aliagus.com.indocinemax.utils

import android.content.Context
import cinemax.indo.aliagus.com.indocinemax.database.DatabaseManagerHelper
import cinemax.indo.aliagus.com.indocinemax.lib.ConnectionProvider
import cinemax.indo.aliagus.com.indocinemax.lib.StringSource
import cinemax.indo.aliagus.com.indocinemax.model.Movie
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

/**
 * Created by ali on 25/02/18.
 */
class ProviderObservables(private var context: Context, private val databaseManagerHelper: DatabaseManagerHelper = DatabaseManagerHelper(context)) {

    val MOVIE_CONTENT = 1
    val LOADING_CONTENT = 2


    fun getObservableGenresMovie(): Observable<String> {
        return Observable.create(ObservableOnSubscribe<String> { emitter ->
            val isConnect = ConnectionProvider.networkStatus(context)
            if (isConnect!!) {
                requestToGetAllGenres(emitter)
            } else {
                emitter.onError(Throwable("You Have No Internet Connection..."))
            }
        }).subscribeOn(Schedulers.io())
    }

    private fun requestToGetAllGenres(emitter: ObservableEmitter<String>) {
        val request = Request.Builder()
                .url(StringSource.GET_ALL_GENRES)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
        val client = OkHttpClient.Builder()
                .build()
        client.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        emitter.onError(e)
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        val respondMessage = response.body()!!.string()
                        if (response.code() == 200) {
                            try {
                                val jsonObject = JSONObject(respondMessage)
                                val jsonArray = jsonObject.getJSONArray("genres")
                                val listJsonGenres = ArrayList<JSONObject>()
                                (0 until jsonArray.length())
                                        .mapTo(listJsonGenres) {
                                            jsonArray.getJSONObject(it)
                                        }
                                val listId = databaseManagerHelper.getListIdGenres(StringSource.colomnGenres)
                                if (listId.isEmpty()) {
                                    databaseManagerHelper.bulkInsertGenresToDatabase(listJsonGenres)
                                }
                                val time = System.currentTimeMillis()
                                val currentDateString = time.toString()
                                databaseManagerHelper.insertToTbKV(
                                        StringSource.colomnKeyValue,
                                        StringSource.LAST_UPDATE,
                                        currentDateString
                                )
                                emitter.onNext("")
                                emitter.onComplete()
                            } catch (ex: Exception) {
                                emitter.onError(Throwable(""))
                            }

                        }
                    }
                })

    }

    fun getObservableMovie(urlData: String, filter: String): Observable<HashMap<String, Any>> {
        return Observable.create(ObservableOnSubscribe<HashMap<String, Any>> { emitter ->
            val isConnect = ConnectionProvider.networkStatus(context)
            var message: String? = "Please check your Internet Connection..."
            if (isConnect!! && filter != "favorite") {
                requestToGetAllMovies(emitter, urlData, filter)
            } else {
                var arrayStringColumn: Array<String>? = null
                when (filter) {
                    "now" -> arrayStringColumn = StringSource.colomnMovieNowPlaying
                    "popular" -> arrayStringColumn = StringSource.colomnMoviePopular
                    "soon" -> arrayStringColumn = StringSource.colomnMovieComingSoon
                    "favorite" -> {
                        arrayStringColumn = StringSource.colomnFavorites
                        message = ""
                    }
                }
                val listAllMovie = databaseManagerHelper.getAllMovieFromDatabase(
                        arrayStringColumn!!
                )
                val listType = ArrayList<Int>()
                for (movie in listAllMovie) {
                    listType.add(MOVIE_CONTENT)
                }
                val hashmap = HashMap<String, Any>()
                hashmap["listMovie"] = listAllMovie
                hashmap["listType"] = listType
                hashmap["message"] = message!!
                emitter.onNext(hashmap)
                emitter.onComplete()
            }
        }).subscribeOn(Schedulers.io())
    }

    private fun requestToGetAllMovies(emitter: ObservableEmitter<HashMap<String, Any>>, urlData: String, filter: String) {
        val request = Request.Builder()
                .url(urlData)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()

        val client = OkHttpClient.Builder()
                .build()
        client.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        emitter.onError(e)
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        val responseMessage = response.body()!!.string()
                        val code = response.code()
                        if (code == 200) {
                            try {
                                //todo get json response
                                val jsonObjectResult = JSONObject(responseMessage)
                                val jsonArrayMovie = jsonObjectResult.getJSONArray("results")
                                val listMovie = ArrayList<Movie>()
                                val listType = ArrayList<Int>()
                                for (a in 0 until jsonArrayMovie.length()) {
                                    val jsonObjectItem = jsonArrayMovie.getJSONObject(a)
                                    val listIdGenres = java.util.ArrayList<String>()
                                    if (jsonObjectItem.has("genre_ids")) {
                                        val jsonArrayIdGenres = jsonObjectItem.getJSONArray("genre_ids")
                                        (0 until jsonArrayIdGenres.length())
                                                .mapTo(listIdGenres) {
                                                    jsonArrayIdGenres.get(it).toString()
                                                }
                                    }
                                    val movie = Movie()
                                    movie.id = (jsonObjectItem.getInt("id").toString())
                                    movie.popularity = (jsonObjectItem.get("popularity").toString())
                                    movie.title = (jsonObjectItem.getString("title"))
                                    movie.overView = (jsonObjectItem.getString("overview"))
                                    movie.releaseDate = (jsonObjectItem.getString("release_date"))
                                    movie.posterPath = (jsonObjectItem.getString("poster_path"))
                                    movie.voteAverage = (jsonObjectItem.get("vote_average").toString())
                                    movie.genresList = (listIdGenres)
                                    listMovie.add(movie)
                                    listType.add(MOVIE_CONTENT)
                                }

                                var arrayStringColon: Array<String>? = null
                                when (filter) {
                                    "now" -> arrayStringColon = StringSource.colomnMovieNowPlaying
                                    "popular" -> arrayStringColon = StringSource.colomnMoviePopular
                                    "soon" -> arrayStringColon = StringSource.colomnMovieComingSoon
                                }

                                val listAllMovie = databaseManagerHelper.getAllMovieFromDatabase(
                                        arrayStringColon!!
                                )
                                if (listAllMovie.isEmpty()) {
                                    databaseManagerHelper.bulkInsertMovieToDatabase(listMovie, arrayStringColon)
                                } else {
                                    databaseManagerHelper.insertMovieToDatabase(
                                            arrayStringColon,
                                            listMovie
                                    )
                                }
                                val hashMap = HashMap<String, Any>()
                                hashMap["listMovie"] = listMovie
                                hashMap["listType"] = listType
                                emitter.onNext(hashMap)
                                emitter.onComplete()
                            } catch (ex: Exception) {
                                emitter.onError(Throwable(""))
                            }

                        }
                    }
                })
    }

    fun getObservablesListPlayers(id: String): Observable<JSONObject> {
        return Observable.create(ObservableOnSubscribe<JSONObject> { emitter ->
            val isConnect = ConnectionProvider.networkStatus(context)
            if (isConnect!!) {
                requestToGetAllPlayers(emitter, id)
            } else {
                emitter.onError(Throwable("You Have No Internet Connection..."))
            }
        }).subscribeOn(Schedulers.io())
    }

    private fun requestToGetAllPlayers(emitter: ObservableEmitter<JSONObject>, id: String) {
        val request = Request.Builder()
                .url(StringSource.BASE_GET_MOVIE + id + StringSource.GET_ALL_PLAYERS)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()

        val client = OkHttpClient.Builder()
                .build()
        client.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        emitter.onError(e)
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        val respondMessage = response.body()!!.string()
                        if (response.code() == 200) {
                            try {
                                val jsonObject = JSONObject(respondMessage)
                                val jsonArray = jsonObject.getJSONArray("cast")
                                val listPlayers = ArrayList<String>()
                                val jsonObjectData = JSONObject()
                                for (a in 0 until jsonArray.length()) {
                                    val jsonObjectItem = jsonArray.getJSONObject(a)
                                    var player: String
                                    player = if (jsonObjectItem.getString("character") != "") {
                                        val name = jsonObjectItem.getString("name")
                                        val character = jsonObjectItem.getString("character")
                                        name + " as " + character
                                    } else {
                                        jsonObjectItem.getString("name")
                                    }
                                    listPlayers.add(player)
                                }
                                val duration = databaseManagerHelper.getRowTbKV(
                                        StringSource.colomnKeyValue,
                                        id
                                )
                                if (duration == "") {
                                    requestToGetDuration(
                                            id,
                                            emitter,
                                            listPlayers
                                    )
                                } else {
                                    jsonObjectData.put("listPlayers", listPlayers)
                                    jsonObjectData.put("duration", duration)
                                    emitter.onNext(jsonObjectData)
                                    emitter.onComplete()
                                }
                            } catch (ex: Exception) {
                                emitter.onError(Throwable(""))
                            }

                        }
                    }
                })
    }

    fun requestToGetDuration(
            id: String,
            emitter: ObservableEmitter<JSONObject>,
            listPlayers: List<String>
    ) {
        val request = Request.Builder()
                .url(StringSource.BASE_GET_MOVIE + id + StringSource.GET_DETAIL)
                .build()

        val client = OkHttpClient.Builder()
                .build()
        client.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {

                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        val respondMessage = response.body()!!.string()
                        val jsonObjectData = JSONObject()
                        if (response.code() == 200) {
                            try {
                                val jsonObject = JSONObject(respondMessage)
                                val runtimeTemp = jsonObject.get("runtime").toString()
                                var duration = "0"
                                if (runtimeTemp != "null") {
                                    val runtime = jsonObject.getInt("runtime")
                                    duration = runtime.toString()
                                    databaseManagerHelper.insertToTbKV(
                                            StringSource.colomnKeyValue,
                                            id,
                                            duration
                                    )
                                }
                                jsonObjectData.put("listPlayers", listPlayers)
                                jsonObjectData.put("duration", duration)
                                emitter.onNext(jsonObjectData)
                                emitter.onComplete()
                            } catch (ex: Exception) {
                                emitter.onError(Throwable(""))
                            }

                        }
                    }
                })
    }

    fun getObservableTrailer(id: String): Observable<List<String>> {
        return Observable.create(ObservableOnSubscribe<List<String>> { emitter ->
            val isConnect = ConnectionProvider.networkStatus(context)
            if (isConnect!!) {
                requestToGetAllTrailer(emitter, id)
            } else {
                emitter.onError(Throwable("You Have No Internet Connection..."))
            }
        }).subscribeOn(Schedulers.io())
    }

    private fun requestToGetAllTrailer(emitter: ObservableEmitter<List<String>>, id: String) {
        val request = Request.Builder()
                .url(StringSource.BASE_GET_MOVIE + id + StringSource.GET_TRAILLER)
                .build()
        val client = OkHttpClient.Builder()
                .build()
        client.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        emitter.onError(Throwable(""))
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        if (response.code() == 200) {
                            try {
                                val responseMessage = response.body()!!.string()
                                val jsonObject = JSONObject(responseMessage)
                                val jsonArray = jsonObject.getJSONArray("results")
                                val listStringKey = ArrayList<String>()
                                (0 until jsonArray.length())
                                        .map { jsonArray.getJSONObject(it) }
                                        .mapTo(listStringKey) {
                                            it.getString("key")
                                        }
                                emitter.onNext(listStringKey)
                                emitter.onComplete()
                            } catch (ex: Exception) {
                                emitter.onError(Throwable(""))
                            }

                        }
                    }
                })
    }

    fun saveMovieToFavoriteDatabase(
            movie: Movie,
            movieCode: Int,
            filter: String
    ): Observable<HashMap<String, Any>> {
        return Observable.create(ObservableOnSubscribe<HashMap<String, Any>> { emitter ->
            val list = ArrayList<Movie>()
            list.add(movie)
            if (movieCode == 1) {
                databaseManagerHelper.insertMovieToDatabase(
                        StringSource.colomnFavorites,
                        list
                )
            } else {
                databaseManagerHelper.deleteRow(
                        movie.id,
                        StringSource.colomnFavorites
                )
            }
            if (filter == "favorite") {
                val listAllMovie = databaseManagerHelper.getAllMovieFromDatabase(
                        StringSource.colomnFavorites
                )
                val listType = ArrayList<Int>()
                for (movie in listAllMovie) {
                    listType.add(MOVIE_CONTENT)
                }
                val hasmap = HashMap<String, Any>()
                hasmap["listMovie"] = listAllMovie
                hasmap["listType"] = listType
                emitter.onNext(hasmap)
                emitter.onComplete()
            }
        }).subscribeOn(Schedulers.io())
    }
}