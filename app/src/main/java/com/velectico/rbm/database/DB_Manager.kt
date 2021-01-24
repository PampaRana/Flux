package com.velectico.rbm.database

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DB_Manager {
    // Keeps track of Opened or Closed Database
    private var mOpenCounter = 0
    private var db: SQLiteDatabase? = null

    //*********** Used to Open the DB Connection ********//
    @Synchronized
    fun openDatabase(): SQLiteDatabase? {
        mOpenCounter += 1
        if (mOpenCounter == 1) {
            db = mDatabaseHelper!!.writableDatabase
        }
        return db
    }

    //*********** Used to Close the DB Connection ********//
    @Synchronized
    fun closeDatabase() {
        mOpenCounter -= 1
        if (mOpenCounter == 0) {
            db!!.close()
        }
    }

    companion object {
        private var instance: DB_Manager? = null
        private var mDatabaseHelper: SQLiteOpenHelper? = null

        //*********** Initialize DB_Manager and SQLiteOpenHelper Instances ********//
        @Synchronized
        fun initializeInstance(helper: SQLiteOpenHelper?) {
            if (instance == null) {
                instance = DB_Manager()
                mDatabaseHelper = helper
            }
        }

        //*********** Returns DB_Manager Instance ********//
        @Synchronized
        fun getInstance(): DB_Manager? {
            checkNotNull(instance) { DB_Manager::class.java.simpleName + " is not initialized, call initializeInstance(..) method first." }
            return instance
        }
    }
}