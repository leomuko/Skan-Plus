package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class CreateBarcodeActivity : AppCompatActivity() {

    var mainFab: FloatingActionButton? = null
    var shareFab: FloatingActionButton? = null
    var downloadFab: FloatingActionButton? = null
    var textToEncryptEditText: EditText? = null
    var imageView: ImageView? = null
    var selectionInputLayout: TextInputLayout? = null
    var codeTypeTv: TextView? = null
    var translationYAxis = 100f
    var menuOpen = false
    val interpolator: OvershootInterpolator = OvershootInterpolator()
    var listOfCodes = arrayOf(
        "AZTEC 2D Barcode",
        "CODE 39 1D",
        "CODE 128 1D",
        "DATA MATRIX 2D",
        "PDF417",
        "QR Code",
    )

    private val REQUEST_CODE_PERMISSION = 13456
    var bitmap: Bitmap? = null
    private val TAG = "CreateBarcodeActivity"
    private var toSave = false
    private var toShare = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_barcode)

        mainFab = findViewById(R.id.fab_main)
        shareFab = findViewById(R.id.fab_share)
        downloadFab = findViewById(R.id.fab_save)
        textToEncryptEditText = findViewById(R.id.text_field)
        imageView = findViewById(R.id.imageView)
        selectionInputLayout = findViewById(R.id.textInputLayout)
        codeTypeTv = findViewById(R.id.keyword)

        selectionInputLayout?.setOnClickListener {
            openCodesDialog()
        }

        showMenu()

        findViewById<Button>(R.id.create_barcode_btn).setOnClickListener {
            createBarcode()
        }

        shareFab?.setOnClickListener {
            toShare = true
            checkWritingPermission()
        }

        downloadFab?.setOnClickListener {
            //saveBarcodeImageToLocalStorage()
            toSave = true
            checkWritingPermission()
        }


    }

    private fun shareImageUri(uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/jpeg"
        startActivity(intent)
    }

    private fun openCodesDialog() {

        val context: Context = ContextThemeWrapper(this, R.style.AppTheme2)
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle("Select Code Encryption To Use")
        //set single choice items
        builder.setSingleChoiceItems(
            listOfCodes,
            -1,
        ) { dialog, i ->
        }
        // alert dialog positive button
        builder.setPositiveButton("Submit") { dialog, which ->
            val position = (dialog as AlertDialog).listView.checkedItemPosition
            //if selected, then get item text
            if (position != -1) {
                val selectedItem = listOfCodes[position]
                codeTypeTv?.text = selectedItem
            }
        }
        builder.setNegativeButton("Cancel", null)
        // create the alert dialog and show it
        val dialog = builder.create()
        dialog.show()

        //initially disable the positive button
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

        //dialog list item click listener
        dialog.listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // enable positive button when user select an item
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .isEnabled = position != -1
            }


    }

    private fun showMenu() {
        //set Alpha
        shareFab?.alpha = 0f
        downloadFab?.alpha = 0f

        //set translation for the expanding buttons
        shareFab?.translationY = translationYAxis
        downloadFab?.translationY = translationYAxis

        mainFab?.setOnClickListener {
            //check if menu is open
            if (menuOpen) {
                closeMenu()
            } else {
                openMenu()
            }
        }
    }

    private fun openMenu() {
        menuOpen = !menuOpen
        mainFab?.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down)
        shareFab?.animate()?.translationY(0f)?.alpha(1f)?.setInterpolator(interpolator)
            ?.setDuration(
                300
            )?.start()
        downloadFab?.animate()?.translationY(0f)?.alpha(1f)?.setInterpolator(interpolator)
            ?.setDuration(
                300
            )?.start()

    }

    private fun closeMenu() {
        menuOpen = !menuOpen
        mainFab?.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up)
        shareFab?.animate()?.translationY(translationYAxis)?.alpha(0f)
            ?.setInterpolator(interpolator)?.setDuration(
                300
            )?.start()
        downloadFab?.animate()?.translationY(translationYAxis)?.alpha(0f)?.setInterpolator(
            interpolator
        )?.setDuration(300)?.start()
    }

    private fun createBarcode() {
        hideKeyboard(this)
        val multiFormatWriter: MultiFormatWriter = MultiFormatWriter()

        if (textToEncryptEditText?.text?.toString().equals("")) {
            Toast.makeText(this, "Please Fill Field for Text To Encrypt", Toast.LENGTH_SHORT).show()
        } else if (codeTypeTv?.text.toString().equals("TAP TO SELECT ENCRYPTION FORMAT")) {
            Toast.makeText(this, "Select Encryption Format ", Toast.LENGTH_SHORT).show()
        } else {

            try {
                val bitMatrix: BitMatrix = multiFormatWriter.encode(
                    textToEncryptEditText?.text.toString(),
                    getBarcodeFormat(codeTypeTv?.text.toString()),
                    500,
                    500
                )

                val barcodeEncoder: BarcodeEncoder = BarcodeEncoder()
                bitmap = barcodeEncoder.createBitmap(bitMatrix)
                imageView?.setImageBitmap(bitmap)
                Toast.makeText(
                    this,
                    "Encrypted Image of Type " + getBarcodeFormat(codeTypeTv?.text.toString()).toString() + " has been created Successfully!!",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    //get barcode format basing off the selected Type
    private fun getBarcodeFormat(s: String): BarcodeFormat {
        var format: BarcodeFormat = BarcodeFormat.QR_CODE
        when (s) {
            "AZTEC 2D Barcode" -> format = BarcodeFormat.AZTEC
            "CODE 39 1D" -> format = BarcodeFormat.CODE_39
            "CODE 128 1D" -> format = BarcodeFormat.CODE_128
            "DATA MATRIX 2D" -> format = BarcodeFormat.DATA_MATRIX
            "PDF417" -> format = BarcodeFormat.PDF_417
            "QR Code" -> format = BarcodeFormat.QR_CODE

        }
        return format

    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    private fun checkWritingPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //make request to allow saving permissions
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSION
                )
            } else {
                if (toSave) {
                    saveImageToStorage()
                } else if (toShare) {
                    shareImage()
                }
            }
        }
    }

    private fun shareImage() {
        toShare = false
        if (bitmap == null) {
            Toast.makeText(this, "No Image to be saved", Toast.LENGTH_SHORT).show()
        } else {
            val externalStorageState = Environment.getExternalStorageState()
            if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
                val storageDirectory =
                    Environment.getExternalStorageDirectory().absolutePath + "/SkanImages"
                val sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
                val currentDate = sdf.format(Date())
                val file = File(storageDirectory, "Skan $currentDate.jpeg")
                try {
                    val dir: File = File(storageDirectory)
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }
                    val stream: OutputStream = FileOutputStream(file)
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    stream.flush()
                    stream.close()
                    MediaScannerConnection.scanFile(
                        this,
                        arrayOf(file.path),
                        arrayOf("image/jpeg"),
                        null
                    )
                    if(Build.VERSION.SDK_INT < 24){
                        val uri = Uri.fromFile(file)
                        shareImageUri(uri)
                    }else {
                        val uri = Uri.parse(file.path)
                        shareImageUri(uri)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    this.applicationContext, "Unable to access storage",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this.applicationContext, "Permissions Granted Successfully!!",
                    Toast.LENGTH_SHORT
                ).show()
                if (toSave) {
                    saveImageToStorage()
                } else if (toShare) {
                    shareImage()
                }
            } else {
                Toast.makeText(
                    this.applicationContext,
                    "Please allow permissions for proper Application Functionality!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun saveImageToStorage() {
        toSave = false
        if (bitmap == null) {
            Toast.makeText(this, "No Image to be saved", Toast.LENGTH_SHORT).show()
        } else {
            val externalStorageState = Environment.getExternalStorageState()
            if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
                val storageDirectory =
                    Environment.getExternalStorageDirectory().absolutePath + "/SkanImages"
                val sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
                val currentDate = sdf.format(Date())
                val file = File(storageDirectory, "Skan $currentDate.jpeg")
                try {
                    val dir: File = File(storageDirectory)
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }
                    val stream: OutputStream = FileOutputStream(file)
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    stream.flush()
                    stream.close()
                    Toast.makeText(
                        this.applicationContext, "Image has been saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    MediaScannerConnection.scanFile(
                        this,
                        arrayOf(file.path),
                        arrayOf("image/jpeg"),
                        null
                    )
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    this.applicationContext, "Unable to access storage",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}