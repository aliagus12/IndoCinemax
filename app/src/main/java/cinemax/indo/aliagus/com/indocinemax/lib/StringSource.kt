package cinemax.indo.aliagus.com.indocinemax.lib

import java.util.*

/**
 * Created by ali on 24/02/18.
 */
class StringSource {
    companion object {
        val BASE_URL = "https://api.themoviedb.org/3/"
        val BASE_GET_MOVIE = BASE_URL + "movie/"
        val API_KEY = "api_key="
        val GET_ALL_GENRES = BASE_URL + "genre/movie/list?" + API_KEY + "&language=en-In"
        val GET_NOW_PLAYING_MOVIE = BASE_URL + "movie/now_playing?" + API_KEY + "&language=en-ID&page=1&region=ID"
        val GET_POPULAR_MOVIE = BASE_URL + "discover/movie?" + API_KEY +
                "&language=en-US&region=ID&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&year=2018"
        val GET_COMING_SOON_MOVIE = BASE_URL + "movie/upcoming?" + API_KEY + "&language=en-US&page=1&region=ID%7CUS"
        val GET_TRAILLER = "/videos?$API_KEY&language=en-US"
        val GET_ALL_PLAYERS = "/credits?" + API_KEY
        val GET_DETAIL = "?" + API_KEY
        val GET_IMAGE_MOVIE = "https://image.tmdb.org/t/p/"
        val SIZE_IMAGE_ADAPTER = "w300"
        val SIZE_IMAGE_DETAIL = "w500"

        val LAST_UPDATE = "lastUpdate"
        val DURATION = "duration"

        //keyValue
        val TABLE_KEY_VALUE = "_kv"
        val COL_KEY = "_key"
        val COL_VALUE = "_value"

        val colomnKeyValue = arrayOf(TABLE_KEY_VALUE, COL_KEY, COL_VALUE)

        val CREATE_TABLE_KEY_VALUE = ("CREATE TABLE IF NOT EXISTS "
                + colomnKeyValue[0] +
                "("
                + colomnKeyValue[1]
                + " TEXT, "
                + colomnKeyValue[2] +
                " TEXT"
                + ")")
        //movie
        val TABLE_MOVIE = "_npl"
        val COL_NO = "_no"
        val COL_ID = "_id"
        val COL_TITLE = "_title"
        val COL_VOTE_AVERAGE = "_voteAverage"
        val COL_POPULARITY = "_popularity"
        val COL_OVERVIEW = "_overView"
        val COL_POSTER_PATH = "_posterPath"
        val COL_RELEASE_DATE = "_releaseDate"
        val COL_GENRES = "_genres"

        val colomnMovieNowPlaying = arrayOf(
                TABLE_MOVIE,
                COL_NO,
                COL_ID,
                COL_TITLE,
                COL_VOTE_AVERAGE,
                COL_POPULARITY,
                COL_OVERVIEW,
                COL_POSTER_PATH,
                COL_RELEASE_DATE,
                COL_GENRES
        )
        val CREATE_TABLE_MOVIE_NOW_PLAYING = ("CREATE TABLE IF NOT EXISTS "
                + colomnMovieNowPlaying[0] +
                "("
                + colomnMovieNowPlaying[1] +
                " TEXT, "
                + colomnMovieNowPlaying[2] +
                " VARCHAR(10), "
                + colomnMovieNowPlaying[3] +
                " VARCHAR(50), "
                + colomnMovieNowPlaying[4] +
                " VARCHAR(15), "
                + colomnMovieNowPlaying[5] +
                " VARCHAR(15), "
                + colomnMovieNowPlaying[6] +
                " VARCHAR(500), "
                + colomnMovieNowPlaying[7] +
                " VARCHAR(100), "
                + colomnMovieNowPlaying[8] +
                " VARCHAR(15), "
                + colomnMovieNowPlaying[9] +
                " VARCHAR(25)"
                + ")")

        //popular
        val TABLE_POPULAR = "_mpo"
        val colomnMoviePopular = arrayOf(
                TABLE_POPULAR,
                COL_NO,
                COL_ID,
                COL_TITLE,
                COL_VOTE_AVERAGE,
                COL_POPULARITY,
                COL_OVERVIEW,
                COL_POSTER_PATH,
                COL_RELEASE_DATE,
                COL_GENRES
        )
        val CREATE_TABLE_MOVIE_POPULAR = ("CREATE TABLE IF NOT EXISTS "
                + colomnMoviePopular[0] +
                "("
                + colomnMoviePopular[1] +
                " TEXT, "
                + colomnMoviePopular[2] +
                " VARCHAR(10), "
                + colomnMoviePopular[3] +
                " VARCHAR(50), "
                + colomnMoviePopular[4] +
                " VARCHAR(15), "
                + colomnMoviePopular[5] +
                " VARCHAR(15), "
                + colomnMoviePopular[6] +
                " VARCHAR(500), "
                + colomnMoviePopular[7] +
                " VARCHAR(100), "
                + colomnMoviePopular[8] +
                " VARCHAR(15), "
                + colomnMoviePopular[9] +
                " VARCHAR(25)"
                + ")")

        //comingsoon
        val TABLE_COMING_SOON = "_cms"
        val colomnMovieComingSoon = arrayOf(
                TABLE_COMING_SOON,
                COL_NO,
                COL_ID,
                COL_TITLE,
                COL_VOTE_AVERAGE,
                COL_POPULARITY,
                COL_OVERVIEW,
                COL_POSTER_PATH,
                COL_RELEASE_DATE,
                COL_GENRES
        )
        val CREATE_TABLE_MOVIE_COMING_SOON = ("CREATE TABLE IF NOT EXISTS "
                + colomnMovieComingSoon[0] +
                "("
                + colomnMovieComingSoon[1] +
                " TEXT, "
                + colomnMovieComingSoon[2] +
                " VARCHAR(10), "
                + colomnMovieComingSoon[3] +
                " VARCHAR(50), "
                + colomnMovieComingSoon[4] +
                " VARCHAR(15), "
                + colomnMovieComingSoon[5] +
                " VARCHAR(15), "
                + colomnMovieComingSoon[6] +
                " VARCHAR(500), "
                + colomnMovieComingSoon[7] +
                " VARCHAR(100), "
                + colomnMovieComingSoon[8] +
                " VARCHAR(15), "
                + colomnMovieComingSoon[9] +
                " VARCHAR(25)"
                + ")")

        //favorite
        val TABLE_FAVORITE = "_fvr"
        val colomnFavorites = arrayOf(
                TABLE_FAVORITE,
                COL_NO,
                COL_ID,
                COL_TITLE,
                COL_VOTE_AVERAGE,
                COL_POPULARITY,
                COL_OVERVIEW,
                COL_POSTER_PATH,
                COL_RELEASE_DATE,
                COL_GENRES
        )
        val CREATE_TABLE_MOVIE_FAVORITE = ("CREATE TABLE IF NOT EXISTS "
                + colomnFavorites[0] +
                "("
                + colomnFavorites[1] +
                " TEXT, "
                + colomnFavorites[2] +
                " VARCHAR(10), "
                + colomnFavorites[3] +
                " VARCHAR(50), "
                + colomnFavorites[4] +
                " VARCHAR(15), "
                + colomnFavorites[5] +
                " VARCHAR(15), "
                + colomnFavorites[6] +
                " VARCHAR(500), "
                + colomnFavorites[7] +
                " VARCHAR(100), "
                + colomnFavorites[8] +
                " VARCHAR(15), "
                + colomnFavorites[9] +
                " VARCHAR(25)"
                + ")")

        //genres
        val COL_NAME = "_name"
        val TABLE_GENRES = "_genres"
        val COL_ID_GENRE = "_id"
        val colomnGenres = arrayOf(TABLE_GENRES, COL_ID_GENRE, COL_NAME)

        val CREATE_TABLE_GENRES = ("CREATE TABLE IF NOT EXISTS "
                + colomnGenres[0] +
                "("
                + colomnGenres[1]
                + " TEXT, "
                + colomnGenres[2] +
                " TEXT"
                + ")")

        fun LIST_ALL_TABLE(): List<Array<String>> {
            val listStringArrays = ArrayList<Array<String>>()
            listStringArrays.add(colomnMovieNowPlaying)
            listStringArrays.add(colomnMoviePopular)
            listStringArrays.add(colomnMovieComingSoon)
            return listStringArrays
        }
    }
}