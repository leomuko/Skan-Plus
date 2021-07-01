package com.scanner.skan.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ScannerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addScannedData(scannedData : ScannerDataModel)

    @Query("SELECT * FROM scanner_table ORDER BY id DESC")
    fun readAllData() : LiveData<List<ScannerDataModel>>

    @Delete
    suspend fun deleteItem(scannedData: ScannerDataModel)

    @Query("DELETE FROM scanner_table")
    suspend fun deleteAll()


}