package com.scanner.skan.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScanViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<ScannerDataModel>>
    private val repository: ScannerRepository

    init {
        val scannerDao = ScannerDatabase.getDatabase(application).scannerDao()
        repository = ScannerRepository(scannerDao)
        readAllData = repository.readAllData

    }

    fun addScannerData(scannerDataModel: ScannerDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addScanData(scannerDataModel)
        }
    }

    fun deleteScannedItem(scannerDataModel: ScannerDataModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteScannedItem(scannerDataModel)
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllItems()
        }
    }

}