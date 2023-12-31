package org.helios.mythicdoors.utils.connection

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.helios.mythicdoors.utils.connection.Contracts.*

class Connection(context: Context?):
    SQLiteOpenHelper(
        context,
        DatabaseContract.DATABASE_NAME,
        null,
        DatabaseContract.DATABASE_VERSION),
    AutoCloseable
{
    /* Este código permite tanto trabajar con Singleton como, en un futuro, implementar DI con Dagger */
    init {
        appContext = context
    }

    companion object {
        private var appContext: Context? = null

        fun setContext(context: Context) {
            appContext = context
        }

        fun getContext(): Context? {
            return appContext
        }
    }


    override fun onCreate(db: SQLiteDatabase?) {
        try {
            if (db != null) createTables(db)
            Log.w("Connection", "DB and tables created. Welcome to the Matrix, Neo!")
        } catch (e: Exception) {
            Log.e("Connection", "Error creating DB: ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    override fun onConfigure(db: SQLiteDatabase) { db.setForeignKeyConstraintsEnabled(true) }

    override fun close() {
        Log.w("Connection", "Closing connection... Back to reality, Neo!")
        super.close()
    }

    private fun createTables(db: SQLiteDatabase?) {
        try {
                DatabaseTablesCreator.dbTablesList.forEach { db?.execSQL(it) }
            } catch (e: android.database.SQLException) {
                Log.e("Connection", "Error creating tables: ${e.message}")
        }
    }
}