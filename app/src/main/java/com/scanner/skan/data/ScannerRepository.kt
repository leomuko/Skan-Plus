package com.scanner.skan.data

import androidx.lifecycle.LiveData

class ScannerRepository(private val scannerDao: ScannerDao) {

    val readAllData : LiveData<List<ScannerDataModel>> = scannerDao.readAllData()

    suspend fun addScanData(scannerDataModel: ScannerDataModel){
        scannerDao.addScannedData(scannerDataModel)
    }

    suspend fun deleteScannedItem(scannerDataModel: ScannerDataModel){
        scannerDao.deleteItem(scannerDataModel)
    }

    suspend fun deleteAllItems(){
        scannerDao.deleteAll()
    }


}