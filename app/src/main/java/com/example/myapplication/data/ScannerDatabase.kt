package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ScannerDataModel::class], version = 1, exportSchema = false)
abstract class ScannerDatabase : RoomDatabase() {

    abstract fun scannerDao() : ScannerDao

    companion object{
        @Volatile
        private var INSTANCE : ScannerDatabase? = null

        fun getDatabase(context: Context) : ScannerDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScannerDatabase::class.java,
                    "scanner_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}