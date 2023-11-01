package org.helios.mythicdoors.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.helios.mythicdoors.utils.Contracts.*

class Connection(context: Context?):
    SQLiteOpenHelper(
        context,
        DatabaseContract.DATABASE_NAME,
        null,
        DatabaseContract.DATABASE_VERSION),
    AutoCloseable
{
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            if (db != null) createTables(db)
            print("DB and tables created. Welcome to the Matrix, Neo!")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    override fun close() {
        print("Closing connection... Back to reality, Neo!")
        super.close()
    }

    private fun createTables(db: SQLiteDatabase?) {
        try {
                DatabaseTablesCreator.dbTablesList.forEach { db?.execSQL(it) }
            } catch (e: android.database.SQLException) {
                print("Error creating tables")
                e.printStackTrace()
        }
    }
}