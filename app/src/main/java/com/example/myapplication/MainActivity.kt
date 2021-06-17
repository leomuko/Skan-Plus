package com.example.myapplication

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*

private const val CAMERA_REQUEST_CODE = 101


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setUpPermissions()

        findViewById<Button>(R.id.scan_barcode_btn).setOnClickListener{
            val intent = Intent(applicationContext, BarCodeActivity::class.java )
            startActivity(intent)
        }

        findViewById<Button>(R.id.text_recognition_button).setOnClickListener{
            /*Toast.makeText(applicationContext, "To do Text Recognition", Toast.LENGTH_SHORT).show()*/
            val intent = Intent(applicationContext, TextRecognitionActivity::class.java )
            startActivity(intent)
        }

        findViewById<Button>(R.id.create_barcode).setOnClickListener{
            val intent = Intent(applicationContext, CreateBarcodeActivity::class.java )
            startActivity(intent)
        }

    }



    private fun setUpPermissions(){
        val permission  = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
        CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode){
            CAMERA_REQUEST_CODE-> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "You need a Camera Permission in order to scan", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Camera Permission granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}