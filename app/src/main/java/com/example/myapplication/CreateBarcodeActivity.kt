package com.example.myapplication

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class CreateBarcodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_barcode)

        findViewById<Button>(R.id.create_barcode_btn).setOnClickListener {
            createBarcode()
        }


    }

    private fun createBarcode() {
        val multiFormatWriter : MultiFormatWriter = MultiFormatWriter()

       try {
           val bitMatrix : BitMatrix = multiFormatWriter.encode(findViewById<EditText>(R.id.text_field).text.toString(),BarcodeFormat.QR_CODE, 500, 500 )
           val barcodeEncoder : BarcodeEncoder = BarcodeEncoder()
           val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
           findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)

       }catch (e : Exception){
           e.printStackTrace()
       }
    }
}