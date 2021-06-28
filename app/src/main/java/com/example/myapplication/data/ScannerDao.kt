package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScannerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addScannedData(scannedData : ScannerDataModel)

    @Query("SELECT * FROM scanner_table ORDER BY id DESC")
    fun readAllData() : LiveData<List<ScannerDataModel>>

}