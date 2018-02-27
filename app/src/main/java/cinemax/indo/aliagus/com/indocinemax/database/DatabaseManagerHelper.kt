package cinemax.indo.aliagus.com.indocinemax.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import cinemax.indo.aliagus.com.indocinemax.lib.StringSource
import cinemax.indo.aliagus.com.indocinemax.model.Movie
import org.json.JSONObject
import java.util.*

/**
 * Created by ali on 26/02/18.
 */
class DatabaseManagerHelper(private var context: Context) : DatabaseHelper(context) {
    internal var databaseManager: DatabaseManager
    private var allMovieFromDatabase: MutableList<Movie>? = null

    init {
        databaseManager = DatabaseManager(this)
    }

    companion object {

        private val TAG = DatabaseManagerHelper::class.java.simpleName
        private var instance: DatabaseManagerHelper? = null

        @Synchronized
        fun getInstance(context: Context): DatabaseManagerHelper {
            if (instance == null) {
                instance = DatabaseManagerHelper(context)
            }
            return instance as DatabaseManagerHelper
        }
    }

    fun bulkInsertMovieToDatabase(movieList: List<Movie>, arrayStringColomn: Array<String>) {
        val db = databaseManager.openDatabase(TAG)
        val query = ("INSERT OR REPLACE INTO "
                + arrayStringColomn[0] +
                "("
                + arrayStringColomn[2] +
                ", "
                + arrayStringColomn[3] +
                ", "
                + arrayStringColomn[4] +
                ", "
                + arrayStringColomn[5] +
                ", "
                + arrayStringColomn[6] +
                ", "
                + arrayStringColomn[7] +
                ", "
                + arrayStringColomn[8] +
                ", "
                + arrayStringColomn[9] +
                ") "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ")
        try {
            db.beginTransaction()
            val stmt = db.compileStatement(query)

            movieList.forEach { movie ->
                stmt.clearBindings()
                stmt.bindString(1, movie.id)
                stmt.bindString(2, movie.title)
                stmt.bindString(3, movie.voteAverage)
                stmt.bindString(4, movie.popularity)
                stmt.bindString(5, movie.overView)
                stmt.bindString(6, movie.posterPath)
                stmt.bindString(7, movie.releaseDate)
                stmt.bindString(8, movie.genresList.toString())
                stmt.execute()
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    fun getAllMovieFromDatabase(arrayColomn: Array<String>): List<Movie> {
        val sqLiteDatabase = databaseManager.openDatabase(TAG)
        val selectQuery = "SELECT * FROM " + arrayColomn[0]
        val cursor = sqLiteDatabase.rawQuery(selectQuery, null)
        allMovieFromDatabase = ArrayList()
        val listIdCurrency = getListId(arrayColomn)

        if (cursor.count > 0) {
            listIdCurrency
                    .map {
                        getMovieByKey(
                                arrayColomn,
                                it
                        )
                    }
                    .forEach { allMovieFromDatabase!!.add(it) }
        }
        return allMovieFromDatabase as ArrayList<Movie>
    }

    private fun getMovieByKey(arrayColomn: Array<String>, key: String): Movie {
        val sqLiteDatabase = databaseManager.openDatabase(TAG)
        val selectQuery = ("SELECT * FROM " + arrayColomn[0] + " WHERE "
                + arrayColomn[2] + " = '" + key + "'")
        val cursor = sqLiteDatabase.rawQuery(selectQuery, null)
        val movie = Movie()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            var genres = cursor.getString(cursor.getColumnIndex(arrayColomn[9]))
            genres = genres.replace("\\[".toRegex(), "")
            genres = genres.replace("]".toRegex(), "")
            genres = genres.trim { it <= ' ' }
            val genresArray = genres.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val listGenres = ArrayList(Arrays.asList(*genresArray))
            movie.id = (cursor.getString(cursor.getColumnIndex(arrayColomn[2])))
            movie.title = (cursor.getString(cursor.getColumnIndex(arrayColomn[3])))
            movie.voteAverage = (cursor.getString(cursor.getColumnIndex(arrayColomn[4])))
            movie.popularity = (cursor.getString(cursor.getColumnIndex(arrayColomn[5])))
            movie.overView = (cursor.getString(cursor.getColumnIndex(arrayColomn[6])))
            movie.posterPath = (cursor.getString(cursor.getColumnIndex(arrayColomn[7])))
            movie.releaseDate = (cursor.getString(cursor.getColumnIndex(arrayColomn[8])))
            movie.genresList = (listGenres)
        } else {
            movie.id = ""
            movie.title = ""
            movie.voteAverage = ""
            movie.popularity = ""
            movie.overView = ""
            movie.posterPath = ""
            movie.releaseDate = ""
            movie.genresList = ArrayList()
        }
        cursor.close()
        return movie
    }

    fun getListId(arrayColomn: Array<String>): List<String> {
        val sqLiteDatabase = databaseManager.openDatabase(TAG)
        val selectQuery = "SELECT * FROM " + arrayColomn[0]
        val cursor = sqLiteDatabase.rawQuery(selectQuery, null)
        val listIdMovie = ArrayList<String>()
        if (cursor.count > 0) {
            if (cursor.moveToFirst()) {
                do {
                    listIdMovie.add(cursor.getString(cursor.getColumnIndex(arrayColomn[2])))
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return listIdMovie
    }

    fun bulkInsertGenresToDatabase(listJsonGenres: List<JSONObject>) {
        val db = databaseManager.openDatabase(TAG)
        val query = ("INSERT OR REPLACE INTO "
                + StringSource.colomnGenres[0] +
                "("
                + StringSource.colomnGenres[1] +
                ", "
                + StringSource.colomnGenres[2] +
                ") "
                + "VALUES (?, ?) ")
        try {
            db.beginTransaction()
            val stmt = db.compileStatement(query)
            for (genres in listJsonGenres) {
                stmt.clearBindings()
                stmt.bindString(1, genres.getInt("id").toString())
                stmt.bindString(2, genres.getString("name"))
                stmt.execute()
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    fun getListIdGenres(arrayColomn: Array<String>): List<String> {
        val sqLiteDatabase = databaseManager.openDatabase(TAG)
        val selectQuery = "SELECT * FROM " + arrayColomn[0]
        val cursor = sqLiteDatabase.rawQuery(selectQuery, null)
        val listId = ArrayList<String>()
        if (cursor.count > 0) {
            if (cursor.moveToFirst()) {
                do {
                    listId.add(cursor.getString(cursor.getColumnIndex(arrayColomn[1])))
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return listId
    }

    fun getListGenres(arrayStrings: Array<String>, genresListId: List<String>): List<String> {
        val listGenres = ArrayList<String>()
        for (a in genresListId.indices) {
            val sqLiteDatabase = databaseManager.openDatabase(TAG)
            val selectQuery = ("SELECT * FROM " + arrayStrings[0] + " WHERE "
                    + arrayStrings[1] + " = '" + genresListId[a] + "'")
            val cursor = sqLiteDatabase.rawQuery(selectQuery, null)
            if (cursor.count > 0) {
                cursor.moveToFirst()
                listGenres.add(cursor.getString(cursor.getColumnIndex(arrayStrings[2])))
            }
            cursor.close()
        }
        return listGenres
    }

    fun getRowTbKV(
            arrayStrings: Array<String>,
            key: String
    ): String {
        val db = databaseManager.openDatabase(TAG)
        val selectQuery = ("SELECT * FROM " + arrayStrings[0] + " WHERE "
                + arrayStrings[1] + " = '" + key + "'")
        val cursor = db.rawQuery(selectQuery, null)
        var value = ""
        if (cursor.count > 0) {
            cursor.moveToFirst()
            value = cursor.getString(cursor.getColumnIndex(arrayStrings[2]))
        }
        cursor.close()
        return value
    }

    fun insertToTbKV(
            arrayStrings: Array<String>,
            key: String,
            value: String
    ) {
        val db = databaseManager.openDatabase(TAG)
        val cv = ContentValues()
        cv.put(arrayStrings[1], key)
        cv.put(arrayStrings[2], value)
        val result = getRowTbKV(arrayStrings, key)
        if (result == "") {
            db.insert(arrayStrings[0], null, cv)
        } else if (result != "" && result != value) {
            val time = java.lang.Long.valueOf(result)
            val now = java.lang.Long.valueOf(value)
            val timeNow = now!! - time!!
            if (timeNow >= 600000) {
                deleteRowTableById()
                val where = arrayStrings[1] + " = ?"
                val args = arrayOf(key)
                db.update(arrayStrings[0], cv, where, args)
            }
        }

    }

    private fun deleteRowTableById() {
        val listAllTable = StringSource.LIST_ALL_TABLE()
        for (arrayStrings in listAllTable) {
            val listId = getListId(arrayStrings)
            if (listId.size > 0) {
                for (id in listId) {
                    deleteRow(id, arrayStrings)
                    Log.d(TAG, "deleted " + id + " " + arrayStrings[0])
                }
            }
        }
    }

    fun deleteRow(id: String, arrayStrings: Array<String>) {
        val sqLiteDatabase = databaseManager.openDatabase(TAG)
        val where = arrayStrings[2] + "= ?"
        val args = arrayOf(id)
        sqLiteDatabase.delete(arrayStrings[0], where, args)
    }

    fun insertMovieToDatabase(
            arrayStrings: Array<String>,
            listMovie: List<Movie>
    ) {
        val listId = getListId(arrayStrings)
        val sqLiteDatabase = databaseManager.openDatabase(TAG)
        for (movie in listMovie) {
            if (!listId.contains(movie.id)) {
                val contentValues = ContentValues()
                contentValues.put(arrayStrings[2], movie.id)
                contentValues.put(arrayStrings[3], movie.title)
                contentValues.put(arrayStrings[4], movie.voteAverage)
                contentValues.put(arrayStrings[5], movie.popularity)
                contentValues.put(arrayStrings[6], movie.overView)
                contentValues.put(arrayStrings[7], movie.posterPath)
                contentValues.put(arrayStrings[8], movie.releaseDate)
                contentValues.put(arrayStrings[9], movie.genresList.toString())
                sqLiteDatabase.insert(arrayStrings[0], null, contentValues)
            }
        }
    }
}
