package org.helios.mythicdoors.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Connection(context: Context?):
    SQLiteOpenHelper(context, Contracts.DATABASE_NAME, null, Contracts.DATABASE_VERSION),
    AutoCloseable
{
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            // TODO: Create tables
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    override fun close() {
        super.close()
    }
}