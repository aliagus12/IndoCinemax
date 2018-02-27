package cinemax.indo.aliagus.com.indocinemax.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cinemax.indo.aliagus.com.indocinemax.lib.StringSource

/**
 * Created by ali on 26/02/18.
 */
open class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        //database version
        val DATABASE_VERSION = 5
        //database name
        val DATABASE_NAME = "kv.db"

        private var instance: DatabaseHelper? = null

        @Synchronized
        fun getInstance(context: Context): DatabaseHelper {
            if (instance == null) {
                instance = DatabaseHelper(context)
            }
            return instance as DatabaseHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(StringSource.CREATE_TABLE_KEY_VALUE)
        db.execSQL(StringSource.CREATE_TABLE_MOVIE_NOW_PLAYING)
        db.execSQL(StringSource.CREATE_TABLE_MOVIE_POPULAR)
        db.execSQL(StringSource.CREATE_TABLE_MOVIE_COMING_SOON)
        db.execSQL(StringSource.CREATE_TABLE_GENRES)
        db.execSQL(StringSource.CREATE_TABLE_MOVIE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + StringSource.TABLE_KEY_VALUE)
        db.execSQL("DROP TABLE IF EXISTS " + StringSource.TABLE_MOVIE)
        db.execSQL("DROP TABLE IF EXISTS " + StringSource.TABLE_POPULAR)
        db.execSQL("DROP TABLE IF EXISTS " + StringSource.TABLE_COMING_SOON)
        db.execSQL("DROP TABLE IF EXISTS " + StringSource.TABLE_GENRES)
        db.execSQL("DROP TABLE IF EXISTS " + StringSource.TABLE_FAVORITE)
        onCreate(db)
    }
}