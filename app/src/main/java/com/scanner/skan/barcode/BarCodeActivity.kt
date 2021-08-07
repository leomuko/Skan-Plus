package com.scanner.skan.barcode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.scanner.skan.R
import kotlinx.android.synthetic.main.activity_bar_code.*

class BarCodeActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_code)

        startAnimation()
        codeScanner()
    }

    private fun startAnimation() {
        val animation : Animation = AnimationUtils.loadAnimation(this, R.anim.scan_animation)
        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                bar.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
        bar.startAnimation(animation)
    }

    private fun codeScanner(){
        codeScanner = CodeScanner(this, findViewById(R.id.scanner_view))

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread{
                    val intent = Intent(this@BarCodeActivity, BarcodeResultActivity::class.java)
                    intent.putExtra("Result", it.text)
                    startActivity(intent)
                    finish()

                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                   Toast.makeText(this@BarCodeActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

       /* findViewById<CodeScannerView>(R.id.scanner_view).setOnClickListener{
            codeScanner.startPreview()

        }*/
        codeScanner.startPreview()


    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }
}