package com.example.myapplication

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.History.HistoryActivity
import com.example.myapplication.data.ScanViewModel
import com.example.myapplication.data.ScannerDataModel

class BarcodeResultActivity : AppCompatActivity() {

    var resultTxtView: TextView? = null
    var resultImage: ImageView? = null
    var resultTypeTxtView: TextView? = null
    var shareButton: Button? = null
    var openResultButton: Button? = null
    var value: String? = ""
    private lateinit var mScanViewModel: ScanViewModel
    var scanData: ScannerDataModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_result)

        val actionBar = supportActionBar
        actionBar!!.apply {
            title = Html.fromHtml("<font color='#ffffff'>Result</font>")
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        mScanViewModel = ViewModelProvider(this).get(ScanViewModel::class.java)

        resultTxtView = findViewById(R.id.resultTv)
        resultTxtView?.movementMethod = ScrollingMovementMethod()
        resultImage = findViewById(R.id.resultImageView)
        resultTypeTxtView = findViewById(R.id.resultTypeTv)
        shareButton = findViewById(R.id.shareBtn)
        openResultButton = findViewById(R.id.openButton)

        shareButton?.setOnClickListener {
            //for sharing
        }

        openResultButton?.setOnClickListener {
            //opening website urls..
        }

        val extras = intent.extras
        if (extras != null) {
            value = extras.getString("Result")
        }

        scanData = intent.extras?.getParcelable("DATA")
        if (scanData != null) {
            populateViews()
        } else {
            doResultCheck()
        }

    }

    private fun populateViews() {
        if (value == "") {
            //for empty result
            resultTypeTxtView?.text = getString(R.string.nothing_detected)
            resultTxtView?.text = ""
            resultImage?.background = ContextCompat.getDrawable(this, R.drawable.ic_warning)
            shareButton?.visibility = View.INVISIBLE
            openResultButton?.visibility = View.INVISIBLE
        } else if (scanData?.type == getString(R.string.product)) {
            // for product result
            resultTypeTxtView?.text = getString(R.string.prod_detected)
            resultTxtView?.text = scanData?.result
            resultImage?.background = ContextCompat.getDrawable(this, R.drawable.ic_product)
            shareButton?.setOnClickListener {
                //enable sharing of product code / text
                shareText(scanData?.result)
            }
            openResultButton?.text = getString(R.string.browse_prod)
            openResultButton?.setOnClickListener {
                //open product in browser
                value?.let { it1 -> doProductSearch(it1) }
            }
        } else if (scanData?.type == getString(R.string.website)) {
            //for url result
            resultTypeTxtView?.text = getString(R.string.url_detected)
            resultTxtView?.text = scanData?.result
            resultImage?.background = ContextCompat.getDrawable(this, R.drawable.ic_globe)
            shareButton?.setOnClickListener {
                //enable sharing of url / text
                shareText(scanData?.result)
            }
            openResultButton?.text = getString(R.string.browse)
            openResultButton?.setOnClickListener {
                //open link in browser
                scanData?.result?.let { it1 -> doWebSearch(it1) }
            }
        } else if (scanData?.type == getString(R.string.contact)) {
            //for contact result
            resultTypeTxtView?.text = getString(R.string.contact_)
            resultTxtView?.text = scanData?.result
            resultImage?.background = ContextCompat.getDrawable(this, R.drawable.ic_contact)
            shareButton?.setOnClickListener {
                //enable sharing of url / text
                shareText(scanData?.result)
            }
            openResultButton?.text = getString(R.string.copy_)
            openResultButton?.setOnClickListener {
                //copy text to clipboard
                copyToClipBoard(scanData?.result)
                Toast.makeText(
                    this,
                    "Contact Information has been copied to clipboard",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            //for everything else
            resultTxtView?.text = scanData?.result
            resultImage?.background = ContextCompat.getDrawable(this, R.drawable.ic_text)
            shareButton?.setOnClickListener {
                //enable sharing of text
                shareText(scanData?.result)
            }
            openResultButton?.text = getString(R.string.copy_)
            openResultButton?.setOnClickListener {
                //copy text to clipboard
                copyToClipBoard(scanData?.result)
                Toast.makeText(this, "Text has been copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun doResultCheck() {
        if (value == "") {
            //for empty result
            resultTypeTxtView?.text = getString(R.string.nothing_detected)
            resultTxtView?.text = ""
            resultImage?.background = ContextCompat.getDrawable(this, R.drawable.ic_warning)
            shareButton?.visibility = View.INVISIBLE
            openResultButton?.visibility = View.INVISIBLE
        } else if (isInteger(value?.substring(0, 2))) {
            // for product result
            resultTypeTxtView?.text = getString(R.string.prod_detected)
            resultTxtView?.text = value
            resultImage?.background = ContextCompat.getDrawable(this, R.drawable.ic_product)
            shareButton?.setOnClickListener {
                //enable sharing of product code / text
                shareText(value)
            }
            openResultButton?.text = getString(R.string.browse_prod)
            openResultButton?.setOnClickListener {
                //open product in browser
                value?.let { it1 -> doProductSearch(it1) }
            }
            saveToDatabase("product", value, true)
        } else if (Patterns.WEB_URL.matcher(value?.toLowerCase()).matches()) {
            //for url result
            resultTypeTxtView?.text = getString(R.string.url_detected)
            resultTxtView?.text = value
            resultImage?.background = ContextCompat.getDrawable(this, R.drawable.ic_globe)
            shareButton?.setOnClickListener {
                //enable sharing of url / text
                shareText(value)
            }
            openResultButton?.text = getString(R.string.browse)
            openResultButton?.setOnClickListener {
                //open link in browser
                value?.let { it1 -> doWebSearch(it1) }
            }
            saveToDatabase("website", value, true)
        } else if (value?.startsWith("BEGIN:VCARD") == true) {
            //for contact result
            resultTypeTxtView?.text = getString(R.string.contact_)
            resultTxtView?.text = value
            resultImage?.background = ContextCompat.getDrawable(this, R.drawable.ic_contact)
            shareButton?.setOnClickListener {
                //enable sharing of url / text
                shareText(value)
            }
            openResultButton?.text = getString(R.string.copy_)
            openResultButton?.setOnClickListener {
                //copy text to clipboard
                copyToClipBoard(value)
                Toast.makeText(
                    this,
                    "Contact Information has been copied to clipboard",
                    Toast.LENGTH_SHORT
                ).show()
            }
            saveToDatabase("contact", value, true)
        } else {
            //for everything else
            resultTxtView?.text = value
            resultImage?.background = ContextCompat.getDrawable(this, R.drawable.ic_text)
            shareButton?.setOnClickListener {
                //enable sharing of text
                shareText(value)
            }
            openResultButton?.text = getString(R.string.copy_)
            openResultButton?.setOnClickListener {
                //copy text to clipboard
                copyToClipBoard(value)
                Toast.makeText(this, "Text has been copied to clipboard", Toast.LENGTH_SHORT).show()
            }
            saveToDatabase("text", value, true)
        }
    }

    private fun saveToDatabase(type: String, result: String?, scan: Boolean) {
        val s: ScannerDataModel? = result?.let { ScannerDataModel(0, type, it, scan) }
        if (s != null) {
            mScanViewModel.addScannerData(s)
        }

    }

    private fun doProductSearch(productValue: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse("https://www.google.com/#q=$productValue")
        startActivity(i)
    }

    private fun copyToClipBoard(text: String?) {
        val clipBoard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("", text)
        clipBoard.setPrimaryClip(clip)
    }

    private fun shareText(text: String?) {
        val textIntent = Intent()
        textIntent.apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
        }
        textIntent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(textIntent, "Share Via"))

    }

    private fun doWebSearch(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }


    fun isInteger(str: String?) = str?.toIntOrNull()?.let { true } ?: false

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (scanData != null) {
            if (item.itemId == android.R.id.home) {
                startActivity(Intent(this, HistoryActivity::class.java))
                finish()
            }
        } else {
            if (item.itemId == android.R.id.home) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }


        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (scanData != null) {
            startActivity(Intent(this, HistoryActivity::class.java))
            finish()

        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }
    }
}