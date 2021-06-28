package com.example.myapplication.data

import androidx.lifecycle.LiveData

class ScannerRepository(private val scannerDao: ScannerDao) {

    val readAllData : LiveData<List<ScannerDataModel>> = scannerDao.readAllData()

    suspend fun addScanData(scannerDataModel: ScannerDataModel){
        scannerDao.addScannedData(scannerDataModel)
    }
}