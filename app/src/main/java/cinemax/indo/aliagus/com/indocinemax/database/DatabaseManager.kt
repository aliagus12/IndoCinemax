package cinemax.indo.aliagus.com.indocinemax.database

import android.database.sqlite.SQLiteDatabase

/**
 * Created by ali on 26/02/18.
 */
class DatabaseManager(private val databaseHelper: DatabaseHelper) {

    companion object {
        private val TAG = DatabaseManager::class.java.simpleName
    }

    private var sqLiteDatabase: SQLiteDatabase? = null

    fun openDatabase(tag: String): SQLiteDatabase {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseHelper.writableDatabase
        } else if (!sqLiteDatabase!!.isOpen) {
            sqLiteDatabase = databaseHelper.writableDatabase
        }
        return sqLiteDatabase!!
    }
}