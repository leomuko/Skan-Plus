package com.scanner.skan.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "scanner_table", indices = [Index(value = ["result"], unique = true)])
data class ScannerDataModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    //the type of barcode scanned i.e  website, text, contact or product
    val type: String,
    @ColumnInfo(name = "result")
    val result: String,
    //true if we scanning barcode, false if we are creating
    val scan : Boolean
) : Parcelable